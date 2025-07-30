package com.example.backendDBP.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "kunde", schema = "public")
public class Kunde {
    @Id
    @Column(name = "nutzername", nullable = false, length = 100)
    private String nutzername;

    //TODO [Reverse Engineering] generate columns from DB
}