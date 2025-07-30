package com.example.backendDBP.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "angebot", schema = "public", indexes = {
        @Index(name = "idx_angebot_preis", columnList = "preis")
})
public class Angebot {
    @Id
    @ColumnDefault("nextval('angebot_angebotid_seq')")
    @Column(name = "angebotid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pnr", nullable = false)
    private Produkt pnr;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "filialeid", nullable = false)
    private Filiale filialeid;

    @Column(name = "zustand", nullable = false, length = 20)
    private String zustand;

    @Column(name = "preis", nullable = false, precision = 10, scale = 2)
    private BigDecimal preis;

    @Column(name = "waehrung", nullable = false, length = 3)
    private String waehrung;

}