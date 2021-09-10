package eu.openanalytics.phaedra.protocolservice.dto;

import eu.openanalytics.phaedra.protocolservice.dto.validation.OnCreate;
import eu.openanalytics.phaedra.protocolservice.dto.validation.OnUpdate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.experimental.NonFinal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Value
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // Jackson deserialize compatibility
@NonFinal
public class FeatureStatDTO {

    @Null(groups = OnCreate.class, message = "Id must be null when creating a FeatureStat")
    @NotNull(groups = OnUpdate.class, message = "Id must be specified when updating a FeatureStat")
    Long id;

    @Null(groups = OnCreate.class, message = "FeatureId must be specified in URL and not repeated in body")
    @NotNull(groups = OnUpdate.class, message = "FeatureId must be specified when updating a FeatureStat")
    Long featureId;

    String welltype;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Name is mandatory")
    String name;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "FormulaId is mandatory")
    Long formulaId;

}
