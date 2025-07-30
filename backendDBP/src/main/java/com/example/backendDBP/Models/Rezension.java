package com.example.backendDBP.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "rezension", schema = "public", indexes = {
        @Index(name = "idx_rezension_bewertung", columnList = "bewertung")
})
public class Rezension {
    @Id
    @ColumnDefault("nextval('rezension_rezensionsid_seq')")
    @Column(name = "rezensionsid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pnr", nullable = false)
    private Produkt pnr;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "nutzername", nullable = false)
    private Kunde nutzername;

    @Column(name = "bewertung")
    private Integer bewertung;

    @Column(name = "rezension", length = Integer.MAX_VALUE)
    private String rezension;

}