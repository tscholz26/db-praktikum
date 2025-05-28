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
@XmlType(name = "KuenstlerId", propOrder = {"produktnr", "kuenstlername"})
public class KuenstlerId implements Serializable {
    private static final long serialVersionUID = -6575831096224583700L;

    @Column(name = "produktnr", nullable = false, length = 50)
    @XmlElement(name = "ProduktNr", required = true)
    private String produktnr;

    @Column(name = "kuenstlername", nullable = false, length = Integer.MAX_VALUE)
    @XmlElement(name = "Kuenstlername", required = true)
    private String kuenstlername;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        KuenstlerId entity = (KuenstlerId) o;
        return Objects.equals(this.kuenstlername, entity.kuenstlername) &&
                Objects.equals(this.produktnr, entity.produktnr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kuenstlername, produktnr);
    }
}