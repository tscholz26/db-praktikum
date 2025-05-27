package com.example.dbprak7.Model;

import java.io.Serializable;
import java.util.Objects;

public class ProduktKategorieId implements Serializable {
    private String produktNr;
    private Integer kategorieID;

    public ProduktKategorieId() {}

    public ProduktKategorieId(String produktNr, Integer kategorieID) {
        this.produktNr = produktNr;
        this.kategorieID = kategorieID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProduktKategorieId that = (ProduktKategorieId) o;
        return Objects.equals(produktNr, that.produktNr) &&
                Objects.equals(kategorieID, that.kategorieID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produktNr, kategorieID);
    }
}
