package com.example.dbprak7.Model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@IdClass(RezensionId.class)
public class Rezension {
    @Id
    private Integer kundenID;
    @Id
    private String produktNr;

    private Integer bewertung;
    private String rezension;
}
