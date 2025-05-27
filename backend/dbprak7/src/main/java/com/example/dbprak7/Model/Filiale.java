package com.example.dbprak7.Model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Filiale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer filialeID;
    private String name;
    private String adresse;
    private String ort;
    private String plz;
}
