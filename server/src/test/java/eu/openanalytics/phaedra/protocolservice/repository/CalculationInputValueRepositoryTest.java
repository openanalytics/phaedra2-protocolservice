package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.CalculationInputValue;
import eu.openanalytics.phaedra.protocolservice.support.Containers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@Sql({"/jdbc/test-data.sql"})
@TestPropertySource(locations = "classpath:application-test.properties")
public class CalculationInputValueRepositoryTest {

    @Autowired
    private CalculationInputValueRepository calculationInputValueRepository;

    @Container
    private static JdbcDatabaseContainer postgreSQLContainer = new PostgreSQLContainer("postgres:13-alpine")
            .withDatabaseName("phaedra2")
            .withUrlParam("currentSchema","protocols")
            .withPassword("inmemory")
            .withUsername("inmemory");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("DB_URL", Containers.postgreSQLContainer::getJdbcUrl);
        registry.add("DB_USER", Containers.postgreSQLContainer::getUsername);
        registry.add("DB_PASSWORD", Containers.postgreSQLContainer::getPassword);
    }

    @Test
    public void contextLoads() {
        assertThat(calculationInputValueRepository).isNotNull();
    }

    @Test
    public void createCalculationInputValue() {
        CalculationInputValue calculationInputValue = new CalculationInputValue(null,1000L,"Column1",1000L,"col1");

        CalculationInputValue saved = calculationInputValueRepository.save(calculationInputValue);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVariableName()).isEqualTo(calculationInputValue.getVariableName());
    }

    @Test
    public void deleteCalculationInputValue() {
        CalculationInputValue calculationInputValue = new CalculationInputValue(null,1000L,"Column1",1000L,"col1");

        CalculationInputValue saved = calculationInputValueRepository.save(calculationInputValue);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVariableName()).isEqualTo(calculationInputValue.getVariableName());

        calculationInputValueRepository.delete(saved);

        Optional<CalculationInputValue> deleted = calculationInputValueRepository.findById(saved.getId());
        assertThat(deleted).isEmpty();
        assertThat(deleted.isPresent()).isFalse();
    }

    @Test
    public void updateCalculationInputValue() {
        //Add new civ
        CalculationInputValue calculationInputValue = new CalculationInputValue(null,1000L,"Column1",1000L,"col1");

        CalculationInputValue saved = calculationInputValueRepository.save(calculationInputValue);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVariableName()).isEqualTo(calculationInputValue.getVariableName());

        CalculationInputValue updated = new CalculationInputValue(saved.getId(),saved.getFeatureId(),"Column2",saved.getSourceFeatureId(),"col2");
        CalculationInputValue saved2 = calculationInputValueRepository.save(updated);

        assertThat(saved2).isNotNull();
        assertThat(saved2.getVariableName()).isEqualTo("col2");
        assertThat(saved2.getSourceMeasColName()).isEqualTo("Column2");
    }

    @Test
    public void getCalculatioInputValueById() {
        //Add new civ
        CalculationInputValue calculationInputValue = new CalculationInputValue(null,1000L,"Column1",1000L,"col1");

        CalculationInputValue saved = calculationInputValueRepository.save(calculationInputValue);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVariableName()).isEqualTo(calculationInputValue.getVariableName());

        CalculationInputValue get = calculationInputValueRepository.findById(saved.getId()).get();
        assertThat(get).isNotNull();
        assertThat(get.getId()).isNotNull();
        assertThat(get.getVariableName()).isEqualTo(calculationInputValue.getVariableName());
    }

    @Test
    public void getCalculationInputValueByFeatureId() {
        CalculationInputValue calculationInputValue = new CalculationInputValue(null,1000L,"Column1",1000L,"col1");

        CalculationInputValue saved = calculationInputValueRepository.save(calculationInputValue);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVariableName()).isEqualTo(calculationInputValue.getVariableName());

        List<CalculationInputValue> get = calculationInputValueRepository.findByFeatureId(saved.getFeatureId());
        assertThat(get).isNotNull();
        assertThat(get.size()).isEqualTo(1);
        assertThat(get.get(0).getId()).isNotNull();
        assertThat(get.get(0).getVariableName()).isEqualTo(calculationInputValue.getVariableName());

    }

    @Test
    public void getCalculationInputValueByProtocolId() {
        CalculationInputValue calculationInputValue = new CalculationInputValue(null,1000L,"Column1",1000L,"col1");

        CalculationInputValue saved = calculationInputValueRepository.save(calculationInputValue);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVariableName()).isEqualTo(calculationInputValue.getVariableName());

        List<CalculationInputValue> get = calculationInputValueRepository.findByProtocolId(1000L);
        assertThat(get).isNotNull();
        assertThat(get.size()).isEqualTo(1);
        assertThat(get.get(0).getId()).isNotNull();
        assertThat(get.get(0).getVariableName()).isEqualTo(calculationInputValue.getVariableName());

    }
}
