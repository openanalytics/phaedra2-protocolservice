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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;

import eu.openanalytics.phaedra.protocolservice.dto.FeatureStatDTO;
import eu.openanalytics.phaedra.protocolservice.exception.DuplicateFeatureStatException;
import eu.openanalytics.phaedra.protocolservice.exception.FeatureNotFoundException;
import eu.openanalytics.phaedra.protocolservice.exception.FeatureStatNotFoundException;
import eu.openanalytics.phaedra.protocolservice.exception.ProtocolNotFoundException;
import eu.openanalytics.phaedra.protocolservice.model.Feature;
import eu.openanalytics.phaedra.protocolservice.model.FeatureStat;
import eu.openanalytics.phaedra.protocolservice.repository.FeatureRepository;
import eu.openanalytics.phaedra.protocolservice.repository.FeatureStatRepository;
import eu.openanalytics.phaedra.util.exceptionhandling.UserVisibleException;

@Service
public class FeatureStatService {

    private final FeatureStatRepository featureStatRepository;
    private final FeatureRepository featureRepository;
    private final ModelMapper modelMapper;
    private final ProtocolService protocolService;
    private final DefaultFeatureStatService defaultFeatureStatService;

    public FeatureStatService(FeatureStatRepository featureStatRepository, FeatureRepository featureRepository, ModelMapper modelMapper,
    		ProtocolService protocolService, DefaultFeatureStatService defaultFeatureStatService) {
        this.featureStatRepository = featureStatRepository;
        this.featureRepository = featureRepository;
        this.modelMapper = modelMapper;
        this.protocolService = protocolService;
        this.defaultFeatureStatService = defaultFeatureStatService;
    }

    public FeatureStatDTO create(long featureId, FeatureStatDTO featureStatDTO) throws FeatureNotFoundException, DuplicateFeatureStatException {
        var feature = featureRepository.findById(featureId);
        if (feature.isEmpty()) {
            throw new FeatureNotFoundException(featureId);
        }
        protocolService.performOwnershipCheck(feature.get().getProtocolId());

        FeatureStat featureStat = modelMapper.map(featureStatDTO)
                .featureId(featureId)
                .build();

        return save(featureStat);
    }

    public FeatureStatDTO update(FeatureStatDTO featureStatDTO) throws UserVisibleException {
        Optional<FeatureStat> existingFeatureStat = featureStatRepository.findById(featureStatDTO.getId());
        if (existingFeatureStat.isEmpty()) {
            throw new FeatureStatNotFoundException(featureStatDTO.getId());
        }

        if (!Objects.equals(existingFeatureStat.get().getFeatureId(), featureStatDTO.getFeatureId())) {
            throw new UserVisibleException("The featureId of a FeatureStat cannot be changed");
        }

        var feature = featureRepository.findById(featureStatDTO.getFeatureId());
        protocolService.performOwnershipCheck(feature.get().getProtocolId());

        FeatureStat updatedFeatureStat = modelMapper.map(featureStatDTO).build();
        return save(updatedFeatureStat);
    }

    public FeatureStatDTO get(Long featureId, Long featureStatId) throws UserVisibleException {
        Optional<FeatureStat> featureStat = featureStatRepository.findById(featureStatId);
        if (featureStat.isEmpty()) {
            throw new FeatureStatNotFoundException(featureStatId);
        }

        if (!Objects.equals(featureStat.get().getFeatureId(), featureId)) {
            throw new UserVisibleException("The provided featureId is incorrect for this FeatureStat");
        }
        return modelMapper.map(featureStat.get()).build();
    }

    public void delete(Long featureId, Long featureStatId) throws UserVisibleException {
        Optional<FeatureStat> featureStat = featureStatRepository.findById(featureStatId);
        if (featureStat.isEmpty()) {
            throw new FeatureStatNotFoundException(featureStatId);
        }

        if (!Objects.equals(featureStat.get().getFeatureId(), featureId)) {
            throw new UserVisibleException("The provided featureId is not equal to the actual featureId of the FeatureStat");
        }

        var feature = featureRepository.findById(featureId);
        protocolService.performOwnershipCheck(feature.get().getProtocolId());

        featureStatRepository.deleteById(featureStatId);
    }

    public List<FeatureStatDTO> getByFeatureId(Long featureId) throws FeatureNotFoundException {
        var feature = featureRepository.findById(featureId);
        if (feature.isEmpty()) {
            throw new FeatureNotFoundException(featureId);
        }

        return featureStatRepository.findByFeatureId(featureId)
                .stream()
                .map((f) -> modelMapper.map(f).build())
                .toList();
    }

    public List<FeatureStatDTO> getByProtocolId(Long protocolId) throws ProtocolNotFoundException {
    	var protocol = protocolService.getProtocolById(protocolId);
        if (protocol == null) {
            throw new ProtocolNotFoundException(protocolId);
        }

        return featureStatRepository.findByProtocolId(protocolId)
                .stream()
                .map((f) -> modelMapper.map(f).build())
                .toList();
    }

    public void createDefaultsForFeature(Feature resFeature) {
        // 1. get all default FeatureStats
        var defaults = defaultFeatureStatService.get();
        var featureStats = new ArrayList<FeatureStat>();

        for (var defaultStat : defaults) {
            var featureStat = FeatureStat.builder()
                    .featureId(resFeature.getId())
                    .plateStat(defaultStat.getPlateStat())
                    .welltypeStat(defaultStat.getWelltypeStat())
                    .name(defaultStat.getName())
                    .formulaId(defaultStat.getFormulaId())
                    .build();
            featureStats.add(featureStat);
        }

        featureStatRepository.saveAll(featureStats);
    }

    /**
     * Saves a {@link FeatureStat} and returns the resulting corresponding {@link FeatureStatDTO}.
     */
    private FeatureStatDTO save(FeatureStat featureStat) throws DuplicateFeatureStatException {
        try {
            FeatureStat newFeatureStat = featureStatRepository.save(featureStat);
            return modelMapper.map(newFeatureStat).build();
        } catch (DbActionExecutionException ex) {
            if (ex.getCause() instanceof DuplicateKeyException) {
                throw new DuplicateFeatureStatException();
            }
            throw ex;
        }
    }

}
