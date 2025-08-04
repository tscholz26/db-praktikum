package com.example.backendDBP.DTOs;

import com.example.backendDBP.models.Kategorie;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KategorieDTO {

    private Integer kategorieid;
    private String kategorienName;
    private List<KategorieDTO> children = new ArrayList<>();

    public KategorieDTO(Integer kategorieid, String kategorienName) {
        this.kategorieid = kategorieid;
        this.kategorienName = kategorienName;
    }

}
