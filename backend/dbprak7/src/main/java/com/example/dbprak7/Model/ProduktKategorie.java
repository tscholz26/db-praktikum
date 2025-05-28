package com.example.dbprak7.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@Getter
@Setter
@Entity
@Table(name = "produkt_kategorie", schema = "public")
@XmlRootElement(name = "ProduktKategorie") // Root-Element für XML-Darstellung
@XmlAccessorType(XmlAccessType.FIELD) // Direktes Binden der Felder
@XmlType(propOrder = {"id", "produktnr", "kategorieid"}) // Bestimmt die Reihenfolge der XML-Elemente
public class ProduktKategorie {

    @EmbeddedId
    @XmlElement(name = "Id", required = true) // Definiert das XML-Element für das eingebettete ID-Objekt
    private ProduktKategorieId id;

    @MapsId("produktnr")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr", nullable = false)
    @XmlElement(name = "ProduktNr", required = true) // Definiert das XML-Element für das Produkt
    private Produkt produktnr;

    @MapsId("kategorieid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "kategorieid", nullable = false)
    @XmlElement(name = "KategorieId", required = true) // Definiert das XML-Element für die Kategorie
    private Kategorie kategorieid;

}
