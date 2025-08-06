package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Director;
import com.example.backendDBP.models.DirectorId;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class DirectorRepository {
    private final SessionFactory sessionFactory;

    public DirectorRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<String> findDirectorNameByPnr(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery("SELECT d.id.name FROM Director d WHERE d.pnr.pnr = :pnr", String.class);
            query.setParameter("pnr", pnr);
            return query.list();
        }
    }

    public List<Director> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Director> query = session.createQuery("FROM Director", Director.class);
            return query.list();
        }
    }
}
