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
public class RezensionId implements Serializable {
    private static final long serialVersionUID = 391811804472931352L;
    @Column(name = "kundenid", nullable = false)
    private Integer kundenid;

    @Column(name = "produktnr", nullable = false, length = 50)
    private String produktnr;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RezensionId entity = (RezensionId) o;
        return Objects.equals(this.kundenid, entity.kundenid) &&
                Objects.equals(this.produktnr, entity.produktnr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kundenid, produktnr);
    }

}