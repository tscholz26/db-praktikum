package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Kuenstler;
import com.example.backendDBP.models.KuenstlerId;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class KuenstlerRepository {
    private final SessionFactory sessionFactory;

    public KuenstlerRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<String> findKuenstlerByPnr(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery("SELECT k.id.name FROM Kuenstler k WHERE k.pnr.pnr = :pnr", String.class);
            query.setParameter("pnr", pnr);
            return query.list();
        }
    }

    public List<Kuenstler> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Kuenstler> query = session.createQuery("FROM Kuenstler", Kuenstler.class);
            return query.list();
        }
    }
}
