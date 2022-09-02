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
package eu.openanalytics.phaedra.protocolservice.service;

import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import eu.openanalytics.phaedra.protocolservice.model.CalculationInputValue;
import eu.openanalytics.phaedra.protocolservice.repository.CalculationInputValueRepository;
import eu.openanalytics.phaedra.protocolservice.repository.FeatureRepository;
import eu.openanalytics.phaedra.protocolservice.repository.ProtocolRepository;
import eu.openanalytics.phaedra.protocolservice.support.Containers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@Sql({"/jdbc/test-data.sql"})
@TestPropertySource(locations = "classpath:application-test.properties")
public class CalculationInputValueServiceTest {
    @Autowired
    private CalculationInputValueService calculationInputValueService;

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private CalculationInputValueRepository calculationInputValueRepository;

    @Autowired
    private ProtocolRepository protocolRepository;

    private static final eu.openanalytics.phaedra.protocolservice.service.ModelMapper modelMapperInit = new ModelMapper();

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("DB_URL", Containers.postgreSQLContainer::getJdbcUrl);
        registry.add("DB_USER", Containers.postgreSQLContainer::getUsername);
        registry.add("DB_PASSWORD", Containers.postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void before() {
        this.calculationInputValueService = new CalculationInputValueService(this.protocolRepository, this.featureRepository, this.calculationInputValueRepository, modelMapperInit);
    }

    @Test
    public void createCalculationInputValueTest() throws Exception{
        Long featureId = 1000L;
        CalculationInputValueDTO calculationInputValueDTO = CalculationInputValueDTO.builder()
                .featureId(featureId)
                .sourceMeasColName("Column1")
                .variableName("col1")
                .formulaId(1000L)
                .build();
        CalculationInputValueDTO res = calculationInputValueService.create(calculationInputValueDTO);
        //Check if returned value is the desired one
        assertThat(res.getId()).isNotNull();
        assertThat(res.getFeatureId()).isEqualTo(featureId);
        assertThat(res.getSourceFeatureId()).isEqualTo(calculationInputValueDTO.getSourceFeatureId());
        assertThat(res.getVariableName()).isEqualTo(calculationInputValueDTO.getVariableName());
        assertThat(res.getSourceMeasColName()).isEqualTo(calculationInputValueDTO.getSourceMeasColName());

        //Check if value is correctly stored in the database
        Optional<CalculationInputValue> res2 = calculationInputValueRepository.findById(res.getId());
        CalculationInputValueDTO converted = modelMapperInit.map(res2.get()).build();
        assertThat(converted.getId()).isEqualTo(res.getId());
        assertThat(converted.getFeatureId()).isEqualTo(featureId);
        assertThat(converted.getSourceFeatureId()).isEqualTo(calculationInputValueDTO.getSourceFeatureId());
        assertThat(converted.getVariableName()).isEqualTo(calculationInputValueDTO.getVariableName());
        assertThat(converted.getSourceMeasColName()).isEqualTo(calculationInputValueDTO.getSourceMeasColName());
    }

    @Test
    public void createCalculationInputValueSourceFeatureIdTest() throws Exception{
        Long featureId = 1000L;
        CalculationInputValueDTO calculationInputValueDTO = CalculationInputValueDTO.builder()
                .featureId(featureId)
                .sourceFeatureId(2L)
                .variableName("col1")
                .formulaId(1000L)
                .build();
        CalculationInputValueDTO res = calculationInputValueService.create(calculationInputValueDTO);
        //Check if returned value is the desired one
        assertThat(res.getId()).isNotNull();
        assertThat(res.getFeatureId()).isEqualTo(featureId);
        assertThat(res.getSourceFeatureId()).isEqualTo(calculationInputValueDTO.getSourceFeatureId());
        assertThat(res.getVariableName()).isEqualTo(calculationInputValueDTO.getVariableName());
        assertThat(res.getSourceMeasColName()).isEqualTo(calculationInputValueDTO.getSourceMeasColName());

        //Check if value is correctly stored in the database
        Optional<CalculationInputValue> res2 = calculationInputValueRepository.findById(res.getId());
        CalculationInputValueDTO converted = modelMapperInit.map(res2.get()).build();
        assertThat(converted.getId()).isEqualTo(res.getId());
        assertThat(converted.getFeatureId()).isEqualTo(featureId);
        assertThat(converted.getSourceFeatureId()).isEqualTo(calculationInputValueDTO.getSourceFeatureId());
        assertThat(converted.getVariableName()).isEqualTo(calculationInputValueDTO.getVariableName());
        assertThat(converted.getSourceMeasColName()).isEqualTo(calculationInputValueDTO.getSourceMeasColName());
    }

    @Test
    public void updateCalculationInputValueTest() throws Exception{
        Long featureId = 1000L;
        CalculationInputValueDTO calculationInputValueDTO = CalculationInputValueDTO.builder()
                .featureId(featureId)
                .sourceMeasColName("Column1")
                .variableName("col1")
                .formulaId(1000L)
                .build();
        CalculationInputValueDTO res = calculationInputValueService.create(calculationInputValueDTO);
        //Check if returned value is the desired one
        assertThat(res.getId()).isNotNull();
        assertThat(res.getFeatureId()).isEqualTo(featureId);
        assertThat(res.getSourceFeatureId()).isEqualTo(calculationInputValueDTO.getSourceFeatureId());
        assertThat(res.getVariableName()).isEqualTo(calculationInputValueDTO.getVariableName());
        assertThat(res.getSourceMeasColName()).isEqualTo(calculationInputValueDTO.getSourceMeasColName());

        //Check if value is correctly stored in the database
        Optional<CalculationInputValue> res2 = calculationInputValueRepository.findById(res.getId());
        CalculationInputValueDTO converted = modelMapperInit.map(res2.get()).build();
        assertThat(converted.getId()).isEqualTo(res.getId());
        assertThat(converted.getFeatureId()).isEqualTo(featureId);
        assertThat(converted.getSourceFeatureId()).isEqualTo(calculationInputValueDTO.getSourceFeatureId());
        assertThat(converted.getVariableName()).isEqualTo(calculationInputValueDTO.getVariableName());
        assertThat(converted.getSourceMeasColName()).isEqualTo(calculationInputValueDTO.getSourceMeasColName());

        CalculationInputValueDTO edited = CalculationInputValueDTO.builder().id(1L).sourceMeasColName("Column2").variableName("col2").featureId(featureId).build();
        CalculationInputValueDTO res3 = calculationInputValueService.update(featureId,edited);
        assertThat(res3.getId()).isNotNull();
        assertThat(res3.getFeatureId()).isEqualTo(featureId);
        assertThat(res3.getSourceFeatureId()).isEqualTo(edited.getSourceFeatureId());
        assertThat(res3.getVariableName()).isEqualTo(edited.getVariableName());
        assertThat(res3.getSourceMeasColName()).isEqualTo(edited.getSourceMeasColName());
    }

    @Test
    public void getByFeatureIdCalculationInputValueOneTest() throws Exception {
        Long featureId = 1000L;
        CalculationInputValueDTO calculationInputValueDTO = CalculationInputValueDTO.builder()
                .featureId(featureId)
                .sourceMeasColName("Column1")
                .variableName("col1")
                .formulaId(1000L)
                .build();
        CalculationInputValueDTO res = calculationInputValueService.create(calculationInputValueDTO);
        //Check if returned value is the desired one
        assertThat(res.getId()).isNotNull();
        assertThat(res.getFeatureId()).isEqualTo(featureId);
        assertThat(res.getSourceFeatureId()).isEqualTo(calculationInputValueDTO.getSourceFeatureId());
        assertThat(res.getVariableName()).isEqualTo(calculationInputValueDTO.getVariableName());
        assertThat(res.getSourceMeasColName()).isEqualTo(calculationInputValueDTO.getSourceMeasColName());

        //Check if value is correctly stored in the database
        List<CalculationInputValueDTO> list = calculationInputValueService.getByFeatureId(1000L);
        assertThat(list.size()).isEqualTo(1);
        CalculationInputValueDTO converted = list.get(0);
        assertThat(converted.getId()).isEqualTo(res.getId());
        assertThat(converted.getFeatureId()).isEqualTo(featureId);
        assertThat(converted.getSourceFeatureId()).isEqualTo(calculationInputValueDTO.getSourceFeatureId());
        assertThat(converted.getVariableName()).isEqualTo(calculationInputValueDTO.getVariableName());
        assertThat(converted.getSourceMeasColName()).isEqualTo(calculationInputValueDTO.getSourceMeasColName());
    }

    @Test
    public void getByFeatureIdCalculationInputValueMultipleTest() throws Exception {
        Long featureId = 1000L;
        CalculationInputValueDTO calculationInputValueDTO = CalculationInputValueDTO.builder()
                .featureId(featureId)
                .sourceMeasColName("Column1")
                .variableName("col1")
                .formulaId(1000L)
                .build();
        CalculationInputValueDTO res = calculationInputValueService.create(calculationInputValueDTO);
        //Check if returned value is the desired one
        assertThat(res.getId()).isNotNull();
        assertThat(res.getFeatureId()).isEqualTo(featureId);
        assertThat(res.getSourceFeatureId()).isEqualTo(calculationInputValueDTO.getSourceFeatureId());
        assertThat(res.getVariableName()).isEqualTo(calculationInputValueDTO.getVariableName());
        assertThat(res.getSourceMeasColName()).isEqualTo(calculationInputValueDTO.getSourceMeasColName());

        CalculationInputValueDTO calculationInputValueDTO2 = CalculationInputValueDTO.builder()
                .featureId(featureId)
                .sourceMeasColName("Column2")
                .variableName("col2")
                .formulaId(1000L)
                .build();
        CalculationInputValueDTO res2 = calculationInputValueService.create(calculationInputValueDTO2);
        //Check if returned value is the desired one
        assertThat(res2.getId()).isNotNull();
        assertThat(res2.getFeatureId()).isEqualTo(featureId);
        assertThat(res2.getSourceFeatureId()).isEqualTo(calculationInputValueDTO2.getSourceFeatureId());
        assertThat(res2.getVariableName()).isEqualTo(calculationInputValueDTO2.getVariableName());
        assertThat(res2.getSourceMeasColName()).isEqualTo(calculationInputValueDTO2.getSourceMeasColName());

        //Check if value is correctly stored in the database
        List<CalculationInputValueDTO> list = calculationInputValueService.getByFeatureId(1000L);
        assertThat(list.size()).isEqualTo(2);

        CalculationInputValueDTO converted1 = list.get(0);
        assertThat(converted1.getId()).isEqualTo(res.getId());
        assertThat(converted1.getFeatureId()).isEqualTo(featureId);
        assertThat(converted1.getSourceFeatureId()).isEqualTo(calculationInputValueDTO.getSourceFeatureId());
        assertThat(converted1.getVariableName()).isEqualTo(calculationInputValueDTO.getVariableName());
        assertThat(converted1.getSourceMeasColName()).isEqualTo(calculationInputValueDTO.getSourceMeasColName());

        CalculationInputValueDTO converted2 = list.get(1);
        assertThat(converted2.getId()).isEqualTo(res2.getId());
        assertThat(converted2.getFeatureId()).isEqualTo(featureId);
        assertThat(converted2.getSourceFeatureId()).isEqualTo(calculationInputValueDTO2.getSourceFeatureId());
        assertThat(converted2.getVariableName()).isEqualTo(calculationInputValueDTO2.getVariableName());
        assertThat(converted2.getSourceMeasColName()).isEqualTo(calculationInputValueDTO2.getSourceMeasColName());
    }

    @Test
    public void getByProtocolIdCalculationInputValueOneTest() throws Exception {
        Long featureId = 3000L;
        CalculationInputValueDTO calculationInputValueDTO = CalculationInputValueDTO.builder()
                .featureId(featureId)
                .sourceMeasColName("Column1")
                .variableName("col1")
                .formulaId(1000L)
                .build();
        CalculationInputValueDTO res = calculationInputValueService.create(calculationInputValueDTO);
        //Check if returned value is the desired one
        assertThat(res.getId()).isNotNull();
        assertThat(res.getFeatureId()).isEqualTo(featureId);
        assertThat(res.getSourceFeatureId()).isEqualTo(calculationInputValueDTO.getSourceFeatureId());
        assertThat(res.getVariableName()).isEqualTo(calculationInputValueDTO.getVariableName());
        assertThat(res.getSourceMeasColName()).isEqualTo(calculationInputValueDTO.getSourceMeasColName());

        //Check if value is correctly stored in the database
        List<CalculationInputValueDTO> list = calculationInputValueService.getByProtocolId(1000L);
        assertThat(list.size()).isEqualTo(1);
        CalculationInputValueDTO converted = list.get(0);
        assertThat(converted.getId()).isEqualTo(res.getId());
        assertThat(converted.getFeatureId()).isEqualTo(featureId);
        assertThat(converted.getSourceFeatureId()).isEqualTo(calculationInputValueDTO.getSourceFeatureId());
        assertThat(converted.getVariableName()).isEqualTo(calculationInputValueDTO.getVariableName());
        assertThat(converted.getSourceMeasColName()).isEqualTo(calculationInputValueDTO.getSourceMeasColName());
    }

    @Test
    public void getByProtocolIdCalculationInputValueMultipleTest() throws Exception {
        Long featureId = 3000L;
        CalculationInputValueDTO calculationInputValueDTO = CalculationInputValueDTO.builder()
                .featureId(featureId)
                .sourceMeasColName("Column1")
                .variableName("col1")
                .formulaId(1000L)
                .build();
        CalculationInputValueDTO res = calculationInputValueService.create(calculationInputValueDTO);
        //Check if returned value is the desired one
        assertThat(res.getId()).isNotNull();
        assertThat(res.getFeatureId()).isEqualTo(featureId);
        assertThat(res.getSourceFeatureId()).isEqualTo(calculationInputValueDTO.getSourceFeatureId());
        assertThat(res.getVariableName()).isEqualTo(calculationInputValueDTO.getVariableName());
        assertThat(res.getSourceMeasColName()).isEqualTo(calculationInputValueDTO.getSourceMeasColName());

        CalculationInputValueDTO calculationInputValueDTO2 = CalculationInputValueDTO.builder()
                .featureId(featureId)
                .sourceMeasColName("Column2")
                .variableName("col2")
                .formulaId(1000L)
                .build();
        CalculationInputValueDTO res2 = calculationInputValueService.create(calculationInputValueDTO2);
        //Check if returned value is the desired one
        assertThat(res2.getId()).isNotNull();
        assertThat(res2.getFeatureId()).isEqualTo(featureId);
        assertThat(res2.getSourceFeatureId()).isEqualTo(calculationInputValueDTO2.getSourceFeatureId());
        assertThat(res2.getVariableName()).isEqualTo(calculationInputValueDTO2.getVariableName());
        assertThat(res2.getSourceMeasColName()).isEqualTo(calculationInputValueDTO2.getSourceMeasColName());

        //Check if value is correctly stored in the database
        List<CalculationInputValueDTO> list = calculationInputValueService.getByProtocolId(1000L);
        assertThat(list.size()).isEqualTo(2);

        CalculationInputValueDTO converted1 = list.get(0);
        assertThat(converted1.getId()).isEqualTo(res.getId());
        assertThat(converted1.getFeatureId()).isEqualTo(featureId);
        assertThat(converted1.getSourceFeatureId()).isEqualTo(calculationInputValueDTO.getSourceFeatureId());
        assertThat(converted1.getVariableName()).isEqualTo(calculationInputValueDTO.getVariableName());
        assertThat(converted1.getSourceMeasColName()).isEqualTo(calculationInputValueDTO.getSourceMeasColName());

        CalculationInputValueDTO converted2 = list.get(1);
        assertThat(converted2.getId()).isEqualTo(res2.getId());
        assertThat(converted2.getFeatureId()).isEqualTo(featureId);
        assertThat(converted2.getSourceFeatureId()).isEqualTo(calculationInputValueDTO2.getSourceFeatureId());
        assertThat(converted2.getVariableName()).isEqualTo(calculationInputValueDTO2.getVariableName());
        assertThat(converted2.getSourceMeasColName()).isEqualTo(calculationInputValueDTO2.getSourceMeasColName());
    }
}

