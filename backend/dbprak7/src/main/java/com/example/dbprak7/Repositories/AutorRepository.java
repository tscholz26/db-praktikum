package com.example.dbprak7.Repositories;

import com.example.dbprak7.Model.Autor;
import com.example.dbprak7.Model.AutorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, AutorId> {
}