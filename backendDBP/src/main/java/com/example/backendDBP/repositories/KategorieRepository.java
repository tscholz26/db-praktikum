package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Kategorie;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import java.util.List;

public class KategorieRepository {
    private final SessionFactory sessionFactory;

    public KategorieRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked")
    public List<Kategorie> findCategoryTreeForProduct(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            String sql = """
                WITH RECURSIVE cat_tree AS (
                    SELECT k.*
                    FROM produkt_kategorie pk
                    JOIN kategorie k
                        ON pk.kategorieid = k.kategorieid
                    WHERE pk.pnr = :pnr
                    UNION
                    SELECT parent.*
                    FROM kategorie parent
                    JOIN cat_tree ct
                        ON ct.oberkategorieid = parent.kategorieid
                )
                SELECT * FROM cat_tree
            """;
            Query<Kategorie> query = session.createNativeQuery(sql, Kategorie.class);
            query.setParameter("pnr", pnr);
            return query.list();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Kategorie> findFullCategoryTree() {
        try (Session session = sessionFactory.openSession()) {
            String sql = """
                WITH RECURSIVE cat_tree AS (
                    SELECT * FROM kategorie WHERE oberkategorieid IS NULL
                    UNION ALL
                    SELECT k.*
                    FROM kategorie k
                    JOIN cat_tree ct ON k.oberkategorieid = ct.kategorieid
                )
                SELECT * FROM cat_tree
            """;
            Query<Kategorie> query = session.createNativeQuery(sql, Kategorie.class);
            return query.list();
        }
    }

    public Kategorie findByKategorieName(String kategorieName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Kategorie> query = session.createQuery("FROM Kategorie k WHERE k.name = :kategorieName", Kategorie.class);
            query.setParameter("kategorieName", kategorieName);
            return query.uniqueResult();
        }
    }

    public List<Kategorie> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Kategorie> query = session.createQuery("FROM Kategorie", Kategorie.class);
            return query.list();
        }
    }
}
