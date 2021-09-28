package eu.openanalytics.phaedra.protocolservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolDTO {
    private Long id;
    private String name;
    private String description;
    private boolean editable;
    private boolean inDevelopment;
    private String lowWelltype;
    private String highWelltype;
}
