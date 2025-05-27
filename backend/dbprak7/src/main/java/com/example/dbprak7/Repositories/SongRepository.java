package com.example.dbprak7.Repositories;

import com.example.dbprak7.Model.Song;
import com.example.dbprak7.Model.SongId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, SongId> {
}