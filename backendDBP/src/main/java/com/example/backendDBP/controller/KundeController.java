package com.example.backendDBP.controller;

import com.example.backendDBP.DTOs.KundeDTO;
import com.example.backendDBP.services.KatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class KundeController {

    private final KatalogService katalogService;

    @Autowired
    public KundeController(KatalogService katalogService) {
        this.katalogService = katalogService;
    }

    private void checkDatabaseConnection() {
        if (katalogService.getSessionFactory() == null || katalogService.getSessionFactory().isClosed()) {
            throw new IllegalStateException("Datenbank ist nicht initialisiert. Bitte zuerst /init aufrufen.");
        }
    }

    @GetMapping("/TrollsKunden")
    public ResponseEntity<?> getTrollsKunden(@RequestParam double grenzwertRating) {
        try {
            checkDatabaseConnection();
            return ResponseEntity.ok(katalogService.getTrolls(grenzwertRating));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Abrufen der Troll-Kunden: " + e.getMessage());
        }
    }
}
