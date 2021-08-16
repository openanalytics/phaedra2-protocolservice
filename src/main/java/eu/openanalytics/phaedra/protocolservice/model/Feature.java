package eu.openanalytics.phaedra.protocolservice.model;

import eu.openanalytics.phaedra.protocolservice.enumeration.FeatureType;
import eu.openanalytics.phaedra.protocolservice.enumeration.ScriptLanguage;
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
    private Long id;
    private String name;
    private String alias;
    private String description;
    private String format;
    @Column("protocol_id")
    private Long protocolId;
    @Column("formula_id")
    private Long formulaId;
    @Column("type")
    private FeatureType featureType;
    @Column("script_language")
    private ScriptLanguage scriptLanguage;
    @Column("calc_sequence")
    private Integer sequence;
    @Column("calc_trigger")
    private String trigger;

    public Feature(Long protocolId) {
        this.protocolId = protocolId;
    }
}
