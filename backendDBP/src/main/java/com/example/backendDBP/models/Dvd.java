package com.example.backendDBP.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "dvd", schema = "public", indexes = {
        @Index(name = "idx_dvd_laufzeit", columnList = "laufzeit")
})
public class Dvd {
    @Id
    @Column(name = "pnr", nullable = false, length = 50)
    private String pnr;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pnr", nullable = false)
    private Produkt produkt;

    @Column(name = "regioncode")
    private Integer regioncode;

    @ColumnDefault("'not specified'")
    @Column(name = "format", length = 100)
    private String format;

    @Column(name = "laufzeit")
    private Integer laufzeit;

}