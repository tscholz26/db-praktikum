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
public class ProduktAehnlichkeitId implements Serializable {
    private static final long serialVersionUID = 5395857252831741600L;
    @Column(name = "pnr1", nullable = false, length = 50)
    private String pnr1;

    @Column(name = "pnr2", nullable = false, length = 50)
    private String pnr2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProduktAehnlichkeitId entity = (ProduktAehnlichkeitId) o;
        return Objects.equals(this.pnr1, entity.pnr1) &&
                Objects.equals(this.pnr2, entity.pnr2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pnr1, pnr2);
    }

}