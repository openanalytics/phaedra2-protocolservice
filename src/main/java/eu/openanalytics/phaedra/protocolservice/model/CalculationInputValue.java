package eu.openanalytics.phaedra.protocolservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class CalculationInputValue {
    @Id
    private Long id;
    private Long featureId;
    private String sourceMeasColName;
    private String sourceFeatureId;
    private String variableName;
}
