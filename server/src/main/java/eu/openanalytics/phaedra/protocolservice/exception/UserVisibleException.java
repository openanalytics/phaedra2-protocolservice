package eu.openanalytics.phaedra.protocolservice.exception;

/**
 * An exception of which the message can be returned to users of the REST API without causing any security risks.
 * I.e. the message does not contain to many details about the internal working of this service (sql queries, (env) variables etc).
 */
public class UserVisibleException extends Exception {

    public UserVisibleException(String msg) {
        super(msg);
    }

}