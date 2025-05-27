package com.example.dbprak7.Model;

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
@Table(name = "filiale", schema = "public")
public class Filiale {
    @Id
    @ColumnDefault("nextval('filiale_filialeid_seq')")
    @Column(name = "filialeid", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "ort", length = 100)
    private String ort;

    @Column(name = "plz", length = 20)
    private String plz;

}