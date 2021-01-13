package eu.openanalytics.phaedra.protocolservice.api;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"classpath:jdbc/schema.sql"})
@AutoConfigureMockMvc
public class FeatureControllerTest {
}
