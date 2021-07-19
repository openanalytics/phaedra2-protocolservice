package eu.openanalytics.phaedra.protocolservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public class Protocol {
    @Id
    @Column("id")
    private Long protocolId;
    @Column("name")
    private String protocolName;
    private String description;
    @Column("editable")
    private boolean editable;
    @Column("in_development")
    private boolean inDevelopment;
    @Column("low_welltype")
    private String lowWelltype;
    @Column("high_welltype")
    private String highWelltype;

    // Getters and setters
    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long id) {
        this.protocolId = id;
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

    public boolean isEditable() {
        return this.editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isInDevelopment() {
        return this.inDevelopment;
    }

    public void setInDevelopment(boolean inDevelopment) {
        this.inDevelopment = inDevelopment;
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
}
