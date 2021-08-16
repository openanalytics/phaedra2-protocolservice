package eu.openanalytics.phaedra.protocolservice.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.openanalytics.phaedra.protocolservice.model.Feature;
import eu.openanalytics.phaedra.protocolservice.model.Protocol;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql({"classpath:jdbc/schema.sql", "classpath:jdbc/test-data.sql"})
@AutoConfigureMockMvc
public class ProtocolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createProtocol() throws Exception {
        Protocol newProtocol = new Protocol();
        newProtocol.setName("A new protocol");
        newProtocol.setDescription("Newly created protocol");
        newProtocol.setEditable(true);
        newProtocol.setInDevelopment(true);
        newProtocol.setLowWelltype("LC");
        newProtocol.setHighWelltype("HC");

        String requestBody = objectMapper.writeValueAsString(newProtocol);

        this.mockMvc.perform(post("/protocols").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{ protocolId: 1001 }"));
    }

    @Test
    public void deleteProtocol() throws Exception {
        Long protocolId = 1000L;

        this.mockMvc.perform(delete("/protocols/{protocolId}", protocolId))
                .andDo(print())
                .andExpect(status().isNoContent());
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
        String newDescription = "New protocol desription";
        protocol.setDescription(newDescription);

        String requestBody = objectMapper.writeValueAsString(protocol);
        this.mockMvc.perform(put("/protocols").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isOk());

        mvcResult = this.mockMvc.perform(get("/protocols/{protocolId}", protocolId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Protocol updatedProtocol = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Protocol.class);
        assertThat(updatedProtocol.getName()).isEqualTo(newName);
        assertThat(updatedProtocol.getDescription()).isEqualTo(newDescription);
    }

    @Test
    public void getAllProtocols() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/protocols"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<Protocol> results = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Protocol>>() {});
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

        List<Feature> features = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Feature>>() {});
        assertThat(features).isNotNull();
        assertThat(features.stream().allMatch(f -> f.getProtocolId().equals(protocolId))).isTrue();
    }
}
