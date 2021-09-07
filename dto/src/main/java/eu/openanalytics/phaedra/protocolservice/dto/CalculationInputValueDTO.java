package eu.openanalytics.phaedra.protocolservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.experimental.NonFinal;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Value
@Builder
@With
@AllArgsConstructor
@NonFinal
public class CalculationInputValueDTO {

    @Null
    Long id;

    @Null
    Long featureId;

    @NotNull
    String variableName;

    String sourceMeasColName;

    Long sourceFeatureId;

    @JsonIgnore
    @AssertTrue(message = "Only one of sourceMeansColumnName or sourceFeatureId may be specified (not both).")
    public boolean isValid() {
        // only one of these fields may contain a value
        return sourceMeasColName == null ^ sourceFeatureId == null;
    }

}
