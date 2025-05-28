package com.example.dbprak7.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Check;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@Getter
@Setter
@Check(constraints = "bewertung >= 1 AND bewertung <= 5")
@Entity
@Table(name = "rezension", schema = "public",
        indexes = {
                @Index(name = "idx_rezension_bewertung", columnList = "bewertung")
        }
)
@XmlRootElement(name = "Rezension") // Root-Element für XML-Darstellung
@XmlAccessorType(XmlAccessType.FIELD) // Direktes Binden der Felder
@XmlType(propOrder = {"id", "kundenid", "produktnr", "bewertung", "rezension"}) // Bestimmt die Reihenfolge der XML-Elemente
public class Rezension {

    @EmbeddedId
    @XmlElement(name = "Id", required = true) // XML-Tag-Name für das eingebettete ID-Objekt
    private RezensionId id;

    @MapsId("kundenid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "kundenid", nullable = false)
    @XmlElement(name = "KundenId", required = true) // XML-Tag-Name für kundenid
    private Kunde kundenid;

    @MapsId("produktnr")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr", nullable = false)
    @XmlElement(name = "ProduktNr", required = true) // XML-Tag-Name für produktnr
    private Produkt produktnr;

    @Column(name = "bewertung")
    @XmlElement(name = "Bewertung", required = true) // XML-Tag-Name für bewertung
    private Integer bewertung;

    @Column(name = "rezension", length = Integer.MAX_VALUE)
    @XmlElement(name = "RezensionText") // XML-Tag-Name für rezension
    private String rezension;

}
