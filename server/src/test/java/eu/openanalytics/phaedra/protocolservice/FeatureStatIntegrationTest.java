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
package eu.openanalytics.phaedra.protocolservice;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

        performRequest(post("/features", input1), HttpStatus.CREATED, FeatureDTO.class);

        // 2. create simple FeatureStat
        var input2 = FeatureStatDTO.builder()
                .formulaId(1L)
                .plateStat(true)
                .welltypeStat(false)
                .name("count")
                .build();

        var res2 = performRequest(post("/features/1/featurestat", input2), HttpStatus.CREATED, FeatureStatDTO.class);

        Assertions.assertEquals(2, res2.getId());
        Assertions.assertEquals(1, res2.getFeatureId());
        Assertions.assertTrue(res2.getPlateStat());
        Assertions.assertFalse(res2.getWelltypeStat());
        Assertions.assertEquals(1, res2.getFormulaId());
        Assertions.assertEquals("count", res2.getName());

        // 3. get specific FeatureStat
        var res3 = performRequest(get("/features/1/featurestat/2"), HttpStatus.OK, FeatureStatDTO.class);
        Assertions.assertEquals(2, res3.getId());
        Assertions.assertEquals(1, res3.getFeatureId());
        Assertions.assertTrue(res3.getPlateStat());
        Assertions.assertFalse(res3.getWelltypeStat());
        Assertions.assertEquals(1, res3.getFormulaId());
        Assertions.assertEquals("count", res3.getName());

        // 4. Delete FeatureStat
        performRequest(delete("/features/1/featurestat/1"), HttpStatus.NO_CONTENT);

        // 6. get FeatureStat again
        var res5 = performRequest(get("/features/1/featurestat/1"), HttpStatus.NOT_FOUND);
        Assertions.assertEquals("{\"error\":\"FeatureStat with id 1 not found!\",\"status\":\"error\"}", res5);
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

        performRequest(post("/features", input1), HttpStatus.CREATED, FeatureDTO.class);

        // 2. create simple FeatureStat
        var input2 = FeatureStatDTO.builder()
                .formulaId(1L)
                .plateStat(true)
                .welltypeStat(false)
                .name("count")
                .build();

        var res2 = performRequest(post("/features/1/featurestat", input2), HttpStatus.CREATED, FeatureStatDTO.class);

        Assertions.assertEquals(2, res2.getId());
        Assertions.assertEquals(1, res2.getFeatureId());
        Assertions.assertTrue(res2.getPlateStat());
        Assertions.assertFalse(res2.getWelltypeStat());
        Assertions.assertEquals(1, res2.getFormulaId());
        Assertions.assertEquals("count", res2.getName());

        // 3. update FeatureStat
        var input3 = FeatureStatDTO.builder()
                .id(2L)
                .featureId(1L)
                .formulaId(10L)
                .plateStat(false)
                .welltypeStat(true)
                .name("count-updated")
                .build();
        var res3 = performRequest(put("/features/1/featurestat/2", input3), HttpStatus.OK, FeatureStatDTO.class);

        Assertions.assertEquals(2, res3.getId());
        Assertions.assertEquals(1, res3.getFeatureId());
        Assertions.assertFalse(res3.getPlateStat());
        Assertions.assertTrue(res3.getWelltypeStat());
        Assertions.assertEquals(10, res3.getFormulaId());
        Assertions.assertEquals("count-updated", res3.getName());
    }

    @Test
    public void queryFeatureStatsTests() throws Exception {
        // 1. create two protocols with two features and two featureStats
        for (int i = 1; i <= 2; i++) {
            var input1 = ProtocolDTO.builder()
                    .name("MyProtocol")
                    .description("MyProtocol")
                    .highWelltype("HC")
                    .lowWelltype("LC")
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

        // 2. get all featureStats of feature 1
        var res2 = performRequest(get("/features/1/featurestat"), HttpStatus.OK);
        Assertions.assertEquals("[{\"featureId\":1,\"formulaId\":1,\"id\":1,\"name\":\"test\",\"plateStat\":true,\"welltypeStat\":true},{\"featureId\":1,\"formulaId\":1,\"id\":2,\"name\":\"count\",\"plateStat\":true,\"welltypeStat\":false}]", res2);

        // 3. get all featureStats of feature 4
        var res3 = performRequest(get("/features/4/featurestat"), HttpStatus.OK);
        Assertions.assertEquals("[{\"featureId\":4,\"formulaId\":1,\"id\":7,\"name\":\"test\",\"plateStat\":true,\"welltypeStat\":true},{\"featureId\":4,\"formulaId\":1,\"id\":8,\"name\":\"count\",\"plateStat\":true,\"welltypeStat\":false}]", res3);

        // 4. get all featureStats by protocol 1
        var res4 = performRequest(get("/protocols/1/featurestat"), HttpStatus.OK);
        Assertions.assertEquals("[{\"featureId\":1,\"formulaId\":1,\"id\":1,\"name\":\"test\",\"plateStat\":true,\"welltypeStat\":true},{\"featureId\":1,\"formulaId\":1,\"id\":2,\"name\":\"count\",\"plateStat\":true,\"welltypeStat\":false},{\"featureId\":2,\"formulaId\":1,\"id\":3,\"name\":\"test\",\"plateStat\":true,\"welltypeStat\":true},{\"featureId\":2,\"formulaId\":1,\"id\":4,\"name\":\"count\",\"plateStat\":true,\"welltypeStat\":false}]", res4);

        // 5. get all featureStats by protocol 2
        var res5 = performRequest(get("/protocols/2/featurestat"), HttpStatus.OK);
        Assertions.assertEquals("[{\"featureId\":3,\"formulaId\":1,\"id\":5,\"name\":\"test\",\"plateStat\":true,\"welltypeStat\":true},{\"featureId\":3,\"formulaId\":1,\"id\":6,\"name\":\"count\",\"plateStat\":true,\"welltypeStat\":false},{\"featureId\":4,\"formulaId\":1,\"id\":7,\"name\":\"test\",\"plateStat\":true,\"welltypeStat\":true},{\"featureId\":4,\"formulaId\":1,\"id\":8,\"name\":\"count\",\"plateStat\":true,\"welltypeStat\":false}]", res5);

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

        var res1 = performRequest(post("/features/42/featurestat", input1), HttpStatus.NOT_FOUND);
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

        var res3 = performRequest(post("/features/1/featurestat", input3), HttpStatus.BAD_REQUEST);
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

        var res4 = performRequest(post("/features/1/featurestat", input4), HttpStatus.BAD_REQUEST);
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

        var res5 = performRequest(post("/features/1/featurestat", input5), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals("{\"error\":\"Validation error\",\"malformed_fields\":{\"valid\":\"Both plateStat and welltypeStat cannot be false\"},\"status\":\"error\"}", res5);

        // 6. duplicate by name for the same featureId
        var input6 = FeatureStatDTO.builder()
                .formulaId(10L)
                .plateStat(true)
                .welltypeStat(true)
                .name("count")
                .build();

        performRequest(post("/features/1/featurestat", input6), HttpStatus.CREATED);

        var input7= FeatureStatDTO.builder()
                .formulaId(1L)
                .plateStat(true)
                .welltypeStat(false)
                .name("count")
                .build();

        var res7 = performRequest(post("/features/1/featurestat", input7), HttpStatus.BAD_REQUEST);
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
        var res1 = performRequest(put("/features/1/featurestat/1", input1), HttpStatus.BAD_REQUEST);
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
        var res2 = performRequest(put("/features/1/featurestat/1", input2), HttpStatus.BAD_REQUEST);
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

        performRequest(post("/features/1/featurestat", input4), HttpStatus.CREATED, FeatureStatDTO.class);

        // 5. missing fields
        var input5 = FeatureStatDTO.builder().build();
        var res5 = performRequest(put("/features/1/featurestat/1", input5), HttpStatus.BAD_REQUEST);
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
        var res6 = performRequest(put("/features/10/featurestat/10", input6), HttpStatus.NOT_FOUND);
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
        var res7 = performRequest(put("/features/10/featurestat/1", input7), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals("{\"error\":\"The featureId of a FeatureStat cannot be changed\",\"status\":\"error\"}", res7);
    }

    @Test
    public void deleteNonExistingFeatureStatTest() throws Exception {
        var res1 = performRequest(delete("/features/10/featurestat/10"), HttpStatus.NOT_FOUND);
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

        performRequest(post("/features/1/featurestat", input2), HttpStatus.CREATED, FeatureStatDTO.class);

        // 3. delete with wrong featureId
        var res3 = performRequest(delete("/features/10/featurestat/1"), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals("{\"error\":\"The provided featureId is not equal to the actual featureId of the FeatureStat\",\"status\":\"error\"}", res3);
    }

    // TODO defaults

}
