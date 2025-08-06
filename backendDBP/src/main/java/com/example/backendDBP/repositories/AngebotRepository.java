package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Angebot;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class AngebotRepository {
    private final SessionFactory sessionFactory;

    public AngebotRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Angebot> findOffersByPnr(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            Query<Angebot> query = session.createQuery("FROM Angebot a WHERE a.produkt.pnr = :pnr AND a.preis > 0.00", Angebot.class);
            query.setParameter("pnr", pnr);
            return query.list();
        }
    }

    public List<Angebot> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Angebot> query = session.createQuery("FROM Angebot", Angebot.class);
            return query.list();
        }
    }
}
