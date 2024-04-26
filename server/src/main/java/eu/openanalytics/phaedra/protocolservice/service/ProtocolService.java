/**
 * Phaedra II
 *
 * Copyright (C) 2016-2024 Open Analytics
 *
 * ===========================================================================
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License as published by
 * The Apache Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Apache License for more details.
 *
 * You should have received a copy of the Apache License
 * along with this program.  If not, see <http://www.apache.org/licenses/>
 */
package eu.openanalytics.phaedra.protocolservice.service;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.Conditions;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.dto.TaggedObjectDTO;
import eu.openanalytics.phaedra.protocolservice.exception.ProtocolNotFoundException;
import eu.openanalytics.phaedra.protocolservice.model.Protocol;
import eu.openanalytics.phaedra.protocolservice.repository.ProtocolRepository;
import eu.openanalytics.phaedra.util.auth.IAuthorizationService;
import eu.openanalytics.phaedra.util.versioning.VersionUtils;

@Service
public class ProtocolService {

    private static final String PHAEDRA_METADATA_SERVICE = "http://phaedra-metadata-service/phaedra/metadata-service";
    private static final String PROTOCOL_OBJECT_CLASS = "PROTOCOL";

    private final RestTemplate restTemplate;
    private final ProtocolRepository protocolRepository;
    private final ModelMapper customModelMapper;
    private final IAuthorizationService authService;

    private final org.modelmapper.ModelMapper modelMapper = new org.modelmapper.ModelMapper();


    public ProtocolService(RestTemplate restTemplate, ProtocolRepository protocolRepository,
                           ModelMapper modelMapper, IAuthorizationService authService) {
        this.restTemplate = restTemplate;
        this.protocolRepository = protocolRepository;
        this.customModelMapper = modelMapper;
        this.authService = authService;
    }

    /**
     * Create a new Protocol
     * @param protocolDTO New protocol
     */
    public ProtocolDTO create(ProtocolDTO protocolDTO) {
    	authService.performAccessCheck(p -> authService.hasUserAccess());
        Protocol newProtocol = updateVersion(customModelMapper.map(protocolDTO));
        return customModelMapper.map(protocolRepository.save(newProtocol));
    }

    /**
     * Update an existing protocol
     * @param protocolDTO Protocol to be updated
     */
    @CacheEvict(value = "protocols", key = "#protocolDTO?.id")
    public ProtocolDTO update(ProtocolDTO protocolDTO) throws ProtocolNotFoundException {
        Protocol protocol = protocolRepository.findById(protocolDTO.getId()).orElse(null);
        if (protocol == null) return null;

        authService.performAccessCheck(p -> authService.hasUserAccess());

        // Map updated DTO fields onto existing protocol
        modelMapper.typeMap(ProtocolDTO.class, Protocol.class)
                .setPropertyCondition(Conditions.isNotNull())
                .map(protocolDTO, protocol);

        protocol = updateVersion(protocol);
        Protocol updated = protocolRepository.save(protocol);
        return customModelMapper.map(updated);
    }

    /**
     * Delete an existing protocol
     * @param protocolId Protocol to be deleted
     */
    @CacheEvict(value = "protocols")
    public void delete(Long protocolId) {
    	performOwnershipCheck(protocolId);
    	protocolRepository.deleteById(protocolId);
    }

    /**
     * Get a protocol for a given id
     * @param protocolId A protocol id
     */
    @Cacheable("protocols")
    public ProtocolDTO getProtocolById(Long protocolId) {
        Optional<Protocol> protocol = protocolRepository.findById(protocolId);
        return protocol.map(customModelMapper::map).orElse(null);
    }

    /**
     * Performs an ownership check on a protocol, if the provided ID is not null
     * and points to a valid protocol.
     *
     * @param protocolId The ID of the protocol to check against.
     */
    public void performOwnershipCheck(Long protocolId) {
    	Optional.ofNullable(protocolId)
    		.map(id -> getProtocolById(id))
    		.ifPresent(protocol -> authService.performOwnershipCheck(protocol.getCreatedBy()));
    }

    /**
     * Add a tag to a protocol
     * @param protocolId Protocol to be tagged
     * @param tag A protocol tag
     */
    public void tagProtocol(Long protocolId, String tag) {
    	performOwnershipCheck(protocolId);

        StringBuilder urlBuilder = new StringBuilder(PHAEDRA_METADATA_SERVICE);
        urlBuilder.append("/tags");

        TaggedObjectDTO taggedObjectDTO = new TaggedObjectDTO(PROTOCOL_OBJECT_CLASS);
        taggedObjectDTO.setObjectId(protocolId);
        taggedObjectDTO.setTag(tag);

        restTemplate.postForObject(urlBuilder.toString(), taggedObjectDTO, ResponseEntity.class);
    }

    /**
     * Get a all available protocols
     */
    public List<ProtocolDTO> getProtocols() {
        List<Protocol> protocols = (List<Protocol>) protocolRepository.findAll();
        if (isNotEmpty(protocols)) {
            return protocols.stream()
                    .map(customModelMapper::map)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Find all protocols containing given tag
     * @param tag
     */
    public List<ProtocolDTO> getProtocolByTag(String tag) {
        StringBuilder urlBuilder = new StringBuilder(PHAEDRA_METADATA_SERVICE);
        urlBuilder.append("/tagged_objects");
        urlBuilder.append("/").append(PROTOCOL_OBJECT_CLASS);
        urlBuilder.append("?tag=").append(tag);

        ResponseEntity<List<TaggedObjectDTO>> responseEntity = restTemplate.exchange(
                urlBuilder.toString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        List<ProtocolDTO> result = responseEntity.getBody().stream().map(to -> {
            Optional<Protocol> protocol = protocolRepository.findById(to.getObjectId());
            return protocol.map(customModelMapper::map).orElse(null);
        }).collect(Collectors.toList()).stream()
                .filter(p -> p != null)
                .collect(Collectors.toList());

        return result;
    }

    /**
     * Update protocol version
     *
     * @param protocolId
     * @throws ProtocolNotFoundException
     */
    public void updateVersion(Long protocolId) throws ProtocolNotFoundException {
        Optional<Protocol> result = protocolRepository.findById(protocolId);
        if (result.isEmpty())
            throw new ProtocolNotFoundException(protocolId);

        Protocol protocol = updateVersion(result.get());
        protocolRepository.save(protocol);
    }

    public Protocol updateVersion(Protocol protocol) {
        Date currentDate = new Date();

        if (protocol.getId() == null) {
        	protocol.setVersionNumber(VersionUtils.generateNewVersion(null));
            protocol.setCreatedOn(currentDate);
            protocol.setCreatedBy(authService.getCurrentPrincipalName());
        } else {
        	protocol.setPreviousVersion(protocol.getVersionNumber());
        	protocol.setVersionNumber(VersionUtils.generateNewVersion(protocol.getVersionNumber()));
            protocol.setUpdatedOn(currentDate);
            protocol.setUpdatedBy(authService.getCurrentPrincipalName());
        }

        return protocol;
    }
    
}
