package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Creator;
import com.example.backendDBP.models.CreatorId;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class CreatorRepository {
    private final SessionFactory sessionFactory;

    public CreatorRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<String> findCreatorNameByPnr(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery("SELECT c.id.name FROM Creator c WHERE c.pnr.pnr = :pnr", String.class);
            query.setParameter("pnr", pnr);
            return query.list();
        }
    }

    public List<Creator> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Creator> query = session.createQuery("FROM Creator", Creator.class);
            return query.list();
        }
    }
}
