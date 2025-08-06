package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Song;
import com.example.backendDBP.models.SongId;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class SongRepository {
    private final SessionFactory sessionFactory;

    public SongRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<String> findSongNameByPnr(String pnr) {
        try (Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery("SELECT s.id.songtitel FROM Song s WHERE s.pnr.pnr = :pnr", String.class);
            query.setParameter("pnr", pnr);
            return query.list();
        }
    }

    public List<Song> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Song> query = session.createQuery("FROM Song", Song.class);
            return query.list();
        }
    }
}
