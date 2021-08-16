package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.Protocol;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql({"classpath:jdbc/schema.sql", "classpath:jdbc/test-data.sql"})
public class ProtocolRepositoryTest {
    @Autowired
    private ProtocolRepository protocolRepository;

    @Test
    public void contextLoads() {
        assertThat(protocolRepository).isNotNull();
    }

    @Test
    public void getProtocols() {
        Iterable<Protocol> allProtocols = protocolRepository.findAll();
        assertThat(allProtocols).isNotNull();
        assertThat(allProtocols).isNotEmpty();
    }

    @Test
    public void getProtocolById() {
        Optional<Protocol> protocol = protocolRepository.findById(1000L);
        assertThat(protocol.isPresent()).isTrue();
    }

    @Test
    public void createNewProtocol() {
        Protocol newProtocol = new Protocol();
        newProtocol.setName("Test");
        newProtocol.setDescription("Test");
        newProtocol.setEditable(true);
        newProtocol.setInDevelopment(true);
        newProtocol.setLowWelltype("NC");
        newProtocol.setHighWelltype("PC");

        Protocol savedProtocol = protocolRepository.save(newProtocol);

        assertThat(savedProtocol).isNotNull();
        assertThat(savedProtocol.getId()).isNotNull();
        assertThat(savedProtocol.getName()).isEqualTo(newProtocol.getName());
    }
}
