package com.example.backendDBP.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "kategorie", schema = "public", indexes = {
        @Index(name = "idx_kategorie_name", columnList = "name"),
        @Index(name = "idx_kategorie_oberkategorieid", columnList = "oberkategorieid")
})
public class Kategorie {
    @Id
    @ColumnDefault("nextval('kategorie_kategorieid_seq')")
    @Column(name = "kategorieid", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "oberkategorieid")
    private Kategorie oberkategorieid;

}