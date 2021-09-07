package eu.openanalytics.phaedra.protocolservice.client.impl;

import eu.openanalytics.phaedra.protocolservice.client.ProtocolServiceClient;
import eu.openanalytics.phaedra.protocolservice.client.exception.ProtocolUnresolvableException;
import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.util.PhaedraRestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class HttpProtocolServiceClient implements ProtocolServiceClient {

    private final RestTemplate restTemplate;

    public HttpProtocolServiceClient(PhaedraRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProtocolDTO getProtocol(long protocolId) throws ProtocolUnresolvableException {
        try {
            var res = restTemplate.getForObject(UrlFactory.protocol(protocolId), ProtocolDTO.class);
            if (res == null) {
                throw new ProtocolUnresolvableException("Protocol could not be converted");
            }
            return res;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProtocolUnresolvableException("Protocol not found");
        } catch (HttpClientErrorException ex) {
            throw new ProtocolUnresolvableException("Error while fetching protocol");
        }
    }

    public List<FeatureDTO> getFeaturesOfProtocol(long protocolId) throws ProtocolUnresolvableException {
        try {
            var res = restTemplate.getForObject(UrlFactory.protocolFeatures(protocolId), FeatureDTO[].class);
            if (res == null) {
                throw new ProtocolUnresolvableException("Features could not be converted");
            }
            return Arrays.asList(res);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProtocolUnresolvableException("Features not found");
        } catch (HttpClientErrorException ex) {
            throw new ProtocolUnresolvableException("Error while fetching features");
        }
    }

    public List<CalculationInputValueDTO> getCalculationInputValuesOfProtocol(long protocolId) throws ProtocolUnresolvableException {
        try {
            var res = restTemplate.getForObject(UrlFactory.protocolCiv(protocolId), CalculationInputValueDTO[].class);
            if (res == null) {
                throw new ProtocolUnresolvableException("Civs could not be converted");
            }
            return Arrays.asList(res);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProtocolUnresolvableException("Civs not found");
        } catch (HttpClientErrorException ex) {
            throw new ProtocolUnresolvableException("Error while fetching Civs");
        }
    }

}