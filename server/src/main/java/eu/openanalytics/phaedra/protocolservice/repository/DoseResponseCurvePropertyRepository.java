package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.DoseResponseCurveProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoseResponseCurvePropertyRepository extends CrudRepository<DoseResponseCurveProperty, Long> {

    /**
     * Retrieve all dose response curve properties for a specific feature
     * @param featureId
     * @return list of dose response curve properties
     */
    List<DoseResponseCurveProperty> findAllByFeatureId(Long featureId);
}
