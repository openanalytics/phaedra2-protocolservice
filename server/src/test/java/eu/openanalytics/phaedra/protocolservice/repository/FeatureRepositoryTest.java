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
package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.Feature;
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

@Testcontainers
@SpringBootTest
@Sql({"/jdbc/test-data.sql"})
@TestPropertySource(locations = "classpath:application-test.properties")
public class FeatureRepositoryTest {

    @Autowired
    private FeatureRepository featureRepository;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("DB_URL", Containers.postgreSQLContainer::getJdbcUrl);
        registry.add("DB_USER", Containers.postgreSQLContainer::getUsername);
        registry.add("DB_PASSWORD", Containers.postgreSQLContainer::getPassword);
    }

    @Test
    public void contextLoads() {
        assertThat(featureRepository).isNotNull();
    }

    @Test
    public void createFeature() {
        Feature newFeature = new Feature();
        newFeature.setProtocolId(1000L);
        newFeature.setName("TestFeature");
        newFeature.setAlias("TF");
        newFeature.setDescription("Insert here the feature description");
        newFeature.setFormat("#.##");
        newFeature.setFormulaId(1L); //Id of a predefined calculation formula

        Feature savedFeature = featureRepository.save(newFeature);

        assertThat(savedFeature).isNotNull();
        assertThat(savedFeature.getId()).isNotNull();
        assertThat(savedFeature.getProtocolId()).isEqualTo(savedFeature.getProtocolId());
    }

    @Test
    public void deleteFeature() {
        Long protocolId = 1000L;

        List<Feature> results1 = featureRepository.findByProtocolId(protocolId);
        assertThat(results1).isNotNull();
        assertThat(results1).isNotEmpty();

        featureRepository.delete(results1.get(0));

        List<Feature> results2 = featureRepository.findByProtocolId(protocolId);
        assertThat(results2).isNotNull();
        assertThat(results2).isNotEmpty();
        assertThat(results2.size()).isLessThan(results1.size());
    }

    @Test
    public void updateFeature() {
        Long protocolId = 1000L;

        List<Feature> result = featureRepository.findByProtocolId(protocolId);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();

        Feature feature = result.get(0);
        String newName = "New feature name";
        String newDescription = "New feature description";
        feature.setName(newName);
        feature.setDescription(newDescription);

        Feature updatedFeature = featureRepository.save(feature);
        assertThat(updatedFeature).isNotNull();
        assertThat(updatedFeature.getName()).isEqualTo(newName);
        assertThat(updatedFeature.getDescription()).isEqualTo(newDescription);
    }


    @Test
    public void getFeatureById() {
        Long featureId = 1000L;

        Optional<Feature> feature = featureRepository.findById(featureId);
        assertThat(feature).isNotNull();
        assertThat(feature.isPresent()).isTrue();
        assertThat(feature.get().getId()).isEqualTo(featureId);
    }

    @Test
    public void getFeatureForAGivenProtocolId() {
        Long protocolId = 1000L;

        List<Feature> features = featureRepository.findByProtocolId(protocolId);
        assertThat(features).isNotNull();
        assertThat(features).isNotEmpty();
        assertThat(features.stream().allMatch(f -> f.getProtocolId().equals(protocolId))).isTrue();
    }
}
