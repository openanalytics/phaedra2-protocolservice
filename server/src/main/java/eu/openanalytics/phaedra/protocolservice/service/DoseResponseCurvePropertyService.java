package eu.openanalytics.phaedra.protocolservice.service;

import eu.openanalytics.phaedra.protocolservice.dto.DRCModelDTO;
import eu.openanalytics.phaedra.protocolservice.model.DoseResponseCurveProperty;
import eu.openanalytics.phaedra.protocolservice.repository.DoseResponseCurvePropertyRepository;
import org.springframework.stereotype.Service;

@Service
public class DoseResponseCurvePropertyService {

    private final DoseResponseCurvePropertyRepository drcPropertyRepository;

    public DoseResponseCurvePropertyService(DoseResponseCurvePropertyRepository drcPropertyRepository) {
        this.drcPropertyRepository = drcPropertyRepository;
    }

    public void create(Long featureId, DRCModelDTO drcModel) {
        // Creete model name
        DoseResponseCurveProperty model = new DoseResponseCurveProperty();
        model.setFeatureId(featureId);
        model.setName("model");
        model.setValue(drcModel.getName());
        drcPropertyRepository.save(model);

        DoseResponseCurveProperty method = new DoseResponseCurveProperty();
        method.setFeatureId(featureId);
        method.setName("method");
        method.setValue(drcModel.getMethod());
        drcPropertyRepository.save(method);

        DoseResponseCurveProperty description = new DoseResponseCurveProperty();
        description.setFeatureId(featureId);
        description.setName("description");
        description.setValue(drcModel.getDescription());
        drcPropertyRepository.save(description);

        for (String propertyName : drcModel.getInputParameters().keySet()) {
            DoseResponseCurveProperty property = new DoseResponseCurveProperty();
            property.setFeatureId(featureId);
            property.setName(propertyName);
            property.setValue(drcModel.getInputParameters().get(propertyName));
            drcPropertyRepository.save(property);
        }
    }
}
