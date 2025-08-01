package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Produkt;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Transactional
@Repository
public interface ProduktRepository extends JpaRepository<Produkt, String> {

    @Query("SELECT p FROM Produkt p WHERE p.pnr = :pnr")
    Produkt findProduktByPnr(@Param("pnr") String pnr);


}
