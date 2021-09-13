package eu.openanalytics.phaedra.protocolservice.client;

import eu.openanalytics.phaedra.protocolservice.client.exception.DefaultFeatureStatUnresolvableException;
import eu.openanalytics.phaedra.protocolservice.client.exception.ProtocolUnresolvableException;
import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import eu.openanalytics.phaedra.protocolservice.dto.DefaultFeatureStatDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureStatDTO;
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;

import java.util.List;

public interface ProtocolServiceClient {

    ProtocolDTO getProtocol(long protocolId) throws ProtocolUnresolvableException;

    List<FeatureDTO> getFeaturesOfProtocol(long protocolId) throws ProtocolUnresolvableException;

    List<CalculationInputValueDTO> getCalculationInputValuesOfProtocol(long protocolId) throws ProtocolUnresolvableException;

    List<FeatureStatDTO> getFeatureStatsOfProtocol(long protocolId) throws ProtocolUnresolvableException;

    DefaultFeatureStatDTO createDefaultFeatureStat(String name, Long formulaId, Boolean plateStat, Boolean wellTypeStat) throws DefaultFeatureStatUnresolvableException;

}