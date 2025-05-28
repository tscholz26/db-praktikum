package com.example.dbprak7.Model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "kategorie", schema = "public",
        indexes = {@Index(name = "idx_kategorie_name", columnList = "name")}
)
@XmlRootElement(name = "Kategorie")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "name"})
public class Kategorie {

    @Id
    @ColumnDefault("nextval('kategorie_kategorieid_seq')")
    @Column(name = "kategorieid", nullable = false)
    @XmlElement(name = "KategorieID", required = true)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    @XmlElement(name = "Name", required = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "oberkategorieid")
    @XmlTransient
    private Kategorie oberkategorieid;

}