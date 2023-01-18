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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import eu.openanalytics.phaedra.protocolservice.dto.DRCModelDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.exception.DuplicateCalculationInputValueException;
import eu.openanalytics.phaedra.protocolservice.exception.FeatureNotFoundException;
import eu.openanalytics.phaedra.protocolservice.exception.ProtocolNotFoundException;
import eu.openanalytics.phaedra.protocolservice.service.CalculationInputValueService;
import eu.openanalytics.phaedra.protocolservice.service.DoseResponseCurvePropertyService;
import eu.openanalytics.phaedra.protocolservice.service.FeatureService;
import eu.openanalytics.phaedra.protocolservice.service.ProtocolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@RestController
@Slf4j
public class ProtocolController {

    private final ProtocolService protocolService;
    private final FeatureService featureService;
    private final CalculationInputValueService calculationInputValueService;
    private final DoseResponseCurvePropertyService drcSettingsService;

    public ProtocolController(ProtocolService protocolService, FeatureService featureService,
                              CalculationInputValueService calculationInputValueService,
                              DoseResponseCurvePropertyService drcSettingsService) {
        this.protocolService = protocolService;
        this.featureService = featureService;
        this.calculationInputValueService = calculationInputValueService;
        this.drcSettingsService = drcSettingsService;
    }

    @PostMapping("/protocols")
    public ResponseEntity<ProtocolDTO> createProtocol(@RequestBody ProtocolDTO newProtocol) throws DuplicateCalculationInputValueException, FeatureNotFoundException, JsonProcessingException {
        log.info("Create new protocol: " + new ObjectMapper().writeValueAsString(newProtocol));

        // step 1 -> create a protocol
        ProtocolDTO savedProtocol = protocolService.create(newProtocol);

        if (isNotEmpty(newProtocol.getFeatures())) {
            for (FeatureDTO featureDTO : newProtocol.getFeatures()) {
                // step 2 -> set the newly created protocol id to feature dto and create the feature
                featureDTO.setProtocolId(savedProtocol.getId());
                FeatureDTO savedFeature = featureService.save(featureDTO);

                if (isNotEmpty(featureDTO.getCivs())) {
                    for (CalculationInputValueDTO civDTO : featureDTO.getCivs()) {
                        // step 3 -> for every feature create the calculation input values
                        civDTO = civDTO.withFeatureId(savedFeature.getId());
                        calculationInputValueService.create(civDTO);
                    }
                }

                // 4th -> if a dose response curve model is defined create the drc model
                if (featureDTO.getDrcModel() != null) {
                    drcSettingsService.save(savedFeature.getId(), featureDTO.getDrcModel());
                }
            }
        }

        return new ResponseEntity<>(savedProtocol, HttpStatus.CREATED);
    }

    @PutMapping("/protocols")
    public ResponseEntity<ProtocolDTO> updateProtocol(@RequestBody ProtocolDTO updateProtocol) throws DuplicateCalculationInputValueException, FeatureNotFoundException, ProtocolNotFoundException, JsonProcessingException {
        log.info("Update existing protocol: " + new ObjectMapper().writeValueAsString(updateProtocol));

        ProtocolDTO updatedProtocol = protocolService.update(updateProtocol);
        if (isNotEmpty(updateProtocol.getFeatures())) {
            for (FeatureDTO featureDTO : updateProtocol.getFeatures()) {

                if (featureDTO.isDeleted()) {
                    log.info("Delete existing protocol (delete feature): " + new ObjectMapper().writeValueAsString(featureDTO));
                    featureService.delete(featureDTO.getId());
                } else {
                    featureDTO.setProtocolId(updatedProtocol.getId());
                    FeatureDTO savedFeature = featureService.save(featureDTO);
                    log.info("Update existing protocol (save feature): " + new ObjectMapper().writeValueAsString(featureDTO));
                    if (isNotEmpty(featureDTO.getCivs())) {
                        for (CalculationInputValueDTO civDTO : featureDTO.getCivs()) {
                            // 3rd -> for every feature create the calculation input values
                            log.info("Update existing protocol (save civs): " + new ObjectMapper().writeValueAsString(civDTO));
                            civDTO = civDTO.withFeatureId(savedFeature.getId());
                            civDTO = calculationInputValueService.update(savedFeature.getId(), civDTO);
                        }
                    }

                    if (featureDTO.getDrcModel() != null) {
                        log.info("Update existing protocol (save drc model): " + new ObjectMapper().writeValueAsString(featureDTO.getDrcModel()));
                        featureDTO.getDrcModel().setFeatureId(savedFeature.getId());
                        drcSettingsService.save(savedFeature.getId(), featureDTO.getDrcModel());
                    }
                }
            }
        }
        return new ResponseEntity<>(updatedProtocol, HttpStatus.OK);
    }

