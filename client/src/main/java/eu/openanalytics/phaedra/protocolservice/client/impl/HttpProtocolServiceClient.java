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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import eu.openanalytics.phaedra.protocolservice.client.ProtocolServiceClient;
import eu.openanalytics.phaedra.protocolservice.client.exception.DefaultFeatureStatUnresolvableException;
import eu.openanalytics.phaedra.protocolservice.client.exception.FeatureUnresolvableException;
import eu.openanalytics.phaedra.protocolservice.client.exception.ProtocolUnresolvableException;
import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import eu.openanalytics.phaedra.protocolservice.dto.DefaultFeatureStatDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureStatDTO;
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.util.PhaedraRestTemplate;
import eu.openanalytics.phaedra.util.auth.IAuthorizationService;

@Component
public class HttpProtocolServiceClient implements ProtocolServiceClient {

    private final RestTemplate restTemplate;
    private final IAuthorizationService authService;
    private final UrlFactory urlFactory;

    private static final String PROP_BASE_URL = "phaedra.protocol-service.base-url";
    private static final String DEFAULT_BASE_URL = "http://phaedra-protocol-service:8080/phaedra/protocol-service";

    public HttpProtocolServiceClient(PhaedraRestTemplate restTemplate, IAuthorizationService authService, Environment environment) {
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.urlFactory = new UrlFactory(environment.getProperty(PROP_BASE_URL, DEFAULT_BASE_URL));
    }

    public ProtocolDTO getProtocol(long protocolId) throws ProtocolUnresolvableException {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(makeHttpHeaders());
            var res = restTemplate.exchange(urlFactory.protocol(protocolId), HttpMethod.GET, httpEntity, ProtocolDTO.class);
            if (res == null) {
                throw new ProtocolUnresolvableException("Protocol could not be converted");
            }
            return res.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProtocolUnresolvableException("Protocol not found");
        } catch (HttpClientErrorException ex) {
            throw new ProtocolUnresolvableException("Error while fetching protocol");
        }
    }

    public List<FeatureDTO> getFeaturesOfProtocol(long protocolId) throws ProtocolUnresolvableException {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(makeHttpHeaders());
            var res = restTemplate.exchange(urlFactory.protocolFeatures(protocolId), HttpMethod.GET, httpEntity, FeatureDTO[].class);
            if (res == null) {
                throw new ProtocolUnresolvableException("Features could not be converted");
            }
            return Arrays.asList(res.getBody());
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProtocolUnresolvableException("Features not found");
        } catch (HttpClientErrorException ex) {
            throw new ProtocolUnresolvableException("Error while fetching features");
        }
    }

    @Override
    public List<FeatureDTO> getFeaturesOfProtocols(List<Long> protocolIds)
        throws ProtocolUnresolvableException {
        try {
            List<FeatureDTO> results = new ArrayList<>();
            for (Long protocolId : protocolIds) {
                results.addAll(getFeaturesOfProtocol(protocolId));
            }
            return results;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProtocolUnresolvableException("Features not found");
        } catch (HttpClientErrorException ex) {
            throw new ProtocolUnresolvableException("Error while fetching features");
        }
    }

    public List<CalculationInputValueDTO> getCalculationInputValuesOfProtocol(long protocolId) throws ProtocolUnresolvableException {
        // 3. get CalculationInputValues corresponding to the feature
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(makeHttpHeaders());
            var res = restTemplate.exchange(urlFactory.protocolCiv(protocolId), HttpMethod.GET, httpEntity, CalculationInputValueDTO[].class);
            if (res == null) {
                throw new ProtocolUnresolvableException("Civs could not be converted");
            }
            return Arrays.asList(res.getBody());
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProtocolUnresolvableException("Civs not found");
        } catch (HttpClientErrorException ex) {
            throw new ProtocolUnresolvableException("Error while fetching Civs");
        }
    }

    public List<FeatureStatDTO> getFeatureStatsOfProtocol(long protocolId) throws ProtocolUnresolvableException {
        // 4. get FeatureStats of protocol
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(makeHttpHeaders());
            var res = restTemplate.exchange(urlFactory.featureStats(protocolId), HttpMethod.GET, httpEntity, FeatureStatDTO[].class);
            if (res == null) {
                throw new ProtocolUnresolvableException("FeatureStats could not be converted");
            }
            return Arrays.asList(res.getBody());
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProtocolUnresolvableException("FeatureStats not found");
        } catch (HttpClientErrorException ex) {
            throw new ProtocolUnresolvableException("Error while fetching FeatureStats");
        }
    }

    @Override
    public DefaultFeatureStatDTO createDefaultFeatureStat(String name, Long formulaId, Boolean plateStat, Boolean wellTypeStat) throws DefaultFeatureStatUnresolvableException {
        try {
            var input = DefaultFeatureStatDTO
                    .builder()
                    .plateStat(plateStat)
                    .welltypeStat(wellTypeStat)
                    .name(name)
                    .formulaId(formulaId)
                    .build();

            var res = restTemplate.postForObject(urlFactory.defaultFeatureStat(), input, DefaultFeatureStatDTO.class);
            if (res == null) {
                throw new DefaultFeatureStatUnresolvableException("FeatureStats could not be converted");
            }
            return res;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new DefaultFeatureStatUnresolvableException("FeatureStats not found");
        } catch (HttpClientErrorException ex) {
            throw new DefaultFeatureStatUnresolvableException("Error while fetching FeatureStats");
        }
    }

    @Override
    public FeatureDTO getFeature(long featureId) throws FeatureUnresolvableException {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(makeHttpHeaders());
            var res = restTemplate.exchange(urlFactory.feature(featureId), HttpMethod.GET, httpEntity, FeatureDTO.class);
            if (res == null) {
                throw new FeatureUnresolvableException("Feature could not be converted");
            }
            return res.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new FeatureUnresolvableException("Feature not found");
        } catch (HttpClientErrorException ex) {
            throw new FeatureUnresolvableException("Error while fetching feature");
        }
    }

    private HttpHeaders makeHttpHeaders() {
    	HttpHeaders httpHeaders = new HttpHeaders();
        String bearerToken = authService.getCurrentBearerToken();
    	if (bearerToken != null) httpHeaders.set(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", bearerToken));
    	return httpHeaders;
    }
}
