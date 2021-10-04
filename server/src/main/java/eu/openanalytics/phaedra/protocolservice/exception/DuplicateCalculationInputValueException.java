package eu.openanalytics.phaedra.protocolservice.exception;

import eu.openanalytics.phaedra.util.exceptionhandling.UserVisibleException;

public class DuplicateCalculationInputValueException extends UserVisibleException {

    public DuplicateCalculationInputValueException() {
        // TODO be more specific
        super("CalculationInputValue with one of these parameters already exists!");
    }

}
