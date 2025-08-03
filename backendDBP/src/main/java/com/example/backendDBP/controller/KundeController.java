package com.example.backendDBP.controller;

import com.example.backendDBP.DTOs.KundeDTO;
import com.example.backendDBP.models.Kunde;
import com.example.backendDBP.services.KatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class KundeController {

    private final KatalogService katalogService;

    @Autowired
    public KundeController(KatalogService katalogService) {
        this.katalogService = katalogService;
    }

    @GetMapping("/TrollsKunden")
    public List<KundeDTO> getTrollsKunden(@RequestParam double grenzwertRating) {
        return katalogService.getTrolls(grenzwertRating);
    }

}
