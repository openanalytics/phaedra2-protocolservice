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
public class DefaultFeatureStat {
    @Id
    Long id;

    Boolean plateStat;

    Boolean welltypeStat;

    String name;

    Long formulaId;
}
