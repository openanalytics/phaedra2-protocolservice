package eu.openanalytics.phaedra.protocolservice.api;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql({"classpath:jdbc/schema.sql"})
@AutoConfigureMockMvc
public class ProtocolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getProtocolList() throws Exception {
        this.mockMvc.perform(get("/protocols"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void createProtocol() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("protocolName", "New Protocol Test");
        map.put("description", "Create a new protocol test");
        map.put("isEditable", "true");
        map.put("isInDevelopment", "true");
        map.put("lowWelltype", "LC");
        map.put("highWelltype", "HC");
        JSONObject jo = new JSONObject(map);

        String requestBody = jo.toString();

        this.mockMvc.perform(post("/protocol").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{protocolId: 1}"));
    }
}
