package eu.openanalytics.phaedra.protocolservice.api;

import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import eu.openanalytics.phaedra.protocolservice.service.CalculationInputValueService;
import eu.openanalytics.phaedra.util.exceptionhandling.HttpMessageNotReadableExceptionHandler;
import eu.openanalytics.phaedra.util.exceptionhandling.MethodArgumentNotValidExceptionHandler;
import eu.openanalytics.phaedra.util.exceptionhandling.UserVisibleException;
import eu.openanalytics.phaedra.util.exceptionhandling.UserVisibleExceptionHandler;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class CalculationInputValueController implements MethodArgumentNotValidExceptionHandler, HttpMessageNotReadableExceptionHandler, UserVisibleExceptionHandler {

    private final CalculationInputValueService calculationInputValueService;

    public CalculationInputValueController(CalculationInputValueService calculationInputValueService) {
        this.calculationInputValueService = calculationInputValueService;
    }

    @PostMapping("/features/{featureId}/calculationinputvalue")
    public ResponseEntity<CalculationInputValueDTO> createCalculationInputValue(@PathVariable Long featureId,
                                                                                @Valid @RequestBody CalculationInputValueDTO calculationInputValueDTO) throws UserVisibleException {
        var res = calculationInputValueService.create(featureId, calculationInputValueDTO);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/features/{featureId}/calculationinputvalue")
    public ResponseEntity<CalculationInputValueDTO> updateCalculationInputValue(@PathVariable Long featureId,
                                                                                @RequestBody CalculationInputValueDTO calculationInputValueDTO) throws UserVisibleException {
        var res = calculationInputValueService.update(featureId, calculationInputValueDTO);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/features/{featureId}/calculationinputvalue")
    public ResponseEntity<List<CalculationInputValueDTO>> getCalculationInputValue(@PathVariable Long featureId) throws UserVisibleException {
        var res = calculationInputValueService.getByFeatureId(featureId);
        if (res.size() != 0)
            return new ResponseEntity<>(res, HttpStatus.OK);
        else
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/protocols/{protocolId}/calculationinputvalue")
    public ResponseEntity<List<CalculationInputValueDTO>> getCalculationInputValueByProtocolIds(@PathVariable Long protocolId) throws UserVisibleException {
        var res = calculationInputValueService.getByProtocolId(protocolId);
        if (res.size() != 0)
            return new ResponseEntity<>(res, HttpStatus.OK);
        else
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

}
