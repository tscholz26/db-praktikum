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
@Table(name = "buch", schema = "public",
        indexes = {
                @Index(name = "idx_buch_verlag", columnList = "verlag")
        }
)
public class Buch {
    @Id
    @Column(name = "produktnr", nullable = false, length = 50)
    private String produktnr;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr", nullable = false)
    private Produkt produkt;

    @Column(name = "isbn", nullable = false, length = 20)
    private String isbn;

    @Column(name = "seitenzahl")
    private Integer seitenzahl;

    @Column(name = "verlag")
    private String verlag;

    @Column(name = "erscheinungsdatum")
    private LocalDate erscheinungsdatum;

}