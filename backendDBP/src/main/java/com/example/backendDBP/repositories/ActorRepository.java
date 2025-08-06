package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Actor;
import com.example.backendDBP.models.ActorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, ActorId> {

    @Query("SELECT a.id.name FROM Actor a WHERE a.pnr.pnr = :pnr")
    List<String> findActorNameByPnr(String pnr);
}
