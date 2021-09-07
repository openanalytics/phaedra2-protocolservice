package eu.openanalytics.phaedra.protocolservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("feature_class")
@Data
@AllArgsConstructor
public class FeatureClassRef {
    @Column("class_id")
    private Long classId;
}
