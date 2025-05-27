package com.example.dbprak7.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "autor", schema = "public")
public class Autor {
    @EmbeddedId
    private AutorId id;

    @MapsId("produktnr")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr", nullable = false)
    private Buch produktnr;

}