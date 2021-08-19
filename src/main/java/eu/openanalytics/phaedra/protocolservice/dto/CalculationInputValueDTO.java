package eu.openanalytics.phaedra.protocolservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class CalculationInputValueDTO {

    @Null
    private Long id;

    @Null
    private Long featureId;

    @NotNull
    private String variableName;

    private String sourceMeasColName;

    private String sourceFeatureId;

    @JsonIgnore
    @AssertTrue(message = "Only one of sourceMeansColumnName or sourceFeatureId may be specified (not both).")
    public boolean isValid() {
        // only one of these fields may contain a value
        return sourceMeasColName == null ^ sourceFeatureId == null;
    }

}
