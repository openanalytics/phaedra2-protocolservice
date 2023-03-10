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

import eu.openanalytics.phaedra.protocolservice.model.CurveSetting;
import eu.openanalytics.phaedra.protocolservice.support.Containers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@Sql({"/jdbc/test-data.sql"})
@TestPropertySource(locations = "classpath:application-test.properties")
public class CurveSettingRepositoryTest {

    @Autowired
    private CurveSettingRepository drcPropertyRepository;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("DB_URL", Containers.postgreSQLContainer::getJdbcUrl);
        registry.add("DB_USER", Containers.postgreSQLContainer::getUsername);
        registry.add("DB_PASSWORD", Containers.postgreSQLContainer::getPassword);
    }

    @Test
    public void contextLoads() {
        assertThat(drcPropertyRepository).isNotNull();
    }

    @Test
    public void createDoseResponseCurveProperty() {
        CurveSetting testProperty1 = new CurveSetting();
        testProperty1.setFeatureId(1000L);
        testProperty1.setName("TestProperty1");
        testProperty1.setValue("TestPropertyValue1");

        CurveSetting result1 = drcPropertyRepository.save(testProperty1);
        assertThat(result1.getId()).isNotNull();
        assertThat(result1.getFeatureId()).isEqualTo(testProperty1.getFeatureId());
        assertThat(result1.getName()).isEqualTo(testProperty1.getName());
        assertThat(result1.getValue()).isEqualTo(testProperty1.getValue());

        CurveSetting testProperty2 = new CurveSetting();
        testProperty2.setFeatureId(1000L);
        testProperty2.setName("TestProperty2");
        testProperty2.setValue("TestPropertyValue2");

        CurveSetting result2 = drcPropertyRepository.save(testProperty2);
        assertThat(result2.getId()).isNotNull();
        assertThat(result2.getFeatureId()).isEqualTo(testProperty2.getFeatureId());
        assertThat(result2.getName()).isEqualTo(testProperty2.getName());
        assertThat(result2.getValue()).isEqualTo(testProperty2.getValue());

        CurveSetting testProperty3 = new CurveSetting();
        testProperty3.setFeatureId(1000L);
        testProperty3.setName("TestProperty3");
        testProperty3.setValue("TestPropertyValue3");

        CurveSetting result3 = drcPropertyRepository.save(testProperty3);
        assertThat(result3.getId()).isNotNull();
        assertThat(result3.getFeatureId()).isEqualTo(testProperty3.getFeatureId());
        assertThat(result3.getName()).isEqualTo(testProperty3.getName());
        assertThat(result3.getValue()).isEqualTo(testProperty3.getValue());
    }

    @Test
    public void findAllDoseResponseCurvePropertyByFeatureId() {
        CurveSetting testProperty1 = new CurveSetting();
        testProperty1.setFeatureId(1000L);
        testProperty1.setName("TestProperty1");
        testProperty1.setValue("TestPropertyValue1");
        drcPropertyRepository.save(testProperty1);

        CurveSetting testProperty2 = new CurveSetting();
        testProperty2.setFeatureId(1000L);
        testProperty2.setName("TestProperty2");
        testProperty2.setValue("TestPropertyValue2");
        drcPropertyRepository.save(testProperty2);

        CurveSetting testProperty3 = new CurveSetting();
        testProperty3.setFeatureId(1000L);
        testProperty3.setName("TestProperty3");
        testProperty3.setValue("TestPropertyValue3");
        drcPropertyRepository.save(testProperty3);

        List<CurveSetting> results = drcPropertyRepository.findAllByFeatureId(1000L);
        assertThat(results).isNotNull().isNotEmpty();
        assertThat(results.size()).isEqualTo(3);

    }

}
