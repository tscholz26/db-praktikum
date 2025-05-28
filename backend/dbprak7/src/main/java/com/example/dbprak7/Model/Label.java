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
@Table(name = "label", schema = "public")
@XmlRootElement(name = "Label")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id"})
public class Label {

    @EmbeddedId
    @XmlElement(name = "Id", required = true)
    private LabelId id;

    @MapsId("produktnr")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produktnr", nullable = false)
    @XmlTransient
    private MusikCd produktnr;

}
