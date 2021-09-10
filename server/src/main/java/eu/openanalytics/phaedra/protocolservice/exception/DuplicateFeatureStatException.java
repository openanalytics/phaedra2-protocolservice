package eu.openanalytics.phaedra.protocolservice.exception;

public class DuplicateFeatureStatException extends UserVisibleException  {

    public DuplicateFeatureStatException() {
        // TODO be more specific
        super("FeatureStat with one of these parameters already exists!");
    }

}
