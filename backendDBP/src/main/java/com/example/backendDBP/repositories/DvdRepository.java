package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Dvd;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class DvdRepository {
    private final SessionFactory sessionFactory;

    public DvdRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Dvd findDvdByPnr(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            Query<Dvd> query = session.createQuery("FROM Dvd d WHERE d.produkt.pnr = :pnr", Dvd.class);
            query.setParameter("pnr", pnr);
            return query.uniqueResult();
        }
    }

    public List<Dvd> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Dvd> query = session.createQuery("FROM Dvd", Dvd.class);
            return query.list();
        }
    }
}
