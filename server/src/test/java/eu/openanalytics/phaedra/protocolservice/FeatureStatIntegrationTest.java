/**
 * Phaedra II
 *
 * Copyright (C) 2016-2025 Open Analytics
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
package eu.openanalytics.phaedra.protocolservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureStatDTO;
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.enumeration.FeatureType;
import eu.openanalytics.phaedra.protocolservice.support.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.assertj.core.api.Assertions.assertThat;

@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class FeatureStatIntegrationTest extends AbstractIntegrationTest {


    @Test
    public void simpleCreateAndGetTest() throws Exception {
        // 1. create simple Feature
        var input1 = FeatureDTO.builder()
                .formulaId(2L)
                .protocolId(1000L)
                .name("example_feature")
                .format("test")
                .sequence(1)
                .type(FeatureType.CALCULATION)
                .trigger("abc")
                .build();

        var featureDTO = performRequest(post("/features", input1), HttpStatus.CREATED, FeatureDTO.class);

        // 2. create simple FeatureStat
        var input2 = FeatureStatDTO.builder()
                .formulaId(1L)
                .plateStat(true)
                .welltypeStat(false)
                .name("count")
                .build();

        var res2 = performRequest(post("/features/" + featureDTO.getId() + "/featurestats", input2), HttpStatus.CREATED, FeatureStatDTO.class);

        assertThat(res2.getId()).isNotNull();
        assertThat(res2.getFeatureId()).isEqualTo(featureDTO.getId());
        assertThat(res2.getPlateStat()).isTrue();
        assertThat(res2.getWelltypeStat()).isFalse();
        assertThat(res2.getFormulaId()).isEqualTo(input2.getFormulaId());
        assertThat(res2.getName()).isEqualTo(input2.getName());

        // 3. get specific FeatureStat
        var res3 = performRequest(get("/features/" + featureDTO.getId() + "/featurestats/" + res2.getId()), HttpStatus.OK, FeatureStatDTO.class);
        assertThat(res3.getId()).isEqualTo(res2.getId());
        assertThat(res3.getFeatureId()).isEqualTo(featureDTO.getId());
        assertThat(res3.getPlateStat()).isTrue();
        assertThat(res3.getWelltypeStat()).isFalse();
        assertThat(res3.getFormulaId()).isEqualTo(res2.getFormulaId());
        assertThat(res3.getName()).isEqualTo(res2.getName());

        // 4. Delete FeatureStat
        performRequest(delete("/features/" + featureDTO.getId() + "/featurestats/" + res3.getId()), HttpStatus.NO_CONTENT);

        // 6. get FeatureStat again
        var featureStatId = 10;
        var res5 = performRequest(get("/features/" + featureDTO.getId() + "/featurestats/" + featureStatId), HttpStatus.NOT_FOUND);
        assertThat(res5).isEqualTo("{\"error\":\"FeatureStat with id " + featureStatId + " not found!\",\"status\":\"error\"}");
    }

    @Test
    public void updateFeatureStat() throws Exception {
        // 1. create simple Feature
        var input1 = FeatureDTO.builder()
                .formulaId(2L)
                .protocolId(1000L)
                .name("example_feature")
                .format("test")
                .sequence(1)
                .type(FeatureType.CALCULATION)
                .trigger("abc")
                .build();

        var featureDTO = performRequest(post("/features", input1), HttpStatus.CREATED, FeatureDTO.class);

        // 2. create simple FeatureStat
        var input2 = FeatureStatDTO.builder()
                .formulaId(1L)
                .plateStat(true)
                .welltypeStat(false)
                .name("count")
                .build();

        var featureStatDTO1 = performRequest(post("/features/1/featurestats", input2), HttpStatus.CREATED, FeatureStatDTO.class);

        assertThat(featureStatDTO1.getId()).isNotNull();
        assertThat(featureStatDTO1.getFeatureId()).isEqualTo(featureDTO.getId());
        assertThat(featureStatDTO1.getPlateStat()).isTrue();
        assertThat(featureStatDTO1.getWelltypeStat()).isFalse();
        assertThat(featureStatDTO1.getFormulaId()).isEqualTo(input2.getFormulaId());
        assertThat(featureStatDTO1.getName()).isEqualTo(input2.getName());

        // 3. update FeatureStat
        var input3 = FeatureStatDTO.builder()
                .id(featureStatDTO1.getId())
                .featureId(featureDTO.getId())
                .formulaId(10L)
                .plateStat(false)
                .welltypeStat(true)
                .name("count-updated")
                .build();
        var featureStatDTO2 = performRequest(put("/features/" + featureDTO.getId() + "/featurestats/" + featureStatDTO1.getId(), input3), HttpStatus.OK, FeatureStatDTO.class);

        assertThat(featureStatDTO2.getId()).isEqualTo(featureStatDTO1.getId());
        assertThat(featureStatDTO2.getFeatureId()).isEqualTo(featureDTO.getId());
        Assertions.assertFalse(featureStatDTO2.getPlateStat());
        Assertions.assertTrue(featureStatDTO2.getWelltypeStat());
        Assertions.assertEquals(10, featureStatDTO2.getFormulaId());
        Assertions.assertEquals("count-updated", featureStatDTO2.getName());
    }

//    @Test
//    TODO: refactor this test
    public void queryFeatureStatsTests() throws Exception {
        // 1. create two protocols with two features and two featureStats
        for (int i = 1; i <= 2; i++) {
            var input1 = ProtocolDTO.builder()
                    .name("MyProtocol")
                    .description("MyProtocol")
                    .highWelltype("HC")
                    .lowWelltype("LC")
                    .versionNumber("1.0")
                    .build();
            performRequest(post("/protocols", input1), HttpStatus.CREATED);

            var input2 = FeatureDTO.builder()
                    .formulaId(2L)
                    .protocolId((long) i)
                    .name("example_feature1")
                    .format("test")
                    .sequence(1)
                    .type(FeatureType.CALCULATION)
                    .trigger("abc")
                    .build();

            var res2 = performRequest(post("/features", input2), HttpStatus.CREATED, FeatureDTO.class);

            var input3 = FeatureStatDTO.builder()
                    .formulaId(1L)
                    .plateStat(true)
                    .welltypeStat(false)
                    .name("count")
                    .build();

            performRequest(post("/features/" + res2.getId() + " /featurestat", input3), HttpStatus.CREATED, FeatureStatDTO.class);

            var input4 = FeatureDTO.builder()
                    .formulaId(2L)
                    .protocolId((long) i)
                    .name("example_feature2")
                    .format("test2")
                    .sequence(1)
                    .type(FeatureType.CALCULATION)
                    .trigger("ab2c")
                    .build();

            var res4 = performRequest(post("/features", input4), HttpStatus.CREATED, FeatureDTO.class);

            var input5 = FeatureStatDTO.builder()
                    .formulaId(1L)
                    .plateStat(true)
                    .welltypeStat(false)
                    .name("count")
                    .build();

            performRequest(post("/features/" + res4.getId() + " /featurestat", input5), HttpStatus.CREATED);
        }
        ObjectMapper mapper = new ObjectMapper();
        // 2. get all featureStats of feature 1
        var res2 = performRequest(get("/features/1/featurestat"), HttpStatus.OK, List.class);
        Assertions.assertEquals(2, res2.size());
        //Map res2 hashmap list to FeatureStatDTO list
        List<FeatureStatDTO> featureStatDTOs = mapper.convertValue(res2, new TypeReference<>() {});
        var f1 = featureStatDTOs.stream().filter(f -> f.getId() == 1).findFirst().get();
        Assertions.assertEquals(1, f1.getFeatureId());
        Assertions.assertTrue(f1.getPlateStat());
        Assertions.assertTrue(f1.getWelltypeStat());
        Assertions.assertEquals(1, f1.getFormulaId());
        Assertions.assertEquals("test", f1.getName());
        var f2 = featureStatDTOs.stream().filter(f -> f.getId() == 2).findFirst().get();
        Assertions.assertEquals(1, f2.getFeatureId());
        Assertions.assertTrue(f2.getPlateStat());
        Assertions.assertFalse(f2.getWelltypeStat());
        Assertions.assertEquals(1, f2.getFormulaId());
        Assertions.assertEquals("count", f2.getName());

        // 3. get all featureStats of feature 4
        var res3 = performRequest(get("/features/4/featurestat"), HttpStatus.OK, List.class);
        Assertions.assertEquals(2, res3.size());
        //Map res3 hashmap list to FeatureStatDTO list
        featureStatDTOs = mapper.convertValue(res3, new TypeReference<>() {});
        var f3 = featureStatDTOs.stream().filter(f -> f.getId() == 6).findFirst().get();
        Assertions.assertEquals(4, f3.getFeatureId());
        Assertions.assertTrue(f3.getPlateStat());
        Assertions.assertTrue(f3.getWelltypeStat());
        Assertions.assertEquals(1, f3.getFormulaId());
        Assertions.assertEquals("test", f3.getName());
        var f4 = featureStatDTOs.stream().filter(f -> f.getId() == 7).findFirst().get();
        Assertions.assertEquals(4, f4.getFeatureId());
        Assertions.assertTrue(f4.getPlateStat());
        Assertions.assertFalse(f4.getWelltypeStat());
        Assertions.assertEquals(1, f4.getFormulaId());
        Assertions.assertEquals("count", f4.getName());

        // 4. get all featureStats by protocol 1
        var res4 = performRequest(get("/protocols/3/featurestat"), HttpStatus.OK, List.class);
        Assertions.assertEquals(2, res4.size());
        //Map res4 hashmap list to FeatureStatDTO list
        featureStatDTOs = mapper.convertValue(res4, new TypeReference<>() {});
        var f5 = featureStatDTOs.stream().filter(f -> f.getId() == 3).findFirst().get();
        Assertions.assertEquals(2, f5.getFeatureId());
        Assertions.assertTrue(f5.getPlateStat());
        Assertions.assertTrue(f5.getWelltypeStat());
        Assertions.assertEquals(1, f5.getFormulaId());
        Assertions.assertEquals("test", f5.getName());
        var f6 = featureStatDTOs.stream().filter(f -> f.getId() == 4).findFirst().get();
        Assertions.assertEquals(2, f6.getFeatureId());
        Assertions.assertTrue(f6.getPlateStat());
        Assertions.assertFalse(f6.getWelltypeStat());
        Assertions.assertEquals(1, f6.getFormulaId());
        Assertions.assertEquals("count", f6.getName());

        // 5. get all featureStats by protocol 2
        var res5 = performRequest(get("/protocols/2/featurestat"), HttpStatus.OK, List.class);
        Assertions.assertEquals(2, res5.size());
        //Map res5 hashmap list to FeatureStatDTO list
        featureStatDTOs = mapper.convertValue(res5, new TypeReference<>() {});
        var f7 = featureStatDTOs.stream().filter(f -> f.getId() == 1).findFirst().get();
        Assertions.assertEquals(1, f7.getFeatureId());
        Assertions.assertTrue(f7.getPlateStat());
        Assertions.assertTrue(f7.getWelltypeStat());
        Assertions.assertEquals(1, f7.getFormulaId());
        Assertions.assertEquals("test", f7.getName());
        var f8 = featureStatDTOs.stream().filter(f -> f.getId() == 2).findFirst().get();
        Assertions.assertEquals(1, f8.getFeatureId());
        Assertions.assertTrue(f8.getPlateStat());
        Assertions.assertFalse(f8.getWelltypeStat());
        Assertions.assertEquals(1, f8.getFormulaId());
        Assertions.assertEquals("count", f8.getName());

        // 6. get all featureStats of non-existing feature
        var res6 = performRequest(get("/features/42/featurestat"), HttpStatus.NOT_FOUND);
        Assertions.assertEquals("{\"error\":\"Feature with id 42 not found!\",\"status\":\"error\"}", res6);

        // 7. get all featureStats of non-existing feature
        var res7 = performRequest(get("/protocols/42/featurestat"), HttpStatus.NOT_FOUND);
        Assertions.assertEquals("{\"error\":\"Protocol with id 42 not found!\",\"status\":\"error\"}", res7);

        // 8. get feature by correct featureStatId but wrong feature id
        var res8 = performRequest(get("/features/1/featurestat/4"), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals("{\"error\":\"The provided featureId is incorrect for this FeatureStat\",\"status\":\"error\"}", res8);
    }

    @Test
    public void createValidationTest() throws Exception {
        // 1. create FeatureStat for non-existing FeatureStat
        var input1 = FeatureStatDTO.builder()
                .formulaId(1L)
                .plateStat(true)
                .welltypeStat(false)
                .name("count")
                .build();

        var res1 = performRequest(post("/features/42/featurestats", input1), HttpStatus.NOT_FOUND);
        Assertions.assertEquals("{\"error\":\"Feature with id 42 not found!\",\"status\":\"error\"}", res1);

        // 2. create simple feature
        var input2 = FeatureDTO.builder()
                .formulaId(2L)
                .protocolId(1000L)
                .name("example_feature")
                .format("test")
                .sequence(1)
                .type(FeatureType.CALCULATION)
                .trigger("abc")
                .build();

        performRequest(post("/features", input2), HttpStatus.CREATED, FeatureDTO.class);

        // 3. test missing fields
        var input3 = FeatureStatDTO.builder().build();

        var res3 = performRequest(post("/features/1/featurestats", input3), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals("{\"error\":\"Validation error\",\"malformed_fields\":{" +
                "\"formulaId\":\"FormulaId is mandatory\"," +
                "\"name\":\"Name is mandatory\"," +
                "\"plateStat\":\"PlateStat is mandatory\"," +
                "\"valid\":\"Both plateStat and welltypeStat cannot be false\"," +
                "\"welltypeStat\":\"WelltypeStat is mandatory\"},\"status\":\"error\"}", res3);

        // 4. too many fields
        var input4 = FeatureStatDTO.builder()
                .id(10L)
                .featureId(10L)
                .formulaId(1L)
                .plateStat(true)
                .welltypeStat(false)
                .name("count")
                .build();

        var res4 = performRequest(post("/features/1/featurestats", input4), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals("{\"error\":\"Validation error\",\"malformed_fields\":{" +
                "\"featureId\":\"FeatureId must be specified in URL and not repeated in body\"," +
                "\"id\":\"Id must be null when creating a FeatureStat\"},\"status\":\"error\"}", res4);

        // 5. both plateStat and wellTypeStat false
        var input5 = FeatureStatDTO.builder()
                .formulaId(1L)
                .plateStat(false)
                .welltypeStat(false)
                .name("count")
                .build();

        var res5 = performRequest(post("/features/1/featurestats", input5), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals("{\"error\":\"Validation error\",\"malformed_fields\":{\"valid\":\"Both plateStat and welltypeStat cannot be false\"},\"status\":\"error\"}", res5);

        // 6. duplicate by name for the same featureId
        var input6 = FeatureStatDTO.builder()
                .formulaId(10L)
                .plateStat(true)
                .welltypeStat(true)
                .name("count")
                .build();

        performRequest(post("/features/1/featurestats", input6), HttpStatus.CREATED);

        var input7= FeatureStatDTO.builder()
                .formulaId(1L)
                .plateStat(true)
                .welltypeStat(false)
                .name("count")
                .build();

        var res7 = performRequest(post("/features/1/featurestats", input7), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals("{\"error\":\"FeatureStat with one of these parameters already exists!\",\"status\":\"error\"}", res7);
    }

    @Test
    public void updateValidationTest() throws Exception {
        // 1. mismatch between featureStatId in URL and body
        var input1 = FeatureStatDTO.builder()
                .id(42L)
                .featureId(1L)
                .formulaId(1L)
                .plateStat(true)
                .welltypeStat(false)
                .name("count")
                .build();
        var res1 = performRequest(put("/features/1/featurestats/1", input1), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals("{\"error\":\"The featureStatId provided in the URL is not equal to the id provided in the body\",\"status\":\"error\"}", res1);

        // 2. mismatch between featureId in URL and body
        var input2 = FeatureStatDTO.builder()
                .id(1L)
                .featureId(421L)
                .formulaId(1L)
                .plateStat(true)
                .welltypeStat(false)
                .name("count")
                .build();
        var res2 = performRequest(put("/features/1/featurestats/1", input2), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals("{\"error\":\"The featureId provided in the URL is not equal to the featureId provided in the body\",\"status\":\"error\"}", res2);

        // 3. create simple Feature
        var input3 = FeatureDTO.builder()
                .formulaId(2L)
                .protocolId(1000L)
                .name("example_feature")
                .format("test")
                .sequence(1)
                .type(FeatureType.CALCULATION)
                .trigger("abc")
                .build();

        performRequest(post("/features", input3), HttpStatus.CREATED, FeatureDTO.class);

        // 4. create simple FeatureStat
        var input4 = FeatureStatDTO.builder()
                .formulaId(1L)
                .plateStat(true)
                .welltypeStat(false)
                .name("count")
                .build();

        performRequest(post("/features/1/featurestats", input4), HttpStatus.CREATED, FeatureStatDTO.class);

        // 5. missing fields
        var input5 = FeatureStatDTO.builder().build();
        var res5 = performRequest(put("/features/1/featurestats/1", input5), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals("{\"error\":\"Validation error\",\"malformed_fields\":{" +
                "\"featureId\":\"FeatureId must be specified when updating a FeatureStat\"," +
                "\"formulaId\":\"FormulaId is mandatory\"," +
                "\"id\":\"Id must be specified when updating a FeatureStat\"," +
                "\"name\":\"Name is mandatory\"," +
                "\"plateStat\":\"PlateStat is mandatory\"," +
                "\"valid\":\"Both plateStat and welltypeStat cannot be false\"," +
                "\"welltypeStat\":\"WelltypeStat is mandatory\"" +
                "},\"status\":\"error\"}", res5);

        // 6. update non-existing featurestat
        var input6 = FeatureStatDTO.builder()
                .id(10L)
                .featureId(10L)
                .formulaId(1L)
                .plateStat(true)
                .welltypeStat(false)
                .name("count")
                .build();
        var res6 = performRequest(put("/features/10/featurestats/10", input6), HttpStatus.NOT_FOUND);
        Assertions.assertEquals("{\"error\":\"FeatureStat with id 10 not found!\",\"status\":\"error\"}", res6);

        // 7. try to change featureId
        var input7 = FeatureStatDTO.builder()
                .id(1L)
                .featureId(10L)
                .formulaId(1L)
                .plateStat(true)
                .welltypeStat(false)
                .name("count")
                .build();
        var res7 = performRequest(put("/features/10/featurestats/1", input7), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals("{\"error\":\"The featureId of a FeatureStat cannot be changed\",\"status\":\"error\"}", res7);
    }

    @Test
    public void deleteNonExistingFeatureStatTest() throws Exception {
        var res1 = performRequest(delete("/features/10/featurestats/10"), HttpStatus.NOT_FOUND);
        Assertions.assertEquals("{\"error\":\"FeatureStat with id 10 not found!\",\"status\":\"error\"}", res1);
    }

    @Test
    public void deleteWrongFeatureId() throws Exception {
        // 1. create simple Feature
        var input1 = FeatureDTO.builder()
                .formulaId(2L)
                .protocolId(1000L)
                .name("example_feature")
                .format("test")
                .sequence(1)
                .type(FeatureType.CALCULATION)
                .trigger("abc")
                .build();

        performRequest(post("/features", input1), HttpStatus.CREATED, FeatureDTO.class);

        // 2. create simple FeatureStat
        var input2 = FeatureStatDTO.builder()
                .formulaId(1L)
                .plateStat(true)
                .welltypeStat(false)
                .name("count")
                .build();

        performRequest(post("/features/1/featurestats", input2), HttpStatus.CREATED, FeatureStatDTO.class);

        // 3. delete with wrong featureId
        var res3 = performRequest(delete("/features/10/featurestats/1"), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals("{\"error\":\"The provided featureId is not equal to the actual featureId of the FeatureStat\",\"status\":\"error\"}", res3);
    }

    // TODO defaults

}
