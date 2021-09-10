package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.DefaultFeatureStat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefaultFeatureStatRepository extends CrudRepository<DefaultFeatureStat, Long> {

    @Override
    List<DefaultFeatureStat> findAll();

}
