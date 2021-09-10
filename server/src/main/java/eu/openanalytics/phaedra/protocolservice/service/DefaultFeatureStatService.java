package eu.openanalytics.phaedra.protocolservice.service;

import eu.openanalytics.phaedra.protocolservice.dto.DefaultFeatureStatDTO;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureStatDTO;
import eu.openanalytics.phaedra.protocolservice.exception.DefaultFeatureStatNotFoundException;
import eu.openanalytics.phaedra.protocolservice.exception.DuplicateFeatureStatException;
import eu.openanalytics.phaedra.protocolservice.exception.FeatureNotFoundException;
import eu.openanalytics.phaedra.protocolservice.exception.UserVisibleException;
import eu.openanalytics.phaedra.protocolservice.model.DefaultFeatureStat;
import eu.openanalytics.phaedra.protocolservice.model.FeatureStat;
import eu.openanalytics.phaedra.protocolservice.repository.DefaultFeatureStatRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultFeatureStatService {

    private final DefaultFeatureStatRepository defaultFeatureStatRepository;
    private final ModelMapper modelMapper;

    public DefaultFeatureStatService(DefaultFeatureStatRepository defaultFeatureStatRepository, ModelMapper modelMapper) {
        this.defaultFeatureStatRepository = defaultFeatureStatRepository;
        this.modelMapper = modelMapper;
    }

    public DefaultFeatureStatDTO create(DefaultFeatureStatDTO defaultFeatureStatDTO) throws FeatureNotFoundException, DuplicateFeatureStatException {
        DefaultFeatureStat defaultFeatureStat = modelMapper.map(defaultFeatureStatDTO).build();

        try {
            return save(defaultFeatureStat);
        } catch (DbActionExecutionException ex) {
            if (ex.getCause() instanceof DuplicateKeyException) {
                throw new DuplicateFeatureStatException();
            }
            throw ex;
        }
    }

    public DefaultFeatureStatDTO update(DefaultFeatureStatDTO defaultFeatureStatDTO) throws UserVisibleException {
        Optional<DefaultFeatureStat> existingDefaultFeatureStat = defaultFeatureStatRepository.findById(defaultFeatureStatDTO.getId());
        if (existingDefaultFeatureStat.isEmpty()) {
            throw new DefaultFeatureStatNotFoundException(defaultFeatureStatDTO.getId());
        }

        DefaultFeatureStat updatedDefaultFeatureStat = modelMapper.map(defaultFeatureStatDTO).build();
        return save(updatedDefaultFeatureStat);
    }

    public DefaultFeatureStatDTO get(Long defaultFeatureStatId) throws UserVisibleException {
        Optional<DefaultFeatureStat> defaultFeatureStat = defaultFeatureStatRepository.findById(defaultFeatureStatId);
        if (defaultFeatureStat.isEmpty()) {
            throw new DefaultFeatureStatNotFoundException(defaultFeatureStatId);
        }
        return modelMapper.map(defaultFeatureStat.get()).build();
    }

    public List<DefaultFeatureStatDTO> get() {
        return defaultFeatureStatRepository.findAll().stream().map((f) -> modelMapper.map(f).build()).toList();
    }

    public void delete(Long defaultFeatureStatId) throws UserVisibleException {
        Optional<DefaultFeatureStat> featureStat = defaultFeatureStatRepository.findById(defaultFeatureStatId);
        if (featureStat.isEmpty()) {
            throw new DefaultFeatureStatNotFoundException(defaultFeatureStatId);
        }

        defaultFeatureStatRepository.deleteById(defaultFeatureStatId);
    }

    /**
     * Saves a {@link FeatureStat} and returns the resulting corresponding {@link FeatureStatDTO}.
     */
    private DefaultFeatureStatDTO save(DefaultFeatureStat featureStat) {
        DefaultFeatureStat newFeatureStat = defaultFeatureStatRepository.save(featureStat);
        return modelMapper.map(newFeatureStat).build();
    }

}
