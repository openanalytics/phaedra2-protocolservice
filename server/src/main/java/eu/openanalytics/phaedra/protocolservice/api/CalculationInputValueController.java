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
import eu.openanalytics.phaedra.protocolservice.service.CalculationInputValueService;
import eu.openanalytics.phaedra.util.exceptionhandling.HttpMessageNotReadableExceptionHandler;
import eu.openanalytics.phaedra.util.exceptionhandling.MethodArgumentNotValidExceptionHandler;
import eu.openanalytics.phaedra.util.exceptionhandling.UserVisibleException;
import eu.openanalytics.phaedra.util.exceptionhandling.UserVisibleExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class CalculationInputValueController implements MethodArgumentNotValidExceptionHandler, HttpMessageNotReadableExceptionHandler, UserVisibleExceptionHandler {

    private final CalculationInputValueService calculationInputValueService;

    public CalculationInputValueController(CalculationInputValueService calculationInputValueService) {
        this.calculationInputValueService = calculationInputValueService;
    }

    @PostMapping("/features/{featureId}/calculationinputvalue")
    public ResponseEntity<CalculationInputValueDTO> createCalculationInputValue(@PathVariable Long featureId,
                                                                                @Valid @RequestBody CalculationInputValueDTO calculationInputValueDTO) throws UserVisibleException {
        if (calculationInputValueDTO.getFeatureId() == null)
            calculationInputValueDTO = calculationInputValueDTO.withFeatureId(featureId);

        var res = calculationInputValueService.create(calculationInputValueDTO);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/features/{featureId}/calculationinputvalue")
    public ResponseEntity<CalculationInputValueDTO> updateCalculationInputValue(@PathVariable Long featureId,
                                                                                @RequestBody CalculationInputValueDTO calculationInputValueDTO) throws UserVisibleException {
        var res = calculationInputValueService.update(featureId, calculationInputValueDTO);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/features/calculationinputvalue/{calculationInputValue}")
    public ResponseEntity<?> deleteFeature(@PathVariable Long calculationInputValue) {
        calculationInputValueService.delete(calculationInputValue);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/features/{featureId}/calculationinputvalue")
    public ResponseEntity<List<CalculationInputValueDTO>> getCalculationInputValue(@PathVariable Long featureId) throws UserVisibleException {
        var res = calculationInputValueService.getByFeatureId(featureId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/protocols/{protocolId}/calculationinputvalue")
    public ResponseEntity<List<CalculationInputValueDTO>> getCalculationInputValueByProtocolIds(@PathVariable Long protocolId) throws UserVisibleException {
        var res = calculationInputValueService.getByProtocolId(protocolId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
