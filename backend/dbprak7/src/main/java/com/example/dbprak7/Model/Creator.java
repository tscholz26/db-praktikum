package com.example.dbprak7.Model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "creator", schema = "public")
@XmlRootElement(name = "Creator")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id"})
public class Creator {

    @EmbeddedId
    @XmlElement(name = "Id", required = true)
    private CreatorId id;

    @MapsId("produktnr")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr", nullable = false)
    @XmlTransient
    private Dvd produktnr;

}
