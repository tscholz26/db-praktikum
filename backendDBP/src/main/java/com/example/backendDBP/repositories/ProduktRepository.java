package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Produkt;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Transactional
@Repository
public interface ProduktRepository extends JpaRepository<Produkt, String> {

    @Query("SELECT p FROM Produkt p WHERE p.pnr = :pnr")
    Produkt findProduktByPnr(@Param("pnr") String pnr);

    @Query("SELECT p FROM Produkt p WHERE p.titel LIKE :pattern ESCAPE '\\'")
    List<Produkt> findProductsByPattern(@Param("pattern") String pattern);

    @Query("SELECT p FROM Produkt p where p.rating is not null order by p.rating desc limit :lim")
    List<Produkt> findTopProducts(@Param("lim") int lim);

    @Query("""
      SELECT DISTINCT p2
      FROM   ProduktAehnlichkeit pa
      JOIN   pa.produkt1 p1
      JOIN   pa.produkt2 p2
      JOIN   Angebot a2
        ON a2.produkt = p2
      WHERE  p1.pnr = :pnr
        AND  a2.preis < (
               SELECT MIN(a1.preis)
               FROM   Angebot a1
               WHERE  a1.produkt = p1
               AND a1.preis > 0.00
             )
        AND a2.preis > 0.00
    """)
    List<Produkt> findSimilarCheaperProducts(@Param("pnr") String pnr);
}
