package com.example.backendDBP.models;

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

    @MapsId("pnr1")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pnr1", nullable = false)
    private Produkt produkt1;

    @MapsId("pnr2")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pnr2", nullable = false)
    private Produkt produkt2;

}