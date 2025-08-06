package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Label;
import com.example.backendDBP.models.LabelId;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class LabelRepository {
    private final SessionFactory sessionFactory;

    public LabelRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<String> findLabelNameByPnr(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery("SELECT l.id.labelname FROM Label l WHERE l.pnr.pnr = :pnr", String.class);
            query.setParameter("pnr", pnr);
            return query.list();
        }
    }

    public List<Label> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Label> query = session.createQuery("FROM Label", Label.class);
            return query.list();
        }
    }
}
