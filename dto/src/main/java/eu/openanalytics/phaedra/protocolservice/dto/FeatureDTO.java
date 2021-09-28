package eu.openanalytics.phaedra.protocolservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import eu.openanalytics.phaedra.protocolservice.enumeration.FeatureType;
import eu.openanalytics.phaedra.protocolservice.enumeration.ScriptLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private ScriptLanguage scriptLanguage; // TODO this should not be needed, because it's already stored in the formula
}
