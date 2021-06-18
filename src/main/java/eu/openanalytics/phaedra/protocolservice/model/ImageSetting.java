package eu.openanalytics.phaedra.protocolservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public class ImageSetting {
    @Id
    public Long imageSettingId;
    public Integer zoomRatio;
    public Integer gamma;
    public Number pixelSizeX;
    public Number pixelSizeY;
    public Number pixelSizeZ;

    //Getters and setters
    public Long getImageSettingId() {
        return imageSettingId;
    }

    public void setImageSettingId(Long imageSettingId) {
        this.imageSettingId = imageSettingId;
    }

    public Integer getZoomRatio() {
        return zoomRatio;
    }

    public void setZoomRatio(Integer zoomRatio) {
        this.zoomRatio = zoomRatio;
    }

    public Integer getGamma() {
        return gamma;
    }

    public void setGamma(Integer gamma) {
        this.gamma = gamma;
    }

    public Number getPixelSizeX() {
        return pixelSizeX;
    }

    public void setPixelSizeX(Number pixelSizeX) {
        this.pixelSizeX = pixelSizeX;
    }

    public Number getPixelSizeY() {
        return pixelSizeY;
    }

    public void setPixelSizeY(Number pixelSizeY) {
        this.pixelSizeY = pixelSizeY;
    }

    public Number getPixelSizeZ() {
        return pixelSizeZ;
    }

    public void setPixelSizeZ(Number pixelSizeZ) {
        this.pixelSizeZ = pixelSizeZ;
    }
}
