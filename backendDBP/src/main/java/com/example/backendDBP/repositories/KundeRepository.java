package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Kunde;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.Transaction;

import java.util.List;

public class KundeRepository {
    private final SessionFactory sessionFactory;

    public KundeRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Kunde findKundeByNutzername(String nutzername) {
        try (Session session = sessionFactory.openSession()) {
            Query<Kunde> query = session.createQuery("FROM Kunde k WHERE k.nutzername = :nutzername", Kunde.class);
            query.setParameter("nutzername", nutzername);
            return query.uniqueResult();
        }
    }

    public List<Object[]> findTrollsKunden(double grenzwertRating) {
        try (Session session = sessionFactory.openSession()) {
            String hql = """
                SELECT r.kunde, round(avg(r.bewertung), 2) as durchschittlichesRating
                FROM Rezension r
                GROUP BY r.kunde
                HAVING avg(r.bewertung) < :grenzwertRating
                ORDER BY 2 DESC
            """;
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("grenzwertRating", grenzwertRating);
            return query.list();
        }
    }

    public Kunde save(Kunde kunde) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(kunde);
                transaction.commit();
                return kunde;
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public List<Kunde> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Kunde> query = session.createQuery("FROM Kunde", Kunde.class);
            return query.list();
        }
    }
}
