package com.example.dbprak7.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "produkt", schema = "public",
        indexes = {
                @Index(name = "idx_produkt_titel", columnList = "titel"),
        }
        )
public class Produkt {
    @Id
    @Column(name = "pnr", nullable = false, length = 50)
    private String pnr;

    @Column(name = "verkaufsrang")
    private Integer verkaufsrang;

    @Column(name = "bild", length = Integer.MAX_VALUE)
    private String bild;

    @Column(name = "titel")
    private String titel;

}