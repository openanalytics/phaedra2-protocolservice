package eu.openanalytics.phaedra.protocolservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaggedObjectDTO {
    private Long objectId;
    private String objectClass;
    private String tag;

    public TaggedObjectDTO(String objectClass) {
        this.objectClass = objectClass;
    }
}
