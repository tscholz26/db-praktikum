package com.example.backendDBP.DTOs;

import com.example.backendDBP.models.Actor;
import com.example.backendDBP.models.Creator;
import com.example.backendDBP.models.Director;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DvdDTO extends ProduktDTO{

    private Integer regioncode;
    private String format;
    private Integer laufzeit;
    private List<String> directors;
    private List<String> creators;
    private List<String> actors;

    public DvdDTO(String pnr, String titel, Integer verkaufsrang, String bild, BigDecimal rating, List<String> directors, List<String> creators, List<String> actors, Integer regioncode, String format, Integer laufzeit) {
        super(pnr, titel, verkaufsrang, bild, rating);
        this.regioncode = regioncode;
        this.format = format;
        this.laufzeit = laufzeit;
        this.directors = directors;
        this.creators = creators;
        this.actors = actors;
    }


}
