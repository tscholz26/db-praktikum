package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Song;
import com.example.backendDBP.models.SongId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, SongId> {

    @Query("SELECT s.id.songtitel FROM Song s WHERE s.pnr.pnr = :pnr")
    List<String> findSongNameByPnr(String pnr);
}
