package com.example.dbprak7.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "angebot", schema = "public")
public class Angebot {
    @EmbeddedId
    private AngebotId id;

    @MapsId("produktnr")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr", nullable = false)
    private Produkt produktnr;

    @MapsId("filialeid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "filialeid", nullable = false)
    private Filiale filialeid;

    @Column(name = "bestand")
    private Integer bestand;

    @Column(name = "preis", precision = 10, scale = 2)
    private BigDecimal preis;

}