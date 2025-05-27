package com.example.dbprak7.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "kunde", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "kunde_nutzername_key", columnNames = {"nutzername"})
})
public class Kunde {
    @Id
    @ColumnDefault("nextval('kunde_kundenid_seq')")
    @Column(name = "kundenid", nullable = false)
    private Integer id;

    @Column(name = "nutzername", nullable = false, length = 100)
    private String nutzername;

}