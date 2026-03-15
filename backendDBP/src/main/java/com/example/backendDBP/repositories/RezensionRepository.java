package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Rezension;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RezensionRepository {
    private final SessionFactory sessionFactory;

    public RezensionRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Rezension> findAllByPnr(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            Query<Rezension> query = session.createQuery("FROM Rezension r WHERE r.produkt.pnr = :pnr", Rezension.class);
            query.setParameter("pnr", pnr);
            return query.list();
        }
    }

    public Rezension save(Rezension rezension) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(rezension);
                transaction.commit();
                return rezension;
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public List<Rezension> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Rezension> query = session.createQuery("FROM Rezension", Rezension.class);
            return query.list();
        }
    }
}
