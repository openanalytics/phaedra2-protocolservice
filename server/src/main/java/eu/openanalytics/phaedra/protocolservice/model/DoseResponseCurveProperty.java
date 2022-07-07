package eu.openanalytics.phaedra.protocolservice.model;

import lombok.*;
import lombok.experimental.NonFinal;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DoseResponseCurveProperty {
    @Id
    Long id;
    @NotNull
    @Column("feature_id")
    Long featureId;
    String name;
    String value;
}
