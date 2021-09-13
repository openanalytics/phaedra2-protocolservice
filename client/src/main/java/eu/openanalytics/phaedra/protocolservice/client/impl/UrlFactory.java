package eu.openanalytics.phaedra.protocolservice.client.impl;

public class UrlFactory {

    private static final String PROTOCOL_SERVICE = "http://phaedra-protocol-service/phaedra/protocol-service";
    private static final String MEAS_SERVICE = "http://phaedra-measurement-service/phaedra/meas-service";

    public static String protocol(long protocolId) {
        return String.format("%s/protocols/%s", PROTOCOL_SERVICE, protocolId);
    }

    public static String protocolFeatures(long protocolId) {
        return String.format("%s/protocols/%s/features", PROTOCOL_SERVICE, protocolId);
    }

    public static String protocolCiv(long protocolId) {
        return String.format("%s/protocols/%s/calculationinputvalue", PROTOCOL_SERVICE, protocolId);
    }

    public static String defaultFeatureStat() {
        return String.format("%s/defaultfeaturestat", PROTOCOL_SERVICE);
    }

}
