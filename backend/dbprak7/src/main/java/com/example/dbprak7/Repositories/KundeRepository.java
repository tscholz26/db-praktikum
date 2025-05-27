package com.example.dbprak7.Repositories;

import com.example.dbprak7.Model.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KundeRepository extends JpaRepository<Kunde, Integer> {
}