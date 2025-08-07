package com.example.backendDBP.controller;


import com.example.backendDBP.services.KatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class KategorieController {

    private final KatalogService katalogService;

    @Autowired
    public KategorieController(KatalogService katalogService) {
        this.katalogService = katalogService;
    }

    private void checkDatabaseConnection() {
        if (katalogService.getSessionFactory() == null || katalogService.getSessionFactory().isClosed()) {
            throw new IllegalStateException("Datenbank ist nicht initialisiert. Bitte zuerst /init aufrufen.");
        }
    }

    @GetMapping("/getKategorieBaum")
    public ResponseEntity<?> getKategorieBaum(@RequestParam String pnr) {
        try {
            checkDatabaseConnection();
            if (pnr == null || pnr.isEmpty()) {
                return ResponseEntity.badRequest().body("Produktnummer (pnr) darf nicht leer sein.");
            }
            return ResponseEntity.ok(katalogService.getCategorieTree(pnr));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Abrufen des Kategoriebaums: " + e.getMessage());
        }
    }

    @GetMapping("/getKompletterKategorieBaum")
    public ResponseEntity<?> getKompletterKategorieBaum() {
        try {
            checkDatabaseConnection();
            return ResponseEntity.ok(katalogService.getFullCategoryTree());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Abrufen des kompletten Kategoriebaums: " + e.getMessage());
        }
    }

    @GetMapping("/getProdukteInKategorie")
    public ResponseEntity<?> getProdukteInKategorie(@RequestParam String kategoriePfad) {
        try {
            checkDatabaseConnection();
            if (kategoriePfad == null || kategoriePfad.isEmpty()) {
                return ResponseEntity.badRequest().body("Kategorie-Pfad darf nicht leer sein.");
            }
            return ResponseEntity.ok(katalogService.getProductsByCategoryPath(kategoriePfad));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Abrufen der Produkte in der Kategorie: " + e.getMessage());
        }
    }
}
