package eu.openanalytics.phaedra.protocolservice.service;

import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.dto.TaggedObjectDTO;
import eu.openanalytics.phaedra.protocolservice.model.Feature;
import eu.openanalytics.phaedra.protocolservice.model.Protocol;
import eu.openanalytics.phaedra.protocolservice.repository.FeatureRepository;
import eu.openanalytics.phaedra.protocolservice.repository.ProtocolRepository;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Service
public class FeatureService {
    private static final ModelMapper modelMapper = new ModelMapper();
    private static final String PHAEDRA_METADATA_SERVICE = "http://PHAEDRA-METADATA-SERVICE/phaedra/metadata-service";
    private static final String FEATURE_OBJECT_CLASS = "FEATURE";

    private final RestTemplate restTemplate;
    private final FeatureRepository featureRepository;
    private final ProtocolRepository protocolRepository;

    public FeatureService(RestTemplate restTemplate, FeatureRepository featureRepository, ProtocolRepository protocolRepository) {
        this.restTemplate = restTemplate;
        this.featureRepository = featureRepository;
        this.protocolRepository = protocolRepository;
    }

    /**
     * Create a new feature
     * @param newFeature New feature
     */
    public Feature create(Feature newFeature) {
        return featureRepository.save(newFeature);
    }

    /**
     * Update an existing feature
     * @param featureDTO Feature updates
     */
    public FeatureDTO update(FeatureDTO featureDTO) {
        Optional<Feature> feature = featureRepository.findById(featureDTO.getId());
        feature.ifPresent(f -> {
            modelMapper.typeMap(FeatureDTO.class, Feature.class)
                    .setPropertyCondition(Conditions.isNotNull())
                    .map(featureDTO, f);
            featureRepository.save(f);
        });
        return featureDTO;
    }

    /**
     * Delete an existing feature
     * @param featureId The feature id
     */
    public void delete(Long featureId) {
        featureRepository.deleteById(featureId);
    }

    /**
     * Tag a feature
     * @param featureId The feature id
     * @param tagName A tag string
     */
    public void tagFeature(Long featureId, String tagName) {
        StringBuilder urlBuilder = new StringBuilder(PHAEDRA_METADATA_SERVICE);
        urlBuilder.append("/tags");

        TaggedObjectDTO request = new TaggedObjectDTO(FEATURE_OBJECT_CLASS);
        request.setTag(tagName);
        request.setObjectId(featureId);

        restTemplate.postForObject(urlBuilder.toString(), request, ResponseEntity.class);
    }

    /**
     * Get feature information by id
     * @param featureId The feature id
     */
    public FeatureDTO findFeatureById(Long featureId) {
        Optional<Feature> feature = featureRepository.findById(featureId);
        FeatureDTO result = feature.map(f -> mapToFeatureDTO(f)).get();
        return result;
    }

    /**
     * Get all available features
     */
    public List<FeatureDTO> findAllFeatures() {
        List<Feature> features = (List<Feature>) featureRepository.findAll();
        return features.stream()
                .map(this::mapToFeatureDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find features for a specific protocol
     * @param protocolId The protocol id
     */
    public List<FeatureDTO> findFeaturesByProtocolId(Long protocolId) {
        List<Feature> features = featureRepository.findByProtocolId(protocolId);
        return features.stream()
                .map(this::mapToFeatureDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find all features containing one of the specified tags
     * @param tag A tag string
     */
    public List<FeatureDTO> findAllFeaturesWithTag(String tag) {
        StringBuilder urlBuilder = new StringBuilder(PHAEDRA_METADATA_SERVICE);
        urlBuilder.append("/").append(FEATURE_OBJECT_CLASS);
        urlBuilder.append("?tag=").append(tag);

        ResponseEntity<List<TaggedObjectDTO>> responseEntity = restTemplate.exchange(
                urlBuilder.toString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        if (isNotEmpty(responseEntity.getBody())) {
            return responseEntity.getBody().stream().map(to -> {
                Optional<Feature> feature = featureRepository.findById(to.getObjectId());
                return feature.map(this::mapToFeatureDTO).orElse(null);
            }).collect(Collectors.toList());
        } else {
            return Collections.EMPTY_LIST;
        }
     }

    /**
     * Map Feature object to FeatureDTO object
     * @param feature The feature entity object
     * @return featureDTO object
     */
    private FeatureDTO mapToFeatureDTO(Feature feature) {
        FeatureDTO featureDTO = new FeatureDTO();
        modelMapper.typeMap(Feature.class, FeatureDTO.class)
                .map(feature, featureDTO);

        Optional<Protocol> protocol = protocolRepository.findById(feature.getProtocolId());
        protocol.ifPresent(p -> {
            ProtocolDTO protocolDTO = new ProtocolDTO();
            modelMapper.typeMap(Protocol.class, ProtocolDTO.class)
                    .map(p, protocolDTO);
            featureDTO.setProtocol(protocolDTO);
        });

        return featureDTO;
    }
}
