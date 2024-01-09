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
package eu.openanalytics.phaedra.protocolservice.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.openanalytics.phaedra.protocolservice.dto.validation.OnCreate;
import eu.openanalytics.phaedra.protocolservice.dto.validation.OnUpdate;
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
public class DefaultFeatureStatDTO {

    @Null(groups = OnCreate.class, message = "Id must be null when creating a DefaultFeatureStat")
    @NotNull(groups = OnUpdate.class, message = "Id must be specified when updating a DefaultFeatureStat")
    Long id;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "PlateStat is mandatory")
    Boolean plateStat;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "WelltypeStat is mandatory")
    Boolean welltypeStat;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Name is mandatory")
    String name;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "FormulaId is mandatory")
    Long formulaId;

    @JsonIgnore
    @AssertTrue(message = "Both plateStat and welltypeStat cannot be false", groups = {OnCreate.class, OnUpdate.class})
    public boolean isValid() {
        return plateStat != null && welltypeStat != null && (plateStat || welltypeStat);
    }

}
