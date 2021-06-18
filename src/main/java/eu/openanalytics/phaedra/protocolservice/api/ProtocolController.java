package eu.openanalytics.phaedra.protocolservice.api;

import eu.openanalytics.phaedra.protocolservice.model.WellFeature;
import eu.openanalytics.phaedra.protocolservice.model.Protocol;
import eu.openanalytics.phaedra.protocolservice.repository.FeatureRepository;
import eu.openanalytics.phaedra.protocolservice.repository.ProtocolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ProtocolController {

    @Autowired
    private ProtocolRepository protocolRepository;

    @Autowired
    private FeatureRepository featureRepository;

    @GetMapping("/protocols")
    public ResponseEntity getProtocolList() {
        List<Protocol> result = new ArrayList<>();
        protocolRepository.getProtocolList().forEach(result::add);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/protocols")
    public ResponseEntity createProtocol(@RequestBody Protocol newProtocol) {
        Protocol savedProtocol = protocolRepository.save(newProtocol);

        Map<String, Long> responseBody = new HashMap<>();
        responseBody.put("protocolId", savedProtocol.getProtocolId());

        ResponseEntity<Map<String, Long>> response = new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        return response;
    }

    @PutMapping("/protocols")
    public ResponseEntity updateProtocol(@RequestBody Protocol updateProtocol) {
        Protocol updatedProtocol = protocolRepository.save(updateProtocol);

        if (updatedProtocol != null)
            return new ResponseEntity(updatedProtocol, HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/protocols/{protocolId}")
    public ResponseEntity deleteProtocol(@PathVariable Long protocolId) {
        Optional<Protocol> result = protocolRepository.findById(protocolId);

        if (result.isPresent()) {
            Protocol protocol = result.get();
            protocolRepository.delete(protocol);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/protocols/{protocolId}")
    public ResponseEntity getProtocol(@PathVariable Long protocolId) {
        Protocol result = protocolRepository.getProtocolById(protocolId);
        if (result != null)
            return new ResponseEntity(result, HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/protocols/{protocolId}/features")
    public ResponseEntity getFeaturesByProtocol(@PathVariable Long protocolId) {
        List<WellFeature> result = new ArrayList<>();
        featureRepository.findAllByProtocolId(protocolId).forEach(result::add);
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
