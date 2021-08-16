package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.Feature;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureRepository extends CrudRepository<Feature, Long> {

    /**
     * Find all features that belong to a specific protocol
     * @param protocolId
     * @return A feature list
     */
    List<Feature> findByProtocolId(Long protocolId);

    /**
     * Find features with name starting with prefix
     * @param featureNamePrefix
     * @return
     */
    List<Feature> findByNameStartsWith(String featureNamePrefix);
}
