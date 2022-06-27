package eu.openanalytics.phaedra.protocolservice;

import com.fasterxml.jackson.annotation.JsonInclude;
import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormulaDTO {
    Long id;
    String name;
    String description;
    String category;
    String formula;
    String language;
    String scope;

    List<CalculationInputValueDTO> civs;

    String previousVersion;
    String versionNumber;

    String createdBy;
    Date createdOn;
    String updatedBy;
    Date updatedOn;
}
