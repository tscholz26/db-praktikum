package com.example.backendDBP.repositories;

import com.example.backendDBP.DTOs.KundeDTO;
import com.example.backendDBP.models.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KundeRepository extends JpaRepository<Kunde, String> {

    @Query("SELECT k FROM Kunde k WHERE k.nutzername = :nutzername")
    Kunde findKundeByNutzername(@Param("nutzername") String nutzername);

    @Query("""
            SELECT r.kunde, round(avg(r.bewertung), 2) as durchschittlichesRating
            FROM Rezension r
            GROUP BY r.kunde
            HAVING avg(r.bewertung) < :grenzwertRating
            ORDER BY 2 DESC
            """)
    List<Object> findTrollsKunden(double grenzwertRating);
}

