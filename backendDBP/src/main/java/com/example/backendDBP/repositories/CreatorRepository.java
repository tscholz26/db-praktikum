package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Creator;
import com.example.backendDBP.models.CreatorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, CreatorId> {

    @Query("SELECT c.id.name FROM Creator c WHERE c.pnr.pnr = :pnr")
    List<String> findCreatorNameByPnr(String pnr);
}
