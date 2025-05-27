package com.example.dbprak7.Repositories;

import com.example.dbprak7.Model.Director;
import com.example.dbprak7.Model.DirectorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director, DirectorId> {
}