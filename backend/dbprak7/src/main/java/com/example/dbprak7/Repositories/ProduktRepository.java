package com.example.dbprak7.Repositories;

import com.example.dbprak7.Model.Produkt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduktRepository extends JpaRepository<Produkt, String> {
}