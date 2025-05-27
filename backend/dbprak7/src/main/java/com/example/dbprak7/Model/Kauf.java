package com.example.dbprak7.Model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Kauf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kaufID;

    private Integer kundenID;
    private String produktNr;
    private LocalDateTime kaufzeit;
    private Integer menge;
    private String name;
    private String adresse;
    private String ort;
    private String plz;
}