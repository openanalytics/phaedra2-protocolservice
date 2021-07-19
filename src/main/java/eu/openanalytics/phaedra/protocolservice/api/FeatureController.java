package eu.openanalytics.phaedra.protocolservice.api;

import com.mashape.unirest.http.utils.MapUtil;
import eu.openanalytics.phaedra.protocolservice.dto.FeatureDTO;
import eu.openanalytics.phaedra.protocolservice.model.Feature;
import eu.openanalytics.phaedra.protocolservice.repository.FeatureRepository;
import eu.openanalytics.phaedra.protocolservice.service.FeatureService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class FeatureController {

    private static final String TAGS = "tags";

    @Autowired
    private FeatureService featureService;

    @PostMapping("/features")
    public ResponseEntity createFeature(@RequestBody Feature newFeature) {
        Feature savedFeature = featureService.create(newFeature);
        return new ResponseEntity(savedFeature, HttpStatus.CREATED);
    }

    @PutMapping("/features")
    public ResponseEntity updateFeature(@RequestBody Feature updateFeature) {
        Feature updatedFeature = featureService.update(updateFeature);
        return new ResponseEntity(updatedFeature, HttpStatus.OK);
    }

    @DeleteMapping("/features/{featureId}")
    public ResponseEntity deleteFeature(@PathVariable Long featureId) {
        featureService.delete(featureId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/features/{featureId}")
    public ResponseEntity getFeatureById(@PathVariable Long featureId) {
        FeatureDTO result = featureService.getFeatureById(featureId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/features")
    public ResponseEntity getFeatures(@RequestParam Map<String, String> queryParams) {
        List<FeatureDTO> result = new ArrayList<>();
        if (MapUtils.isEmpty(queryParams)) {
            result.addAll(featureService.getAllFeatures());
        } else {
            result.addAll(featureService.getFeaturesByQueryParams(queryParams));
        }

        if (CollectionUtils.isEmpty(result)) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(result, HttpStatus.OK);
        }
    }

    @PostMapping("/features/{featureId}/tag")
    public ResponseEntity addTagToFeature(@PathVariable ("featureId") Long featureId, @Valid @RequestBody List<String> tags) {
        for (String tag: tags) {
            featureService.addTagToFeature(tag, featureId);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
