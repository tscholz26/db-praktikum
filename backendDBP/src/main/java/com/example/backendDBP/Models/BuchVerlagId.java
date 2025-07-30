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
public class BuchVerlagId implements Serializable {
    private static final long serialVersionUID = -6746514028556443663L;
    @Column(name = "pnr", nullable = false, length = 50)
    private String pnr;

    @Column(name = "verlag", nullable = false)
    private String verlag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BuchVerlagId entity = (BuchVerlagId) o;
        return Objects.equals(this.pnr, entity.pnr) &&
                Objects.equals(this.verlag, entity.verlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pnr, verlag);
    }

}