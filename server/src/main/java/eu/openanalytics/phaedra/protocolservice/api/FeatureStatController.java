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

import eu.openanalytics.phaedra.protocolservice.dto.FeatureStatDTO;
import eu.openanalytics.phaedra.protocolservice.dto.validation.OnCreate;
import eu.openanalytics.phaedra.protocolservice.dto.validation.OnUpdate;
import eu.openanalytics.phaedra.protocolservice.service.FeatureStatService;
import eu.openanalytics.phaedra.util.exceptionhandling.HttpMessageNotReadableExceptionHandler;
import eu.openanalytics.phaedra.util.exceptionhandling.MethodArgumentNotValidExceptionHandler;
import eu.openanalytics.phaedra.util.exceptionhandling.UserVisibleException;
import eu.openanalytics.phaedra.util.exceptionhandling.UserVisibleExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@Validated
public class FeatureStatController implements MethodArgumentNotValidExceptionHandler, HttpMessageNotReadableExceptionHandler, UserVisibleExceptionHandler {

    private final FeatureStatService featureStatService;

    public FeatureStatController(FeatureStatService featureStatService) {
        this.featureStatService = featureStatService;
    }

    @PostMapping("/features/{featureId}/featurestats")
    @ResponseStatus(HttpStatus.CREATED)
    public FeatureStatDTO createFeatureStat(@PathVariable Long featureId,
                                            @Validated(OnCreate.class) @RequestBody FeatureStatDTO featureStatDTO) throws UserVisibleException {
        return featureStatService.create(featureId, featureStatDTO);
    }

    /**
     * Performs an update of the FeatureStat, replacing the FeatureStat (not patching it!).
     * This corresponds to the idea of RESTFUL: https://stackoverflow.com/a/19736570/1393103
     */
    @PutMapping("/features/{featureId}/featurestats/{featureStatId}")
    @ResponseStatus(HttpStatus.OK)
    public FeatureStatDTO updateFeatureStat(@PathVariable Long featureId,
                                            @PathVariable Long featureStatId,
                                            @Validated(OnUpdate.class) @RequestBody FeatureStatDTO featureStatDTO) throws UserVisibleException {

        if (!Objects.equals(featureStatDTO.getId(), featureStatId)) {
            throw new UserVisibleException("The featureStatId provided in the URL is not equal to the id provided in the body");
        }

        if (!Objects.equals(featureStatDTO.getFeatureId(), featureId)) {
            throw new UserVisibleException("The featureId provided in the URL is not equal to the featureId provided in the body");
        }

        return featureStatService.update(featureStatDTO);
    }

    @DeleteMapping("/features/{featureId}/featurestats/{featureStatId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFeatureStat(@PathVariable Long featureId,
                                  @PathVariable Long featureStatId) throws UserVisibleException {
        featureStatService.delete(featureId, featureStatId);
    }

    @GetMapping("/features/{featureId}/featurestats/{featureStatId}")
    @ResponseStatus(HttpStatus.OK)
    public FeatureStatDTO getFeatureStat(@PathVariable Long featureId,
                                         @PathVariable Long featureStatId) throws UserVisibleException {
        return featureStatService.get(featureId, featureStatId);
    }

    @GetMapping("/features/{featureId}/featurestats")
    @ResponseStatus(HttpStatus.OK)
    public List<FeatureStatDTO> getFeatureStatByFeatureId(@PathVariable Long featureId) throws UserVisibleException {
        return featureStatService.getByFeatureId(featureId);
    }

    @GetMapping("/protocols/{protocolId}/featurestats")
    @ResponseStatus(HttpStatus.OK)
    public List<FeatureStatDTO> getFeatureStatByProtocolId(@PathVariable Long protocolId) throws UserVisibleException {
        return featureStatService.getByProtocolId(protocolId);
    }

}
