package com.example.dbprak7.Model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Produkt {
    @Id
    private String pNr;
    private Integer verkaufsrang;
    private String bild;
    private String titel;
}
