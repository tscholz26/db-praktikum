package com.example.dbprak7.Model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@IdClass(AngebotId.class)
public class Angebot {
    @Id
    private String produktNr;
    @Id
    private Integer filialeID;

    private Integer bestand;
    private BigDecimal preis;
}
