package eu.openanalytics.phaedra.protocolservice.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
public class Tag {
    @Id
    @Column("id")
    private Long tagId;
    @Column("name")
    private String tagName;

    public Tag(String tagName) {
        this.tagName = tagName;
    }
}
