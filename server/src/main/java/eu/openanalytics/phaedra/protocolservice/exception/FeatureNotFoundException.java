package eu.openanalytics.phaedra.protocolservice.exception;

import eu.openanalytics.phaedra.util.exceptionhandling.EntityNotFoundException;

public class FeatureNotFoundException extends EntityNotFoundException {

    public FeatureNotFoundException(long featureId) {
        super(String.format("Feature with id %s not found!", featureId));
    }

}
