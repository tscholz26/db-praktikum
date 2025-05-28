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
@Table(name = "song", schema = "public")
@XmlRootElement(name = "Song") // Root-Element für XML-Darstellung
@XmlAccessorType(XmlAccessType.FIELD) // Direktes Binden der Felder
@XmlType(propOrder = {"id", "produktnr"}) // Bestimmt die Reihenfolge der XML-Elemente
public class Song {

    @EmbeddedId
    @XmlElement(name = "Id", required = true) // XML-Tag-Name für das eingebettete ID-Objekt
    private SongId id;

    @MapsId("produktnr")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr", nullable = false)
    @XmlElement(name = "ProduktNr", required = true) // XML-Tag-Name für produktnr
    private MusikCd produktnr;

}
