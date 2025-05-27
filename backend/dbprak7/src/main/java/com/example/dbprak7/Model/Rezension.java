package com.example.dbprak7.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "rezension", schema = "public")
public class Rezension {
    @EmbeddedId
    private RezensionId id;

    @MapsId("kundenid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "kundenid", nullable = false)
    private Kunde kundenid;

    @MapsId("produktnr")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr", nullable = false)
    private Produkt produktnr;

    @Column(name = "bewertung")
    private Integer bewertung;

    @Column(name = "rezension", length = Integer.MAX_VALUE)
    private String rezension;

}