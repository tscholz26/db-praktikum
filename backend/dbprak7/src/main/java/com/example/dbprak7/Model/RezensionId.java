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
@XmlType(propOrder = {"kundenid", "produktnr"}) // Bestimmt die Reihenfolge der XML-Elemente
public class RezensionId implements Serializable {
    private static final long serialVersionUID = 391811804472931352L;

    @XmlElement(name = "KundenId", required = true) // XML-Tag-Name für kundenid
    @Column(name = "kundenid", nullable = false)
    private Integer kundenid;

    @XmlElement(name = "ProduktNr", required = true) // XML-Tag-Name für produktnr
    @Column(name = "produktnr", nullable = false, length = 50)
    private String produktnr;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RezensionId entity = (RezensionId) o;
        return Objects.equals(this.kundenid, entity.kundenid) &&
                Objects.equals(this.produktnr, entity.produktnr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kundenid, produktnr);
    }
}
