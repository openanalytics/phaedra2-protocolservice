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
import eu.openanalytics.phaedra.protocolservice.model.CurveSetting;
import eu.openanalytics.phaedra.protocolservice.repository.CurveSettingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurveSettingService {

    private final CurveSettingRepository drcPropertyRepository;

    public CurveSettingService(CurveSettingRepository drcPropertyRepository) {
        this.drcPropertyRepository = drcPropertyRepository;
    }

    public void save(Long featureId, DRCModelDTO drcModel) {
        // Creete model name
        CurveSetting model = drcPropertyRepository.findAllByFeatureIdAndName(featureId, "model");
        if (model != null) model.setValue(drcModel.getName());
        else model = new CurveSetting(featureId, "model", drcModel.getName());
        drcPropertyRepository.save(model);

        CurveSetting method = drcPropertyRepository.findAllByFeatureIdAndName(featureId, "method");
        if (method != null) method.setValue(drcModel.getMethod());
        else method = new CurveSetting(featureId, "method", drcModel.getMethod());
        drcPropertyRepository.save(method);

        CurveSetting description = drcPropertyRepository.findAllByFeatureIdAndName(featureId, "description");
        if (description != null) description.setValue(drcModel.getDescription());
        else description = new CurveSetting(featureId, "description", drcModel.getDescription());
        drcPropertyRepository.save(description);

        CurveSetting slope = drcPropertyRepository.findAllByFeatureIdAndName(featureId, "slope");
        if (slope != null) slope.setValue(drcModel.getSlope());
        else slope = new CurveSetting(featureId, "slope", drcModel.getSlope());
        drcPropertyRepository.save(slope);

        for (String propertyName : drcModel.getInputParameters().keySet()) {
            CurveSetting property = drcPropertyRepository.findAllByFeatureIdAndName(featureId, propertyName);
            if (property != null) property.setValue(drcModel.getInputParameters().get(propertyName));
            else property = new CurveSetting(featureId, propertyName, drcModel.getInputParameters().get(propertyName));
            drcPropertyRepository.save(property);
        }
    }

    public DRCModelDTO getByFeatureId(Long featureId) {
        List<CurveSetting> drcProperties = drcPropertyRepository.findAllByFeatureId(featureId);
        if (drcProperties.isEmpty())
            return null;

        DRCModelDTO drcModelDTO = new DRCModelDTO();
        drcModelDTO.setFeatureId(featureId);

        for (CurveSetting drcProp: drcProperties) {
            if (drcProp.getName().equalsIgnoreCase("model")) drcModelDTO.setName(drcProp.getValue());
            else if (drcProp.getName().equalsIgnoreCase("description")) drcModelDTO.setDescription(drcProp.getValue());
            else if (drcProp.getName().equalsIgnoreCase("method")) drcModelDTO.setMethod(drcProp.getValue());
            else if (drcProp.getName().equalsIgnoreCase("slope")) drcModelDTO.setSlope(drcProp.getValue());
            else drcModelDTO.getInputParameters().put(drcProp.getName(), drcProp.getValue());
        }

        return drcModelDTO;
    }
}
