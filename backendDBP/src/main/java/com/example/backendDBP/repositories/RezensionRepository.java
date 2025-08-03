package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Rezension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RezensionRepository extends JpaRepository<Rezension, Integer> {

    @Query("SELECT r FROM Rezension r WHERE r.produkt.pnr = :pnr")
    List<Rezension> findAllByPnr(@Param("pnr") String pnr);
}
