package eu.openanalytics.phaedra.protocolservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("feature_tag")
@Data
@AllArgsConstructor
public class FeatureTagRef {
    @Column("tag_id")
    private Long tagId;
}
