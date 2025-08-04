package com.example.backendDBP.repositories;

import com.example.backendDBP.models.Kategorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KategorieRepository extends JpaRepository<Kategorie, Integer> {

    @Query(value =
            "WITH RECURSIVE cat_tree AS (                                       \n" +
                    "  SELECT k.*                                                    \n" +
                    "  FROM produkt_kategorie pk                            \n" +
                    "  JOIN kategorie k                                     \n" +
                    "    ON pk.kategorieid = k.kategorieid                         \n" +
                    "  WHERE pk.pnr = :pnr                                         \n" +
                    "  UNION                                                    \n" +
                    "  SELECT parent.*                                              \n" +
                    "  FROM kategorie parent                                \n" +
                    "  JOIN cat_tree ct                                            \n" +
                    "    ON ct.oberkategorieid = parent.kategorieid                \n" +
                    ")                                                              \n" +
                    "SELECT *                                                      \n" +
                    "FROM cat_tree                                                \n"
            , nativeQuery = true)
    List<Kategorie> findCategoryTreeForProduct(@Param("pnr") String pnr);
}
