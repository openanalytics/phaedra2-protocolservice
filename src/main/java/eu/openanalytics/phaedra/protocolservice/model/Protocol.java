package eu.openanalytics.phaedra.protocolservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
public class Protocol {
    @Id
    private Long id;
    private String name;
    private String description;
    private boolean editable;
    @Column("in_development")
    private boolean inDevelopment;
    @Column("low_welltype")
    private String lowWelltype;
    @Column("high_welltype")
    private String highWelltype;

}
