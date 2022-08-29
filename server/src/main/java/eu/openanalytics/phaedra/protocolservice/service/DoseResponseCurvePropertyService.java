package eu.openanalytics.phaedra.protocolservice.service;

import eu.openanalytics.phaedra.protocolservice.dto.DRCModelDTO;
import eu.openanalytics.phaedra.protocolservice.model.DoseResponseCurveProperty;
import eu.openanalytics.phaedra.protocolservice.repository.DoseResponseCurvePropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoseResponseCurvePropertyService {

    private final DoseResponseCurvePropertyRepository drcPropertyRepository;

    public DoseResponseCurvePropertyService(DoseResponseCurvePropertyRepository drcPropertyRepository) {
        this.drcPropertyRepository = drcPropertyRepository;
    }

    public void save(Long featureId, DRCModelDTO drcModel) {
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

        DoseResponseCurveProperty slope = new DoseResponseCurveProperty();
        slope.setFeatureId(featureId);
        slope.setName("slope");
        slope.setValue(drcModel.getSlope());
        drcPropertyRepository.save(slope);

        for (String propertyName : drcModel.getInputParameters().keySet()) {
            DoseResponseCurveProperty property = new DoseResponseCurveProperty();
            property.setFeatureId(featureId);
            property.setName(propertyName);
            property.setValue(drcModel.getInputParameters().get(propertyName));
            drcPropertyRepository.save(property);
        }
    }

    public DRCModelDTO getByFeatureId(Long featureId) {
        DRCModelDTO drcModelDTO = new DRCModelDTO();
        drcModelDTO.setFeatureId(featureId);

        List<DoseResponseCurveProperty> drcProperties = drcPropertyRepository.findAllByFeatureId(featureId);
        for (DoseResponseCurveProperty drcProp: drcProperties) {
            if (drcProp.getName().equalsIgnoreCase("model")) drcModelDTO.setName(drcProp.getValue());
            else if (drcProp.getName().equalsIgnoreCase("description")) drcModelDTO.setDescription(drcProp.getValue());
            else if (drcProp.getName().equalsIgnoreCase("method")) drcModelDTO.setMethod(drcProp.getValue());
            else if (drcProp.getName().equalsIgnoreCase("slope")) drcModelDTO.setSlope(drcProp.getValue());
            else drcModelDTO.getInputParameters().put(drcProp.getName(), drcProp.getValue());
        }

        return drcModelDTO;
    }
}
