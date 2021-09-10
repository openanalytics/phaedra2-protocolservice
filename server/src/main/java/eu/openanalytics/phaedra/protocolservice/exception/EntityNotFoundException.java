package eu.openanalytics.phaedra.protocolservice.exception;

abstract public class EntityNotFoundException extends UserVisibleException {

    public EntityNotFoundException(String msg) {
        super(msg);
    }

}
