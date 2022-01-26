package eu.openanalytics.phaedra.protocolservice.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.service.FeatureService;

@RestController
public class FeatureController {

    private static final String TAGS = "tags";

    @Autowired
    private FeatureService featureService;

    // TODO creating feature with non-existing protocolId returns 500
    // -> use rest URL? e.g. /protocol/10/feature ?
    // TODO it is possible to create features with the same name
    @PostMapping("/features")
    public ResponseEntity<FeatureDTO> createFeature(@RequestBody FeatureDTO newFeature) {
        FeatureDTO savedFeature = featureService.create(newFeature);
        return new ResponseEntity<>(savedFeature, HttpStatus.CREATED);
    }

    // TODO use rest URL: PUT /features/1/ instead of PUT /features ? cfr .delete
    // TODO validate the feature exists (updating non-existent feature does nothing and returns 200)
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
        if(result != null)
            return new ResponseEntity(result, HttpStatus.OK);
        else return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/features/{featureId}/tag")
    public ResponseEntity addTagToFeature(@PathVariable ("featureId") Long featureId, @RequestParam("tag") String tag) {
        featureService.tagFeature(featureId, tag);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
