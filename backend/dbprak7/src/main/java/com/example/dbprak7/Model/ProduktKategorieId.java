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
@XmlType(propOrder = {"produktnr", "kategorieid"}) // Bestimmt die Reihenfolge der XML-Elemente
public class ProduktKategorieId implements Serializable {
    private static final long serialVersionUID = -4304902752239884577L;

    @XmlElement(name = "ProduktNr", required = true) // XML-Tag-Name für produktnr
    @Column(name = "produktnr", nullable = false, length = 50)
    private String produktnr;

    @XmlElement(name = "KategorieId", required = true) // XML-Tag-Name für kategorieid
    @Column(name = "kategorieid", nullable = false)
    private Integer kategorieid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProduktKategorieId entity = (ProduktKategorieId) o;
        return Objects.equals(this.produktnr, entity.produktnr) &&
                Objects.equals(this.kategorieid, entity.kategorieid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produktnr, kategorieid);
    }
}
