package com.example.backendDBP.controller;

import com.example.backendDBP.DTOs.AngebotDTO;
import com.example.backendDBP.models.Angebot;
import com.example.backendDBP.services.KatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class AngebotController {

    private final KatalogService katalogService;

    @Autowired
    public AngebotController(KatalogService katalogService) {
        this.katalogService = katalogService;
    }

    @GetMapping("/getAngebote")
    public List<AngebotDTO> getAngebot(@RequestParam String pnr) {
        return katalogService.getOffers(pnr);
    }
}
