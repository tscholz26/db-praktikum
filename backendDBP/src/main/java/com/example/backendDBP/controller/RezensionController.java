package com.example.backendDBP.controller;


import com.example.backendDBP.DTOs.RezensionDTO;
import com.example.backendDBP.models.Rezension;
import com.example.backendDBP.services.KatalogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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

    @GetMapping("/getRezensionen")
    public List<RezensionDTO> getProduktRezensionen(@RequestParam String pnr) {
        return katalogService.getProductReviews(pnr);
    }

    @PostMapping("/addRezension")
    public Rezension addRezension(@RequestBody @Valid RezensionDTO rezensionDTO) {
        return katalogService.addNewReview(rezensionDTO);
    }
}
