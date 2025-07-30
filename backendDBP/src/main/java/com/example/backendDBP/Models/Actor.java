package com.example.backendDBP.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "actor", schema = "public")
public class Actor {
    @EmbeddedId
    private ActorId id;

    @MapsId("pnr")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pnr", nullable = false)
    private Dvd pnr;

}