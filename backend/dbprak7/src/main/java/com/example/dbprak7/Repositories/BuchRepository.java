package com.example.dbprak7.Repositories;

import com.example.dbprak7.Model.Buch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuchRepository extends JpaRepository<Buch, String> {
}