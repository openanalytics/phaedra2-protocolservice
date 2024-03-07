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

import java.io.File;
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

import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.model.Feature;
import eu.openanalytics.phaedra.protocolservice.model.Protocol;
import eu.openanalytics.phaedra.protocolservice.support.Containers;

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
        newProtocol.setVersionNumber("1.0.0");

        String requestBody = objectMapper.writeValueAsString(newProtocol);
        MvcResult mvcResult = this.mockMvc.perform(post("/protocols").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        ProtocolDTO protocolDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProtocolDTO.class);
        assertThat(protocolDTO).isNotNull();
        assertThat(protocolDTO.getId()).isEqualTo(1);
        assertThat(protocolDTO.getPreviousVersion()).isNull();
        assertThat(protocolDTO.getVersionNumber().split("-")[0]).isEqualTo("1.0.0");
    }

    @Test
    public void createProtocolWithFeatures() throws Exception {
        String requestBody = "{ \"name\": \"Test\", \"description\": \"test\", \"lowWelltype\": \"LC\", \"highWellType\": null, \"versionNumber\": \"1.0.0\", \"features\": [ { \"name\": \"F1\", \"alias\": \"Feature 1\", \"description\": null, \"format\": \"#.##\", \"type\": \"CALCULATION\", \"sequence\": 0, \"protocolId\": null, \"formulaId\": 24, \"formula\": { \"id\": 24, \"name\": \"adder\", \"description\": null, \"category\": \"CALCULATION\", \"formula\": \"output <- input$dup_abc + input$dup_xyz + input$abc_xyz\", \"language\": \"R\", \"scope\": \"WELL\", \"previousVersion\": null, \"versionNumber\": \"1.0.0\", \"createdBy\": \"Anonymous\", \"createdOn\": \"2021-10-01T10:28:47.941165\", \"updatedBy\": null, \"updatedOn\": null }, \"civs\": [ { \"variableName\": \"abc_xyz\", \"sourceInput\": \"MEASUREMENT\", \"sourceMeasColName\": \"p1\", \"formulaId\": 24 }, { \"variableName\": \"dup_abc\", \"sourceInput\": \"MEASUREMENT\", \"sourceMeasColName\": \"p2\", \"formulaId\": 24 }, { \"variableName\": \"dup_xyz\", \"sourceInput\": \"MEASUREMENT\", \"sourceMeasColName\": \"p3\", \"formulaId\": 24 } ], \"trigger\": null } ], \"tags\": [], \"properties\": [], \"highWelltype\": \"HC\", \"createdOn\": \"2022-06-24T13:50:43.297Z\" }";
        ProtocolDTO newProtocolDTO = this.objectMapper.readValue(requestBody, ProtocolDTO.class);

        assertThat(newProtocolDTO.getFeatures().size()).isEqualTo(1);
        assertThat(newProtocolDTO.getFeatures().get(0).getCivs()).isNotEmpty();
        assertThat(newProtocolDTO.getFeatures().get(0).getCivs().size()).isEqualTo(3);

        requestBody = this.objectMapper.writeValueAsString(newProtocolDTO);

        MvcResult pResponse = this.mockMvc.perform(post("/protocols").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        ProtocolDTO protocolDTO = this.objectMapper.readValue(pResponse.getResponse().getContentAsString(), ProtocolDTO.class);
        assertThat(protocolDTO).isNotNull();
        assertThat(protocolDTO.getId()).isEqualTo(1);
        assertThat(protocolDTO.getPreviousVersion()).isNull();
        assertThat(protocolDTO.getVersionNumber().split("-")[0]).isEqualTo("1.0.0");

        MvcResult fResponse = this.mockMvc.perform(get("/protocols/" + protocolDTO.getId() + "/features"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<FeatureDTO> featureList = this.objectMapper.readValue(fResponse.getResponse().getContentAsString(), new TypeReference<>() {});

        for (FeatureDTO featureDTO: featureList) {
            MvcResult civResponse = this.mockMvc.perform(get("/features/" + featureDTO.getId() + "/calculationinputvalues"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            List<CalculationInputValueDTO> civList = this.objectMapper.readValue(civResponse.getResponse().getContentAsString(), new TypeReference<>() {});
            assertThat(civList).isNotEmpty();
        }
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
        ProtocolDTO protocol = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProtocolDTO.class);
        assertThat(protocol).isNotNull();
        assertThat(protocol.getId()).isEqualTo(protocolId);

        String newName = "New protocol name";
        protocol.setName(newName);
        String newDescription = "New protocol description";
        protocol.setDescription(newDescription);

        //Check number of features
        mvcResult = this.mockMvc.perform(get("/protocols/{protocolId}/features", protocolId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<Feature> features = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(features.size()).isEqualTo(6);

        String requestBody = objectMapper.writeValueAsString(protocol);
        this.mockMvc.perform(put("/protocols/{protocolId}", protocolId).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isOk());

        mvcResult = this.mockMvc.perform(get("/protocols/{protocolId}", protocolId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        ProtocolDTO updatedProtocol = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProtocolDTO.class);
        assertThat(updatedProtocol.getName()).isEqualTo(newName);
        assertThat(updatedProtocol.getDescription()).isEqualTo(newDescription);
        assertThat(updatedProtocol.getVersionNumber()).isNotEqualTo(protocol.getVersionNumber());
        assertThat(updatedProtocol.getPreviousVersion()).isEqualTo(protocol.getVersionNumber());

        //Check number of features
        mvcResult = this.mockMvc.perform(get("/protocols/{protocolId}/features", updatedProtocol.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        features = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(features.size()).isEqualTo(6);
    }

    @Test
    public void updateProtocol2() throws Exception {
        String path = "src/test/resources/json/UpdateProtocol2.json";
        File file = new File(path);

        ProtocolDTO protocolDTO = this.objectMapper.readValue(file, ProtocolDTO.class);
        assertThat(protocolDTO).isNotNull();

        String requestBody = objectMapper.writeValueAsString(protocolDTO);
        MvcResult mvcResult = this.mockMvc.perform(put("/protocols/{protocolId}", 1000L).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void updateProtocolName() throws Exception {
        String path = "src/test/resources/json/UpdateProtocol.json";
        File file = new File(path);

        ProtocolDTO protocolDTO = this.objectMapper.readValue(file, ProtocolDTO.class);
        assertThat(protocolDTO).isNotNull();

        String requestBody = objectMapper.writeValueAsString(protocolDTO);
        MvcResult mvcResult = this.mockMvc.perform(put("/protocols/{protocolId}", 1000L).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
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

    @Test
    public void updateProtocolDoesFeaturesGetCoppied() throws Exception {
        Protocol protocol = new Protocol();
        protocol.setName("New protocol name");
        protocol.setDescription("New protocol description");
        String newVersion = "2.0.0";
        protocol.setVersionNumber(newVersion);
        protocol.setId(1000L);

        // update protocol
        String requestBody = objectMapper.writeValueAsString(protocol);
        MvcResult res = this.mockMvc.perform(put("/protocols/{protocolId}", protocol.getId()).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Protocol updatedProtocol = objectMapper.readValue(res.getResponse().getContentAsString(), Protocol.class);
        assertThat(updatedProtocol.getId()).isEqualTo(protocol.getId());
        assertThat(updatedProtocol.getName()).isEqualTo(protocol.getName());
        assertThat(updatedProtocol.getDescription()).isEqualTo(protocol.getDescription());

        MvcResult mvcResult = this.mockMvc.perform(get("/protocols/{protocolId}/features", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<Feature> features = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(features).isNotNull();
        assertThat(features.stream().allMatch(f -> f.getProtocolId().equals(1L))).isTrue();
    }

    @Test
    public void createNewProtocolWithFeaturesContainingDRCModel() throws Exception {
        String path = "src/test/resources/json/ProtocolWithFeaturesContainingDRCModel.json";
        File file = new File(path);

        ProtocolDTO protocolDTO = this.objectMapper.readValue(file, ProtocolDTO.class);
        assertThat(protocolDTO).isNotNull();

        String requestBody = objectMapper.writeValueAsString(protocolDTO);
        MvcResult mvcResult = this.mockMvc.perform(post("/protocols").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        ProtocolDTO created = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProtocolDTO.class);
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();

        mvcResult = this.mockMvc.perform(get("/protocols/{protocolId}/features", created.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<FeatureDTO> featureDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(featureDTOs).isNotEmpty();
    }
}
