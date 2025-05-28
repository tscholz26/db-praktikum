package com.example.dbprak7.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlType;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD) // Direktes Binden der Felder
@XmlType(propOrder = {"produktnr", "songtitel"}) // Bestimmt die Reihenfolge der XML-Elemente
public class SongId implements Serializable {
    private static final long serialVersionUID = 8745337289664919952L;

    @XmlElement(name = "ProduktNr", required = true) // XML-Tag-Name für produktnr
    @Column(name = "produktnr", nullable = false, length = 50)
    private String produktnr;

    @XmlElement(name = "SongTitel", required = true) // XML-Tag-Name für songtitel
    @Column(name = "songtitel", nullable = false, length = Integer.MAX_VALUE)
    private String songtitel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SongId entity = (SongId) o;
        return Objects.equals(this.songtitel, entity.songtitel) &&
                Objects.equals(this.produktnr, entity.produktnr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songtitel, produktnr);
    }
}
