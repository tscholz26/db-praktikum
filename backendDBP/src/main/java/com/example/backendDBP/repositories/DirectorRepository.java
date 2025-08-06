package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Director;
import com.example.backendDBP.models.DirectorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectorRepository extends JpaRepository<Director, DirectorId> {

    @Query("SELECT d.id.name FROM Director d WHERE d.pnr.pnr = :pnr")
    List<String> findDirectorNameByPnr(String pnr);
}
