/**
 * Phaedra II
 *
 * Copyright (C) 2016-2023 Open Analytics
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

import eu.openanalytics.phaedra.protocolservice.dto.DRCModelDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.dto.TaggedObjectDTO;
import eu.openanalytics.phaedra.protocolservice.exception.FeatureNotFoundException;
import eu.openanalytics.phaedra.protocolservice.model.Feature;
import eu.openanalytics.phaedra.protocolservice.repository.FeatureRepository;
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
public class FeatureService {

    private static final String PHAEDRA_METADATA_SERVICE = "http://phaedra-metadata-service/phaedra/metadata-service";
    private static final String FEATURE_OBJECT_CLASS = "FEATURE";

    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;
    private final FeatureRepository featureRepository;
    private final FeatureStatService featureStatService;
    private final DoseResponseCurvePropertyService drcPropertyService;
    private final CalculationInputValueService civService;

    public FeatureService(ModelMapper modelMapper, RestTemplate restTemplate, FeatureRepository featureRepository,
                          FeatureStatService featureStatService, DoseResponseCurvePropertyService drcPropertyService, CalculationInputValueService civService) {
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
        this.featureRepository = featureRepository;
        this.featureStatService = featureStatService;
        this.drcPropertyService = drcPropertyService;
        this.civService = civService;
    }

    /**
     * Create a new feature
     *
     * @param featureDTO New feature
     */
    public FeatureDTO save(FeatureDTO featureDTO) {
        //TODO: set updatedBy and updatedOn properties
        Feature feature = modelMapper.map(featureDTO);
        var saved = featureRepository.save(feature);
        if (featureDTO.getId() == null)
            featureStatService.createDefaultsForFeature(saved);
        return modelMapper.map(saved);
    }

    /**
     * Delete an existing feature
     *
     * @param featureId The feature id
     */
    public void delete(Long featureId) {
        featureRepository.deleteById(featureId);
    }

    /**
     * Tag a feature
     *
     * @param featureId The feature id
     * @param tagName   A tag string
     */
    public void tagFeature(Long featureId, String tagName) {
//    	Optional.ofNullable(findFeatureById(featureId))
//			.ifPresent(feature -> protocolService.performOwnershipCheck(feature.getProtocolId()));

        StringBuilder urlBuilder = new StringBuilder(PHAEDRA_METADATA_SERVICE);
        urlBuilder.append("/tags");

        TaggedObjectDTO request = new TaggedObjectDTO(FEATURE_OBJECT_CLASS);
        request.setTag(tagName);
        request.setObjectId(featureId);

        restTemplate.postForObject(urlBuilder.toString(), request, ResponseEntity.class);
    }

    /**
     * Get feature information by id
     *
     * @param featureId The feature id
     */
    public FeatureDTO findFeatureById(Long featureId) throws FeatureNotFoundException {
        Optional<Feature> feature = featureRepository.findById(featureId);
        FeatureDTO featureDTO = feature.map(modelMapper::map).orElse(null);

        if (featureDTO != null) {
            featureDTO.setDrcModel(drcPropertyService.getByFeatureId(featureId));
            featureDTO.setCivs(civService.getByFeatureId(featureId));
        }

        return featureDTO;
    }

    /**
     * Get all available features
     */
    public List<FeatureDTO> findAllFeatures() {
        List<Feature> features = (List<Feature>) featureRepository.findAll();
        return features.stream()
                .map(modelMapper::map)
                .collect(Collectors.toList());
    }

    /**
     * Find features for a specific protocol
     *
     * @param protocolId The protocol id
     */
    public List<FeatureDTO> findFeaturesByProtocolId(Long protocolId) {
        List<Feature> features = featureRepository.findByProtocolId(protocolId);
        return features.stream()
                .map(modelMapper::map)
                .collect(Collectors.toList());
    }

    /**
     * Find all features containing one of the specified tags
     *
     * @param tag A tag string
     */
    public List<FeatureDTO> findAllFeaturesWithTag(String tag) {
        StringBuilder urlBuilder = new StringBuilder(PHAEDRA_METADATA_SERVICE);
        urlBuilder.append("/tagged_objects");
        urlBuilder.append("/").append(FEATURE_OBJECT_CLASS);
        urlBuilder.append("?tag=").append(tag);

        ResponseEntity<List<TaggedObjectDTO>> responseEntity = restTemplate.exchange(
                urlBuilder.toString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        if (isNotEmpty(responseEntity.getBody())) {
            return responseEntity.getBody().stream().map(to -> {
                Optional<Feature> feature = featureRepository.findById(to.getObjectId());
                return feature.map(modelMapper::map).orElse(null);
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Duplicate all features for a specific protocol to new version except the updated feature.
     * @param oldProtocolId The old protocol id
     * @param newProtocolId The new protocol id
     * @param updatedFeatureId The updated feature id
     */
    public void updateFeaturesToNewProtocol(Long oldProtocolId, Long newProtocolId, Long updatedFeatureId) {
        List<FeatureDTO> featureDTOS = this.findFeaturesByProtocolId(oldProtocolId);
        for (FeatureDTO f : featureDTOS){
            // Skip the updated feature
            if (f.getId().equals(updatedFeatureId)){
                continue;
            }
            f.setId(null);
            f.setProtocolId(newProtocolId);
            this.save(f);
        }
    }

}
