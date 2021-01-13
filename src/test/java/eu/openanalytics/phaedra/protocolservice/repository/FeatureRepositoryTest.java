package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.Feature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

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
    public void createNewFeature() {
        long protocolId = 0;

        Feature newFeature = new Feature(protocolId);
        newFeature.setName("TestFeature");
        newFeature.setShortName("TF");
        newFeature.setNumeric(true);
        newFeature.setLogarithmic(true);
        newFeature.setRequired(true);
        newFeature.setKeyFeature(true);
        newFeature.setToUpload(true);
        newFeature.setAnnotation(false);
        newFeature.setClassificationRestricted(false);
        newFeature.setCurveNormalization("NONE");
        newFeature.setNormalizationLanguage("Javascript");
        newFeature.setNormalizationFormula("Insert here code for the normalization formula");
        newFeature.setNormalizationScope(0);
        newFeature.setDescription("Insert here the feature description");
        newFeature.setFormatString("#.##");
        newFeature.setLowWellType("NC");
        newFeature.setHighWellType("PC");
        newFeature.setCalculationFormula("Insert here code for the calculation formula");
        newFeature.setCalculationLanguage("Javascript");
        newFeature.setCalculationFormulaId(1L); //Id of a predefined calculation formula
        newFeature.setCalculationTrigger(""); //What triggers the calculation
        newFeature.setCalculationSequence(0); //The calculation order
        newFeature.setFeatureGroupId(null); //Optional: the id of a FeatureGroup;

        Feature savedFeature = featureRepository.save(newFeature);

        assertThat(savedFeature).isNotNull();
        assertThat(savedFeature.getId()).isNotNull();
        assertThat(savedFeature.getProtocolId()).isEqualTo(protocolId);
    }

    @Test
    public void getFeatureForAGivenProtocolId() {
        long protocolId = 10L;

        List<Feature> result = featureRepository.findAllByProtocolId(protocolId);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.stream().allMatch(f -> f.getProtocolId().equals(protocolId))).isTrue();
    }

    @Test
    public void getFeatureForAGivenFeatureGroupId() {
        long featureGroupId = 20L;

        List<Feature> result = featureRepository.findAllByFeatureGroupId(featureGroupId);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.stream().allMatch(f -> f.getFeatureGroupId().equals(featureGroupId))).isTrue();
    }
}
