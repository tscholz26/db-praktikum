package com.example.dbprak7.Repositories;

import com.example.dbprak7.Model.Kuenstler;
import com.example.dbprak7.Model.KuenstlerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KuenstlerRepository extends JpaRepository<Kuenstler, KuenstlerId> {
}