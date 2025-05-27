package com.example.dbprak7.Model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@IdClass(ProduktKategorieId.class)
public class ProduktKategorie {
    @Id
    private String produktNr;
    @Id
    private Integer kategorieID;
}

