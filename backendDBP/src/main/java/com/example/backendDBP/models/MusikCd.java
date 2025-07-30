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
@Table(name = "musik_cd", schema = "public")
public class MusikCd {
    @Id
    @Column(name = "pnr", nullable = false, length = 50)
    private String pnr;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pnr", nullable = false)
    private Produkt produkt;

    @Column(name = "erscheinungsdatum")
    private LocalDate erscheinungsdatum;

}