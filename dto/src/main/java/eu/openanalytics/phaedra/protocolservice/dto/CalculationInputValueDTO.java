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
package eu.openanalytics.phaedra.protocolservice.dto;

import javax.validation.constraints.AssertTrue;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.openanalytics.phaedra.protocolservice.enumeration.InputSource;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.experimental.NonFinal;

@Value
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // Jackson deserialize compatibility
@NonFinal
public class CalculationInputValueDTO {

    Long id;
    Long featureId;
    Long formulaId;
    String variableName;
    String sourceMeasColName;
    Long sourceFeatureId;
    InputSource inputSource;

    @JsonIgnore
    @AssertTrue(message = "Only one of sourceMeansColumnName or sourceFeatureId may be specified (not both).")
    public boolean isValid() {
        // only one of these fields may contain a value
        return sourceMeasColName == null ^ sourceFeatureId == null;
    }

}
