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
package eu.openanalytics.phaedra.protocolservice.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.model.Feature;
import eu.openanalytics.phaedra.protocolservice.model.FeatureStat;
import eu.openanalytics.phaedra.protocolservice.support.Containers;

@Testcontainers
@SpringBootTest
@Sql({"/jdbc/test-data.sql"})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class FeatureControllerTest {
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
    public void contextLoads() {
        assertThat(mockMvc).isNotNull();
        assertThat(objectMapper).isNotNull();
    }

    @Test
    public void createFeature() throws Exception {
        Feature newFeature = new Feature();
        newFeature.setName("A new feature");
        newFeature.setDescription("Creating a new feature");
        newFeature.setProtocolId(1000L);
        newFeature.setFormat("#.###");

        String requestBody = this.objectMapper.writeValueAsString(newFeature);

        MvcResult mvcResult = this.mockMvc.perform(post("/features")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        Feature createdFeature = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Feature.class);
        assertThat(createdFeature).isNotNull();
        assertThat(createdFeature.getId()).isNotNull();
        assertThat(createdFeature.getName()).isEqualTo(newFeature.getName());
        assertThat(createdFeature.getDescription()).isEqualTo(newFeature.getDescription());
    }

    @Test
    public void deleteFeature() throws Exception {
        Long featureId = 1000L;

        this.mockMvc.perform(delete("/features/{featureId}", featureId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void updateFeature() throws Exception {
        Long featureId = 3000L;

        // Retrieve the feature from the server
        MvcResult mvcResult = this.mockMvc.perform(get("/features/{featureId}", featureId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Feature feature = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Feature.class);
        assertThat(feature).isNotNull();
        assertThat(feature.getId()).isEqualTo(featureId);
        assertThat(feature.getProtocolId()).isNotNull();

        // Retrieve the protocol for the feature
        mvcResult = this.mockMvc.perform(get("/protocols/{protocolId}", feature.getProtocolId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        ProtocolDTO protocol = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProtocolDTO.class);
        assertThat(protocol).isNotNull();

        // Update the feature name and description
        String updatedName = "Updated feature name";
        String updateDescription = "Update feature description";
        feature.setName(updatedName);
        feature.setDescription(updateDescription);

        // Push the updated feature to the server
        String requestBody = objectMapper.writeValueAsString(feature);
        mvcResult = this.mockMvc.perform(put("/features/{featureId}", featureId).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Feature updatedFeature = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Feature.class);
        assertThat(updatedFeature).isNotNull();
        assertThat(updatedFeature.getName()).isEqualTo(feature.getName());
        assertThat(updatedFeature.getDescription()).isEqualTo(feature.getDescription());
        assertThat(updatedFeature.getProtocolId()).isEqualTo(feature.getProtocolId());

        // Get the protocol again from the server, this time the protocol update date and version should be different
        mvcResult = this.mockMvc.perform(get("/protocols/{protocolId}", updatedFeature.getProtocolId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        ProtocolDTO updatedProtocol = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProtocolDTO.class);
        assertThat(updatedProtocol.getVersionNumber()).isNotEqualTo(protocol.getVersionNumber());
        assertThat(updatedProtocol.getPreviousVersion()).isEqualTo(protocol.getVersionNumber());
        assertThat(updatedProtocol.getUpdatedBy()).isNotEqualTo(protocol.getUpdatedOn());
    }

    @Test
    public void getFeatureById() throws Exception {
        Long featureId = 3000L;

        MvcResult mvcResult = this.mockMvc.perform(get("/features/{featureId}", featureId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Feature feature = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Feature.class);
        assertThat(feature).isNotNull();
        assertThat(feature.getId()).isEqualTo(featureId);
    }

    @Test
    public void getFeaturesByFeatureGroupId() throws Exception{
        long groupId = 20L;

        MvcResult mvcResult = this.mockMvc.perform(get("/features").param("groupId", Long.toString(groupId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<Feature> result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    public void deleteCascadeTest() throws Exception {
        Feature newFeature = new Feature();
        newFeature.setName("A new feature");
        newFeature.setDescription("Creating a new feature");
        newFeature.setProtocolId(1000L);
        newFeature.setFormulaId(1L);
        newFeature.setFormat("#.###");

        String requestBody = this.objectMapper.writeValueAsString(newFeature);

        MvcResult mvcResult = this.mockMvc.perform(post("/features")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        Feature createdFeature = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Feature.class);
        assertThat(createdFeature).isNotNull();
        assertThat(createdFeature.getId()).isNotNull();
        assertThat(createdFeature.getName()).isEqualTo(newFeature.getName());
        assertThat(createdFeature.getDescription()).isEqualTo(newFeature.getDescription());

        MvcResult res = this.mockMvc.perform(get("/features/{featureId}/featurestats", createdFeature.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<FeatureStat> featureStats = this.objectMapper.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(featureStats.size()).isNotEqualTo(0);

        this.mockMvc.perform(delete("/features/{featureId}", createdFeature.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void createNewFeatureDoesNewVersionGetCreated() throws Exception {
        Feature newFeature = new Feature();
        newFeature.setName("A new feature");
        newFeature.setDescription("Creating a new feature");
        newFeature.setProtocolId(1000L);
        newFeature.setFormulaId(1L);
        newFeature.setFormat("#.###");

        String requestBody = this.objectMapper.writeValueAsString(newFeature);

        MvcResult mvcResult = this.mockMvc.perform(post("/features")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        Feature createdFeature = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Feature.class);
        assertThat(createdFeature).isNotNull();
        assertThat(createdFeature.getId()).isNotNull();
        assertThat(createdFeature.getName()).isEqualTo(newFeature.getName());
        assertThat(createdFeature.getDescription()).isEqualTo(newFeature.getDescription());
    }

    @Test
    public void updateFeatureDoesNewVersionGetCreated() throws Exception {
        Feature newFeature = new Feature();
        newFeature.setName("A new feature");
        newFeature.setId(1000L);
        newFeature.setProtocolId(1000L);

        // Update the feature
        String requestBody = this.objectMapper.writeValueAsString(newFeature);
        MvcResult mvcResult = this.mockMvc.perform(put("/features/{featureId}", 1000L).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Feature updatedFeature = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Feature.class);
        assertThat(updatedFeature).isNotNull();
        assertThat(updatedFeature.getName()).isEqualTo(newFeature.getName());
        assertThat(updatedFeature.getProtocolId()).isEqualTo(newFeature.getProtocolId());

        //Get features for new protocol
        MvcResult res = this.mockMvc.perform(get("/protocols/{protocolId}/features", updatedFeature.getProtocolId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<Feature> features = this.objectMapper.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(features.size()).isEqualTo(6);
    }
}
