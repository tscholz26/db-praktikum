package com.example.dbprak7.Model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class ErrorData {
    @Id
    private String pnr;

    @Column(nullable = false)
    private String fehlermeldung;
}
