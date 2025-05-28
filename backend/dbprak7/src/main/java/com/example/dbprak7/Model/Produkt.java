package com.example.dbprak7.Model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
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
@XmlRootElement(name = "Produkt")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"pnr", "verkaufsrang", "bild", "titel"})
public class Produkt {

    @Id
    @Column(name = "pnr", nullable = false, length = 50)
    @XmlElement(name = "PNr", required = true)
    private String pnr;

    @Column(name = "verkaufsrang")
    @XmlElement(name = "Verkaufsrang")
    private Integer verkaufsrang;

    @Column(name = "bild", length = Integer.MAX_VALUE)
    @XmlElement(name = "Bild")
    private String bild;

    @Column(name = "titel")
    @XmlElement(name = "Titel", required = true)
    private String titel;

}