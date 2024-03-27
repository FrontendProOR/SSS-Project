package com.example.sss.repozitorijumi;

import com.example.sss.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AgentRepozitorijum extends JpaRepository<Agent, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO agenti (vlasnik, agent) VALUES (:vlasnikId, :noviId)", nativeQuery = true)
    int insert(@Param("noviId") int noviId, @Param("vlasnikId") int vlasnikId);
}
