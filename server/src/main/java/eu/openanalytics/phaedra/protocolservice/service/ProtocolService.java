/**
 * Phaedra II
 *
 * Copyright (C) 2016-2022 Open Analytics
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

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.dto.TaggedObjectDTO;
import eu.openanalytics.phaedra.protocolservice.model.Protocol;
import eu.openanalytics.phaedra.protocolservice.repository.ProtocolRepository;
import eu.openanalytics.phaedra.util.auth.IAuthorizationService;

@Service
public class ProtocolService {

    private static final String PHAEDRA_METADATA_SERVICE = "http://phaedra-metadata-service/phaedra/metadata-service";
    private static final String PROTOCOL_OBJECT_CLASS = "PROTOCOL";

    private final RestTemplate restTemplate;
    private final ProtocolRepository protocolRepository;
    private final ModelMapper modelMapper;
    private final IAuthorizationService authService;


    public ProtocolService(RestTemplate restTemplate, ProtocolRepository protocolRepository,
                           ModelMapper modelMapper, IAuthorizationService authService) {
        this.restTemplate = restTemplate;
        this.protocolRepository = protocolRepository;
        this.modelMapper = modelMapper;
        this.authService = authService;
    }

    /**
     * Create a new Protocol
     * @param protocolDTO New protocol
     */
    public ProtocolDTO create(ProtocolDTO protocolDTO) {
    	authService.performAccessCheck(p -> authService.hasUserAccess());

        Protocol newProtocol = modelMapper.map(protocolDTO);
        Date currentDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd.hhmmss");
        newProtocol.setPreviousVersion(null);
        newProtocol.setVersionNumber(newProtocol.getVersionNumber()+"-"+format.format(currentDate));
        newProtocol.setCreatedBy(authService.getCurrentPrincipalName());
        newProtocol.setCreatedOn(currentDate);
        protocolDTO.setId(protocolRepository.save(newProtocol).getId());

        return protocolDTO;
    }

    /**
     * Update an existing protocol
     * @param protocolDTO Protocol to be updated
     */
    @CacheEvict(value = "protocols", key = "#protocolDTO?.id")
    public ProtocolDTO update(ProtocolDTO protocolDTO) {
        return protocolRepository.findById(protocolDTO.getId())
        	.map(protocol -> {
                // Check the ownership of the protocol
        		performOwnershipCheck(protocol.getId());

                // Map the ProtocolDTO to Protocol model object
        		modelMapper.map(protocolDTO, protocol);

                // Update the version number and updated by and on properties
                Date currentDate = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd.hhmmss");
                protocol.setVersionNumber(protocol.getVersionNumber()+"-"+format.format(currentDate));
                protocol.setUpdatedBy(authService.getCurrentPrincipalName());
				protocol.setUpdatedOn(currentDate);

				// Delete id to create a new copy of the protocol in the DB
				protocol.setId(null);
				protocol = protocolRepository.save(protocol);

                // Update the ProtocolDTO id
                protocolDTO.setId(protocol.getId());
                return protocolDTO;
        	})
        	.orElse(null);
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
        return protocol.map(modelMapper::map).orElse(null);
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
                    .map(modelMapper::map)
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
                new ParameterizedTypeReference<List<TaggedObjectDTO>>() {});

        List<ProtocolDTO> result = responseEntity.getBody().stream().map(to -> {
            Optional<Protocol> protocol = protocolRepository.findById(to.getObjectId());
            return protocol.map(modelMapper::map).orElse(null);
        }).collect(Collectors.toList()).stream()
                .filter(p -> p != null)
                .collect(Collectors.toList());

        return result;
    }

}
