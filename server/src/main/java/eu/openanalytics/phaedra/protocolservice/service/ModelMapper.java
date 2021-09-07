package eu.openanalytics.phaedra.protocolservice.service;

import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.model.CalculationInputValue;
import eu.openanalytics.phaedra.protocolservice.model.Feature;
import eu.openanalytics.phaedra.protocolservice.model.Protocol;
import org.modelmapper.Conditions;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.NameTransformers;
import org.modelmapper.convention.NamingConventions;
import org.springframework.stereotype.Service;

@Service
public class ModelMapper {

    private final org.modelmapper.ModelMapper modelMapper = new org.modelmapper.ModelMapper();

    public ModelMapper() {
        Configuration builderConfiguration = modelMapper.getConfiguration().copy()
                .setDestinationNameTransformer(NameTransformers.builder())
                .setDestinationNamingConvention(NamingConventions.builder());

        modelMapper.createTypeMap(CalculationInputValueDTO.class, CalculationInputValue.CalculationInputValueBuilder.class, builderConfiguration)
                .setPropertyCondition(Conditions.isNotNull());

        modelMapper.createTypeMap(CalculationInputValue.class, CalculationInputValueDTO.CalculationInputValueDTOBuilder.class, builderConfiguration)
                .setPropertyCondition(Conditions.isNotNull());

        modelMapper.createTypeMap(Protocol.class, ProtocolDTO.class)
                .setPropertyCondition(Conditions.isNotNull());

        modelMapper.createTypeMap(ProtocolDTO.class, Protocol.class)
                .setPropertyCondition(Conditions.isNotNull());

        modelMapper.createTypeMap(Feature.class, FeatureDTO.class)
                .setPropertyCondition(Conditions.isNotNull());

        modelMapper.createTypeMap(FeatureDTO.class, Feature.class)
                .setPropertyCondition(Conditions.isNotNull());

        modelMapper.validate(); // ensure that objects can be mapped
    }

    /**
     * Maps a {@link CalculationInputValue} to a {@link CalculationInputValueDTO.CalculationInputValueDTOBuilder}.
     * The return value can be further customized by calling the builder methods.
     */
    public CalculationInputValueDTO.CalculationInputValueDTOBuilder map(CalculationInputValue calculationInputValue) {
        CalculationInputValueDTO.CalculationInputValueDTOBuilder builder = CalculationInputValueDTO.builder();
        modelMapper.map(calculationInputValue, builder);
        return builder;
    }

    /**
     * Maps a {@link CalculationInputValueDTO} to a {@link CalculationInputValue.CalculationInputValueBuilder}.
     * The return value can be further customized by calling the builder methods.
     */
    public CalculationInputValue.CalculationInputValueBuilder map(CalculationInputValueDTO calculationInputValueDTO) {
        CalculationInputValue.CalculationInputValueBuilder builder = CalculationInputValue.builder();
        modelMapper.map(calculationInputValueDTO, builder);
        return builder;
    }

    /**
     * Maps a {@link ProtocolDTO} to a {@link Protocol}.
     */
    public Protocol map(ProtocolDTO protocolDTO) {
        Protocol protocol = new Protocol();
        modelMapper.map(protocolDTO, protocol);
        return protocol;
    }

    /**
     * Maps a {@link Protocol} to a {@link ProtocolDTO}.
     */
    public ProtocolDTO map(Protocol protocol) {
        ProtocolDTO protocolDTO = new ProtocolDTO();
        modelMapper.map(protocol, protocolDTO);
        return protocolDTO;
    }

    /**
     * Maps a {@link FeatureDTO} to a {@link Feature}.
     */
    public Feature map(FeatureDTO featureDTO) {
        Feature feature = new Feature();
        modelMapper.map(featureDTO, feature);
        return feature;
    }

    /**
     * Maps a {@link Feature} to a {@link FeatureDTO}.
     */
    public FeatureDTO map(Feature feature) {
        FeatureDTO featureDTO = new FeatureDTO();
        modelMapper.map(feature, featureDTO);
        return featureDTO;
    }
}
