package com.example.backendDBP.controller;

import com.example.backendDBP.models.Produkt;
import com.example.backendDBP.services.KatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ProduktController {

    private final KatalogService katalogService;

    @Autowired
    public ProduktController(KatalogService katalogService) {
        this.katalogService = katalogService;
    }

    @GetMapping("/getProdukt/{pnr}")
    public Produkt getProdukt(@PathVariable String pnr) {
        return katalogService.getProduct(pnr);
    }

    @GetMapping("/test")
    public String HelloWorld() {
        return katalogService.HelloWorld();
    }


}
