package com.example.backendDBP.repositories;

import com.example.backendDBP.DTOs.AngebotDTO;
import com.example.backendDBP.models.Angebot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AngebotRepository extends JpaRepository<Angebot, Integer> {

    @Query("SELECT a FROM Angebot a WHERE a.produkt.pnr = :pnr")
    List<Angebot> findOffersByPnr(String pnr);
}
