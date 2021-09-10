package eu.openanalytics.phaedra.protocolservice.service;

import eu.openanalytics.phaedra.protocolservice.dto.CalculationInputValueDTO;
import eu.openanalytics.phaedra.protocolservice.exception.DuplicateCalculationInputValueException;
import eu.openanalytics.phaedra.protocolservice.exception.FeatureNotFoundException;
import eu.openanalytics.phaedra.protocolservice.exception.ProtocolNotFoundException;
import eu.openanalytics.phaedra.protocolservice.model.CalculationInputValue;
import eu.openanalytics.phaedra.protocolservice.repository.CalculationInputValueRepository;
import eu.openanalytics.phaedra.protocolservice.repository.FeatureRepository;
import eu.openanalytics.phaedra.protocolservice.repository.ProtocolRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalculationInputValueService {

    private final FeatureRepository featureRepository;
    private final ProtocolRepository protocolRepository;
    private final CalculationInputValueRepository calculationInputValueRepository;
    private final ModelMapper modelMapper;

    public CalculationInputValueService(FeatureRepository featureRepository, ProtocolRepository protocolRepository, CalculationInputValueRepository calculationInputValueRepository, ModelMapper modelMapper) {
        this.featureRepository = featureRepository;
        this.protocolRepository = protocolRepository;
        this.calculationInputValueRepository = calculationInputValueRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Create a CalculationInputValue
     *
     * @param featureId
     * @param calculationInputValueDTO the CalculationInputValue to create
     * @return the resulting DTO
     * @throws FeatureNotFoundException when the given feature is not found
     * @throws DuplicateCalculationInputValueException when a CalculationInputValue for this feature already exists with the given values
     */
    public CalculationInputValueDTO create(Long featureId, CalculationInputValueDTO calculationInputValueDTO) throws FeatureNotFoundException, DuplicateCalculationInputValueException {
        var feature = featureRepository.findById(featureId);
        if (feature.isEmpty()) {
            throw new FeatureNotFoundException(featureId);
        }

        CalculationInputValue calculationInputValue = modelMapper.map(calculationInputValueDTO)
                .featureId(featureId)
                .build();

        try {
            return save(calculationInputValue);
        } catch (DbActionExecutionException ex) {
            if (ex.getCause() instanceof DuplicateKeyException) {
                throw new DuplicateCalculationInputValueException();
            }
            throw ex;
        }
    }

    /**
     * Get all {@link CalculationInputValueDTO} of a feature.
     * @param featureId the feature to the {@link CalculationInputValue} from
     * @return the CalculationInputValues of the feature
     * @throws FeatureNotFoundException when the feature is not found
     */
    public List<CalculationInputValueDTO> getByFeatureId(Long featureId) throws FeatureNotFoundException {
        var feature = featureRepository.findById(featureId);
        if (feature.isEmpty()) {
            throw new FeatureNotFoundException(featureId);
        }

        return calculationInputValueRepository.findByFeatureId(featureId)
                .stream()
                .map((f) -> modelMapper.map(f).build())
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Get all {@link CalculationInputValueDTO} of a protocol by looking at the features of the given protocol.
     * @param protocolId the protocol to fetch the {@link CalculationInputValue} from
     * @return the CalculationInputValues of the protocol
     * @throws ProtocolNotFoundException when the protocol is not found
     */
    public List<CalculationInputValueDTO> getByProtocolId(Long protocolId) throws ProtocolNotFoundException {
        var protocol = protocolRepository.findById(protocolId);
        if (protocol.isEmpty()) {
            throw new ProtocolNotFoundException(protocolId);
        }

        return calculationInputValueRepository.findByProtocolId(protocolId)
                .stream()
                .map((f) -> modelMapper.map(f).build())
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Saves a {@link CalculationInputValue} and returns the resulting corresponding {@link CalculationInputValueDTO}.
     */
    private CalculationInputValueDTO save(CalculationInputValue civ) {
        CalculationInputValue newCiv = calculationInputValueRepository.save(civ);
        return modelMapper.map(newCiv).build();
    }

}
