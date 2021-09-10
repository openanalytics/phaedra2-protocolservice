package eu.openanalytics.phaedra.protocolservice.service;

import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import eu.openanalytics.phaedra.protocolservice.dto.DefaultFeatureStatDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureStatDTO;
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.model.CalculationInputValue;
import eu.openanalytics.phaedra.protocolservice.model.DefaultFeatureStat;
import eu.openanalytics.phaedra.protocolservice.model.Feature;
import eu.openanalytics.phaedra.protocolservice.model.FeatureStat;
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

        modelMapper.createTypeMap(Protocol.class, ProtocolDTO.class)
                .setPropertyCondition(Conditions.isNotNull());

        modelMapper.createTypeMap(ProtocolDTO.class, Protocol.class)
                .setPropertyCondition(Conditions.isNotNull());

        modelMapper.createTypeMap(Feature.class, FeatureDTO.class)
                .setPropertyCondition(Conditions.isNotNull());

        modelMapper.createTypeMap(FeatureDTO.class, Feature.class)
                .setPropertyCondition(Conditions.isNotNull());

        modelMapper.createTypeMap(CalculationInputValueDTO.class, CalculationInputValue.CalculationInputValueBuilder.class, builderConfiguration);

        modelMapper.createTypeMap(CalculationInputValue.class, CalculationInputValueDTO.CalculationInputValueDTOBuilder.class, builderConfiguration);

        modelMapper.createTypeMap(FeatureStat.class, FeatureStatDTO.FeatureStatDTOBuilder.class, builderConfiguration);

        modelMapper.createTypeMap(FeatureStatDTO.class, FeatureStat.FeatureStatBuilder.class, builderConfiguration);

        modelMapper.createTypeMap(DefaultFeatureStat.class, DefaultFeatureStatDTO.DefaultFeatureStatDTOBuilder.class, builderConfiguration);

        modelMapper.createTypeMap(DefaultFeatureStatDTO.class, DefaultFeatureStat.DefaultFeatureStatBuilder.class, builderConfiguration);

        modelMapper.validate(); // ensure that objects can be mapped
    }

    /**
     * Maps a {@link CalculationInputValue} to a {@link CalculationInputValueDTO.CalculationInputValueDTOBuilder}.
     * The return value can be further customized by calling the builder methods.
     */
    public CalculationInputValueDTO.CalculationInputValueDTOBuilder map(CalculationInputValue calculationInputValue) {
        return modelMapper.map(calculationInputValue, CalculationInputValueDTO.CalculationInputValueDTOBuilder.class);
    }

    /**
     * Maps a {@link CalculationInputValueDTO} to a {@link CalculationInputValue.CalculationInputValueBuilder}.
     * The return value can be further customized by calling the builder methods.
     */
    public CalculationInputValue.CalculationInputValueBuilder map(CalculationInputValueDTO calculationInputValueDTO) {
        return modelMapper.map(calculationInputValueDTO, CalculationInputValue.CalculationInputValueBuilder.class);
    }

    /**
     * Maps a {@link ProtocolDTO} to a {@link Protocol}.
     */
    public Protocol map(ProtocolDTO protocolDTO) {
        return modelMapper.map(protocolDTO, Protocol.class);
    }

    /**
     * Maps a {@link Protocol} to a {@link ProtocolDTO}.
     */
    public ProtocolDTO map(Protocol protocol) {
        return modelMapper.map(protocol, ProtocolDTO.class);
    }

    /**
     * Maps a {@link FeatureDTO} to a {@link Feature}.
     */
    public Feature map(FeatureDTO featureDTO) {
        return modelMapper.map(featureDTO, Feature.class);
    }

    /**
     * Maps a {@link Feature} to a {@link FeatureDTO}.
     */
    public FeatureDTO map(Feature feature) {
        return modelMapper.map(feature, FeatureDTO.class);
    }

    /**
     * Maps a {@link FeatureStatDTO} to a {@link FeatureStat.FeatureStatBuilder}.
     * The return value can be further customized by calling the builder methods.
     */
    public FeatureStat.FeatureStatBuilder map(FeatureStatDTO featureStatDTO) {
        return modelMapper.map(featureStatDTO, FeatureStat.FeatureStatBuilder.class);
    }

    /**
     * Maps a {@link FeatureStat} to a {@link FeatureStatDTO.FeatureStatDTOBuilder}.
     * The return value can be further customized by calling the builder methods.
     */
    public FeatureStatDTO.FeatureStatDTOBuilder map(FeatureStat featureStat) {
        return modelMapper.map(featureStat, FeatureStatDTO.FeatureStatDTOBuilder.class);
    }

    /**
     * Maps a {@link DefaultFeatureStat} to a {@link DefaultFeatureStatDTO.DefaultFeatureStatDTOBuilder}.
     * The return value can be further customized by calling the builder methods.
     */
    public DefaultFeatureStatDTO.DefaultFeatureStatDTOBuilder map(DefaultFeatureStat defaultFeatureStat) {
        return modelMapper.map(defaultFeatureStat, DefaultFeatureStatDTO.DefaultFeatureStatDTOBuilder.class);
    }

    /**
     * Maps a {@link DefaultFeatureStatDTO} to a {@link DefaultFeatureStat.DefaultFeatureStatBuilder}.
     * The return value can be further customized by calling the builder methods.
     */
    public DefaultFeatureStat.DefaultFeatureStatBuilder map(DefaultFeatureStatDTO defaultFeatureStatDTO) {
        return modelMapper.map(defaultFeatureStatDTO, DefaultFeatureStat.DefaultFeatureStatBuilder.class);
    }
}
