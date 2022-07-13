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
package eu.openanalytics.phaedra.protocolservice.model;

import eu.openanalytics.phaedra.protocolservice.enumeration.FeatureType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;


@Data()
@NoArgsConstructor
public class Feature {

    @Id
    private Long id;
    private String name;
    private String alias;
    private String description;
    private String format;
    @Column("protocol_id")
    private Long protocolId;
    @Column("formula_id")
    private Long formulaId;
    @Column("type")
    private FeatureType featureType;
    @Column("calc_sequence")
    private Integer sequence;
    @Column("calc_trigger")
    private String trigger;
    //TODO: Add updatedBy and updatedOn properties

    public Feature(Long protocolId) {
        this.protocolId = protocolId;
    }
}
