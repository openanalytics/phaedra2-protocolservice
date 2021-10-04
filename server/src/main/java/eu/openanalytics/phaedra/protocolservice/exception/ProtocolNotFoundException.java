package eu.openanalytics.phaedra.protocolservice.exception;

import eu.openanalytics.phaedra.util.exceptionhandling.EntityNotFoundException;

public class ProtocolNotFoundException extends EntityNotFoundException {

    public ProtocolNotFoundException(long featureId) {
        super(String.format("Protocol with id %s not found!", featureId));
    }

}
