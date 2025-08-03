package com.example.backendDBP.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@Entity
@Table(name = "rezension", schema = "public", indexes = {
        @Index(name = "idx_rezension_bewertung", columnList = "bewertung")
})
public class Rezension {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rezension_seq")
    @SequenceGenerator(name = "rezension_seq", sequenceName = "rezension_rezensionsid_seq", allocationSize = 1)
    @Column(name = "rezensionsid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pnr")
    private Produkt produkt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "nutzername")
    private Kunde kunde;

    @Range(min = 1, max = 5)
    @Column(name = "bewertung")
    private Integer bewertung;

    @Column(name = "rezension", length = Integer.MAX_VALUE)
    private String rezension;

}