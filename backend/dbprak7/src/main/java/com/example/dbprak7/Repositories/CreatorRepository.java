package com.example.dbprak7.Repositories;

import com.example.dbprak7.Model.Creator;
import com.example.dbprak7.Model.CreatorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorRepository extends JpaRepository<Creator, CreatorId> {
}