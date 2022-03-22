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

import eu.openanalytics.phaedra.protocolservice.model.Protocol;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@SpringBootTest
@Sql({"/jdbc/test-data.sql"})
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProtocolRepositoryTest {

    @Autowired
    private ProtocolRepository protocolRepository;

    @Container
    private static JdbcDatabaseContainer postgreSQLContainer = new PostgreSQLContainer("postgres:13-alpine")
            .withDatabaseName("phaedra2")
            .withUrlParam("currentSchema","protocols")
            .withPassword("inmemory")
            .withUsername("inmemory");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("DB_URL", Containers.postgreSQLContainer::getJdbcUrl);
        registry.add("DB_USER", Containers.postgreSQLContainer::getUsername);
        registry.add("DB_PASSWORD", Containers.postgreSQLContainer::getPassword);
    }

    @Test
    public void contextLoads() {
        assertThat(protocolRepository).isNotNull();
    }

    @Test
    public void getProtocols() {
        Iterable<Protocol> allProtocols = protocolRepository.findAll();
        assertThat(allProtocols).isNotNull();
        assertThat(allProtocols).isNotEmpty();
    }

    @Test
    public void getProtocolById() {
        Optional<Protocol> protocol = protocolRepository.findById(1000L);
        assertThat(protocol.isPresent()).isTrue();
    }

    @Test
    public void createNewProtocol() {
        Protocol newProtocol = new Protocol();
        newProtocol.setName("Test");
        newProtocol.setDescription("Test");
        newProtocol.setLowWelltype("NC");
        newProtocol.setHighWelltype("PC");
        newProtocol.setVersionNumber("1.0-20220322.115833");

        Protocol savedProtocol = protocolRepository.save(newProtocol);

        assertThat(savedProtocol).isNotNull();
        assertThat(savedProtocol.getId()).isNotNull();
        assertThat(savedProtocol.getName()).isEqualTo(newProtocol.getName());
    }
}
