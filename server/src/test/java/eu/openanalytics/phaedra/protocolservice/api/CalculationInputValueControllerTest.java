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
import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import eu.openanalytics.phaedra.protocolservice.model.CalculationInputValue;
import eu.openanalytics.phaedra.protocolservice.service.CalculationInputValueService;
import eu.openanalytics.phaedra.protocolservice.support.Containers;
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

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Testcontainers
@SpringBootTest
@Sql({"/jdbc/test-data.sql"})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CalculationInputValueControllerTest {

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
    public void CalculationInputValuePostTest() throws Exception {
        Long featureId = 1000L;
        CalculationInputValueDTO calculationInputValueDTO = CalculationInputValueDTO.builder().sourceMeasColName("Column1").variableName("col1").build();
        String requestBody = objectMapper.writeValueAsString(calculationInputValueDTO);
        MvcResult mvcResult = this.mockMvc.perform(post("/features/"+featureId+"/calculationinputvalue").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        CalculationInputValueDTO body = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CalculationInputValueDTO.class);
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(1L);
        assertThat(body.getSourceMeasColName()).isEqualTo("Column1");
        assertThat(body.getVariableName()).isEqualTo("col1");
    }

    @Test
    public void CalculationInputValuePutTest() throws Exception {
        Long featureId = 1000L;
        CalculationInputValueDTO calculationInputValueDTO = CalculationInputValueDTO.builder().sourceMeasColName("Column1").variableName("col1").build();
        String requestBody = objectMapper.writeValueAsString(calculationInputValueDTO);
        MvcResult mvcResult = this.mockMvc.perform(post("/features/"+featureId+"/calculationinputvalue").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        CalculationInputValueDTO body = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CalculationInputValueDTO.class);
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(1L);
        assertThat(body.getSourceMeasColName()).isEqualTo("Column1");
        assertThat(body.getVariableName()).isEqualTo("col1");

        CalculationInputValueDTO changed = CalculationInputValueDTO.builder().id(1L).sourceMeasColName("Column2").variableName("col2").featureId(1000L).build();
        String requestBody2 = objectMapper.writeValueAsString(changed);
        MvcResult mvcResult2 = this.mockMvc.perform(put("/features/"+featureId+"/calculationinputvalue").contentType(MediaType.APPLICATION_JSON).content(requestBody2))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        CalculationInputValueDTO body2 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), CalculationInputValueDTO.class);
        assertThat(body2).isNotNull();
        assertThat(body2.getId()).isEqualTo(1L);
        assertThat(body2.getSourceMeasColName()).isEqualTo("Column2");
        assertThat(body2.getVariableName()).isEqualTo("col2");
    }

    @Test
    public void CalculationInputValueGetFoundTest() throws Exception {
        Long featureId = 1000L;
        CalculationInputValueDTO calculationInputValueDTO = CalculationInputValueDTO.builder().sourceMeasColName("Column1").variableName("col1").build();
        String requestBody = objectMapper.writeValueAsString(calculationInputValueDTO);
        MvcResult mvcResult = this.mockMvc.perform(post("/features/"+featureId+"/calculationinputvalue").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        CalculationInputValueDTO body = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CalculationInputValueDTO.class);
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(1L);
        assertThat(body.getSourceMeasColName()).isEqualTo("Column1");
        assertThat(body.getVariableName()).isEqualTo("col1");

        MvcResult mvcResult2 = this.mockMvc.perform(get("/features/"+featureId+"/calculationinputvalue"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<CalculationInputValueDTO> body2 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), List.class);
        assertThat(body2).isNotNull();
        assertThat(body2.size()).isEqualTo(1);
        CalculationInputValueDTO ret = objectMapper.convertValue(body2.get(0), CalculationInputValueDTO.class);
        assertThat(ret.getId()).isEqualTo(1L);
        assertThat(ret.getSourceMeasColName()).isEqualTo("Column1");
        assertThat(ret.getVariableName()).isEqualTo("col1");
    }

    @Test
    public void CalculationInputValueGetNotFoundTest() throws Exception {
        Long featureId = 1000L;

        MvcResult mvcResult2 = this.mockMvc.perform(get("/features/"+featureId+"/calculationinputvalue"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<CalculationInputValueDTO> body2 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), new TypeReference<List<CalculationInputValueDTO>>() {});
        assertThat(body2).isEmpty();
    }

    @Test
    public void CalculationInputValueGetByProtocolIdFoundTest() throws Exception {
        Long protocolId = 1000L;
        Long featureId = 3000L;
        CalculationInputValueDTO calculationInputValueDTO = CalculationInputValueDTO.builder().sourceMeasColName("Column1").variableName("col1").build();
        String requestBody = objectMapper.writeValueAsString(calculationInputValueDTO);
        MvcResult mvcResult = this.mockMvc.perform(post("/features/"+featureId+"/calculationinputvalue").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        CalculationInputValueDTO body = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CalculationInputValueDTO.class);
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(1L);
        assertThat(body.getSourceMeasColName()).isEqualTo("Column1");
        assertThat(body.getVariableName()).isEqualTo("col1");

        MvcResult mvcResult2 = this.mockMvc.perform(get("/protocols/"+protocolId+"/calculationinputvalue"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<CalculationInputValueDTO> body2 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), List.class);
        assertThat(body2).isNotNull();
        assertThat(body2.size()).isEqualTo(1);
        CalculationInputValueDTO ret = objectMapper.convertValue(body2.get(0), CalculationInputValueDTO.class);
        assertThat(ret.getId()).isEqualTo(1L);
        assertThat(ret.getSourceMeasColName()).isEqualTo("Column1");
        assertThat(ret.getVariableName()).isEqualTo("col1");
    }

    @Test
    public void CalculationInputValueGetByProtocolIdNotFoundTest() throws Exception {
        Long protocolId = 1000L;

        MvcResult mvcResult2 = this.mockMvc.perform(get("/features/"+protocolId+"/calculationinputvalue"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<CalculationInputValueDTO> body2 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), new TypeReference<List<CalculationInputValueDTO>>() {});
        assertThat(body2).isEmpty();
    }
}
