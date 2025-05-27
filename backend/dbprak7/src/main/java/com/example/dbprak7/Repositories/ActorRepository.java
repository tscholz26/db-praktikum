package com.example.dbprak7.Repositories;

import com.example.dbprak7.Model.Actor;
import com.example.dbprak7.Model.ActorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, ActorId> {
}