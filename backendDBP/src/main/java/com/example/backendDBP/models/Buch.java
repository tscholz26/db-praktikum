package com.example.backendDBP.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "buch", schema = "public")
public class Buch {
    @Id
    @Column(name = "pnr", nullable = false, length = 50)
    private String pnr;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pnr", nullable = false)
    private Produkt produkt;

    @Column(name = "isbn", nullable = false, length = 20)
    private String isbn;

    @Column(name = "seitenzahl")
    private Integer seitenzahl;

    @Column(name = "erscheinungsdatum")
    private LocalDate erscheinungsdatum;

    @Column(name = "auflage", length = 50)
    private String auflage;

}