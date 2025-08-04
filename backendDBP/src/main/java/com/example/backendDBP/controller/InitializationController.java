package com.example.backendDBP.controller;

import com.example.backendDBP.services.KatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            return ResponseEntity.ok("Connected to the database successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("[ERROR] Failed to establish connection to the database." + e.getMessage());
        }
    }

    @PostMapping("/finish")
    public ResponseEntity<String> finish() {
        try {
            katalogService.finish();
            return ResponseEntity.ok("Database connection closed!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("[ERROR] Failed to close database connection" + e.getMessage());
        }
    }
}
