package eu.openanalytics.phaedra.protocolservice.service;

import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.model.Feature;
import eu.openanalytics.phaedra.protocolservice.model.Protocol;
import eu.openanalytics.phaedra.protocolservice.model.Tag;
import eu.openanalytics.phaedra.protocolservice.repository.FeatureRepository;
import eu.openanalytics.phaedra.protocolservice.repository.ProtocolRepository;
import eu.openanalytics.phaedra.protocolservice.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeatureService {

    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private ProtocolRepository protocolRepository;
    @Autowired
    private TagRepository tagRepository;

    /**
     * Tag a feature
     * @param tagName
     * @param featureId
     */
    public void addTagToFeature(String tagName, Long featureId) {
        Optional<Feature> feature = featureRepository.findById(featureId);
        feature.ifPresent(f -> {
            Tag tag = tagRepository.findByName(tagName);
            if (tag == null) {
                tag = tagRepository.save(new Tag(tagName));
            }
            f.addTag(tag);
            featureRepository.save(f);
        });
    }

    public Feature create(Feature newFeature) {
        return null;
    }

    /**
     * Get feature information by id
     * @param featureId
     * @return
     */
    public FeatureDTO getFeatureById(Long featureId) {
        Optional<Feature> feature = featureRepository.findById(featureId);
        FeatureDTO result = feature.map(f -> mapToFeatureDTO(f)).get();
        return result;
    }

    public List<FeatureDTO> getAllFeatures() {

        return null;
    }

    public List<FeatureDTO> getFeatureByTags(List<String> tags) {
        List<Feature> features = featureRepository.findByTagsIn(tags);

        List<FeatureDTO> result = features.stream()
                .map(f -> mapToFeatureDTO(f))
                .collect(Collectors.toList());

        return result;
     }

    private FeatureDTO mapToFeatureDTO(Feature feature) {
        FeatureDTO result = new FeatureDTO();

        result.setId(feature.getFeatureId());
        result.setName(feature.getFeatureName());
        result.setAlias(feature.getFeatureAlias());
        result.setDescription(feature.getDescription());
        result.setFormat(feature.getFormat());
        result.setType(feature.getFeatureType());

        Optional<Protocol> protocol = protocolRepository.findById(feature.getProtocolId());
        protocol.ifPresent(p -> {
            ProtocolDTO protocolDTO = new ProtocolDTO();
            protocolDTO.setName(p.getProtocolName());
            protocolDTO.setDescription(p.getDescription());
            result.setProtocol(protocolDTO);
        });

        List<String> tags = feature.getTags().stream()
                .map(ftr -> tagRepository.findById(ftr.getTagId()).get().getTagName())
                .collect(Collectors.toList());
        result.setTags(tags);

        return result;
    }

    public List<FeatureDTO> getFeaturesByQueryParams(Map<String, String> queryParams) {
        return null;
    }
}
