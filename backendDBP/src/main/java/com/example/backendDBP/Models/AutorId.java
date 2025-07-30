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
public class AutorId implements Serializable {
    private static final long serialVersionUID = 6712931282688136503L;
    @Column(name = "pnr", nullable = false, length = 50)
    private String pnr;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AutorId entity = (AutorId) o;
        return Objects.equals(this.pnr, entity.pnr) &&
                Objects.equals(this.name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pnr, name);
    }

}