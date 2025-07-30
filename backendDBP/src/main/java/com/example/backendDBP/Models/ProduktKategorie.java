package com.example.backendDBP.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "produkt_kategorie", schema = "public", indexes = {
        @Index(name = "idx_produkt_kategorie_pnr", columnList = "pnr"),
        @Index(name = "idx_produkt_kategorie_kategorieid", columnList = "kategorieid")
})
public class ProduktKategorie {
    @EmbeddedId
    private ProduktKategorieId id;

    @MapsId("pnr")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pnr", nullable = false)
    private Produkt pnr;

    @MapsId("kategorieid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "kategorieid", nullable = false)
    private Kategorie kategorieid;

}