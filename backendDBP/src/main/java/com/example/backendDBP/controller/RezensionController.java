package com.example.backendDBP.controller;


import com.example.backendDBP.DTOs.RezensionDTO;
import com.example.backendDBP.models.Rezension;
import com.example.backendDBP.services.KatalogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    private void checkDatabaseConnection() {
        if (katalogService.getSessionFactory() == null || katalogService.getSessionFactory().isClosed()) {
            throw new IllegalStateException("Datenbank ist nicht initialisiert. Bitte zuerst /init aufrufen.");
        }
    }

    @GetMapping("/getRezensionen")
    public ResponseEntity<?> getProduktRezensionen(@RequestParam String pnr) {
        try {
            checkDatabaseConnection();
            return ResponseEntity.ok(katalogService.getProductReviews(pnr));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Abrufen der Rezensionen: " + e.getMessage());
        }
    }

    @PostMapping("/addRezension")
    public ResponseEntity<?> addRezension(@RequestBody @Valid RezensionDTO rezensionDTO) {
        try {
            checkDatabaseConnection();
            return ResponseEntity.ok(katalogService.addNewReview(rezensionDTO));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Hinzufügen der Rezension: " + e.getMessage());
        }
    }
}
