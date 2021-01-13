package eu.openanalytics.phaedra.protocolservice.api;

import eu.openanalytics.phaedra.protocolservice.model.Protocol;
import eu.openanalytics.phaedra.protocolservice.repository.ProtocolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ProtocolController {

    @Autowired
    private ProtocolRepository protocolRepository;

    @GetMapping("/protocols")
    public Optional<Object> getProtocolList() {
        List<Protocol> result = new ArrayList<>();
        protocolRepository.findAll().forEach(result::add);
        return Optional.of(result);
    }

    @PostMapping("/protocol")
    public Optional<Object> createNewProtocol(@RequestBody Protocol newProtocol) {
        Protocol savedProtocol = protocolRepository.save(newProtocol);

        Map<String, Long> result = new HashMap<>();
        result.put("protocolId", savedProtocol.getId());
        return Optional.of(result);
    }
}
