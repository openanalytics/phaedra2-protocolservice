package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.CalculationInputValue;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculationInputValueRepository extends CrudRepository<CalculationInputValue, Long> {

    List<CalculationInputValue> findByFeatureId(Long featureId);

    @Query("SELECT civ.* FROM calculation_input_value as civ" +
            " JOIN feature as feat ON feat.id = civ.feature_id" +
            " WHERE feat.protocol_id = :protocolId ")
    List<CalculationInputValue> findByProtocolId(@Param("protocolId") Long protocolId);
}
