package eu.openanalytics.phaedra.protocolservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import eu.openanalytics.phaedra.protocolservice.enumeration.FeatureType;
import eu.openanalytics.phaedra.protocolservice.enumeration.ScriptLanguage;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeatureDTO {
    private Long id;
    private String name;
    private String alias;
    private String description;
    private String format;
    private FeatureType type;
    private Integer sequence;
    private Long protocolId;
    private Long formulaId;
    private String trigger;
    private ScriptLanguage scriptLanguage;
}
