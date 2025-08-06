package com.example.backendDBP.repositories;

import com.example.backendDBP.models.BuchVerlag;
import com.example.backendDBP.models.BuchVerlagId;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class BuchVerlagRepository {
    private final SessionFactory sessionFactory;

    public BuchVerlagRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<String> findVerlagByPnr(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery("SELECT bv.id.verlag FROM BuchVerlag bv WHERE bv.buch.pnr = :pnr", String.class);
            query.setParameter("pnr", pnr);
            return query.list();
        }
    }

    public List<BuchVerlag> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<BuchVerlag> query = session.createQuery("FROM BuchVerlag", BuchVerlag.class);
            return query.list();
        }
    }
}
