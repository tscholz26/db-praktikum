package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Autor;
import com.example.backendDBP.models.AutorId;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class AutorRepository {
    private final SessionFactory sessionFactory;

    public AutorRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<String> findAutorNameByPnr(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery("SELECT a.id.name FROM Autor a WHERE a.pnr.pnr = :pnr", String.class);
            query.setParameter("pnr", pnr);
            return query.list();
        }
    }

    public List<Autor> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Autor> query = session.createQuery("FROM Autor", Autor.class);
            return query.list();
        }
    }
}
