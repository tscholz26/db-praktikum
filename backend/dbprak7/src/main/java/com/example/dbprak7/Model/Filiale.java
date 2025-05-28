package com.example.dbprak7.Model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "filiale", schema = "public",
        indexes = {
                @Index(name = "idx_filiale_ort", columnList = "ort"),
                @Index(name = "idx_filiale_name", columnList = "name")
        }
)
@XmlRootElement(name = "Filiale")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "name", "adresse", "ort", "plz"})
public class Filiale {

    @Id
    @ColumnDefault("nextval('filiale_filialeid_seq')")
    @Column(name = "filialeid", nullable = false)
    @XmlElement(name = "FilialeID", required = true)
    private Integer id;

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