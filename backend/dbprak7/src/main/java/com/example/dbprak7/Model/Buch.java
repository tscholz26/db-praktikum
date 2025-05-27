package com.example.dbprak7.Model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Buch {
    @Id
    private String produktNr;

    @OneToOne
    @MapsId
    @JoinColumn(name = "produktNr")
    private Produkt produkt;

    private String isbn;
    private Integer seitenzahl;
    private String verlag;
    private LocalDate erscheinungsdatum;

    @Column(columnDefinition = "text[]")
    private String[] autoren;
}
