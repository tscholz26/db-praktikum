package com.example.backendDBP.DTOs;

import com.example.backendDBP.models.Autor;
import com.example.backendDBP.models.BuchVerlag;
import com.example.backendDBP.models.Label;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuchDTO extends ProduktDTO{

    private String isbn;
    private Integer seitenzahl;
    private LocalDate erscheinungsdatum;
    private String auflage;
    private List<String> verlage;
    private List<String> autoren;

    public BuchDTO(String pnr, String titel, Integer verkaufsrang, String bild, BigDecimal rating, String isbn, Integer seitenzahl, LocalDate erscheinungsdatum, String auflage, List<String> verlage, List<String> autoren) {
        super(pnr, titel, verkaufsrang, bild, rating);
        this.isbn = isbn;
        this.seitenzahl = seitenzahl;
        this.erscheinungsdatum = erscheinungsdatum;
        this.auflage = auflage;
        this.verlage = verlage;
        this.autoren = autoren;
    }

}
