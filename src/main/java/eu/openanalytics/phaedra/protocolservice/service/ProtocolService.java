package eu.openanalytics.phaedra.protocolservice.service;

import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.dto.TaggedObjectDTO;
import eu.openanalytics.phaedra.protocolservice.model.Protocol;
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

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
public class ProtocolService {
    private static final ModelMapper modelMapper = new ModelMapper();
    private static final String PHAEDRA_METADATA_SERVICE = "http://phaedra-metadata-service/phaedra/metadata-service";
    private static final String PROTOCOL_OBJECT_CLASS = "PROTOCOL";

    private final RestTemplate restTemplate;

    private final ProtocolRepository protocolRepository;

    public ProtocolService(RestTemplate restTemplate, ProtocolRepository protocolRepository) {
        this.restTemplate = restTemplate;
        this.protocolRepository = protocolRepository;
    }

    /**
     * Create a new Protocol
     * @param protocolDTO New protocol
     */
    public ProtocolDTO create(ProtocolDTO protocolDTO) {
        Protocol newProtocol = new Protocol();
        modelMapper.typeMap(ProtocolDTO.class, Protocol.class)
                .map(protocolDTO, newProtocol);
        protocolRepository.save(newProtocol);
        return mapToProtocolDTO(newProtocol);
    }

    /**
     * Update an existing protocol
     * @param protocolDTO Protocol to be updated
     */
    public ProtocolDTO update(ProtocolDTO protocolDTO) {
        Optional<Protocol> protocol = protocolRepository.findById(protocolDTO.getId());
        protocol.ifPresent(p -> {
            modelMapper.typeMap(ProtocolDTO.class, Protocol.class)
                    .setPropertyCondition(Conditions.isNotNull())
                    .map(protocolDTO, p);
            protocolRepository.save(p);
        });
        return protocolDTO;
    }

    /**
     * Delete an existing protocol
     * @param protocolId Protocol to be deleted
     */
    public void delete(Long protocolId) {
        protocolRepository.deleteById(protocolId);
    }

    /**
     * Get a protocol for a given id
     * @param protocolId A protocol id
     */
    public ProtocolDTO getProtocolById(Long protocolId) {
        Optional<Protocol> protocol = protocolRepository.findById(protocolId);
        return protocol.map(this::mapToProtocolDTO).orElse(null);
    }

    /**
     * Add a tag to a protocol
     * @param protocolId Protocol to be tagged
     * @param tag A protocol tag
     */
    public void tagProtocol(Long protocolId, String tag) {
        StringBuilder urlBuilder = new StringBuilder(PHAEDRA_METADATA_SERVICE);
        urlBuilder.append("/tags");

        TaggedObjectDTO taggedObjectDTO = new TaggedObjectDTO(PROTOCOL_OBJECT_CLASS);
        taggedObjectDTO.setObjectId(protocolId);
        taggedObjectDTO.setTag(tag);

        restTemplate.postForObject(urlBuilder.toString(), taggedObjectDTO, ResponseEntity.class);
    }

    /**
     * Get a all available protocols
     */
    public List<ProtocolDTO> getProtocols() {
        List<Protocol> protocols = (List<Protocol>) protocolRepository.findAll();
        if (isNotEmpty(protocols)) {
            return protocols.stream()
                    .map(this::mapToProtocolDTO)
                    .collect(Collectors.toList());
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * Find all protocols containing given tag
     * @param tag
     */
    public List<ProtocolDTO> getProtocolByTag(String tag) {
        StringBuilder urlBuilder = new StringBuilder(PHAEDRA_METADATA_SERVICE);
        urlBuilder.append("/tagged_objects");
        urlBuilder.append("/").append(PROTOCOL_OBJECT_CLASS);
        urlBuilder.append("?tag=").append(tag);

        ResponseEntity<List<TaggedObjectDTO>> responseEntity = restTemplate.exchange(
                urlBuilder.toString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TaggedObjectDTO>>() {});

        List<ProtocolDTO> result = responseEntity.getBody().stream().map(to -> {
            Optional<Protocol> protocol = protocolRepository.findById(to.getObjectId());
            return protocol.map(this::mapToProtocolDTO).orElse(null);
        }).collect(Collectors.toList()).stream()
                .filter(p -> p != null)
                .collect(Collectors.toList());

        return result;
    }

    /**
     * Helper method to map a Protocol object to a ProtocolDTO object
     * @param protocol
     */
    private ProtocolDTO mapToProtocolDTO(Protocol protocol) {
        ProtocolDTO protocolDTO = new ProtocolDTO();
        modelMapper.typeMap(Protocol.class, ProtocolDTO.class).map(protocol, protocolDTO);
        return protocolDTO;
    }
}
