package eu.openanalytics.phaedra.protocolservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public class FeatureGroup {

    @Id
    @Column("GROUP_ID")
    private Long id;
    private String groupName;
    private String description;
    private Integer groupType;
    private Long protocolId;

    public FeatureGroup(Long protocolId) {
        this.protocolId = protocolId;
    }

    // Getters and setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }
}
