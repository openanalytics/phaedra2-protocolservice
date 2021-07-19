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
     * Find all features with the same tag(s)
     * @param tags
     * @return
     */
    @Query("select f.* from protocols.feature as f where f.id in (select ft.feature_id from protocols.feature_tag as ft join protocols.tag t on t.id = ft.tag_id where t.name in (:tags))")
    List<Feature> findByTagsIn(@Param("tags") List<String> tags);

    /**
     * Find features with name starting with prefix
     * @param featureNamePrefix
     * @return
     */
    List<Feature> findByFeatureNameStartsWith(String featureNamePrefix);
}
