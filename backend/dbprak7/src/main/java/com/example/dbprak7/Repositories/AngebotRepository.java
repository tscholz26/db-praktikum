package com.example.dbprak7.Repositories;

import com.example.dbprak7.Model.Angebot;
import com.example.dbprak7.Model.AngebotId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AngebotRepository extends JpaRepository<Angebot, AngebotId> {
}