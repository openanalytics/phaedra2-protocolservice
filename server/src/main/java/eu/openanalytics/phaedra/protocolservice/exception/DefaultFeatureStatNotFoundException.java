package eu.openanalytics.phaedra.protocolservice.exception;

import eu.openanalytics.phaedra.util.exceptionhandling.EntityNotFoundException;

public class DefaultFeatureStatNotFoundException extends EntityNotFoundException {

    public DefaultFeatureStatNotFoundException(long defaultFeatureStatId) {
        super(String.format("DefaultFeatureStat with id %s not found!", defaultFeatureStatId));
    }

}
