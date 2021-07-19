package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.Tag;
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
    private TagRepository tagRepository;


    @Test
    public void createFeatureGroup() {
        String newTagName = "New Feature Tag";

        Tag newTag = new Tag(newTagName);

        Tag savedTag = (Tag) tagRepository.save(newTag);

        assertThat(savedTag).isNotNull();
        assertThat(savedTag.getTagId()).isNotNull();
        assertThat(savedTag.getTagName()).isEqualTo(newTagName);
    }
}
