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
@Table(name = "musik_cd", schema = "public")
@XmlRootElement(name = "MusikCD")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"produktnr", "erscheinungsdatum"})
public class MusikCd {

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

    @Column(name = "erscheinungsdatum")
    @XmlElement(name = "Erscheinungsdatum")
    private LocalDate erscheinungsdatum;

}