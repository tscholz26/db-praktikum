package com.example.backendDBP.api;

import com.example.backendDBP.DTOs.RezensionDTO;
import com.example.backendDBP.models.*;

import java.util.List;
import java.util.Properties;

public interface MediastoreServiceAPI {
   void init(Properties properties);
   void finish();

    String HelloWorld();

    Produkt getProduct(String pnr);
    List<Produkt> getProducts(String pattern);

    List<Kategorie> getCategorieTree(String pnr);

    List<Produkt> getProductsByCategoryPath(List<Kategorie> kategoriePath);
    List<Produkt> getTopProducts(int lim);
    List<Produkt> getSimilarCheaperProducts(String pnr);
    List<RezensionDTO> getProductReviews(String pnr);
    Rezension addNewReview(RezensionDTO rezensionDTO);
    List<Kunde> getTrolls(double grenzwertRating);
    List<Angebot> getOffers(String pnr);

}
