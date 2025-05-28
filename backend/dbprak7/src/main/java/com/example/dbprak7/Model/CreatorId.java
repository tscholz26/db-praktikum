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
@XmlType(name = "CreatorId", propOrder = {"produktnr", "name"})
public class CreatorId implements Serializable {
    private static final long serialVersionUID = 2581966523252858714L;

    @Column(name = "produktnr", nullable = false, length = 50)
    @XmlElement(name = "ProduktNr", required = true)
    private String produktnr;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    @XmlElement(name = "Name", required = true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CreatorId entity = (CreatorId) o;
        return Objects.equals(this.name, entity.name) &&
                Objects.equals(this.produktnr, entity.produktnr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, produktnr);
    }
}