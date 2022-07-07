package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.DoseResponseCurveProperty;
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

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@Sql({"/jdbc/test-data.sql"})
@TestPropertySource(locations = "classpath:application-test.properties")
public class DoseResponseCurvePropertyRepositoryTest {

    @Autowired
    private DoseResponseCurvePropertyRepository drcPropertyRepository;

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
        assertThat(drcPropertyRepository).isNotNull();
    }

    @Test
    public void createDoseResponseCurveProperty() {
        DoseResponseCurveProperty testProperty1 = new DoseResponseCurveProperty();
        testProperty1.setFeatureId(1000L);
        testProperty1.setName("TestProperty1");
        testProperty1.setValue("TestPropertyValue1");

        DoseResponseCurveProperty result1 = drcPropertyRepository.save(testProperty1);
        assertThat(result1.getId()).isNotNull();
        assertThat(result1.getFeatureId()).isEqualTo(testProperty1.getFeatureId());
        assertThat(result1.getName()).isEqualTo(testProperty1.getName());
        assertThat(result1.getValue()).isEqualTo(testProperty1.getValue());

        DoseResponseCurveProperty testProperty2 = new DoseResponseCurveProperty();
        testProperty2.setFeatureId(1000L);
        testProperty2.setName("TestProperty2");
        testProperty2.setValue("TestPropertyValue2");

        DoseResponseCurveProperty result2 = drcPropertyRepository.save(testProperty2);
        assertThat(result2.getId()).isNotNull();
        assertThat(result2.getFeatureId()).isEqualTo(testProperty2.getFeatureId());
        assertThat(result2.getName()).isEqualTo(testProperty2.getName());
        assertThat(result2.getValue()).isEqualTo(testProperty2.getValue());

        DoseResponseCurveProperty testProperty3 = new DoseResponseCurveProperty();
        testProperty3.setFeatureId(1000L);
        testProperty3.setName("TestProperty3");
        testProperty3.setValue("TestPropertyValue3");

        DoseResponseCurveProperty result3 = drcPropertyRepository.save(testProperty3);
        assertThat(result3.getId()).isNotNull();
        assertThat(result3.getFeatureId()).isEqualTo(testProperty3.getFeatureId());
        assertThat(result3.getName()).isEqualTo(testProperty3.getName());
        assertThat(result3.getValue()).isEqualTo(testProperty3.getValue());
    }

    @Test
    public void findAllDoseResponseCurvePropertyByFeatureId() {
        DoseResponseCurveProperty testProperty1 = new DoseResponseCurveProperty();
        testProperty1.setFeatureId(1000L);
        testProperty1.setName("TestProperty1");
        testProperty1.setValue("TestPropertyValue1");
        drcPropertyRepository.save(testProperty1);

        DoseResponseCurveProperty testProperty2 = new DoseResponseCurveProperty();
        testProperty2.setFeatureId(1000L);
        testProperty2.setName("TestProperty2");
        testProperty2.setValue("TestPropertyValue2");
        drcPropertyRepository.save(testProperty2);

        DoseResponseCurveProperty testProperty3 = new DoseResponseCurveProperty();
        testProperty3.setFeatureId(1000L);
        testProperty3.setName("TestProperty3");
        testProperty3.setValue("TestPropertyValue3");
        drcPropertyRepository.save(testProperty3);

        List<DoseResponseCurveProperty> results = drcPropertyRepository.findAllByFeatureId(1000L);
        assertThat(results).isNotNull().isNotEmpty();
        assertThat(results.size()).isEqualTo(3);

    }

}
