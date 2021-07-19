package eu.openanalytics.phaedra.protocolservice.model;

import lombok.Data;

@Data
public class CalculationInputValue {
    private Long featureId;
    private String sourceMeasColName;
    private String sourceFeatureId;
}
