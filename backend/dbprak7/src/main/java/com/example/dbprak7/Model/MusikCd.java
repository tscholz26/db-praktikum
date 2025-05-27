package com.example.dbprak7.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "musik_cd", schema = "public")
public class MusikCd {
    @Id
    @Column(name = "produktnr", nullable = false, length = 50)
    private String produktnr;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr", nullable = false)
    private Produkt produkt;

    @Column(name = "label")
    private String label;

    @Column(name = "erscheinungsdatum")
    private LocalDate erscheinungsdatum;

}