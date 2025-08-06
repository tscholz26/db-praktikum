package com.example.backendDBP.controller;

import com.example.backendDBP.services.KatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class InitializationController {

    private final KatalogService katalogService;

    @Autowired
    public InitializationController(KatalogService katalogService) {
        this.katalogService = katalogService;
    }

    @PostMapping("/init")
    public ResponseEntity<String> init() {
        try {
            katalogService.init();
            return ResponseEntity.ok("Datenbankverbindung erfolgreich hergestellt");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Fehler beim Aufbau der Datenbankverbindung: " + e.getMessage());
        }
    }

    @PostMapping("/finish")
    public ResponseEntity<String> finish() {
        try {
            if (katalogService.getSessionFactory() == null || katalogService.getSessionFactory().isClosed()) {
                return ResponseEntity.badRequest().body("Keine aktive Datenbankverbindung vorhanden");
            }
            katalogService.finish();
            return ResponseEntity.ok("Datenbankverbindung erfolgreich geschlossen");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Fehler beim Schließen der Datenbankverbindung: " + e.getMessage());
        }
    }

    @GetMapping("/dbstatus")
    public ResponseEntity<String> getDatabaseStatus() {
        try {
            if (katalogService.getSessionFactory() == null) {
                return ResponseEntity.ok("Status: Nicht initialisiert");
            }
            boolean isClosed = katalogService.getSessionFactory().isClosed();
            return ResponseEntity.ok("Status: " + (isClosed ? "Geschlossen" : "Verbunden"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Fehler beim Abrufen des Datenbankstatus: " + e.getMessage());
        }
    }
}
