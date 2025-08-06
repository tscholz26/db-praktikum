package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Label;
import com.example.backendDBP.models.LabelId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<Label, LabelId> {

    @Query("SELECT l.id.labelname FROM Label l WHERE l.pnr.pnr = :pnr")
    List<String> findLabelNameByPnr(String pnr);
}
