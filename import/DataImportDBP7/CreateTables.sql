DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- 1. Basistabelle Produkt
CREATE TABLE Produkt (
                         PNr          VARCHAR(50) PRIMARY KEY,
                         Verkaufsrang INTEGER,
                         Bild         TEXT,
                         Titel        VARCHAR(255),
                         Rating       DECIMAL(3,2)
);

-- 2. Musik-CD als Spezialisierung von Produkt ohne Array-Attribute
CREATE TABLE Musik_CD (
                          PNr         VARCHAR(50) PRIMARY KEY
                              REFERENCES Produkt(PNr)
                                  ON DELETE CASCADE
                                  ON UPDATE CASCADE,
                          Erscheinungsdatum DATE
);

-- Label als eigene Relation, da in XML-Datei sehr oft mehrere Labels vorkommen
CREATE TABLE Label (
                      PNr   VARCHAR(50) NOT NULL
                          REFERENCES Musik_CD(PNr)
                              ON DELETE CASCADE
                              ON UPDATE CASCADE,
                      Labelname   VARCHAR(255) NOT NULL,
                      PRIMARY KEY (PNr, Labelname)
);

-- 2a. Songs als eigene Relation
CREATE TABLE Song (
                      PNr   VARCHAR(50) NOT NULL
                          REFERENCES Musik_CD(PNr)
                              ON DELETE CASCADE
                              ON UPDATE CASCADE,
                      Songtitel   TEXT       NOT NULL,
                      PRIMARY KEY (PNr, Songtitel)
);

-- 2b. Kuenstler als eigene Relation
CREATE TABLE Kuenstler (
                           PNr    VARCHAR(50) NOT NULL
                               REFERENCES Musik_CD(PNr)
                                   ON DELETE CASCADE
                                   ON UPDATE CASCADE,
                           Kuenstlername TEXT      NOT NULL,
                           PRIMARY KEY (PNr, Kuenstlername)
);

-- 3. DVD als Spezialisierung von Produkt ohne Array-Attribute
CREATE TABLE DVD (
                     PNr   VARCHAR(50) PRIMARY KEY
                         REFERENCES Produkt(PNr)
                             ON DELETE CASCADE
                             ON UPDATE CASCADE,
                     Regioncode  INTEGER,
                     Format      VARCHAR(100) DEFAULT 'not specified',
                     Laufzeit    INTEGER
);

-- 3a. Creator als eigene Relation
CREATE TABLE Creator (
                         PNr VARCHAR(50) NOT NULL
                             REFERENCES DVD(PNr)
                                 ON DELETE CASCADE
                                 ON UPDATE CASCADE,
                         Name      TEXT      NOT NULL,
                         PRIMARY KEY (PNr, Name)
);

-- 3b. Actor als eigene Relation
CREATE TABLE Actor (
                       PNr VARCHAR(50) NOT NULL
                           REFERENCES DVD(PNr)
                               ON DELETE CASCADE
                               ON UPDATE CASCADE,
                       Name      TEXT      NOT NULL,
                       PRIMARY KEY (PNr, Name)
);

-- 3c. Director als eigene Relation
CREATE TABLE Director (
                          PNr VARCHAR(50) NOT NULL
                              REFERENCES DVD(PNr)
                                  ON DELETE CASCADE
                                  ON UPDATE CASCADE,
                          Name      TEXT      NOT NULL,
                          PRIMARY KEY (PNr, Name)
);

-- 4. Buch als Spezialisierung von Produkt ohne Array-Attribute
CREATE TABLE Buch (
                      PNr         VARCHAR(50) PRIMARY KEY
                          REFERENCES Produkt(PNr)
                              ON DELETE CASCADE
                              ON UPDATE CASCADE,
                      ISBN              VARCHAR(20) NOT NULL,
                      Seitenzahl        INTEGER,
                      Erscheinungsdatum DATE,
                      Auflage           VARCHAR(50)
);

