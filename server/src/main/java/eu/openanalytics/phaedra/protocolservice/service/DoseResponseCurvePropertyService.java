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
import eu.openanalytics.phaedra.protocolservice.model.DoseResponseCurveProperty;
import eu.openanalytics.phaedra.protocolservice.repository.DoseResponseCurvePropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoseResponseCurvePropertyService {

    private final DoseResponseCurvePropertyRepository drcPropertyRepository;

    public DoseResponseCurvePropertyService(DoseResponseCurvePropertyRepository drcPropertyRepository) {
        this.drcPropertyRepository = drcPropertyRepository;
    }

    public void save(Long featureId, DRCModelDTO drcModel) {
        // Creete model name
        DoseResponseCurveProperty model = new DoseResponseCurveProperty();
        model.setFeatureId(featureId);
        model.setName("model");
        model.setValue(drcModel.getName());
        drcPropertyRepository.save(model);

        DoseResponseCurveProperty method = new DoseResponseCurveProperty();
        method.setFeatureId(featureId);
        method.setName("method");
        method.setValue(drcModel.getMethod());
        drcPropertyRepository.save(method);

        DoseResponseCurveProperty description = new DoseResponseCurveProperty();
        description.setFeatureId(featureId);
        description.setName("description");
        description.setValue(drcModel.getDescription());
        drcPropertyRepository.save(description);

        DoseResponseCurveProperty slope = new DoseResponseCurveProperty();
        slope.setFeatureId(featureId);
        slope.setName("slope");
        slope.setValue(drcModel.getSlope());
        drcPropertyRepository.save(slope);

        for (String propertyName : drcModel.getInputParameters().keySet()) {
            DoseResponseCurveProperty property = new DoseResponseCurveProperty();
            property.setFeatureId(featureId);
            property.setName(propertyName);
            property.setValue(drcModel.getInputParameters().get(propertyName));
            drcPropertyRepository.save(property);
        }
    }

    public DRCModelDTO getByFeatureId(Long featureId) {
        List<DoseResponseCurveProperty> drcProperties = drcPropertyRepository.findAllByFeatureId(featureId);
        if (drcProperties.isEmpty())
            return null;

        DRCModelDTO drcModelDTO = new DRCModelDTO();
        drcModelDTO.setFeatureId(featureId);

        for (DoseResponseCurveProperty drcProp: drcProperties) {
            if (drcProp.getName().equalsIgnoreCase("model")) drcModelDTO.setName(drcProp.getValue());
            else if (drcProp.getName().equalsIgnoreCase("description")) drcModelDTO.setDescription(drcProp.getValue());
            else if (drcProp.getName().equalsIgnoreCase("method")) drcModelDTO.setMethod(drcProp.getValue());
            else if (drcProp.getName().equalsIgnoreCase("slope")) drcModelDTO.setSlope(drcProp.getValue());
            else drcModelDTO.getInputParameters().put(drcProp.getName(), drcProp.getValue());
        }

        return drcModelDTO;
    }
}
