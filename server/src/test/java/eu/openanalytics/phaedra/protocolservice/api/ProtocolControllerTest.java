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
package eu.openanalytics.phaedra.protocolservice.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.model.Feature;
import eu.openanalytics.phaedra.protocolservice.model.Protocol;
import eu.openanalytics.phaedra.protocolservice.support.Containers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@Sql({"/jdbc/test-data.sql"})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProtocolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("DB_URL", Containers.postgreSQLContainer::getJdbcUrl);
        registry.add("DB_USER", Containers.postgreSQLContainer::getUsername);
        registry.add("DB_PASSWORD", Containers.postgreSQLContainer::getPassword);
    }

    @Test
    public void createProtocol() throws Exception {
        Protocol newProtocol = new Protocol();
        newProtocol.setName("A new protocol");
        newProtocol.setDescription("Newly created protocol");
        newProtocol.setLowWelltype("LC");
        newProtocol.setHighWelltype("HC");
        newProtocol.setVersionNumber("1.0");

        String requestBody = objectMapper.writeValueAsString(newProtocol);
        MvcResult mvcResult = this.mockMvc.perform(post("/protocols").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        ProtocolDTO protocolDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProtocolDTO.class);
        assertThat(protocolDTO).isNotNull();
        assertThat(protocolDTO.getId()).isEqualTo(1);
        assertThat(protocolDTO.getPreviousVersion()).isNull();
        assertThat(protocolDTO.getVersionNumber().split("-")[0]).isEqualTo("1.0");
    }

    @Test
    public void deleteProtocol() throws Exception {
        Long protocolId = 1000L;

        this.mockMvc.perform(delete("/protocols/{protocolId}", protocolId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void updateProtocol() throws Exception {
        Long protocolId = 1000L;

        MvcResult mvcResult = this.mockMvc.perform(get("/protocols/{protocolId}", protocolId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Protocol protocol = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Protocol.class);
        assertThat(protocol).isNotNull();
        assertThat(protocol.getId()).isEqualTo(protocolId);

        String newName = "New protocol name";
        protocol.setName(newName);
        String newDescription = "New protocol description";
        protocol.setDescription(newDescription);
        protocol.setPreviousVersion(protocol.getVersionNumber());
        String newVersion = "2.0";
        protocol.setVersionNumber(newVersion);

        //Check number of features
        mvcResult = this.mockMvc.perform(get("/protocols/{protocolId}/features", protocolId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<Feature> features = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(features.size()).isEqualTo(6);

        String requestBody = objectMapper.writeValueAsString(protocol);
        this.mockMvc.perform(put("/protocols").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isOk());

        mvcResult = this.mockMvc.perform(get("/protocols/{protocolId}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Protocol updatedProtocol = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Protocol.class);
        assertThat(updatedProtocol.getName()).isEqualTo(newName);
        assertThat(updatedProtocol.getDescription()).isEqualTo(newDescription);
        assertThat(updatedProtocol.getPreviousVersion()).isEqualTo(protocol.getPreviousVersion());
        assertThat(updatedProtocol.getVersionNumber().split("-")[0]).isEqualTo(newVersion);

        //Check number of features
        mvcResult = this.mockMvc.perform(get("/protocols/{protocolId}/features", updatedProtocol.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        features = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(features.size()).isEqualTo(6);
    }

    @Test
    public void getAllProtocols() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/protocols"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<Protocol> results = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(results).isNotNull();
        assertThat(results).isNotEmpty();
    }

    @Test
    public void getProtocolById() throws Exception{
        Long protocolId = 1000L;

        MvcResult mvcResult = this.mockMvc.perform(get("/protocols/{protocolId}", protocolId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Protocol result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Protocol.class);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(protocolId);
    }

    @Test
    public void getFeaturesByProtocolId() throws Exception {
        Long protocolId = 1000L;

        MvcResult mvcResult = this.mockMvc.perform(get("/protocols/{protocolId}/features", protocolId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<Feature> features = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(features).isNotNull();
        assertThat(features.stream().allMatch(f -> f.getProtocolId().equals(protocolId))).isTrue();
    }
}
