package com.example.dbprak7.Model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Kategorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kategorieID;

    private String name;

    @ManyToOne
    @JoinColumn(name = "oberkategorieID")
    private Kategorie oberkategorie;
}

