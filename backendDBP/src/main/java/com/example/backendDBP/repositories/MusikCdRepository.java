package com.example.backendDBP.repositories;

import com.example.backendDBP.models.MusikCd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MusikCdRepository extends JpaRepository<MusikCd, String> {

    @Query("SELECT m FROM MusikCd m WHERE m.produkt.pnr = :pnr")
    MusikCd findMusikCdByPnr(@Param("pnr") String pnr);
}
