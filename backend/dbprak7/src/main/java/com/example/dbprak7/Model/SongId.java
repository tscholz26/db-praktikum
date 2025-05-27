package com.example.dbprak7.Model;

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
    private static final long serialVersionUID = 8745337289664919952L;
    @Column(name = "produktnr", nullable = false, length = 50)
    private String produktnr;

    @Column(name = "songtitel", nullable = false, length = Integer.MAX_VALUE)
    private String songtitel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SongId entity = (SongId) o;
        return Objects.equals(this.songtitel, entity.songtitel) &&
                Objects.equals(this.produktnr, entity.produktnr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songtitel, produktnr);
    }

}