package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Buch;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class BuchRepository {
    private final SessionFactory sessionFactory;

    public BuchRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Buch findBuchByPnr(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            Query<Buch> query = session.createQuery("FROM Buch b WHERE b.produkt.pnr = :pnr", Buch.class);
            query.setParameter("pnr", pnr);
            return query.uniqueResult();
        }
    }

    public List<Buch> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Buch> query = session.createQuery("FROM Buch", Buch.class);
            return query.list();
        }
    }
}
