package eu.openanalytics.phaedra.protocolservice.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.openanalytics.phaedra.protocolservice.model.Feature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql({"classpath:jdbc/schema.sql", "classpath:jdbc/test-data.sql"})
@AutoConfigureMockMvc
public class FeatureControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        newFeature.setFormatString("#.###");

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
                .andExpect(status().isNoContent());

    }

    @Test
    public void updateFeature() throws Exception {
        Long featureId = 2000L;

        MvcResult mvcResult = this.mockMvc.perform(get("/features/{featureId}", featureId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Feature feature = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Feature.class);
        assertThat(feature).isNotNull();
        assertThat(feature.getId()).isEqualTo(featureId);

        String updatedName = "Updated feature name";
        String updateDescription = "Update feature description";
        feature.setName(updatedName);
        feature.setDescription(updateDescription);

        String requestBody = objectMapper.writeValueAsString(feature);

        mvcResult = this.mockMvc.perform(put("/features").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Feature updatedFeature = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Feature.class);
        assertThat(updatedFeature).isNotNull();
        assertThat(updatedFeature.getId()).isEqualTo(feature.getId());
        assertThat(updatedFeature.getName()).isEqualTo(feature.getName());
        assertThat(updatedFeature.getDescription()).isEqualTo(feature.getDescription());
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
        Long groupId = 20L;

        MvcResult mvcResult = this.mockMvc.perform(get("/features").param("groupId", groupId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<Feature> result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Feature>>() {});
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.stream().allMatch(r -> r.getFeatureGroupId().equals(groupId)));
    }
}
