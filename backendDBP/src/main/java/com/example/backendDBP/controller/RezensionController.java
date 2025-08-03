package com.example.backendDBP.controller;


import com.example.backendDBP.DTOs.RezensionDTO;
import com.example.backendDBP.models.Rezension;
import com.example.backendDBP.services.KatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class RezensionController {

    private final KatalogService katalogService;

    @Autowired
    public RezensionController(KatalogService katalogService) {
        this.katalogService = katalogService;
    }

    @GetMapping("/getProduktRezensionen")
    public List<RezensionDTO> getProduktRezensionen(@RequestParam String pnr) {
        return katalogService.getProductReviews(pnr);
    }

    @PostMapping("/addRezension")
    public Rezension addRezension(@RequestBody RezensionDTO rezensionDTO) {
        return katalogService.addNewReview(rezensionDTO);
    }
}
