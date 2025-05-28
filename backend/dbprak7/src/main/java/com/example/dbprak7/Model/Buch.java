package com.example.dbprak7.Model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "buch", schema = "public",
        indexes = {@Index(name = "idx_buch_verlag", columnList = "verlag")}
)
@XmlRootElement(name = "Buch")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"produktnr", "isbn", "seitenzahl", "verlag", "erscheinungsdatum", "auflage"})
public class Buch {
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

    @Column(name = "isbn", nullable = false, length = 20)
    @XmlElement(name = "ISBN", required = true)
    private String isbn;

    @Column(name = "seitenzahl")
    @XmlElement(name = "Seitenzahl")
    private Integer seitenzahl;

    @Column(name = "verlag")
    @XmlElement(name = "Verlag")
    private String verlag;

    @Column(name = "erscheinungsdatum")
    @XmlElement(name = "Erscheinungsdatum")
    private LocalDate erscheinungsdatum;

    @Column(name = "auflage")
    @XmlElement(name = "Auflage")
    private Integer auflage;

}
