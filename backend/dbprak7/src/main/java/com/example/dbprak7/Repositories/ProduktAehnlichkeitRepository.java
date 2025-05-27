package com.example.dbprak7.Repositories;

import com.example.dbprak7.Model.ProduktAehnlichkeit;
import com.example.dbprak7.Model.ProduktAehnlichkeitId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduktAehnlichkeitRepository extends JpaRepository<ProduktAehnlichkeit, ProduktAehnlichkeitId> {
}