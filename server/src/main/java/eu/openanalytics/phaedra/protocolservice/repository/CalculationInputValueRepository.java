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
package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.CalculationInputValue;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculationInputValueRepository extends CrudRepository<CalculationInputValue, Long> {

    List<CalculationInputValue> findByFeatureId(Long featureId);
    List<CalculationInputValue> findByFeatureIdAndFormulaId(Long featureId, Long formulaId);
    CalculationInputValue findByFeatureIdAndFormulaIdAndVariableName(long featureId, long formulaId, String variableName);

    @Query("SELECT civ.* FROM calculation_input_value as civ" +
            " JOIN feature as feat ON feat.id = civ.feature_id AND feat.formula_id = civ.formula_id" +
            " WHERE feat.protocol_id = :protocolId ")
    List<CalculationInputValue> findByProtocolId(@Param("protocolId") Long protocolId);
}
