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

import eu.openanalytics.phaedra.protocolservice.enumeration.InputSource;
import eu.openanalytics.phaedra.protocolservice.model.CalculationInputValue;
import eu.openanalytics.phaedra.protocolservice.support.Containers;
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

//TODO: Update tests that includes tests for inputSource
@Testcontainers
@SpringBootTest
@Sql({"/jdbc/test-data.sql"})
@TestPropertySource(locations = "classpath:application-test.properties")
public class CalculationInputValueRepositoryTest {

    @Autowired
    private CalculationInputValueRepository calculationInputValueRepository;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("DB_URL", Containers.postgreSQLContainer::getJdbcUrl);
        registry.add("DB_USER", Containers.postgreSQLContainer::getUsername);
        registry.add("DB_PASSWORD", Containers.postgreSQLContainer::getPassword);
    }

    @Test
    public void contextLoads() {
        assertThat(calculationInputValueRepository).isNotNull();
    }

    @Test
    public void createCalculationInputValue() {
        CalculationInputValue calculationInputValue = new CalculationInputValue(null,1000L, 1000L,"Column1",1000L,"col1", InputSource.MEASUREMENT);

        CalculationInputValue saved = calculationInputValueRepository.save(calculationInputValue);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVariableName()).isEqualTo(calculationInputValue.getVariableName());
    }

    @Test
    public void deleteCalculationInputValue() {
        CalculationInputValue calculationInputValue = new CalculationInputValue(null,1000L, 1000L,"Column1",1000L,"col1", InputSource.FEATURE);

        CalculationInputValue saved = calculationInputValueRepository.save(calculationInputValue);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVariableName()).isEqualTo(calculationInputValue.getVariableName());

        calculationInputValueRepository.delete(saved);

        Optional<CalculationInputValue> deleted = calculationInputValueRepository.findById(saved.getId());
        assertThat(deleted).isEmpty();
        assertThat(deleted.isPresent()).isFalse();
    }

    @Test
    public void updateCalculationInputValue() {
        //Add new civ
        CalculationInputValue calculationInputValue = new CalculationInputValue(null,1000L, 1000L,"Column1",1000L,"col1", InputSource.FEATURE);

        CalculationInputValue saved = calculationInputValueRepository.save(calculationInputValue);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVariableName()).isEqualTo(calculationInputValue.getVariableName());

        CalculationInputValue updated = new CalculationInputValue(saved.getId(),saved.getFeatureId(), 1000L,"Column2",saved.getSourceFeatureId(),"col2", InputSource.FEATURE);
        CalculationInputValue saved2 = calculationInputValueRepository.save(updated);

        assertThat(saved2).isNotNull();
        assertThat(saved2.getVariableName()).isEqualTo("col2");
        assertThat(saved2.getSourceMeasColName()).isEqualTo("Column2");
    }

    @Test
    public void getCalculatioInputValueById() {
        //Add new civ
        CalculationInputValue calculationInputValue = new CalculationInputValue(null,1000L, 1000L,"Column1",1000L,"col1", InputSource.FEATURE);

        CalculationInputValue saved = calculationInputValueRepository.save(calculationInputValue);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVariableName()).isEqualTo(calculationInputValue.getVariableName());

        CalculationInputValue get = calculationInputValueRepository.findById(saved.getId()).get();
        assertThat(get).isNotNull();
        assertThat(get.getId()).isNotNull();
        assertThat(get.getVariableName()).isEqualTo(calculationInputValue.getVariableName());
    }

    @Test
    public void getCalculationInputValueByFeatureId() {
        CalculationInputValue calculationInputValue = new CalculationInputValue(null,1000L, 1000L,"Column1",1000L,"col1", InputSource.FEATURE);

        CalculationInputValue saved = calculationInputValueRepository.save(calculationInputValue);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVariableName()).isEqualTo(calculationInputValue.getVariableName());

        List<CalculationInputValue> get = calculationInputValueRepository.findByFeatureIdAndFormulaId(saved.getFeatureId(), saved.getFormulaId());
        assertThat(get).isNotNull();
        assertThat(get.size()).isEqualTo(1);
        assertThat(get.get(0).getId()).isNotNull();
        assertThat(get.get(0).getVariableName()).isEqualTo(calculationInputValue.getVariableName());

    }

    @Test
    public void getCalculationInputValueByProtocolId() {
        CalculationInputValue calculationInputValue = new CalculationInputValue(null,1000L, 1000L,"Column1",1000L,"col1", InputSource.FEATURE);

        CalculationInputValue saved = calculationInputValueRepository.save(calculationInputValue);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVariableName()).isEqualTo(calculationInputValue.getVariableName());

        List<CalculationInputValue> get = calculationInputValueRepository.findByProtocolId(1000L);
        assertThat(get).isNotNull();
        assertThat(get.size()).isEqualTo(1);
        assertThat(get.get(0).getId()).isNotNull();
        assertThat(get.get(0).getVariableName()).isEqualTo(calculationInputValue.getVariableName());

    }
}
