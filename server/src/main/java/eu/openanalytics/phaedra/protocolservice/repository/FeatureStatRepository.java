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

import eu.openanalytics.phaedra.protocolservice.model.FeatureStat;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureStatRepository extends CrudRepository<FeatureStat, Long> {

    @Query("SELECT feature_stat.* FROM feature_stat" +
            " JOIN feature as feat ON feat.id = feature_stat.feature_id" +
            " WHERE feat.id = :featureId ")
    List<FeatureStat> findByFeatureId(@Param("featureId") Long featureId);

    @Query("SELECT feature_stat.* FROM feature_stat" +
            " JOIN feature as feat ON feat.id = feature_stat.feature_id" +
            " WHERE feat.protocol_id = :protocolId ")
    List<FeatureStat> findByProtocolId(@Param("protocolId") Long featureId);

}
