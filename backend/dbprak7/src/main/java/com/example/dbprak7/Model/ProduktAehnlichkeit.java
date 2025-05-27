package com.example.dbprak7.Model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@IdClass(ProduktAehnlichkeitId.class)
public class ProduktAehnlichkeit {
    @Id
    private String produktNr1;
    @Id
    private String produktNr2;
}
