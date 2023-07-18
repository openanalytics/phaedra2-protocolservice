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

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import eu.openanalytics.phaedra.protocolservice.dto.DefaultFeatureStatDTO;
import eu.openanalytics.phaedra.protocolservice.dto.validation.OnCreate;
import eu.openanalytics.phaedra.protocolservice.dto.validation.OnUpdate;
import eu.openanalytics.phaedra.protocolservice.service.DefaultFeatureStatService;
import eu.openanalytics.phaedra.util.exceptionhandling.HttpMessageNotReadableExceptionHandler;
import eu.openanalytics.phaedra.util.exceptionhandling.MethodArgumentNotValidExceptionHandler;
import eu.openanalytics.phaedra.util.exceptionhandling.UserVisibleException;
import eu.openanalytics.phaedra.util.exceptionhandling.UserVisibleExceptionHandler;

@RestController
@RequestMapping("/defaultfeaturestats")
@Validated
public class DefaultFeatureStatController implements MethodArgumentNotValidExceptionHandler, HttpMessageNotReadableExceptionHandler, UserVisibleExceptionHandler {

    private final DefaultFeatureStatService defaultFeatureStatService;

    public DefaultFeatureStatController(DefaultFeatureStatService defaultFeatureStatService) {
        this.defaultFeatureStatService = defaultFeatureStatService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DefaultFeatureStatDTO createDefaultFeatureStat(@Validated(OnCreate.class) @RequestBody DefaultFeatureStatDTO defaultFeatureStatDTO) throws UserVisibleException {
        return defaultFeatureStatService.create(defaultFeatureStatDTO);
    }

    /**
     * Performs an update of the DefaultFeatureStat, replacing the DefaultFeatureStat (not patching it!).
     * This corresponds to the idea of RESTFUL: https://stackoverflow.com/a/19736570/1393103
     */
    @PutMapping("/{defaultFeatureStatId}")
    @ResponseStatus(HttpStatus.OK)
    public DefaultFeatureStatDTO updateDefaultFeatureStat(@PathVariable Long defaultFeatureStatId,
                                                          @Validated(OnUpdate.class) @RequestBody DefaultFeatureStatDTO defaultFeatureStatDTO) throws UserVisibleException {
        return defaultFeatureStatService.update(defaultFeatureStatDTO.withId(defaultFeatureStatId));
    }

    @DeleteMapping("/{defaultFeatureStatId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDefaultFeatureStat(@PathVariable Long defaultFeatureStatId) throws UserVisibleException {
        defaultFeatureStatService.delete(defaultFeatureStatId);
    }

    @GetMapping("/{defaultFeatureStatId}")
    @ResponseStatus(HttpStatus.OK)
    public DefaultFeatureStatDTO getDefaultFeatureStat(@PathVariable Long defaultFeatureStatId) throws UserVisibleException {
        return defaultFeatureStatService.get(defaultFeatureStatId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DefaultFeatureStatDTO> getDefaultFeatureStats() {
        return defaultFeatureStatService.get();
    }

}
