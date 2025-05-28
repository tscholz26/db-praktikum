package com.example.dbprak7.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AngebotId", propOrder = {"produktnr", "filialeid"})
public class AngebotId implements Serializable {
    private static final long serialVersionUID = -1251393006985053426L;

    @Column(name = "produktnr", nullable = false, length = 50)
    @XmlElement(name = "ProduktNr", required = true)
    private String produktnr;

    @Column(name = "filialeid", nullable = false)
    @XmlElement(name = "FilialeId", required = true)
    private Integer filialeid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AngebotId entity = (AngebotId) o;
        return Objects.equals(this.filialeid, entity.filialeid) &&
                Objects.equals(this.produktnr, entity.produktnr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filialeid, produktnr);
    }
}