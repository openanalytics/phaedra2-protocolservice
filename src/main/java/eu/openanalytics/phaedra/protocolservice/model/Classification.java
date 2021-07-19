package eu.openanalytics.phaedra.protocolservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
public class Classification {
    @Id
    private Long id;
    private String name;
    private String description;
    private Integer color;
    private String symbol;
    @NotNull
    private Integer value;

}
