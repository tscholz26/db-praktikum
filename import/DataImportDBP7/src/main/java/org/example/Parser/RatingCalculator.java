package org.example.Parser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RatingCalculator {

    private final Connection con;

    public RatingCalculator(Connection con) {
        this.con = con;
    }

    public void calculateRatings() {

        String updateQuery = """
                UPDATE public.produkt p
                    SET rating = r.rating
                    FROM (
                            SELECT PNr, AVG(bewertung) AS rating
                            FROM public.rezension
                            GROUP BY PNr
                    ) r
                WHERE p.pnr = r.pnr;
        """;

        try {
            PreparedStatement stmt = con.prepareStatement(updateQuery);
            stmt.executeUpdate();
            System.out.println("\u001B[32m[SUCCESS] Calculated average product ratings successfully.\u001B[0m");
        } catch (SQLException e) {
            System.out.println("\u001B[31m[ERROR] Error calculating product ratings: " + e.getMessage() + "\u001B[0m");
        }


    }


}
