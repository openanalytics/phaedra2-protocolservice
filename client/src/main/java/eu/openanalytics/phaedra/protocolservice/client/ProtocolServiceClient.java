/**
 * Phaedra II
 *
 * Copyright (C) 2016-2023 Open Analytics
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
