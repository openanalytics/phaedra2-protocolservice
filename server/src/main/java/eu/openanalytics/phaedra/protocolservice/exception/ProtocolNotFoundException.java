package eu.openanalytics.phaedra.protocolservice.exception;

public class ProtocolNotFoundException extends EntityNotFoundException {

    public ProtocolNotFoundException(long featureId) {
        super(String.format("Protocol with id %s not found!", featureId));
    }

}
