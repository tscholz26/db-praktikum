package com.example.dbprak7.Model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "angebot", schema = "public",
        indexes = {@Index(name = "idx_angebot_preis", columnList = "preis")}
)
@XmlRootElement(name = "Angebot")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "bestand", "preis"})
public class Angebot {

    @EmbeddedId
    @XmlElement(name = "Id", required = true)
    private AngebotId id;

    @MapsId("produktnr")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr", nullable = false)
    @XmlTransient
    private Produkt produktnr;

    @MapsId("filialeid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "filialeid", nullable = false)
    @XmlTransient
    private Filiale filialeid;

    @Column(name = "bestand")
    @XmlElement(name = "Bestand")
    private Integer bestand;

    @Column(name = "preis", precision = 10, scale = 2)
    @XmlElement(name = "Preis")
    private BigDecimal preis;

}
