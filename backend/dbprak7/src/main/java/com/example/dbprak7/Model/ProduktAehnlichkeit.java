package com.example.dbprak7.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "produkt_aehnlichkeit", schema = "public")
public class ProduktAehnlichkeit {
    @EmbeddedId
    private ProduktAehnlichkeitId id;

    @MapsId("produktnr1")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr1", nullable = false)
    private Produkt produktnr1;

    @MapsId("produktnr2")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr2", nullable = false)
    private Produkt produktnr2;

}