package com.example.backendDBP.DTOs;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AngebotDTO {

    private Integer angebotid;
    private String pnr;
    private Integer filialeid;
    private String filialeName;
    private String zustand;
    private BigDecimal preis;
    private String waehrung;

}
