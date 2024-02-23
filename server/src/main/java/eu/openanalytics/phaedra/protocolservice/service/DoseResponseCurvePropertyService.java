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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import eu.openanalytics.phaedra.protocolservice.record.InputParameter;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;

import eu.openanalytics.phaedra.protocolservice.dto.DRCModelDTO;
import eu.openanalytics.phaedra.protocolservice.model.DoseResponseCurveProperty;
import eu.openanalytics.phaedra.protocolservice.repository.DoseResponseCurvePropertyRepository;

@Service
public class DoseResponseCurvePropertyService {

    private final DoseResponseCurvePropertyRepository drcPropertyRepository;

    public DoseResponseCurvePropertyService(DoseResponseCurvePropertyRepository drcPropertyRepository) {
        this.drcPropertyRepository = drcPropertyRepository;
    }

    public void save(Long featureId, DRCModelDTO drcModel) {
        // Creete model name
        DoseResponseCurveProperty model = drcPropertyRepository.findAllByFeatureIdAndName(featureId, "model");
        if (model != null) model.setValue(drcModel.getName());
        else model = new DoseResponseCurveProperty(featureId, "model", drcModel.getName());
        drcPropertyRepository.save(model);

        DoseResponseCurveProperty description = drcPropertyRepository.findAllByFeatureIdAndName(featureId, "description");
        if (description != null) description.setValue(drcModel.getDescription());
        else description = new DoseResponseCurveProperty(featureId, "description", drcModel.getDescription());
        drcPropertyRepository.save(description);

        drcModel.getInputParameters().stream().forEach(inputParameter -> {
            DoseResponseCurveProperty property = drcPropertyRepository.findAllByFeatureIdAndName(featureId, inputParameter.name());
            if (property != null) property.setValue(inputParameter.value());
            else property = new DoseResponseCurveProperty(featureId, inputParameter.name(), inputParameter.value());
            drcPropertyRepository.save(property);
        });

    }

    public DRCModelDTO getByFeatureId(Long featureId) {
        List<DoseResponseCurveProperty> drcProperties = drcPropertyRepository.findAllByFeatureId(featureId);
        if (drcProperties.isEmpty()) return null;

        DRCModelDTO drcModelDTO = new DRCModelDTO();
        drcModelDTO.setFeatureId(featureId);
        mapProperties(drcProperties, drcModelDTO);

        return drcModelDTO;
    }

    public List<DRCModelDTO> getAll() {
        List<DoseResponseCurveProperty> drcProperties = IterableUtils.toList(drcPropertyRepository.findAll());

        Map<Long, List<DoseResponseCurveProperty>> propsPerFeature = drcProperties.stream().collect(
        		Collectors.groupingBy(DoseResponseCurveProperty::getFeatureId,
        		Collectors.toList()));

        return propsPerFeature.keySet().stream().map(featureId -> {
        	DRCModelDTO model = new DRCModelDTO();
            model.setFeatureId(featureId);
            mapProperties(propsPerFeature.get(featureId), model);
            return model;
        }).toList();
    }

    private void mapProperties(List<DoseResponseCurveProperty> properties, DRCModelDTO model) {
    	for (DoseResponseCurveProperty prop: properties) {
    		if (prop.getName().equalsIgnoreCase("model")) model.setName(prop.getValue());
    		else if (prop.getName().equalsIgnoreCase("description")) model.setDescription(prop.getValue());
    		else model.getInputParameters().add(new InputParameter(prop.getName(), prop.getValue()));
    	}
    }
}
