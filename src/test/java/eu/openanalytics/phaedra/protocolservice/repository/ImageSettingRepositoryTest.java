package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.ImageSetting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql({"classpath:jdbc/schema.sql", "classpath:jdbc/test-data.sql"})
public class ImageSettingRepositoryTest {

    @Autowired
    private ImageSettingRepository imageSettingRepository;

    @Test
    public void contextLoads() {
        assertThat(imageSettingRepository).isNotNull();
    }

    @Test
    public void createImageSetting() {
        ImageSetting newImageSetting = new ImageSetting();
        newImageSetting.setZoomRatio(2);
        newImageSetting.setGamma(300);
        newImageSetting.setPixelSizeX(1.2);
        newImageSetting.setPixelSizeY(1.2);
        newImageSetting.setPixelSizeZ(1.2);

        ImageSetting savedImageSetting = imageSettingRepository.save(newImageSetting);

        assertThat(savedImageSetting).isNotNull();
        assertThat(savedImageSetting.getId()).isNotNull();

    }
}
