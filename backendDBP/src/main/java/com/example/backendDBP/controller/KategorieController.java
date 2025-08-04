package com.example.backendDBP.controller;

import com.example.backendDBP.DTOs.KategorieDTO;
import com.example.backendDBP.models.Kategorie;
import com.example.backendDBP.services.KatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class KategorieController {

    private final KatalogService katalogService;

    @Autowired
    public KategorieController(KatalogService katalogService) {
        this.katalogService = katalogService;
    }

    @GetMapping("/getKategorieBaum")
    public List<KategorieDTO> getKategorieBaum(@RequestParam String pnr){
        if (pnr == null || pnr.isEmpty()) {
            throw new IllegalArgumentException("Produktnummer (pnr) darf nicht leer sein.");
        }
        return katalogService.getCategorieTree(pnr);
    }
}
