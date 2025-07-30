package com.example.backendDBP.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "kauf", schema = "public")
public class Kauf {
    @Id
    @ColumnDefault("nextval('kauf_kaufid_seq')")
    @Column(name = "kaufid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "nutzername")
    private Kunde nutzername;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pnr", nullable = false)
    private Produkt pnr;

    @Column(name = "kaufzeit")
    private Instant kaufzeit;

    @Column(name = "preis", nullable = false, precision = 10, scale = 2)
    private BigDecimal preis;

    @Column(name = "warenkorbpreis", precision = 10, scale = 2)
    private BigDecimal warenkorbpreis;

    @Column(name = "menge", nullable = false)
    private Integer menge;

    @Column(name = "name")
    private String name;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "ort", length = 100)
    private String ort;

    @Column(name = "plz", length = 20)
    private String plz;

}