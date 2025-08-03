package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface KundeRepository extends JpaRepository<Kunde, String> {

    @Query("SELECT k FROM Kunde k WHERE k.nutzername = :nutzername")
    Kunde findKundeByNutzername(@Param("nutzername") String nutzername);

}

