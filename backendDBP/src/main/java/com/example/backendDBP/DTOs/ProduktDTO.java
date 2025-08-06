package com.example.backendDBP.DTOs;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProduktDTO {
    private String pnr;
    private String titel;
    private Integer verkaufsrang;
    private String bild;
    private BigDecimal rating;

    public ProduktDTO(String pnr, String titel, Integer verkaufsrang, String bild, BigDecimal rating) {
        this.pnr = pnr;
        this.titel = titel;
        this.verkaufsrang = verkaufsrang;
        this.bild = bild;
        this.rating = rating;
    }
}
