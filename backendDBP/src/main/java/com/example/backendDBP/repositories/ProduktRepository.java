package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Kategorie;
import com.example.backendDBP.models.Produkt;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProduktRepository {
    private static final Logger log = LoggerFactory.getLogger(ProduktRepository.class);
    private final SessionFactory sessionFactory;

    public ProduktRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Produkt findProduktByPnr(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            String hql = """
                SELECT DISTINCT p
                FROM Produkt p
                WHERE p.pnr = :pnr
                """;
            Query<Produkt> query = session.createQuery(hql, Produkt.class);
            query.setParameter("pnr", pnr.trim());

            Produkt produkt = query.uniqueResult();
            if (produkt == null) {
                throw new IllegalArgumentException("Kein Produkt gefunden für PNR: " + pnr);
            }
            return produkt;
        } catch (Exception e) {
            String errorMsg = "Fehler beim Suchen des Produkts mit PNR " + pnr + ": " + e.getMessage();
            log.error(errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public List<Produkt> findProductsByPattern(String pattern) {
        try (Session session = sessionFactory.openSession()) {
            Query<Produkt> query = session.createQuery("FROM Produkt p WHERE p.titel LIKE :pattern ESCAPE '\\'", Produkt.class);
            query.setParameter("pattern", pattern);
            return query.list();
        } catch (Exception e) {
            log.error("Fehler beim Suchen von Produkten mit Muster {}: {}", pattern, e.getMessage());
            throw new RuntimeException("Fehler beim Suchen von Produkten: " + e.getMessage(), e);
        }
    }

    public List<Produkt> findTopProducts(int lim) {
        try (Session session = sessionFactory.openSession()) {
            Query<Produkt> query = session.createQuery("FROM Produkt p WHERE p.rating is not null ORDER BY p.rating DESC", Produkt.class);
            query.setMaxResults(lim);
            return query.list();
        } catch (Exception e) {
            log.error("Fehler beim Suchen der Top-Produkte: {}", e.getMessage());
            throw new RuntimeException("Fehler beim Suchen der Top-Produkte: " + e.getMessage(), e);
        }
    }

    public List<Produkt> findSimilarCheaperProducts(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT DISTINCT p2 " +
                    "FROM ProduktAehnlichkeit pa " +
                    "JOIN pa.produkt1 p1 " +
                    "JOIN pa.produkt2 p2 " +
                    "JOIN Angebot a2 ON a2.produkt = p2 " +
                    "WHERE p1.pnr = :pnr " +
                    "AND a2.preis < " +
                    "(SELECT MIN(a1.preis) FROM Angebot a1 WHERE a1.produkt = p1 AND a1.preis > 0.00) AND a2.preis > 0.00";
            Query<Produkt> query = session.createQuery(hql, Produkt.class);
            query.setParameter("pnr", pnr);
            return query.list();
        } catch (Exception e) {
            log.error("Fehler beim Suchen ähnlicher günstiger Produkte für PNR {}: {}", pnr, e.getMessage());
            throw new RuntimeException("Fehler beim Suchen ähnlicher günstiger Produkte: " + e.getMessage(), e);
        }
    }

    public List<Produkt> findProdukteByKategorie(Kategorie kategorie) {
        try (Session session = sessionFactory.openSession()) {
            // Finde alle Produkte in der Kategorie und deren Unterkategorien
            String sql = """
                WITH RECURSIVE kategorie_tree AS (
                    -- Basis: Die übergebene Kategorie
                    SELECT kategorieid
                    FROM kategorie
                    WHERE kategorieid = :kategorieId
                    
                    UNION ALL
                    
                    -- Rekursion: Alle Unterkategorien
                    SELECT k.kategorieid
                    FROM kategorie k
                    JOIN kategorie_tree kt ON k.oberkategorieid = kt.kategorieid
                )
                SELECT DISTINCT p.*
                FROM kategorie_tree kt
                JOIN produkt_kategorie pk ON kt.kategorieid = pk.kategorieid
                JOIN produkt p ON pk.pnr = p.pnr
                """;
            Query<Produkt> query = session.createNativeQuery(sql, Produkt.class);
            query.setParameter("kategorieId", kategorie.getId());
            return query.list();
        } catch (Exception e) {
            log.error("Fehler beim Suchen von Produkten für Kategorie {} und deren Unterkategorien: {}", kategorie, e.getMessage());
            throw new RuntimeException("Fehler beim Suchen von Produkten: " + e.getMessage(), e);
        }
    }

    public List<Produkt> findAll() {
        try (Session session = sessionFactory.openSession()) {
            String hql = """
                SELECT DISTINCT p
                FROM Produkt p
                ORDER BY p.titel
                """;
            Query<Produkt> query = session.createQuery(hql, Produkt.class);
            return query.list();
        } catch (Exception e) {
            log.error("Fehler beim Suchen aller Produkte: {}", e.getMessage());
            throw new RuntimeException("Fehler beim Suchen aller Produkte: " + e.getMessage(), e);
        }
    }
}
