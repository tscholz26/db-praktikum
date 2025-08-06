package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Buch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BuchRepository extends JpaRepository<Buch, String> {

    @Query("SELECT b FROM Buch b WHERE b.produkt.pnr = :pnr")
    Buch findBuchByPnr(@Param("pnr") String pnr);
}
