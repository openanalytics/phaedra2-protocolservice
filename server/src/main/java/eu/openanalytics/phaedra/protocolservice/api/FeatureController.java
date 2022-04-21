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

import java.text.SimpleDateFormat;
import java.util.List;

import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.service.ProtocolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.service.FeatureService;

@RestController
public class FeatureController {

    @Autowired
    private FeatureService featureService;
    @Autowired
    private ProtocolService protocolService;

    // TODO creating feature with non-existing protocolId returns 500
    // -> use rest URL? e.g. /protocol/10/feature ?
    // TODO it is possible to create features with the same name
    @PostMapping("/features")
    public ResponseEntity<FeatureDTO> createFeature(@RequestBody FeatureDTO newFeature) {
        //create new protocol version
        ProtocolDTO newProtocol = createNewVersionProtocol(newFeature.getProtocolId(), null);
        newFeature.setProtocolId(newProtocol.getId());
        FeatureDTO savedFeature = featureService.create(newFeature);
        return new ResponseEntity<>(savedFeature, HttpStatus.CREATED);
    }

    // TODO use rest URL: PUT /features/1/ instead of PUT /features ? cfr .delete
    // TODO validate the feature exists (updating non-existent feature does nothing and returns 200)
    @PutMapping("/features")
    public ResponseEntity<?> updateFeature(@RequestBody FeatureDTO updateFeature) {
        //create new protocol version
        ProtocolDTO newProtocol = createNewVersionProtocol(updateFeature.getProtocolId(), updateFeature.getId());
        FeatureDTO updatedFeature = featureService.update(updateFeature, newProtocol.getId());
        return new ResponseEntity<>(updatedFeature, HttpStatus.OK);
    }

    @DeleteMapping("/features/{featureId}")
    public ResponseEntity<?> deleteFeature(@PathVariable Long featureId) {
        featureService.delete(featureId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/features")
    public ResponseEntity<?> getFeatures() {
        List<FeatureDTO> response = featureService.findAllFeatures();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/features", params = {"tag"})
    public ResponseEntity<?> getFeaturesWithTag(@RequestParam(value = "tag", required = false) String tag) {
        List<FeatureDTO> response = featureService.findAllFeaturesWithTag(tag);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/features/{featureId}")
    public ResponseEntity<?> getFeatureById(@PathVariable Long featureId) {
        FeatureDTO result = featureService.findFeatureById(featureId);
        if (result == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/features/{featureId}/tag")
    public ResponseEntity<?> addTagToFeature(@PathVariable ("featureId") Long featureId, @RequestParam("tag") String tag) {
        featureService.tagFeature(featureId, tag);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Creates a new protocol version for the given protocol.
     * @param protocolId The protocol to create a new version for.
     * @param updatedFeatureId The feature that gets updated.
     * @return protocolDTO The new protocol version.
     */
    private ProtocolDTO createNewVersionProtocol(Long protocolId, Long updatedFeatureId) {
        ProtocolDTO currentProtocolDTO = protocolService.getProtocolById(protocolId);
        //Change versionNumber
        Double newVersion = Double.parseDouble(currentProtocolDTO.getVersionNumber().split("-")[0])+0.01;
        currentProtocolDTO.setPreviousVersion(currentProtocolDTO.getVersionNumber());
        currentProtocolDTO.setVersionNumber(newVersion.toString());
        ProtocolDTO newProtocol = protocolService.update(currentProtocolDTO);
        //Duplicate current features
        featureService.updateFeaturesToNewProtocol(protocolId,newProtocol.getId(), updatedFeatureId);
        return newProtocol;
    }
}
