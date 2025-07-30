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
public class CreatorId implements Serializable {
    private static final long serialVersionUID = 5162511786406249849L;
    @Column(name = "pnr", nullable = false, length = 50)
    private String pnr;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CreatorId entity = (CreatorId) o;
        return Objects.equals(this.pnr, entity.pnr) &&
                Objects.equals(this.name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pnr, name);
    }

}