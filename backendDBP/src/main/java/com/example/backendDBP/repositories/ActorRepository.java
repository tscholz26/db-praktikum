package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Actor;
import com.example.backendDBP.models.ActorId;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class ActorRepository {
    private final SessionFactory sessionFactory;

    public ActorRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<String> findActorNameByPnr(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery("SELECT a.id.name FROM Actor a WHERE a.pnr.pnr = :pnr", String.class);
            query.setParameter("pnr", pnr);
            return query.list();
        }
    }

    public List<Actor> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Actor> query = session.createQuery("FROM Actor", Actor.class);
            return query.list();
        }
    }
}
