package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.enumeration.ScriptLanguage;
import eu.openanalytics.phaedra.protocolservice.model.Feature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql({"classpath:jdbc/schema.sql", "classpath:jdbc/test-data.sql"})
public class FeatureRepositoryTest {

    @Autowired
    private FeatureRepository featureRepository;

    @Test
    public void contextLoads() throws Exception {
        assertThat(featureRepository).isNotNull();
    }

    @Test
    public void createFeature() {
        long protocolId = 0;

        Feature newFeature = new Feature(protocolId);
        newFeature.setFeatureName("TestFeature");
        newFeature.setFeatureAlias("TF");
        newFeature.setDescription("Insert here the feature description");
        newFeature.setFormat("#.##");
        newFeature.setFormulaId(1L); //Id of a predefined calculation formula

        Feature savedFeature = featureRepository.save(newFeature);

        assertThat(savedFeature).isNotNull();
        assertThat(savedFeature.getFeatureId()).isNotNull();
        assertThat(savedFeature.getProtocolId()).isEqualTo(protocolId);
    }

    @Test
    public void deleteFeature() throws Exception {
        Long protocolId = 10L;

        List<Feature> results1 = featureRepository.findByProtocolId(protocolId);
        assertThat(results1).isNotNull();
        assertThat(results1).isNotEmpty();

        featureRepository.delete(results1.get(0));

        List<Feature> results2 = featureRepository.findByProtocolId(protocolId);
        assertThat(results2).isNotNull();
        assertThat(results2).isNotEmpty();
        assertThat(results2.size()).isLessThan(results1.size());
    }

    @Test
    public void updateFeature() throws Exception {
        Long protocolId = 20L;

        List<Feature> result = featureRepository.findByProtocolId(protocolId);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();

        Feature feature = result.get(0);
        String newName = "New feature name";
        String newDescription = "New feature description";
        feature.setFeatureName(newName);
        feature.setDescription(newDescription);

        Feature updatedFeature = featureRepository.save(feature);
        assertThat(updatedFeature).isNotNull();
        assertThat(updatedFeature.getFeatureName()).isEqualTo(newName);
        assertThat(updatedFeature.getDescription()).isEqualTo(newDescription);
    }


    @Test
    public void getFeatureById() {
        Long featureId = 1000L;

        Optional<Feature> feature = featureRepository.findById(featureId);
        assertThat(feature).isNotNull();
        assertThat(feature.isPresent()).isTrue();
        assertThat(feature.get().getFeatureId()).isEqualTo(featureId);
    }

    @Test
    public void getFeatureForAGivenProtocolId() {
        Long protocolId = 10L;

        List<Feature> features = featureRepository.findByProtocolId(protocolId);
        assertThat(features).isNotNull();
        assertThat(features).isNotEmpty();
        assertThat(features.stream().allMatch(f -> f.getProtocolId().equals(protocolId))).isTrue();
    }

    @Test
    public void getFeatureForAGivenFeatureGroupId() {
        List<Long> tagIds = Arrays.asList(20L);

        List<Feature> features = featureRepository.findByTagsIn(Arrays.asList("TestNew","TestDoorSasa1"));
        assertThat(features).isNotNull();
        assertThat(features).isNotEmpty();
    }

}
