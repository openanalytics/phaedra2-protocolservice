/**
 * Phaedra II
 *
 * Copyright (C) 2016-2024 Open Analytics
 *
 * ===========================================================================
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License as published by
 * The Apache Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Apache License for more details.
 *
 * You should have received a copy of the Apache License
 * along with this program.  If not, see <http://www.apache.org/licenses/>
 */
package eu.openanalytics.phaedra.protocolservice.client.impl;

public class UrlFactory {

    private static final String PROTOCOL_SERVICE = "http://phaedra-protocol-service:8080/phaedra/protocol-service";

    public static String protocol(long protocolId) {
        return String.format("%s/protocols/%s", PROTOCOL_SERVICE, protocolId);
    }

    public static String protocolFeatures(long protocolId) {
        return String.format("%s/protocols/%s/features", PROTOCOL_SERVICE, protocolId);
    }

    public static String protocolCiv(long protocolId) {
        return String.format("%s/protocols/%s/calculationinputvalues", PROTOCOL_SERVICE, protocolId);
    }

    public static String defaultFeatureStat() {
        return String.format("%s/defaultfeaturestats", PROTOCOL_SERVICE);
    }

    public static String featureStats(long protocolId) {
        return String.format("%s/protocols/%s/featurestats", PROTOCOL_SERVICE, protocolId);
    }

    public static String feature(long featureId) {
        return String.format("%s/features/%s", PROTOCOL_SERVICE, featureId);
    }
}
