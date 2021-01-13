package eu.openanalytics.phaedra.protocolservice;

import eu.openanalytics.phaedra.protocolservice.model.Protocol;
import eu.openanalytics.phaedra.protocolservice.repository.ProtocolRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql({"classpath:jdbc/schema.sql", "classpath:jdbc/test-data.sql"})
public class ProtocolServiceTest {

    @Autowired
    private ProtocolRepository protocolRepository;

    @Test
    public void contextLoads() throws Exception {
        assertThat(protocolRepository).isNotNull();
    }

    @Test
    public void getProtocols() throws Exception {
        List<Protocol> allProtocols = new ArrayList<>();
        protocolRepository.findAll().forEach(allProtocols::add);

        assertThat(allProtocols).isNotNull();
        assertThat(allProtocols).isNotEmpty();
    }

    @Test
    public void getProtocolById() throws Exception {
        Optional<Protocol> protocol = protocolRepository.findById(0L);
        assertThat(protocol.isPresent()).isFalse();
    }
}