CREATE TABLE Buch_Verlag (
                      PNr VARCHAR(50),
                      Verlag VARCHAR(255),
                      PRIMARY KEY (PNr, Verlag),
                      FOREIGN KEY (PNr) REFERENCES Buch(PNr) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 4a. Autor als eigene Relation
CREATE TABLE Autor (
                       PNr VARCHAR(50) NOT NULL
                           REFERENCES Buch(PNr)
                               ON DELETE CASCADE
                               ON UPDATE CASCADE,
                       Name      TEXT      NOT NULL,
                       PRIMARY KEY (PNr, Name)
);

-- 5. Kategorien
CREATE TABLE Kategorie (
                           KategorieID     SERIAL PRIMARY KEY,
                           Name            VARCHAR(100) NOT NULL,
                           OberkategorieID INTEGER
                               REFERENCES Kategorie(KategorieID)
                                   ON DELETE SET NULL
                                   ON UPDATE CASCADE
);

CREATE TABLE Produkt_Kategorie (
                                   PNr   VARCHAR(50) NOT NULL
                                       REFERENCES Produkt(PNr)
                                           ON DELETE CASCADE
                                           ON UPDATE CASCADE,
                                   KategorieID INTEGER      NOT NULL
                                       REFERENCES Kategorie(KategorieID)
                                           ON DELETE CASCADE
                                           ON UPDATE CASCADE,
                                   PRIMARY KEY (PNr, KategorieID)
);

-- 6. Filialen und Angebote
CREATE TABLE Filiale (
                         FilialeID SERIAL PRIMARY KEY,
                         Name      VARCHAR(255),
                         Adresse   VARCHAR(255),
                         PLZ       VARCHAR(20)
);

CREATE TABLE Angebot (
                         AngebotID SERIAL PRIMARY KEY,
                         PNr VARCHAR(50) NOT NULL
                             REFERENCES Produkt(PNr)
                                 ON DELETE CASCADE
                                 ON UPDATE CASCADE,
                         FilialeID INTEGER      NOT NULL
                             REFERENCES Filiale(FilialeID)
                                 ON DELETE CASCADE
                                 ON UPDATE CASCADE,
                         Zustand   varchar(20) NOT NULL,
                         Preis     DECIMAL(10,2) NOT NULL CHECK (Preis >= 0),
                         Waehrung  varchar(3) NOT NULL
);

-- 7. Kunden, Käufe und Rezensionen
CREATE TABLE Kunde (
                      Nutzername VARCHAR(100) PRIMARY KEY
);

CREATE TABLE Kauf (
                      KaufID    SERIAL PRIMARY KEY,
                      Nutzername  VARCHAR(100)
                          REFERENCES Kunde(Nutzername)
                              ON DELETE CASCADE
                              ON UPDATE CASCADE,
                      PNr VARCHAR(50) NOT NULL
                          REFERENCES Produkt(PNr)
                              ON DELETE CASCADE
                              ON UPDATE CASCADE,
                      Kaufzeit  TIMESTAMP,
                      Preis     DECIMAL(10,2) NOT NULL CHECK (Preis >= 0),
                      WarenkorbPreis DECIMAL(10,2) CHECK ( WarenkorbPreis = Menge * Preis ),
                      Menge     INTEGER   NOT NULL,
                      Name      VARCHAR(255),
                      Adresse   VARCHAR(255),
                      Ort       VARCHAR(100),
                      PLZ       VARCHAR(20)
);

CREATE TABLE Rezension (
                           RezensionsID SERIAL UNIQUE NOT NULL,
                           PNr VARCHAR(50) NOT NULL
                               REFERENCES Produkt(PNr)
                                   ON DELETE CASCADE
                                   ON UPDATE CASCADE,
                           Nutzername VARCHAR(100) NOT NULL
                               REFERENCES Kunde(Nutzername)
                                   ON DELETE CASCADE
                                   ON UPDATE CASCADE,
                           Bewertung INTEGER CHECK (Bewertung between 1 AND 5),
                           Rezension TEXT,
                           PRIMARY KEY (RezensionsID)
);

-- 8. Produkt-Ähnlichkeiten
CREATE TABLE Produkt_Aehnlichkeit (
                                      PNr1 VARCHAR(50) NOT NULL
                                          REFERENCES Produkt(PNr)
                                              ON DELETE CASCADE
                                              ON UPDATE CASCADE,
                                      PNr2 VARCHAR(50) NOT NULL
                                          REFERENCES Produkt(PNr)
                                              ON DELETE CASCADE
                                              ON UPDATE CASCADE,
                                      PRIMARY KEY (PNr1, PNr2)
);

-- 9. ErrorData
CREATE TABLE ErrorData (
                            ErrorID SERIAL PRIMARY KEY,
                            EntityName VARCHAR(100) NOT NULL,
                            FehlerAttribut VARCHAR(100) NOT NULL,
                            Fehlermeldung TEXT NOT NULL,
                            Fehlerklasse VARCHAR(100)
);

-- CREATE INDEXES
CREATE INDEX IF NOT EXISTS idx_produkt_titel ON produkt (titel);
CREATE INDEX IF NOT EXISTS idx_dvd_laufzeit ON dvd (laufzeit);
CREATE INDEX IF NOT EXISTS idx_kategorie_name ON kategorie (name);
CREATE INDEX IF NOT EXISTS idx_filiale_name ON filiale (name);
CREATE INDEX IF NOT EXISTS idx_angebot_preis ON angebot (preis);
CREATE INDEX IF NOT EXISTS idx_rezension_bewertung ON rezension (bewertung);



-- CREATE TRIGGERS
CREATE OR REPLACE FUNCTION update_produkt_rating() RETURNS TRIGGER AS $$
BEGIN
    UPDATE Produkt
    SET Rating = (
        SELECT ROUND(AVG(Bewertung)::numeric, 2)
        FROM Rezension
        WHERE PNr = NEW.PNr
    )
    WHERE PNr = NEW.PNr;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER rezension_rating_update
    AFTER INSERT OR UPDATE OR DELETE ON Rezension
    FOR EACH ROW
EXECUTE FUNCTION update_produkt_rating();