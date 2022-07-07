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
package eu.openanalytics.phaedra.protocolservice.api;

import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.exception.DuplicateCalculationInputValueException;
import eu.openanalytics.phaedra.protocolservice.exception.FeatureNotFoundException;
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
    public ResponseEntity<ProtocolDTO> createProtocol(@RequestBody ProtocolDTO newProtocol) throws DuplicateCalculationInputValueException, FeatureNotFoundException {
        ProtocolDTO savedProtocol = protocolService.create(newProtocol);

        if (isNotEmpty(newProtocol.getFeatures())) {
            for (FeatureDTO featureDTO : newProtocol.getFeatures()) {
                featureDTO.setProtocolId(savedProtocol.getId());
                FeatureDTO savedFeature = featureService.create(featureDTO);

                if (isNotEmpty(featureDTO.getCivs())) {
                    for (CalculationInputValueDTO civDTO : featureDTO.getCivs()) {
                        civDTO.withFeatureId(featureDTO.getId());
                        CalculationInputValueDTO savedCIV = calculationInputValueService.create(savedFeature.getId(), civDTO);
                    }
                }

                if (featureDTO.getDrcModel() != null) {
                    drcSettingsService.create(savedFeature.getId(), featureDTO.getDrcModel());
                }
            }
        }

        return new ResponseEntity<>(savedProtocol, HttpStatus.CREATED);
    }

    @PutMapping("/protocols")
    public ResponseEntity<ProtocolDTO> updateProtocol(@RequestBody ProtocolDTO updateProtocol) {
        ProtocolDTO updatedProtocol = protocolService.update(updateProtocol);
        featureService.updateFeaturesToNewProtocol(updateProtocol.getId(), updatedProtocol.getId(), null);
        return new ResponseEntity<>(updatedProtocol, HttpStatus.OK);
    }

    @PutMapping("/protocols/{protocolId}/tag")
    public ResponseEntity<String> tagProtocol(@PathVariable Long protocolId, @RequestParam("tag") String tag) {
        protocolService.tagProtocol(protocolId, tag);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/protocols/{protocolId}")
    public ResponseEntity<String> deleteProtocol(@PathVariable Long protocolId) {
        protocolService.delete(protocolId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/protocols")
    public ResponseEntity<List<ProtocolDTO>> getProtocols() {
        log.info("Get all protocols");
        List<ProtocolDTO> response = protocolService.getProtocols();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/protocols", params = {"tag"})
    public ResponseEntity<List<ProtocolDTO>> getProtocolByTag(@RequestParam(value = "tag", required = false) String tag) {
        List<ProtocolDTO> response = protocolService.getProtocolByTag(tag);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/protocols/{protocolId}")
    public ResponseEntity<ProtocolDTO> getProtocol(@PathVariable Long protocolId) {
        ProtocolDTO protocol = protocolService.getProtocolById(protocolId);

        List<FeatureDTO> features = featureService.findFeaturesByProtocolId(protocolId);
        protocol.setFeatures(features);

        for (FeatureDTO f: features) {
            try {
                List<CalculationInputValueDTO> civs = calculationInputValueService.getByFeatureId(f.getId());
                f.setCivs(civs);
            } catch (FeatureNotFoundException e) {

            }
        }
        return new ResponseEntity<>(protocol, HttpStatus.OK);
    }

    @GetMapping("/protocols/{protocolId}/features")
    public ResponseEntity<List<FeatureDTO>> getProtocolFeatures(@PathVariable Long protocolId) {
        List<FeatureDTO> features = featureService.findFeaturesByProtocolId(protocolId);
        for (FeatureDTO f: features) {
            try {
                List<CalculationInputValueDTO> civs = calculationInputValueService.getByFeatureId(f.getId());
                f.setCivs(civs);
            } catch (FeatureNotFoundException e) {

            }
        }
        return new ResponseEntity<>(features, HttpStatus.OK);
    }
}
