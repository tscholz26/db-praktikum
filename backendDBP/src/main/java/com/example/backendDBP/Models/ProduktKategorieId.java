package com.example.backendDBP.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ProduktKategorieId implements Serializable {
    private static final long serialVersionUID = -8924117868892316359L;
    @Column(name = "pnr", nullable = false, length = 50)
    private String pnr;

    @Column(name = "kategorieid", nullable = false)
    private Integer kategorieid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProduktKategorieId entity = (ProduktKategorieId) o;
        return Objects.equals(this.pnr, entity.pnr) &&
                Objects.equals(this.kategorieid, entity.kategorieid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pnr, kategorieid);
    }

}