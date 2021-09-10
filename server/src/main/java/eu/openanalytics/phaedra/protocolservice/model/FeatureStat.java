package eu.openanalytics.phaedra.protocolservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.experimental.NonFinal;
import org.springframework.data.annotation.Id;

@Value
@Builder
@With
@AllArgsConstructor
@NonFinal
public class FeatureStat {
    @Id
    Long id;

    Long featureId;

    String welltype;

    String name;

    Long formulaId;
}
