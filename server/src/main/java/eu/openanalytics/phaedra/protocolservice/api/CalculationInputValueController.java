package eu.openanalytics.phaedra.protocolservice.api;

import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import eu.openanalytics.phaedra.protocolservice.exception.UserVisibleException;
import eu.openanalytics.phaedra.protocolservice.service.CalculationInputValueService;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Validated
public class CalculationInputValueController {

    private final CalculationInputValueService calculationInputValueService;

    public CalculationInputValueController(CalculationInputValueService calculationInputValueService) {
        this.calculationInputValueService = calculationInputValueService;
    }

    @PostMapping("/features/{featureId}/calculationinputvalue")
    public ResponseEntity<CalculationInputValueDTO> createCalculationInputValue(@PathVariable Long featureId,
                                                                                @Valid @RequestBody CalculationInputValueDTO calculationInputValueDTO) throws UserVisibleException {
        calculationInputValueDTO.setFeatureId(featureId);
        var res = calculationInputValueService.create(calculationInputValueDTO);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/features/{featureId}/calculationinputvalue")
    public ResponseEntity<CalculationInputValueDTO> updateCalculationInputValue(@PathVariable Long featureId,
                                                                                @Valid @RequestBody CalculationInputValueDTO calculationInputValueDTO) throws UserVisibleException {
        throw new NotImplementedException("TODO");
    }

    @GetMapping("/features/{featureId}/calculationinputvalue")
    public ResponseEntity<List<CalculationInputValueDTO>> getCalculationInputValue(@PathVariable Long featureId) throws UserVisibleException {
        var res = calculationInputValueService.getByFeatureId(featureId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/protocols/{protocolId}/calculationinputvalue")
    public ResponseEntity<List<CalculationInputValueDTO>> getCalculationInputValueByProtocolIds(@PathVariable Long protocolId) throws UserVisibleException {
        var res = calculationInputValueService.getByProtocolId(protocolId);
        return ResponseEntity.ok(res);
    }

    // TODO move to phaedra-commons ?
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserVisibleException.class)
    public HashMap<String, Object> handleValidationExceptions(UserVisibleException ex) {
        return new HashMap<>() {{
            put("status", "error");
            put("error", ex.getMessage());
        }};
    }

    // TODO move to phaedra-commons ?
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HashMap<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new HashMap<>() {{
            put("status", "error");
            put("error", "Validation error");
            put("malformed_fields", ex.getBindingResult()
                    .getAllErrors()
                    .stream().
                    collect(Collectors.toMap(
                            error -> ((FieldError) error).getField(),
                            error -> Optional.ofNullable(error.getDefaultMessage()).orElse("Field is invalid"))
                    )
            );
        }};
    }

}
