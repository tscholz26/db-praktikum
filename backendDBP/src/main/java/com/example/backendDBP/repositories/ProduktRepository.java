package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Label;
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

    @Query("SELECT p FROM Produkt p WHERE p.titel LIKE %:pattern%")
    List<Produkt> findProductByPattern(@Param("pattern") String pattern);

    @Query("SELECT p FROM Produkt p where p.rating is not null order by p.rating desc limit :lim")
    List<Produkt> findTopProducts(@Param("lim") int lim);

    @Query("""
    SELECT a2.pnr
    FROM Angebot a2
    WHERE a2.pnr IN (
        SELECT CASE
            WHEN pa.pnr1 = :pnr THEN pa.pnr2
            ELSE pa.pnr1
        END
        FROM ProduktAehnlichkeit pa
        WHERE pa.pnr1 = :pnr OR pa.pnr2 = :pnr
    )
    AND a2.preis < (
        SELECT a1.preis
        FROM Angebot a1
        WHERE a1.pnr = :pnr
    )
""")
    List<String> findSimilarCheaperProducts(@Param("pnr") String pnr);

    @Query("SELECT p FROM Produkt p WHERE p.pnr IN :pnrs")
    List<Produkt> findProdukteByPnrs(@Param("pnrs") List<String> pnrs);


}
