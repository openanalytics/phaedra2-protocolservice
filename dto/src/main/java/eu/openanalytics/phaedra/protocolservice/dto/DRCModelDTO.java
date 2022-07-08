package eu.openanalytics.phaedra.protocolservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DRCModelDTO {
    Long featureId;
    String name;
    String description;
    String method;
    Map<String, String> inputParameters = new HashMap<>();
}
