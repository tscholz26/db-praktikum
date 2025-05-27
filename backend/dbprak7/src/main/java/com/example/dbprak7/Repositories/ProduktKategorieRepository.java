package com.example.dbprak7.Repositories;

import com.example.dbprak7.Model.ProduktKategorie;
import com.example.dbprak7.Model.ProduktKategorieId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduktKategorieRepository extends JpaRepository<ProduktKategorie, ProduktKategorieId> {
}