    @PutMapping("/protocols/{protocolId}/tag")
    public ResponseEntity<String> tagProtocol(@PathVariable Long protocolId, @RequestParam("tag") String tag) {
        protocolService.tagProtocol(protocolId, tag);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/protocols/{protocolId}")
    public ResponseEntity<String> deleteProtocol(@PathVariable Long protocolId) {
        log.info("Update existing protocol: id = " + protocolId);

        protocolService.delete(protocolId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/protocols")
    public ResponseEntity<List<ProtocolDTO>> getProtocols() {
        log.info("Get all protocols");
        List<ProtocolDTO> allProtocols = protocolService.getProtocols();

        for (ProtocolDTO protocol: allProtocols) {
            List<FeatureDTO> features = featureService.findFeaturesByProtocolId(protocol.getId());
            protocol.setFeatures(features);

            for (FeatureDTO feature : features) {
                try {
                    List<CalculationInputValueDTO> civs = calculationInputValueService.getByFeatureId(feature.getId());
                    feature.setCivs(civs);

                    DRCModelDTO drcModel = drcSettingsService.getByFeatureId(feature.getId());
                    feature.setDrcModel(drcModel);
                } catch (FeatureNotFoundException e) {
                    //TODO: Throw an appropriate error
                }
            }
        }

        return new ResponseEntity<>(allProtocols, HttpStatus.OK);
    }

    @GetMapping(value = "/protocols", params = {"tag"})
    public ResponseEntity<List<ProtocolDTO>> getProtocolByTag(@RequestParam(value = "tag", required = false) String tag) {
        log.info("Get protocols by tag: " + tag);

        List<ProtocolDTO> protocolsByTag = protocolService.getProtocolByTag(tag);

        for (ProtocolDTO protocol: protocolsByTag) {
            List<FeatureDTO> features = featureService.findFeaturesByProtocolId(protocol.getId());
            protocol.setFeatures(features);

            for (FeatureDTO feature : features) {
                try {
                    List<CalculationInputValueDTO> civs = calculationInputValueService.getByFeatureId(feature.getId());
                    feature.setCivs(civs);

                    DRCModelDTO drcModel = drcSettingsService.getByFeatureId(feature.getId());
                    feature.setDrcModel(drcModel);
                } catch (FeatureNotFoundException e) {
                    //TODO: Throw an appropriate error
                }
            }
        }

        return new ResponseEntity<>(protocolsByTag, HttpStatus.OK);
    }

    @GetMapping("/protocols/{protocolId}")
    public ResponseEntity<ProtocolDTO> getProtocol(@PathVariable Long protocolId) {
        log.info("Get protocol by id: id = " + protocolId);

        ProtocolDTO protocol = protocolService.getProtocolById(protocolId);

        List<FeatureDTO> features = featureService.findFeaturesByProtocolId(protocolId);
        protocol.setFeatures(features);

        for (FeatureDTO feature: features) {
            try {
                List<CalculationInputValueDTO> civs = calculationInputValueService.getByFeatureId(feature.getId());
                feature.setCivs(civs);

                DRCModelDTO drcModel = drcSettingsService.getByFeatureId(feature.getId());
                feature.setDrcModel(drcModel);
            } catch (FeatureNotFoundException e) {
                //TODO: Throw an appropriate error
            }
        }
        return new ResponseEntity<>(protocol, HttpStatus.OK);
    }

    @GetMapping("/protocols/{protocolId}/features")
    public ResponseEntity<List<FeatureDTO>> getProtocolFeatures(@PathVariable Long protocolId) {
        log.info("Get protocol features by protocol id: id = " + protocolId);

        List<FeatureDTO> features = featureService.findFeaturesByProtocolId(protocolId);
        for (FeatureDTO feature: features) {
            try {
                List<CalculationInputValueDTO> civs = calculationInputValueService.getByFeatureId(feature.getId());
                feature.setCivs(civs);

                DRCModelDTO drcModel = drcSettingsService.getByFeatureId(feature.getId());
                feature.setDrcModel(drcModel);
            } catch (FeatureNotFoundException e) {
                //TODO: Throw an appropriate error
            }
        }
        return new ResponseEntity<>(features, HttpStatus.OK);
    }
}
