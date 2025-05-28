package com.example.dbprak7.Model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "errordata", schema = "public",
        indexes = {@Index(name = "idx_error_fehlermeldung", columnList = "fehlermeldung")}
)
@XmlRootElement(name = "ErrorData")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"pnr", "fehlermeldung"})
public class Errordata {

    @Id
    @Column(name = "pnr", nullable = false, length = 50)
    @XmlElement(name = "PNr", required = true)
    private String pnr;

    @Column(name = "fehlermeldung", nullable = false, length = Integer.MAX_VALUE)
    @XmlElement(name = "Fehlermeldung", required = true)
    private String fehlermeldung;

}
