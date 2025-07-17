-- CREATE TRIGGERS
CREATE OR REPLACE FUNCTION update_produkt_rating() RETURNS TRIGGER AS $$
DECLARE
    produkt_id VARCHAR(50);
BEGIN
    IF TG_OP = 'DELETE' THEN
        produkt_id := OLD.PNr;
    ELSE
        produkt_id := NEW.PNr;
    END IF;

    UPDATE Produkt
    SET Rating = (
        SELECT ROUND(CAST(AVG(Bewertung) AS DECIMAL), 2)
        FROM Rezension
        WHERE PNr = produkt_id
    )
    WHERE PNr = produkt_id;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER rezension_rating_update
    AFTER INSERT OR UPDATE OR DELETE ON Rezension
    FOR EACH ROW
EXECUTE FUNCTION update_produkt_rating();