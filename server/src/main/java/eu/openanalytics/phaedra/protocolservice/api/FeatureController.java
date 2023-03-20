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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.exception.FeatureNotFoundException;
import eu.openanalytics.phaedra.protocolservice.exception.ProtocolNotFoundException;
import eu.openanalytics.phaedra.protocolservice.service.FeatureService;
import eu.openanalytics.phaedra.protocolservice.service.ProtocolService;

@RestController
@RequestMapping("/features")
public class FeatureController {

    @Autowired
    private FeatureService featureService;
    @Autowired
    private ProtocolService protocolService;

    // TODO creating feature with non-existing protocolId returns 500 -> use rest URL? e.g. /protocol/10/feature ?
    // TODO it is possible to create features with the same name
    @PostMapping
    public ResponseEntity<FeatureDTO> createFeature(@RequestBody FeatureDTO newFeature) throws ProtocolNotFoundException {
        //create new protocol version
//        ProtocolDTO newProtocol = createNewVersionProtocol(newFeature.getProtocolId(), null);
//        newFeature.setProtocolId(newProtocol.getId());
        FeatureDTO savedFeature = featureService.save(newFeature);
        protocolService.updateVersion(savedFeature.getProtocolId());
        return new ResponseEntity<>(savedFeature, HttpStatus.CREATED);
    }

    // TODO use rest URL: PUT /features/1/ instead of PUT /features ? cfr .delete
    // TODO validate the feature exists (updating non-existent feature does nothing and returns 200)
    @PutMapping("/{featureId}")
    public ResponseEntity<?> updateFeature(@RequestBody FeatureDTO updateFeature, @PathVariable long featureId) throws ProtocolNotFoundException {
        //update new protocol version
    	updateFeature.setId(featureId);
        protocolService.updateVersion(updateFeature.getProtocolId());
        FeatureDTO updatedFeature = featureService.save(updateFeature);
        return new ResponseEntity<>(updatedFeature, HttpStatus.OK);
    }

    @DeleteMapping("/{featureId}")
    public ResponseEntity<?> deleteFeature(@PathVariable Long featureId) {
        featureService.delete(featureId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getFeatures() {
        List<FeatureDTO> response = featureService.findAllFeatures();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(params = {"tag"})
    public ResponseEntity<?> getFeaturesWithTag(@RequestParam(value = "tag", required = false) String tag) {
        List<FeatureDTO> response = featureService.findAllFeaturesWithTag(tag);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{featureId}")
    public ResponseEntity<?> getFeatureById(@PathVariable Long featureId) {
        FeatureDTO result = null;
        try {
            result = featureService.findFeatureById(featureId);
        } catch (FeatureNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{featureId}/tag")
    public ResponseEntity<?> addTagToFeature(@PathVariable ("featureId") Long featureId, @RequestParam("tag") String tag) {
        featureService.tagFeature(featureId, tag);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
