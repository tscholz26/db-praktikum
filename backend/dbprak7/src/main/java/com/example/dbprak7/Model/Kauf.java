package com.example.dbprak7.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "kundenid", nullable = false)
    private Kunde kundenid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr", nullable = false)
    private Produkt produktnr;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "kaufzeit", nullable = false)
    private Instant kaufzeit;

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