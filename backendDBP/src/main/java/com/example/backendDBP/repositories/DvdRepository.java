package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Dvd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DvdRepository extends JpaRepository<Dvd, String> {

    @Query("SELECT d FROM Dvd d WHERE d.produkt.pnr = :pnr")
    Dvd findDvdByPnr(String pnr);
}
