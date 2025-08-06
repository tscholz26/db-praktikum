package com.example.backendDBP.api;

import com.example.backendDBP.DTOs.*;
import com.example.backendDBP.models.*;

import java.util.List;
import java.util.Properties;

public interface MediastoreServiceAPI {
   void init();
   void finish();

    String HelloWorld();

    ProduktDTO getProduct(String pnr);
    List<Produkt> getProducts(String pattern);

    List<KategorieDTO> getCategorieTree(String pnr);
    List<KategorieDTO> getFullCategoryTree();

    List<Produkt> getProductsByCategoryPath(List<Kategorie> kategoriePath);
    List<Produkt> getTopProducts(int lim);
    List<Produkt> getSimilarCheaperProducts(String pnr);
    List<RezensionDTO> getProductReviews(String pnr);
    Rezension addNewReview(RezensionDTO rezensionDTO);
    List<KundeDTO> getTrolls(double grenzwertRating);
    List<AngebotDTO> getOffers(String pnr);

}
