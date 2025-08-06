package com.example.backendDBP.repositories;

import com.example.backendDBP.models.MusikCd;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class MusikCdRepository {
    private final SessionFactory sessionFactory;

    public MusikCdRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public MusikCd findMusikCdByPnr(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            Query<MusikCd> query = session.createQuery("FROM MusikCd m WHERE m.produkt.pnr = :pnr", MusikCd.class);
            query.setParameter("pnr", pnr);
            return query.uniqueResult();
        }
    }

    public List<MusikCd> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<MusikCd> query = session.createQuery("FROM MusikCd", MusikCd.class);
            return query.list();
        }
    }
}
