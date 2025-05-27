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
public class ProduktAehnlichkeitId implements Serializable {
    private static final long serialVersionUID = 4330940335764384605L;
    @Column(name = "produktnr1", nullable = false, length = 50)
    private String produktnr1;

    @Column(name = "produktnr2", nullable = false, length = 50)
    private String produktnr2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProduktAehnlichkeitId entity = (ProduktAehnlichkeitId) o;
        return Objects.equals(this.produktnr2, entity.produktnr2) &&
                Objects.equals(this.produktnr1, entity.produktnr1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produktnr2, produktnr1);
    }

}