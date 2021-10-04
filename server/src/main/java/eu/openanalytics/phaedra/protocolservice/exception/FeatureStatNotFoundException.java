package eu.openanalytics.phaedra.protocolservice.exception;

import eu.openanalytics.phaedra.util.exceptionhandling.EntityNotFoundException;

public class FeatureStatNotFoundException extends EntityNotFoundException {

    public FeatureStatNotFoundException(long featureStatId) {
        super(String.format("FeatureStat with id %s not found!", featureStatId));
    }

}
