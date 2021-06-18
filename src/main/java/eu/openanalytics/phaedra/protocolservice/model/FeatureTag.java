package eu.openanalytics.phaedra.protocolservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public class FeatureTag {

    @Id
    @Column("f_tag_id")
    private Long featureTagId;
    @Column("f_tag_name")
    private String featureTagName;

    public Long getFeatureTagId() {
        return featureTagId;
    }

    public void setFeatureTagId(Long featureTagId) {
        this.featureTagId = featureTagId;
    }

    public String getFeatureTagName() {
        return featureTagName;
    }

    public void setFeatureTagName(String featureTagName) {
        this.featureTagName = featureTagName;
    }
}
