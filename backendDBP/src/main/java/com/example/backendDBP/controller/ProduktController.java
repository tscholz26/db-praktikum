package com.example.backendDBP.controller;

import com.example.backendDBP.DTOs.ProduktDTO;
import com.example.backendDBP.models.Produkt;
import com.example.backendDBP.services.KatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class ProduktController {

    private final KatalogService katalogService;

    @Autowired
    public ProduktController(KatalogService katalogService) {
        this.katalogService = katalogService;
    }

    @GetMapping("/test")
    public String HelloWorld() {
        return katalogService.HelloWorld();
    }

    private void checkDatabaseConnection() {
        if (katalogService.getSessionFactory() == null || katalogService.getSessionFactory().isClosed()) {
            throw new IllegalStateException("Datenbank ist nicht initialisiert. Bitte zuerst /init aufrufen.");
        }
    }

    @GetMapping("/getProdukt")
    public ResponseEntity<?> getProdukt(@RequestParam String pnr) {
        try {
            checkDatabaseConnection();
            ProduktDTO produkt = katalogService.getProduct(pnr);
            if (produkt == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(produkt);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Produkt nicht gefunden: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Abrufen des Produkts: " + e.getMessage());
        }
    }

    @GetMapping("/getProdukte")
    public ResponseEntity<?> getProdukte(@RequestParam(required = false) String pattern) {
        try {
            checkDatabaseConnection();
            return ResponseEntity.ok(katalogService.getProducts(pattern));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Abrufen der Produkte: " + e.getMessage());
        }
    }

    @GetMapping("/getTopProdukte")
    public ResponseEntity<?> getTopProdukte(@RequestParam int lim) {
        try {
            checkDatabaseConnection();
            return ResponseEntity.ok(katalogService.getTopProducts(lim));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Abrufen der Top-Produkte: " + e.getMessage());
        }
    }

    @GetMapping("/getBilligereProdukte")
    public ResponseEntity<?> getBilligereProdukte(@RequestParam String pnr) {
        try {
            checkDatabaseConnection();
            return ResponseEntity.ok(katalogService.getSimilarCheaperProducts(pnr));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Abrufen der günstigeren Produkte: " + e.getMessage());
        }
    }

}