package eu.openanalytics.phaedra.protocolservice.model;

import eu.openanalytics.phaedra.protocolservice.enumeration.FeatureType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.*;

@Data()
@NoArgsConstructor
public class Feature {

    @Id
    @Column("id")
    private Long featureId;
    @Column("name")
    private String featureName;
    @Column("alias")
    private String featureAlias;
    private String description;
    private String format;
    @Column("protocol_id")
    private Long protocolId;
    @Column("formula_id")
    private Long formulaId;
    @Column("type")
    private FeatureType featureType;

    @MappedCollection(idColumn = "feature_id")
    private Set<FeatureTagRef> tags = new HashSet<>();

    public Feature(Long protocolId) {
        this.protocolId = protocolId;
    }

    public void addTag(Tag tag) {
        this.tags.add(new FeatureTagRef(tag.getTagId()));
    }
}
