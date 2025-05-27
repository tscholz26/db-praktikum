package com.example.dbprak7.Repositories;

import com.example.dbprak7.Model.Rezension;
import com.example.dbprak7.Model.RezensionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RezensionRepository extends JpaRepository<Rezension, RezensionId> {
}