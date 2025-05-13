/**
 * Phaedra II
 *
 * Copyright (C) 2016-2025 Open Analytics
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

    private String baseURL;
    
    public UrlFactory(String baseURL) {
    	this.baseURL = baseURL;
	}

    public String protocol(long protocolId) {
        return String.format("%s/protocols/%s", baseURL, protocolId);
    }

    public String protocolFeatures(long protocolId) {
        return String.format("%s/protocols/%s/features", baseURL, protocolId);
    }

    public String protocolCiv(long protocolId) {
        return String.format("%s/protocols/%s/calculationinputvalues", baseURL, protocolId);
    }

    public String defaultFeatureStat() {
        return String.format("%s/defaultfeaturestats", baseURL);
    }

    public String featureStats(long protocolId) {
        return String.format("%s/protocols/%s/featurestats", baseURL, protocolId);
    }

    public String feature(long featureId) {
        return String.format("%s/features/%s", baseURL, featureId);
    }
}
