package com.example.sss.repozitorijumi;

import com.example.sss.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AgentRepozitorijum extends JpaRepository<Agent, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO agenti (vlasnik, agent, active) VALUES (:vlasnikId, :noviId, true)", nativeQuery = true)
    int insert(@Param("noviId") int noviId, @Param("vlasnikId") int vlasnikId);

    @Query(value = "SELECT * FROM agenti WHERE agent = :id AND active = true", nativeQuery = true)
    Agent nadjivlasnika(@Param("id") int id);

    @Query(value = "SELECT * FROM agenti WHERE vlasnik = :id AND active = true", nativeQuery = true)
    List<Agent> nadjiSveAgentePodVlasnikom(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE agenti SET active = false WHERE agent = :id", nativeQuery = true)
    int obrisiOdnos(@Param("id") int id);

    @Query(value = "SELECT * FROM agenti WHERE agent = :aid AND vlasnik = :vid AND active = true", nativeQuery = true)
    Agent nadjiAgenta(@Param("aid") int aid, @Param("vid") int vid);
}
