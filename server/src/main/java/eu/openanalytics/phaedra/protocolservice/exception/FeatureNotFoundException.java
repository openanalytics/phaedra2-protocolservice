package eu.openanalytics.phaedra.protocolservice.exception;

public class FeatureNotFoundException extends EntityNotFoundException {

    public FeatureNotFoundException(long featureId) {
        super(String.format("Feature with id %s not found!", featureId));
    }

}
