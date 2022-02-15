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

import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.dto.TaggedObjectDTO;
import eu.openanalytics.phaedra.protocolservice.model.Protocol;
import eu.openanalytics.phaedra.protocolservice.repository.ProtocolRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
public class ProtocolService {
    private static final String PHAEDRA_METADATA_SERVICE = "http://phaedra-metadata-service/phaedra/metadata-service";
    private static final String PROTOCOL_OBJECT_CLASS = "PROTOCOL";

    private final RestTemplate restTemplate;

    private final ProtocolRepository protocolRepository;
    private final ModelMapper modelMapper;

    public ProtocolService(RestTemplate restTemplate, ProtocolRepository protocolRepository, ModelMapper modelMapper) {
        this.restTemplate = restTemplate;
        this.protocolRepository = protocolRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Create a new Protocol
     * @param protocolDTO New protocol
     */
    public ProtocolDTO create(ProtocolDTO protocolDTO) {
        Protocol newProtocol = modelMapper.map(protocolDTO);

        return modelMapper.map(protocolRepository.save(newProtocol));
    }

    /**
     * Update an existing protocol
     * @param protocolDTO Protocol to be updated
     */
    public ProtocolDTO update(ProtocolDTO protocolDTO) {
        Optional<Protocol> protocol = protocolRepository.findById(protocolDTO.getId());
        protocol.ifPresent(p -> {
            p = modelMapper.map(protocolDTO);
            protocolRepository.save(p);
        });
        return protocolDTO;
    }

    /**
     * Delete an existing protocol
     * @param protocolId Protocol to be deleted
     */
    public void delete(Long protocolId) {
        protocolRepository.deleteById(protocolId);
    }

    /**
     * Get a protocol for a given id
     * @param protocolId A protocol id
     */
    public ProtocolDTO getProtocolById(Long protocolId) {
        Optional<Protocol> protocol = protocolRepository.findById(protocolId);
        return protocol.map(modelMapper::map).orElse(null);
    }

    /**
     * Add a tag to a protocol
     * @param protocolId Protocol to be tagged
     * @param tag A protocol tag
     */
    public void tagProtocol(Long protocolId, String tag) {
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
            return Collections.EMPTY_LIST;
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
