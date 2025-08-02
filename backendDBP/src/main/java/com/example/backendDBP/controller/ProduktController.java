package com.example.backendDBP.controller;

import com.example.backendDBP.models.Produkt;
import com.example.backendDBP.models.Rezension;
import com.example.backendDBP.services.KatalogService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/getProdukt")
    public Produkt getProdukt(@RequestParam String pnr) {
        return katalogService.getProduct(pnr);
    }


    @GetMapping("/getProdukte")
    public List<Produkt> getProdukte(@RequestParam(required = false) String pattern) {
        return katalogService.getProducts(pattern);
    }

    @GetMapping("/getTopProdukte")
    public List<Produkt> getTopProdukte(@RequestParam int lim) {
        return katalogService.getTopProducts(lim);
    }

    @GetMapping("/getBilligereProdukte")
    public List<Produkt> getBilligereProdukte(@RequestParam String pnr) {
        return katalogService.getSimilarCheaperProducts(pnr);
    }




}