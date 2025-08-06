package com.example.backendDBP.repositories;

import com.example.backendDBP.models.BuchVerlag;
import com.example.backendDBP.models.BuchVerlagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuchVerlagRepository extends JpaRepository<BuchVerlag, BuchVerlagId> {

    @Query("SELECT bv.id.verlag FROM BuchVerlag bv WHERE bv.pnr.pnr = :pnr")
    List<String> findVerlagByPnr(String pnr);
}

