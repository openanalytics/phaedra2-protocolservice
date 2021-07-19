package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.Tag;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
    /**
     * Find tag object by name
     * @param tagName
     * @return
     */
    @Query("select * from protocols.tag where name = :tagName")
    Tag findByName(String tagName);
}
