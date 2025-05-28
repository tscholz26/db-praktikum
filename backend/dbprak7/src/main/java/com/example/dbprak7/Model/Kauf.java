package com.example.dbprak7.Model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "kauf", schema = "public")
@XmlRootElement(name = "Kauf")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "kaufzeit", "menge", "name", "adresse", "ort", "plz"})
public class Kauf {

    @Id
    @ColumnDefault("nextval('kauf_kaufid_seq')")
    @Column(name = "kaufid", nullable = false)
    @XmlElement(name = "KaufID", required = true)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "kundenid", nullable = false)
    @XmlTransient
    private Kunde kundenid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr", nullable = false)
    @XmlTransient
    private Produkt produktnr;

    @Column(name = "kaufzeit", nullable = true)
    @XmlElement(name = "Kaufzeit")
    private Timestamp kaufzeit;

    @Column(name = "menge", nullable = false)
    @XmlElement(name = "Menge", required = true)
    private Integer menge;

    @Column(name = "name")
    @XmlElement(name = "Name")
    private String name;

    @Column(name = "adresse")
    @XmlElement(name = "Adresse")
    private String adresse;

    @Column(name = "ort", length = 100)
    @XmlElement(name = "Ort")
    private String ort;

    @Column(name = "plz", length = 20)
    @XmlElement(name = "PLZ")
    private String plz;

}