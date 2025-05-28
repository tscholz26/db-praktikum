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
@XmlType(name = "LabelId", propOrder = {"produktnr", "labelname"})
public class LabelId implements Serializable {
    private static final long serialVersionUID = 8745337289664919952L;

    @Column(name = "produktnr", nullable = false, length = 50)
    @XmlElement(name = "ProduktNr", required = true)
    private String produktnr;

    @Column(name = "labelname", nullable = false, length = Integer.MAX_VALUE)
    @XmlElement(name = "Labelname", required = true)
    private String labelname;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LabelId entity = (LabelId) o;
        return Objects.equals(this.labelname, entity.labelname) &&
                Objects.equals(this.produktnr, entity.produktnr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labelname, produktnr);
    }
}
