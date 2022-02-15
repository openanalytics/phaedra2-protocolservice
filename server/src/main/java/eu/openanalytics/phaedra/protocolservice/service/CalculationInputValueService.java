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

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;

import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import eu.openanalytics.phaedra.protocolservice.exception.DuplicateCalculationInputValueException;
import eu.openanalytics.phaedra.protocolservice.exception.FeatureNotFoundException;
import eu.openanalytics.phaedra.protocolservice.exception.ProtocolNotFoundException;
import eu.openanalytics.phaedra.protocolservice.model.CalculationInputValue;
import eu.openanalytics.phaedra.protocolservice.repository.CalculationInputValueRepository;

@Service
public class CalculationInputValueService {

    private final ProtocolService protocolService;
    private final FeatureService featureService;
    private final CalculationInputValueRepository calculationInputValueRepository;
    private final ModelMapper modelMapper;

    public CalculationInputValueService(ProtocolService protocolService, FeatureService featureService,
    		CalculationInputValueRepository calculationInputValueRepository, ModelMapper modelMapper) {
        this.protocolService = protocolService;
        this.featureService = featureService;
        this.calculationInputValueRepository = calculationInputValueRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Create a CalculationInputValue
     *
     * @param featureId
     * @param calculationInputValueDTO the CalculationInputValue to create
     * @return the resulting DTO
     * @throws FeatureNotFoundException                when the given feature is not found
     * @throws DuplicateCalculationInputValueException when a CalculationInputValue for this feature already exists with the given values
     */
    public CalculationInputValueDTO create(Long featureId, CalculationInputValueDTO calculationInputValueDTO) throws FeatureNotFoundException, DuplicateCalculationInputValueException {
        var feature = featureService.findFeatureById(featureId);
        if (feature == null) {
            throw new FeatureNotFoundException(featureId);
        }
        protocolService.performOwnershipCheck(feature.getProtocolId());

        CalculationInputValue calculationInputValue = modelMapper.map(calculationInputValueDTO)
                .featureId(featureId)
                .build();

        return save(calculationInputValue);
    }

    public CalculationInputValueDTO update(Long featureId, CalculationInputValueDTO calculationInputValueDTO) throws FeatureNotFoundException, DuplicateCalculationInputValueException {
    	var feature = featureService.findFeatureById(featureId);
    	if (feature == null) {
            throw new FeatureNotFoundException(featureId);
        }
    	protocolService.performOwnershipCheck(feature.getProtocolId());
    	
        Optional<CalculationInputValue> calculationInputValue = calculationInputValueRepository.findById(calculationInputValueDTO.getId());
        calculationInputValue.ifPresent(c -> {
            c = modelMapper.map(calculationInputValueDTO).build();
            calculationInputValueRepository.save(c);
        });
        return calculationInputValueDTO;
    }

    public void delete(Long calculationInputValueId) {
        calculationInputValueRepository.deleteById(calculationInputValueId);
    }


    /**
     * Get all {@link CalculationInputValueDTO} of a feature.
     *
     * @param featureId the feature to the {@link CalculationInputValue} from
     * @return the CalculationInputValues of the feature
     * @throws FeatureNotFoundException when the feature is not found
     */
    public List<CalculationInputValueDTO> getByFeatureId(Long featureId) throws FeatureNotFoundException {
    	var feature = featureService.findFeatureById(featureId);
    	if (feature == null) {
            throw new FeatureNotFoundException(featureId);
        }

        return calculationInputValueRepository.findByFeatureId(featureId)
                .stream()
                .map((f) -> modelMapper.map(f).build())
                .toList();
    }

    /**
     * Get all {@link CalculationInputValueDTO} of a protocol by looking at the features of the given protocol.
     *
     * @param protocolId the protocol to fetch the {@link CalculationInputValue} from
     * @return the CalculationInputValues of the protocol
     * @throws ProtocolNotFoundException when the protocol is not found
     */
    public List<CalculationInputValueDTO> getByProtocolId(Long protocolId) throws ProtocolNotFoundException {
    	var protocol = protocolService.getProtocolById(protocolId);
        if (protocol == null) {
            throw new ProtocolNotFoundException(protocolId);
        }

        return calculationInputValueRepository.findByProtocolId(protocolId)
                .stream()
                .map((f) -> modelMapper.map(f).build())
                .toList();
    }

    /**
     * Saves a {@link CalculationInputValue} and returns the resulting corresponding {@link CalculationInputValueDTO}.
     */
    private CalculationInputValueDTO save(CalculationInputValue civ) throws DuplicateCalculationInputValueException {
        try {
            CalculationInputValue newCiv = calculationInputValueRepository.save(civ);
            return modelMapper.map(newCiv).build();
        } catch (DbActionExecutionException ex) {
            if (ex.getCause() instanceof DuplicateKeyException) {
                throw new DuplicateCalculationInputValueException();
            }
            throw ex;
        }
    }

}
