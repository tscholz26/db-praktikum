package com.example.dbprak7.Model;

import java.io.Serializable;
import java.util.Objects;

public class ProduktAehnlichkeitId implements Serializable {
    private String produktNr1;
    private String produktNr2;

    public ProduktAehnlichkeitId() {}

    public ProduktAehnlichkeitId(String produktNr1, String produktNr2) {
        this.produktNr1 = produktNr1;
        this.produktNr2 = produktNr2;
    }

    // equals und hashCode Methoden
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProduktAehnlichkeitId that = (ProduktAehnlichkeitId) o;
        return Objects.equals(produktNr1, that.produktNr1) &&
                Objects.equals(produktNr2, that.produktNr2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produktNr1, produktNr2);
    }
}
