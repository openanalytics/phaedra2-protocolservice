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
package eu.openanalytics.phaedra.protocolservice.api;

import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import eu.openanalytics.phaedra.protocolservice.dto.DRCModelDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.exception.FeatureNotFoundException;
import eu.openanalytics.phaedra.protocolservice.service.CalculationInputValueService;
import eu.openanalytics.phaedra.protocolservice.service.DoseResponseCurvePropertyService;
import eu.openanalytics.phaedra.protocolservice.service.FeatureService;
import eu.openanalytics.phaedra.protocolservice.service.ProtocolService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProtocolGraphQLController {

    private final ProtocolService protocolService;
    private final FeatureService featureService;
    private final CalculationInputValueService calculationInputValueService;
    private final DoseResponseCurvePropertyService drcSettingsService;

    public ProtocolGraphQLController(ProtocolService protocolService, FeatureService featureService,
                                     CalculationInputValueService calculationInputValueService,
                                     DoseResponseCurvePropertyService drcSettingsService) {
        this.protocolService = protocolService;
        this.featureService = featureService;
        this.calculationInputValueService = calculationInputValueService;
        this.drcSettingsService = drcSettingsService;
    }

    @QueryMapping
    public List<ProtocolDTO> getProtocols() {
        List<ProtocolDTO> result = protocolService.getProtocols();

        result.forEach(protocol -> {
            List<FeatureDTO> features = featureService.findFeaturesByProtocolId(protocol.getId());
            protocol.setFeatures(features);
            features.forEach(feature -> {
                try {
                    List<CalculationInputValueDTO> civs = calculationInputValueService.getByFeatureId(feature.getId());
                    feature.setCivs(civs);

                    DRCModelDTO drcModel = drcSettingsService.getByFeatureId(feature.getId());
                    feature.setDrcModel(drcModel);
                } catch (FeatureNotFoundException e) {
                    //TODO: Throw an appropriate error
                }
            });
        });

        return result;
    }

    @QueryMapping
    public List<ProtocolDTO> getProtocolsByTag(@Argument String tag) {
        List<ProtocolDTO> result = protocolService.getProtocolByTag(tag);

        result.forEach(protocol -> {
            List<FeatureDTO> features = featureService.findFeaturesByProtocolId(protocol.getId());
            protocol.setFeatures(features);
            features.forEach(feature -> {
                try {
                    List<CalculationInputValueDTO> civs = calculationInputValueService.getByFeatureId(feature.getId());
                    feature.setCivs(civs);

                    DRCModelDTO drcModel = drcSettingsService.getByFeatureId(feature.getId());
                    feature.setDrcModel(drcModel);
                } catch (FeatureNotFoundException e) {
                    //TODO: Throw an appropriate error
                }
            });
        });

        return result;
    }

    @QueryMapping
    public ProtocolDTO getProtocolById(@Argument Long protocolId) {
        ProtocolDTO result = protocolService.getProtocolById(protocolId);

        List<FeatureDTO> features = featureService.findFeaturesByProtocolId(protocolId);
        result.setFeatures(features);

        features.forEach(feature -> {
            try {
                List<CalculationInputValueDTO> civs = calculationInputValueService.getByFeatureId(feature.getId());
                feature.setCivs(civs);

                DRCModelDTO drcModel = drcSettingsService.getByFeatureId(feature.getId());
                feature.setDrcModel(drcModel);
            } catch (FeatureNotFoundException e) {
                //TODO: Throw an appropriate error
            }
        });
        return result;
    }

    @QueryMapping
    public List<FeatureDTO> getFeaturesByProtocolId(@Argument Long protocolId) {
        List<FeatureDTO> result = featureService.findFeaturesByProtocolId(protocolId);

        result.forEach(feature -> {
            try {
                List<CalculationInputValueDTO> civs = calculationInputValueService.getByFeatureId(feature.getId());
                feature.setCivs(civs);

                DRCModelDTO drcModel = drcSettingsService.getByFeatureId(feature.getId());
                feature.setDrcModel(drcModel);
            } catch (FeatureNotFoundException e) {
                //TODO: Throw an appropriate error
            }
        });

        return result;
    }
}
