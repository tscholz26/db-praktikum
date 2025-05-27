package com.example.dbprak7.Model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
public class MusikCD {
    @Id
    @Column(name = "produktNr")
    private String produktNr;

    @OneToOne
    @MapsId
    @JoinColumn(name = "produktNr")
    private Produkt produkt;

    private String label;
    private LocalDate erscheinungsdatum;

    @Column(columnDefinition = "text[]")
    private String[] songs;

    @Column(columnDefinition = "text[]")
    private String[] künstler;
}
