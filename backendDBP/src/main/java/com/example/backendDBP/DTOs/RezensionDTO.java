package com.example.backendDBP.DTOs;

import com.example.backendDBP.models.Rezension;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RezensionDTO {
    private Integer id;
    private String pnr;
    private String produktname;
    private String nutzername;
    private Integer bewertung;
    private String rezension;

    public RezensionDTO(String pnr, String nutzername, String bewertung, String rezension) {
        this.pnr = pnr;
        this.nutzername = nutzername;
        this.bewertung = Integer.parseInt(bewertung);
        this.rezension = rezension;
    }

}


