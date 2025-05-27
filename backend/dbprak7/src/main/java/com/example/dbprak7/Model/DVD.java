package com.example.dbprak7.Model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class DVD {
    @Id
    private String produktNr;

    @OneToOne
    @MapsId
    @JoinColumn(name = "produktNr")
    private Produkt produkt;

    private Integer regioncode;
    private String format;
    private Integer laufzeit;

    @Column(columnDefinition = "text[]")
    private String[] creator;

    @Column(columnDefinition = "text[]")
    private String[] actor;

    @Column(columnDefinition = "text[]")
    private String[] director;
}
