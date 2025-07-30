package com.example.backendDBP.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "filiale", schema = "public", indexes = {
        @Index(name = "idx_filiale_name", columnList = "name")
})
public class Filiale {
    @Id
    @ColumnDefault("nextval('filiale_filialeid_seq')")
    @Column(name = "filialeid", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "plz", length = 20)
    private String plz;

}