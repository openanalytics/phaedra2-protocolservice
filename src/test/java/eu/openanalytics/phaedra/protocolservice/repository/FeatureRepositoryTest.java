package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.WellFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

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

        WellFeature newFeature = new WellFeature(protocolId);
        newFeature.setFeatureName("TestFeature");
        newFeature.setShortName("TF");
        newFeature.setNumeric(true);
        newFeature.setLogarithmic(true);
        newFeature.setRequired(true);
        newFeature.setKey(true);
        newFeature.setUploaded(true);
        newFeature.setAnnotation(false);
        newFeature.setClassificationRestricted(false);
        newFeature.setCurveNormalization("NONE");
        newFeature.setNormalizationLanguage("Javascript");
        newFeature.setNormalizationFormula("Insert here code for the normalization formula");
        newFeature.setNormalizationScope(0);
        newFeature.setDescription("Insert here the feature description");
        newFeature.setFormatString("#.##");
        newFeature.setLowWelltype("NC");
        newFeature.setHighWelltype("PC");
        newFeature.setCalcFormula("Insert here code for the calculation formula");
        newFeature.setCalcLanguage("Javascript");
        newFeature.setCalcFormulaId(1L); //Id of a predefined calculation formula
        newFeature.setCalcTrigger(""); //What triggers the calculation
        newFeature.setCalcSequence(0); //The calculation order
        newFeature.setGroupId(null); //Optional: the id of a FeatureGroup;

        WellFeature savedFeature = featureRepository.save(newFeature);

        assertThat(savedFeature).isNotNull();
        assertThat(savedFeature.getFeatureId()).isNotNull();
        assertThat(savedFeature.getProtocolId()).isEqualTo(protocolId);
    }

    @Test
    public void deleteFeature() throws Exception {
        Long protocolId = 10L;

        List<WellFeature> results1 = featureRepository.findAllByProtocolId(protocolId);
        assertThat(results1).isNotNull();
        assertThat(results1).isNotEmpty();

        featureRepository.delete(results1.get(0));

        List<WellFeature> results2 = featureRepository.findAllByProtocolId(protocolId);
        assertThat(results2).isNotNull();
        assertThat(results2).isNotEmpty();
        assertThat(results2.size()).isLessThan(results1.size());
    }

    @Test
    public void updateFeature() throws Exception {
        Long protocolId = 20L;

        List<WellFeature> result = featureRepository.findAllByProtocolId(protocolId);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();

        WellFeature feature = result.get(0);
        String newName = "New feature name";
        String newDescription = "New feature description";
        feature.setFeatureName(newName);
        feature.setDescription(newDescription);

        WellFeature updatedFeature = featureRepository.save(feature);
        assertThat(updatedFeature).isNotNull();
        assertThat(updatedFeature.getFeatureName()).isEqualTo(newName);
        assertThat(updatedFeature.getDescription()).isEqualTo(newDescription);
    }


    @Test
    public void getFeatureById() {
        Long featureId = 1000L;

        Optional<WellFeature> feature = featureRepository.findById(featureId);
        assertThat(feature).isNotNull();
        assertThat(feature.isPresent()).isTrue();
        assertThat(feature.get().getFeatureId()).isEqualTo(featureId);
    }

    @Test
    public void getFeatureForAGivenProtocolId() {
        Long protocolId = 10L;

        List<WellFeature> features = featureRepository.findAllByProtocolId(protocolId);
        assertThat(features).isNotNull();
        assertThat(features).isNotEmpty();
        assertThat(features.stream().allMatch(f -> f.getProtocolId().equals(protocolId))).isTrue();
    }

    @Test
    public void getFeatureForAGivenFeatureGroupId() {
        Long featureGroupId = 20L;

        List<WellFeature> features = featureRepository.findAllByFeatureGroupId(featureGroupId);
        assertThat(features).isNotNull();
        assertThat(features).isNotEmpty();
        assertThat(features.stream().allMatch(f -> f.getGroupId().equals(featureGroupId))).isTrue();
    }

}
