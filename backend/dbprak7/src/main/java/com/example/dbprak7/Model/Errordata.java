package com.example.dbprak7.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "errordata", schema = "public",
        indexes = {
                @Index(name = "idx_error_fehlermeldung", columnList = "fehlermeldung")
}
)
public class Errordata {
    @Id
    @Column(name = "pnr", nullable = false, length = 50)
    private String pnr;

    @Column(name = "fehlermeldung", nullable = false, length = Integer.MAX_VALUE)
    private String fehlermeldung;

}