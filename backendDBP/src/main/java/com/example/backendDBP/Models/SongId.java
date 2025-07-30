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
public class SongId implements Serializable {
    private static final long serialVersionUID = 6525975055599869132L;
    @Column(name = "pnr", nullable = false, length = 50)
    private String pnr;

    @Column(name = "songtitel", nullable = false, length = Integer.MAX_VALUE)
    private String songtitel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SongId entity = (SongId) o;
        return Objects.equals(this.songtitel, entity.songtitel) &&
                Objects.equals(this.pnr, entity.pnr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songtitel, pnr);
    }

}