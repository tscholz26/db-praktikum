package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Kuenstler;
import com.example.backendDBP.models.KuenstlerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KuenstlerRepository extends JpaRepository<Kuenstler, KuenstlerId> {

    @Query("SELECT k.id.name FROM Kuenstler k WHERE k.pnr.pnr = :pnr")
    List<String> findKuenstlerByPnr(String pnr);
}
