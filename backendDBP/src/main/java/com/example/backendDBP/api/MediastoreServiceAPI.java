package com.example.backendDBP.api;

import com.example.backendDBP.models.*;

import java.util.List;
import java.util.Properties;

public interface MediastoreServiceAPI {
   void init(Properties properties);
   void finish();

    Produkt getProduct(String pnr);
    List<Produkt> getProducts(String pattern);

    List<Kategorie> getCategorieTree(String pnr);

    List<Produkt> getProductsByCategoryPath(List<Kategorie> kategoriePath);
    List<Produkt> getTopProducts(int limit);
    List<Produkt> getSimilarCheaperProducts(String pnr);
    List<Rezension> getProductReviews(String pnr);
    void addNewReview(String pnr, String nutzername, int bewertung, String rezension);
    List<Kunde> getTrolls(double grenzwertRating);
    List<Angebot> getOffers(String pnr);

}
