package com.example.backendDBP.services;

import com.example.backendDBP.api.MediastoreServiceAPI;
import com.example.backendDBP.models.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Properties;

public class KatalogService implements MediastoreServiceAPI {

    private SessionFactory sessionFactory;


    @Override
    public void init(Properties properties) {
        // 1) Hibernate-Konfiguration anlegen
        Configuration hibernateConfig = new Configuration();

        // Lese die Konfigurationseigenschaften aus dem Properties-Objekt
        String url     = properties.getProperty("spring.datasource.url");
        String user    = properties.getProperty("spring.datasource.username");
        String pass    = properties.getProperty("spring.datasource.password");
        String driver  = properties.getProperty("spring.datasource.driver-class-name");
        String dialect = properties.getProperty("spring.jpa.database-platform");
        String hbm2ddl = properties.getProperty("spring.jpa.hibernate.ddl-auto");

        // Setze die Konfigurationseigenschaften
        hibernateConfig.setProperty("hibernate.connection.driver_class", driver);
        hibernateConfig.setProperty("hibernate.connection.url", url);
        hibernateConfig.setProperty("hibernate.connection.username", user);
        hibernateConfig.setProperty("hibernate.connection.password", pass);
        hibernateConfig.setProperty("hibernate.dialect", dialect);
        hibernateConfig.setProperty("hibernate.hbm2ddl.auto", hbm2ddl);



        // 2) Annotated Entity-Klassen registrieren
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Produkt.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Kategorie.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Rezension.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Kunde.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Angebot.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Actor.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Angebot.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Autor.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Dvd.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Buch.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.BuchVerlag.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Creator.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Director.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Errordata.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Filiale.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Kauf.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Kuenstler.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Label.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.MusikCd.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.ProduktAehnlichkeit.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.ProduktKategorie.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Song.class);

        // 3) ServiceRegistry aufbauen
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(hibernateConfig.getProperties())
                .build();

        // 4) SessionFactory erzeugen
        this.sessionFactory = hibernateConfig.buildSessionFactory(registry);
    }

    @Override
    public void finish() {
        if (this.sessionFactory != null) {
            this.sessionFactory.close();
        }
    }

    @Override
    public Produkt getProduct(String pnr) {
        // Implementiere die Logik, um ein Produkt anhand der Produktnummer (pnr) zu erhalten
        return null; // Platzhalter, implementiere die Logik hier
    }

    @Override
    public List<Produkt> getProducts(String pattern) {
        // Implementiere die Logik, um Produkte anhand eines Suchmusters zu erhalten
        return null; // Platzhalter, implementiere die Logik hier
    }

    @Override
    public List<Kategorie> getCategorieTree(String pnr) {
        // Implementiere die Logik, um den Kategoriebaum für ein Produkt zu erhalten
        return null; // Platzhalter, implementiere die Logik hier
    }

    @Override
    public List<Produkt> getProductsByCategoryPath(List<Kategorie> kategoriePath) {
        // Implementiere die Logik, um Produkte anhand eines Kategoriepfads zu erhalten
        return null; // Platzhalter, implementiere die Logik hier
    }

    @Override
    public List<Produkt> getTopProducts(int limit) {
        // Implementiere die Logik, um die Top-Produkte zu erhalten
        return null; // Platzhalter, implementiere die Logik hier
    }

    @Override
    public List<Produkt> getSimilarCheaperProducts(String pnr) {
        // Implementiere die Logik, um ähnliche, günstigere Produkte zu erhalten
        return null; // Platzhalter, implementiere die Logik hier
    }

    @Override
    public List<Rezension> getProductReviews(String pnr) {
        // Implementiere die Logik, um Rezensionen für ein Produkt zu erhalten
        return null; // Platzhalter, implementiere die Logik hier
    }

    @Override
    public void addNewReview(String pnr, String nutzername, int bewertung, String rezension) {
        // Implementiere die Logik, um eine neue Rezension hinzuzufügen
        // Hier sollte die Logik zum Hinzufügen einer Rezension implementiert werden
    }

    @Override
    public List<Kunde> getTrolls(double grenzwertRating) {
        // Implementiere die Logik, um Troll-Kunden zu erhalten
        return null; // Platzhalter, implementiere die Logik hier
    }

    @Override
    public List<Angebot> getOffers(String pnr) {
        // Implementiere die Logik, um Angebote für ein Produkt zu erhalten
        return null; // Platzhalter, implementiere die Logik hier
    }

}
