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
package eu.openanalytics.phaedra.protocolservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.exception.ProtocolNotFoundException;
import eu.openanalytics.phaedra.protocolservice.model.Protocol;
import eu.openanalytics.phaedra.protocolservice.repository.ProtocolRepository;
import eu.openanalytics.phaedra.protocolservice.support.Containers;
import eu.openanalytics.phaedra.util.auth.IAuthorizationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@Testcontainers
@SpringBootTest
@Sql({"/jdbc/test-data.sql"})
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProtocolServiceTest {
    @Autowired
    private ProtocolService protocolService;
    @Autowired
    private ProtocolRepository protocolRepository;
    @Autowired
    private IAuthorizationService authService;

    private MockRestServiceServer mockServer;
    private RestTemplate restTemplate = new RestTemplate();
    private ModelMapper modelMapper = new ModelMapper();
    private ObjectMapper objectMapper = new ObjectMapper();

    private static DockerImageName pgImage = DockerImageName.parse("registry.openanalytics.eu/library/postgres:13-alpine").asCompatibleSubstituteFor("postgres");
    @Container
    private static JdbcDatabaseContainer postgreSQLContainer = new PostgreSQLContainer(pgImage)
            .withDatabaseName("phaedra2")
            .withUrlParam("currentSchema", "plates")
            .withPassword("inmemory")
            .withUsername("inmemory");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("DB_URL", Containers.postgreSQLContainer::getJdbcUrl);
        registry.add("DB_USER", Containers.postgreSQLContainer::getUsername);
        registry.add("DB_PASSWORD", Containers.postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void before() {
        protocolService = new ProtocolService(restTemplate, protocolRepository, modelMapper, authService);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void createNewProtocol() throws IOException {
        String path = "src/test/resources/json/ProtocolWithFeaturesContainingDRCModel.json";
        File file = new File(path);

        ProtocolDTO protocolDTO = this.objectMapper.readValue(file, ProtocolDTO.class);

        ProtocolDTO created = protocolService.create(protocolDTO);
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getVersionNumber().matches("\\d.\\d.\\d-\\d{8}.\\d{6}")).isTrue();
        assertThat(created.getPreviousVersion()).isNull();
    }

    @Test
    void updateProtocol() throws IOException, ProtocolNotFoundException, InterruptedException {
        String path = "src/test/resources/json/ProtocolWithFeaturesContainingDRCModel.json";
        File file = new File(path);

        ProtocolDTO protocolDTO = this.objectMapper.readValue(file, ProtocolDTO.class);

        ProtocolDTO created = protocolService.create(protocolDTO);
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getVersionNumber().matches("\\d.\\d.\\d-\\d{8}.\\d{6}")).isTrue();
        assertThat(created.getPreviousVersion()).isNull();

        Thread.sleep(5000L);

        created.setName("NewProtocolName");
        created.setDescription("New protocol name");

        ProtocolDTO updated = protocolService.update(created);
        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(created.getId());
        assertThat(updated.getVersionNumber().matches("\\d.\\d.\\d-\\d{8}.\\d{6}")).isTrue();
        assertThat(updated.getVersionNumber()).isNotEqualTo(created.getVersionNumber());
        assertThat(updated.getPreviousVersion()).isEqualTo(created.getVersionNumber());;
    }

    @Test
    void getAllProtocols() {
        List<ProtocolDTO> allProtocols = protocolService.getProtocols();
        assertThat(allProtocols).isNotEmpty();
        assertThat(allProtocols.size()).isEqualTo(1);
    }

    @Test
    void getProtocolById() {
        long protocolId = 1000L;

        ProtocolDTO aProtocol = protocolService.getProtocolById(protocolId);
        assertThat(aProtocol).isNotNull();
        assertThat(aProtocol.getId()).isEqualTo(protocolId);

        ProtocolDTO nullProtocol = protocolService.getProtocolById(protocolId * 10);
        assertThat(nullProtocol).isNull();
    }

    @Test
    void deleteProtocol() {
        long protocolId = 1000L;
        protocolService.delete(protocolId);

        ProtocolDTO deleted = protocolService.getProtocolById(protocolId);
        assertThat(deleted).isNull();
    }

    @Test
    void tagProtocol() throws URISyntaxException {
        long protocolId = 1000L;

        mockServer.expect(requestTo(new URI("http://phaedra-metadata-service/phaedra/metadata-service/tags")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK));

        protocolService.tagProtocol(protocolId, "Protocol1000");
    }

    @Test
    void updateProtocolVersion() throws IOException {
        String path = "src/test/resources/json/ProtocolWithFeaturesContainingDRCModel.json";
        File file = new File(path);

        ProtocolDTO protocolDTO = this.objectMapper.readValue(file, ProtocolDTO.class);
        Protocol protocol = this.modelMapper.map(protocolDTO);

        Protocol updated = protocolService.updateVersion(protocol);
        assertThat(updated.getVersionNumber().matches("\\d.\\d.\\d-\\d{8}.\\d{6}")).isTrue();
        assertThat(updated.getPreviousVersion()).isNull();
    }

    @Test
    void updateProtocolVersionById() throws ProtocolNotFoundException {
        long protocolId = 1000L;

        ProtocolDTO protocolDTO = protocolService.getProtocolById(protocolId);
        protocolService.updateVersion(protocolId);
        ProtocolDTO updated = protocolService.getProtocolById(protocolId);

        assertThat(updated.getVersionNumber().matches("\\d.\\d.\\d-\\d{8}.\\d{6}")).isTrue();
        assertThat(updated.getPreviousVersion()).isEqualTo(protocolDTO.getVersionNumber());
    }
}
