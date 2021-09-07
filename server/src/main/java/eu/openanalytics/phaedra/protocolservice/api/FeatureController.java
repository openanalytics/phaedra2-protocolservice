package eu.openanalytics.phaedra.protocolservice.api;

import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.model.Feature;
import eu.openanalytics.phaedra.protocolservice.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class FeatureController {

    private static final String TAGS = "tags";

    @Autowired
    private FeatureService featureService;

    @PostMapping("/features")
    public ResponseEntity createFeature(@RequestBody FeatureDTO newFeature) {
        Feature savedFeature = featureService.create(newFeature);
        return new ResponseEntity(savedFeature, HttpStatus.CREATED);
    }

    @PutMapping("/features")
    public ResponseEntity updateFeature(@RequestBody FeatureDTO updateFeature) {
        FeatureDTO updatedFeature = featureService.update(updateFeature);
        return new ResponseEntity(updatedFeature, HttpStatus.OK);
    }

    @DeleteMapping("/features/{featureId}")
    public ResponseEntity deleteFeature(@PathVariable Long featureId) {
        featureService.delete(featureId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/features")
    public ResponseEntity getFeatures() {
        List<FeatureDTO> response = featureService.findAllFeatures();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping(value = "/features", params = {"tag"})
    public ResponseEntity getFeaturesWithTag(@RequestParam(value = "tag", required = false) String tag) {
        List<FeatureDTO> response = featureService.findAllFeaturesWithTag(tag);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/features/{featureId}")
    public ResponseEntity getFeatureById(@PathVariable Long featureId) {
        FeatureDTO result = featureService.findFeatureById(featureId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PutMapping("/features/{featureId}/tag")
    public ResponseEntity addTagToFeature(@PathVariable ("featureId") Long featureId, @RequestParam("tag") String tag) {
        featureService.tagFeature(featureId, tag);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
