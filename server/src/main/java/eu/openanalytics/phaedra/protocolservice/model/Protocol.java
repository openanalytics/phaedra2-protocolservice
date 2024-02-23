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

import lombok.Data;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
public class Protocol {

	@Id
    private Long id;
	private String name;
    private String description;

    @Column("low_welltype")
    private String lowWelltype;
    @Column("high_welltype")
    private String highWelltype;
    @Column("previous_version")
    private String previousVersion;
    @Column("version_number")
    private String versionNumber;

    @Column("created_on")
    private Date createdOn;
    @Column("created_by")
    private String createdBy;
    @Column("updated_on")
    private Date updatedOn;
    @Column("updated_by")
    private String updatedBy;
}
