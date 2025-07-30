package com.example.backendDBP.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "errordata", schema = "public")
public class Errordatum {
    @Id
    @ColumnDefault("nextval('errordata_errorid_seq')")
    @Column(name = "errorid", nullable = false)
    private Integer id;

    @Column(name = "entityname", nullable = false, length = 100)
    private String entityname;

    @Column(name = "fehlerattribut", nullable = false, length = 100)
    private String fehlerattribut;

    @Column(name = "fehlermeldung", nullable = false, length = Integer.MAX_VALUE)
    private String fehlermeldung;

    @Column(name = "fehlerklasse", length = 100)
    private String fehlerklasse;

}