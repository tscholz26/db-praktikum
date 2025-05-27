package com.example.dbprak7.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "errordata", schema = "public")
public class Errordatum {
    @Id
    @Column(name = "pnr", nullable = false, length = 50)
    private String pnr;

    @Column(name = "fehlermeldung", nullable = false, length = Integer.MAX_VALUE)
    private String fehlermeldung;

}