package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Autor;
import com.example.backendDBP.models.AutorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, AutorId> {

    @Query("SELECT a.id.name FROM Autor a WHERE a.pnr.pnr = :pnr")
    List<String> findAutorNameByPnr(String pnr);
}
