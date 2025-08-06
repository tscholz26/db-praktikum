package com.example.backendDBP.DTOs;

import com.example.backendDBP.models.Kuenstler;
import com.example.backendDBP.models.Label;
import com.example.backendDBP.models.Song;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MusikCdDTO extends ProduktDTO{

    private List<String> kuenstler;
    private LocalDate erscheinungsdatum;
    private List<String> labels;
    private List<String> songs;

    public MusikCdDTO(String pnr, String titel, Integer verkaufsrang, String bild, BigDecimal rating, List<String> kuenstler, LocalDate erscheinungsdatum, List<String> labels, List<String> songs) {
        super(pnr, titel, verkaufsrang, bild, rating);
        this.kuenstler = kuenstler;
        this.erscheinungsdatum = erscheinungsdatum;
        this.labels = labels;
        this.songs = songs;
    }

}
