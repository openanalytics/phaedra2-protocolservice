/**
 * Phaedra II
 *
 * Copyright (C) 2016-2025 Open Analytics
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

import eu.openanalytics.phaedra.protocolservice.model.DoseResponseCurveProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoseResponseCurvePropertyRepository extends CrudRepository<DoseResponseCurveProperty, Long> {

    /**
     * Retrieve all dose response curve properties for a specific feature
     * @param featureId
     * @return list of dose response curve properties
     */
    List<DoseResponseCurveProperty> findAllByFeatureId(Long featureId);

    /**
     * Retrieve a specific property by featureId and name
     * @param featureId The feature id
     * @param name The drc property name
     * @return
     */
    DoseResponseCurveProperty findAllByFeatureIdAndName(Long featureId, String name);
}
