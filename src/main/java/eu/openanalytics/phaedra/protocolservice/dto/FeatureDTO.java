package eu.openanalytics.phaedra.protocolservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import eu.openanalytics.phaedra.protocolservice.enumeration.FeatureType;
import lombok.Data;

import java.util.List;

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
    private ProtocolDTO protocol;
    private FormulaDTO formula;
}
