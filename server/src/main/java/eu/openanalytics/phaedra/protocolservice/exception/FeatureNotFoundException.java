package eu.openanalytics.phaedra.protocolservice.exception;

public class FeatureNotFoundException extends UserVisibleException {

    public FeatureNotFoundException(long featureId) {
        super(String.format("Feature with id %s not found!", featureId));
    }

}
