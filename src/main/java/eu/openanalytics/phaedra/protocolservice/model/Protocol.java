package eu.openanalytics.phaedra.protocolservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public class Protocol {
    @Id
    @Column("PROTOCOL_ID")
    private Long id;
    private String protocolName;
    private String description;
    private Long defaultFeatureId;
    private String defaultLims;
    private String defaultLayoutTemplate;
    private String defaultCaptureConfig;
    private boolean isEditable;
    private boolean isInDevelopment;
    private String teamCode;
    private String uploadSystem;
    private String lowWelltype;
    private String highWelltype;
    private Long imageSettingId;
    private boolean isMultiDimSubwellData;
    private String defaultMultiploMethod;
    private String defaultMultiploParameter;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDefaultFeatureId() {
        return defaultFeatureId;
    }

    public void setDefaultFeatureId(Long defaultFeatureId) {
        this.defaultFeatureId = defaultFeatureId;
    }

    public String getDefaultLims() {
        return defaultLims;
    }

    public void setDefaultLims(String defaultLims) {
        this.defaultLims = defaultLims;
    }

    public String getDefaultLayoutTemplate() {
        return defaultLayoutTemplate;
    }

    public void setDefaultLayoutTemplate(String defaultLayoutTemplate) {
        this.defaultLayoutTemplate = defaultLayoutTemplate;
    }

    public String getDefaultCaptureConfig() {
        return defaultCaptureConfig;
    }

    public void setDefaultCaptureConfig(String defaultCaptureConfig) {
        this.defaultCaptureConfig = defaultCaptureConfig;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getUploadSystem() {
        return uploadSystem;
    }

    public void setUploadSystem(String uploadSystem) {
        this.uploadSystem = uploadSystem;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        this.isEditable = editable;
    }

    public boolean isInDevelopment() {
        return isInDevelopment;
    }

    public void setInDevelopment(boolean inDevelopment) {
        this.isInDevelopment = inDevelopment;
    }

    public String getLowWelltype() {
        return lowWelltype;
    }

    public void setLowWelltype(String lowWelltype) {
        this.lowWelltype = lowWelltype;
    }

    public String getHighWelltype() {
        return highWelltype;
    }

    public void setHighWelltype(String highWelltype) {
        this.highWelltype = highWelltype;
    }

    public Long getImageSettingId() {
        return imageSettingId;
    }

    public void setImageSettingId(Long imageSettingId) {
        this.imageSettingId = imageSettingId;
    }

    public boolean isMultiDimSubwellData() {
        return isMultiDimSubwellData;
    }

    public void setMultiDimSubwellData(boolean multiDimSubwellData) {
        this.isMultiDimSubwellData = multiDimSubwellData;
    }

    public String getDefaultMultiploMethod() {
        return defaultMultiploMethod;
    }

    public void setDefaultMultiploMethod(String defaultMultiploMethod) {
        this.defaultMultiploMethod = defaultMultiploMethod;
    }

    public String getDefaultMultiploParameter() {
        return defaultMultiploParameter;
    }

    public void setDefaultMultiploParameter(String defaultMultiploParameter) {
        this.defaultMultiploParameter = defaultMultiploParameter;
    }
}
