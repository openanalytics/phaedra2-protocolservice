/**
 * Phaedra II
 *
 * Copyright (C) 2016-2024 Open Analytics
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
package eu.openanalytics.phaedra.protocolservice.model;

import eu.openanalytics.phaedra.protocolservice.enumeration.InputSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.experimental.NonFinal;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Value
@Builder
@With
@AllArgsConstructor
@NonFinal
public class CalculationInputValue {
    @Id
    Long id;
    Long featureId;
    Long formulaId;
    String sourceMeasColName;
    Long sourceFeatureId;
    String variableName;
    @Column("input_source")
    InputSource inputSource;
}
