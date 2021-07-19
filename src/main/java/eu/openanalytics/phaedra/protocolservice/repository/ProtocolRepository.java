package eu.openanalytics.phaedra.protocolservice.repository;

import eu.openanalytics.phaedra.protocolservice.model.Protocol;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProtocolRepository extends CrudRepository<Protocol, Long> {

    /**
     * Get all defined protocols
     * @return
     */
    @Query("select * from protocols.protocol")
    public List<Protocol> getProtocolList();

    /**
     * Get a protocol by protocolId
     * @param protocolId
     * @return
     */
    @Query("select * from protocols.protocol where id = :protocolId")
    public Protocol getProtocolById(@Param("protocolId") Long protocolId);
}
