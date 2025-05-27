package com.example.dbprak7.Model;

import java.io.Serializable;
import java.util.Objects;

public class RezensionId implements Serializable {
    private Integer kundenID;
    private String produktNr;

    public RezensionId() {}

    public RezensionId(Integer kundenID, String produktNr) {
        this.kundenID = kundenID;
        this.produktNr = produktNr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RezensionId that = (RezensionId) o;
        return Objects.equals(kundenID, that.kundenID) &&
                Objects.equals(produktNr, that.produktNr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kundenID, produktNr);
    }
}
