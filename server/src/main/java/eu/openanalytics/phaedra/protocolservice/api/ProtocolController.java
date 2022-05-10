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

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.List;

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
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.service.FeatureService;
import eu.openanalytics.phaedra.protocolservice.service.ProtocolService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ProtocolController {

    private final ProtocolService protocolService;
    private final FeatureService featureService;

    public ProtocolController(ProtocolService protocolService, FeatureService featureService) {
        this.protocolService = protocolService;
        this.featureService = featureService;
    }

    @PostMapping("/protocols")
    public ResponseEntity<ProtocolDTO> createProtocol(@RequestBody ProtocolDTO newProtocol) {
        ProtocolDTO savedProtocol = protocolService.create(newProtocol);
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
        if (isNotEmpty(response))
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/protocols", params = {"tag"})
    public ResponseEntity<List<ProtocolDTO>> getProtocolByTag(@RequestParam(value = "tag", required = false) String tag) {
        List<ProtocolDTO> response = protocolService.getProtocolByTag(tag);
        if (isNotEmpty(response))
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/protocols/{protocolId}")
    public ResponseEntity<ProtocolDTO> getProtocol(@PathVariable Long protocolId) {
        ProtocolDTO response = protocolService.getProtocolById(protocolId);
        if (response != null)
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/protocols/{protocolId}/features")
    public ResponseEntity<List<FeatureDTO>> getProtocolFeatures(@PathVariable Long protocolId) {
        List<FeatureDTO> response = featureService.findFeaturesByProtocolId(protocolId);
        if (isNotEmpty(response))
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
