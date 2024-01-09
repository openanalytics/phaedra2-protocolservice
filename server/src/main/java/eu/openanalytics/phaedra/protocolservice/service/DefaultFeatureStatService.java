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

import eu.openanalytics.phaedra.protocolservice.dto.DefaultFeatureStatDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureStatDTO;
import eu.openanalytics.phaedra.protocolservice.exception.DefaultFeatureStatNotFoundException;
import eu.openanalytics.phaedra.protocolservice.exception.DuplicateFeatureStatException;
import eu.openanalytics.phaedra.protocolservice.exception.FeatureNotFoundException;
import eu.openanalytics.phaedra.protocolservice.model.DefaultFeatureStat;
import eu.openanalytics.phaedra.protocolservice.model.FeatureStat;
import eu.openanalytics.phaedra.protocolservice.repository.DefaultFeatureStatRepository;
import eu.openanalytics.phaedra.util.auth.IAuthorizationService;
import eu.openanalytics.phaedra.util.exceptionhandling.UserVisibleException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultFeatureStatService {

    private final DefaultFeatureStatRepository defaultFeatureStatRepository;
    private final ModelMapper modelMapper;
    private final IAuthorizationService authService;

    public DefaultFeatureStatService(DefaultFeatureStatRepository defaultFeatureStatRepository, ModelMapper modelMapper, IAuthorizationService authService) {
        this.defaultFeatureStatRepository = defaultFeatureStatRepository;
        this.modelMapper = modelMapper;
        this.authService = authService;
    }

    public DefaultFeatureStatDTO create(DefaultFeatureStatDTO defaultFeatureStatDTO) throws FeatureNotFoundException, DuplicateFeatureStatException {
    	authService.performAccessCheck(p -> authService.hasAdminAccess());

        DefaultFeatureStat defaultFeatureStat = modelMapper.map(defaultFeatureStatDTO).build();
        return save(defaultFeatureStat);
    }

    public DefaultFeatureStatDTO update(DefaultFeatureStatDTO defaultFeatureStatDTO) throws UserVisibleException {
    	authService.performAccessCheck(p -> authService.hasAdminAccess());

        Optional<DefaultFeatureStat> existingDefaultFeatureStat = defaultFeatureStatRepository.findById(defaultFeatureStatDTO.getId());
        if (existingDefaultFeatureStat.isEmpty()) {
            throw new DefaultFeatureStatNotFoundException(defaultFeatureStatDTO.getId());
        }

        DefaultFeatureStat updatedDefaultFeatureStat = modelMapper.map(defaultFeatureStatDTO).build();
        return save(updatedDefaultFeatureStat);
    }

    public DefaultFeatureStatDTO get(Long defaultFeatureStatId) throws UserVisibleException {
        Optional<DefaultFeatureStat> defaultFeatureStat = defaultFeatureStatRepository.findById(defaultFeatureStatId);
        if (defaultFeatureStat.isEmpty()) {
            throw new DefaultFeatureStatNotFoundException(defaultFeatureStatId);
        }
        return modelMapper.map(defaultFeatureStat.get()).build();
    }

    public List<DefaultFeatureStatDTO> get() {
        return defaultFeatureStatRepository.findAll().stream().map((f) -> modelMapper.map(f).build()).toList();
    }

    public void delete(Long defaultFeatureStatId) throws UserVisibleException {
    	authService.performAccessCheck(p -> authService.hasAdminAccess());

        Optional<DefaultFeatureStat> featureStat = defaultFeatureStatRepository.findById(defaultFeatureStatId);
        if (featureStat.isEmpty()) {
            throw new DefaultFeatureStatNotFoundException(defaultFeatureStatId);
        }

        defaultFeatureStatRepository.deleteById(defaultFeatureStatId);
    }

    /**
     * Saves a {@link FeatureStat} and returns the resulting corresponding {@link FeatureStatDTO}.
     */
    private DefaultFeatureStatDTO save(DefaultFeatureStat featureStat) throws DuplicateFeatureStatException {
        try {
            DefaultFeatureStat newFeatureStat = defaultFeatureStatRepository.save(featureStat);
            return modelMapper.map(newFeatureStat).build();
        } catch (DbActionExecutionException ex) {
            if (ex.getCause() instanceof DuplicateKeyException) {
                throw new DuplicateFeatureStatException();
            }
            throw ex;
        }
    }

}
