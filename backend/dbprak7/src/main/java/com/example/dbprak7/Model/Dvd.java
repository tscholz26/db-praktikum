package com.example.dbprak7.Model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "dvd", schema = "public",
        indexes = {@Index(name = "idx_dvd_laufzeit", columnList = "laufzeit")}
)
@XmlRootElement(name = "DVD")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"produktnr", "regioncode", "format", "laufzeit"})
public class Dvd {

    @Id
    @Column(name = "produktnr", nullable = false, length = 50)
    @XmlElement(name = "ProduktNr", required = true)
    private String produktnr;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr", nullable = false)
    @XmlTransient
    private Produkt produkt;

    @Column(name = "regioncode")
    @XmlElement(name = "Regioncode")
    private Integer regioncode;

    @Column(name = "format", length = 100)
    @XmlElement(name = "Format")
    private String format;

    @Column(name = "laufzeit")
    @XmlElement(name = "Laufzeit")
    private Integer laufzeit;

}
