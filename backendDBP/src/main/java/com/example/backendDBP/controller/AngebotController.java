package com.example.backendDBP.controller;

import com.example.backendDBP.DTOs.AngebotDTO;
import com.example.backendDBP.services.KatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class AngebotController {

    private final KatalogService katalogService;

    @Autowired
    public AngebotController(KatalogService katalogService) {
        this.katalogService = katalogService;
    }

    private void checkDatabaseConnection() {
        if (katalogService.getSessionFactory() == null || katalogService.getSessionFactory().isClosed()) {
            throw new IllegalStateException("Datenbank ist nicht initialisiert. Bitte zuerst /init aufrufen.");
        }
    }

    @GetMapping("/getAngebote")
    public ResponseEntity<?> getAngebot(@RequestParam String pnr) {
        try {
            checkDatabaseConnection();
            return ResponseEntity.ok(katalogService.getOffers(pnr));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Abrufen der Angebote: " + e.getMessage());
        }
    }
}
