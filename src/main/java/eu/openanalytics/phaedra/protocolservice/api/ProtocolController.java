package eu.openanalytics.phaedra.protocolservice.api;

import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.dto.ProtocolDTO;
import eu.openanalytics.phaedra.protocolservice.service.FeatureService;
import eu.openanalytics.phaedra.protocolservice.service.ProtocolService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections4.CollectionUtils.*;

@CrossOrigin
@RestController
@Slf4j
public class ProtocolController {

    private final ProtocolService protocolService;
    private final FeatureService featureService;

    public ProtocolController(ProtocolService protocolService, FeatureService featureService) {
        this.protocolService = protocolService;
        this.featureService = featureService;
    }

    @PostMapping("/protocols")
    public ResponseEntity<Map<String, Long>> createProtocol(@RequestBody ProtocolDTO newProtocol) {
        ProtocolDTO savedProtocol = protocolService.create(newProtocol);

        Map<String, Long> responseBody = new HashMap<>();
        responseBody.put("protocolId", savedProtocol.getId());

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @PutMapping("/protocols")
    public ResponseEntity<ProtocolDTO> updateProtocol(@RequestBody ProtocolDTO updateProtocol) {
        ProtocolDTO updatedProtocol = protocolService.update(updateProtocol);
        return new ResponseEntity<>(updatedProtocol, HttpStatus.OK);
    }

    @PutMapping("/protocols/{protocolId}/tag")
    public ResponseEntity<String> tagProtocol(@PathVariable Long protocolId, @RequestParam("tag") String tag) {
        protocolService.tagProtocol(protocolId, tag);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/protocols/{protocolId}")
    public ResponseEntity<String> deleteProtocol(@PathVariable Long protocolId) {
        protocolService.delete(protocolId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/protocols")
    public ResponseEntity<List<ProtocolDTO>> getProtocols() {
        log.info("Get all protocols");
        List<ProtocolDTO> response = protocolService.getProtocols();
        if (isNotEmpty(response))
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/protocols", params = {"tag"})
    public ResponseEntity<List<ProtocolDTO>> getProtocolByTag(@RequestParam(value = "tag", required = false) String tag) {
        List<ProtocolDTO> response = protocolService.getProtocolByTag(tag);
        if (isNotEmpty(response))
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/protocols/{protocolId}")
    public ResponseEntity<ProtocolDTO> getProtocol(@PathVariable Long protocolId) {
        ProtocolDTO response = protocolService.getProtocolById(protocolId);
        if (response != null)
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/protocols/{protocolId}/features")
    public ResponseEntity<List<FeatureDTO>> getProtocolFeatures(@PathVariable Long protocolId) {
        List<FeatureDTO> response = featureService.findFeaturesByProtocolId(protocolId);
        if (isNotEmpty(response))
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
