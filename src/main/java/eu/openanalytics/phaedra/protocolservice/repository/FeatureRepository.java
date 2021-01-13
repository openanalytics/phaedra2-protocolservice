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
    @Query("select * from feature where protocol_id = :protocolId")
    List<Feature> findAllByProtocolId(@Param("protocolId") Long protocolId);

    /**
     * Find all features that belong ta a specific feature group
     * @param featureGroupId
     * @return A feature list
     */
    @Query("select * from feature where group_id = :featureGroupId")
    List<Feature> findAllByFeatureGroupId(@Param("featureGroupId") Long featureGroupId);
}
