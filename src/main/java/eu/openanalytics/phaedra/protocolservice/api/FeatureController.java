package eu.openanalytics.phaedra.protocolservice.api;

import eu.openanalytics.phaedra.protocolservice.model.WellFeature;
import eu.openanalytics.phaedra.protocolservice.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class FeatureController {

    @Autowired
    private FeatureRepository featureRepository;

    @PostMapping("/features")
    public ResponseEntity createFeature(@RequestBody WellFeature newFeature) {
        WellFeature savedFeature = featureRepository.save(newFeature);
        return new ResponseEntity(savedFeature, HttpStatus.CREATED);
    }

    @PutMapping("/features")
    public ResponseEntity updateFeature(@RequestBody WellFeature updateFeature) {
        WellFeature updatedFeature = featureRepository.save(updateFeature);
        return new ResponseEntity(updatedFeature, HttpStatus.OK);
    }

    @DeleteMapping("/features/{featureId}")
    public ResponseEntity deleteFeature(@PathVariable Long featureId) {
        featureRepository.deleteById(featureId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/features/{featureId}")
    public ResponseEntity getFeatureById(@PathVariable Long featureId) {
        Optional<WellFeature> result = featureRepository.findById(featureId);
        if (result.isPresent())
            return new ResponseEntity(result.get(), HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/features")
    public ResponseEntity getFeaturesByGroupId(@RequestParam("groupId") Long groupId) {
        List<WellFeature> result = featureRepository.findAllByFeatureGroupId(groupId);
        if (result != null) {
            return new ResponseEntity(result, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
}
