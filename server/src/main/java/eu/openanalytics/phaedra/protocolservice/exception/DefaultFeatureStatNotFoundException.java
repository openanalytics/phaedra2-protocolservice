package eu.openanalytics.phaedra.protocolservice.exception;

public class DefaultFeatureStatNotFoundException  extends EntityNotFoundException {

    public DefaultFeatureStatNotFoundException(long defaultFeatureStatId) {
        super(String.format("DefaultFeatureStat with id %s not found!", defaultFeatureStatId));
    }

}
