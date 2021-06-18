package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.FeatureTag;
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
        Long newFeatureTagId = 1000L;
        String newFeatureTagName = "New Feature Tag";

        FeatureTag newFeatureTag = new FeatureTag();
        newFeatureTag.setFeatureTagId(newFeatureTagId);
        newFeatureTag.setFeatureTagName(newFeatureTagName);

        FeatureTag savedFeatureGroup = featureGroupRepository.save(newFeatureTag);

        assertThat(savedFeatureGroup).isNotNull();
        assertThat(savedFeatureGroup.getFeatureTagId()).isEqualTo(newFeatureTagId);
        assertThat(savedFeatureGroup.getFeatureTagName()).isEqualTo(newFeatureTagName);
    }
}
