package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.FeatureGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql({"classpath:jdbc/schema.sql", "classpath:jdbc/test-data.sql"})
public class FeatureGroupRepositoryTest {

    @Autowired
    private FeatureGroupRepository featureGroupRepository;


    @Test
    public void createFeatureGroup() {
        Long protocolId = 0L;

        FeatureGroup newFeatureGroup = new FeatureGroup(protocolId);
        newFeatureGroup.setGroupName("New Feature Group");
        newFeatureGroup.setDescription("A new feature group for test");
        newFeatureGroup.setGroupType(0);

        FeatureGroup savedFeatureGroup = featureGroupRepository.save(newFeatureGroup);

        assertThat(savedFeatureGroup).isNotNull();
        assertThat(savedFeatureGroup.getId()).isNotNull();
        assertThat(savedFeatureGroup.getProtocolId()).isEqualTo(protocolId);
    }
}
