-- 1. Basistabelle Produkt
CREATE TABLE Produkt (
                         PNr          VARCHAR(50) PRIMARY KEY,
                         Verkaufsrang INTEGER,
                         Bild         TEXT,
                         Titel        VARCHAR(255)
);

-- 2. Musik-CD als Spezialisierung von Produkt ohne Array-Attribute
CREATE TABLE Musik_CD (
                          ProduktNr         VARCHAR(50) PRIMARY KEY
                              REFERENCES Produkt(PNr)
                                  ON DELETE CASCADE
                                  ON UPDATE CASCADE,
                          Label             VARCHAR(255),
                          Erscheinungsdatum DATE
);

-- 2a. Songs als eigene Relation
CREATE TABLE Song (
                      ProduktNr   VARCHAR(50) NOT NULL
                          REFERENCES Musik_CD(ProduktNr)
                              ON DELETE CASCADE
                              ON UPDATE CASCADE,
                      Songtitel   TEXT       NOT NULL,
                      PRIMARY KEY (ProduktNr, Songtitel)
);

-- 2b. Kuenstler als eigene Relation
CREATE TABLE Kuenstler (
                           ProduktNr    VARCHAR(50) NOT NULL
                               REFERENCES Musik_CD(ProduktNr)
                                   ON DELETE CASCADE
                                   ON UPDATE CASCADE,
                           Kuenstlername TEXT      NOT NULL,
                           PRIMARY KEY (ProduktNr, Kuenstlername)
);

-- 3. DVD als Spezialisierung von Produkt ohne Array-Attribute
CREATE TABLE DVD (
                     ProduktNr   VARCHAR(50) PRIMARY KEY
                         REFERENCES Produkt(PNr)
                             ON DELETE CASCADE
                             ON UPDATE CASCADE,
                     Regioncode  INTEGER,
                     Format      VARCHAR(100),
                     Laufzeit    INTEGER
);

-- 3a. Creator als eigene Relation
CREATE TABLE Creator (
                         ProduktNr VARCHAR(50) NOT NULL
                             REFERENCES DVD(ProduktNr)
                                 ON DELETE CASCADE
                                 ON UPDATE CASCADE,
                         Name      TEXT      NOT NULL,
                         PRIMARY KEY (ProduktNr, Name)
);

-- 3b. Actor als eigene Relation
CREATE TABLE Actor (
                       ProduktNr VARCHAR(50) NOT NULL
                           REFERENCES DVD(ProduktNr)
                               ON DELETE CASCADE
                               ON UPDATE CASCADE,
                       Name      TEXT      NOT NULL,
                       PRIMARY KEY (ProduktNr, Name)
);

-- 3c. Director als eigene Relation
CREATE TABLE Director (
                          ProduktNr VARCHAR(50) NOT NULL
                              REFERENCES DVD(ProduktNr)
                                  ON DELETE CASCADE
                                  ON UPDATE CASCADE,
                          Name      TEXT      NOT NULL,
                          PRIMARY KEY (ProduktNr, Name)
);

-- 4. Buch als Spezialisierung von Produkt ohne Array-Attribute
CREATE TABLE Buch (
                      ProduktNr         VARCHAR(50) PRIMARY KEY
                          REFERENCES Produkt(PNr)
                              ON DELETE CASCADE
                              ON UPDATE CASCADE,
                      ISBN              VARCHAR(20) NOT NULL,
                      Seitenzahl        INTEGER,
                      Verlag            VARCHAR(255),
                      Erscheinungsdatum DATE
);

-- 4a. Autor als eigene Relation
CREATE TABLE Autor (
                       ProduktNr VARCHAR(50) NOT NULL
                           REFERENCES Buch(ProduktNr)
                               ON DELETE CASCADE
                               ON UPDATE CASCADE,
                       Name      TEXT      NOT NULL,
                       PRIMARY KEY (ProduktNr, Name)
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
                                   ProduktNr   VARCHAR(50) NOT NULL
                                       REFERENCES Produkt(PNr)
                                           ON DELETE CASCADE
                                           ON UPDATE CASCADE,
                                   KategorieID INTEGER      NOT NULL
                                       REFERENCES Kategorie(KategorieID)
                                           ON DELETE CASCADE
                                           ON UPDATE CASCADE,
                                   PRIMARY KEY (ProduktNr, KategorieID)
);

-- 6. Filialen und Angebote
CREATE TABLE Filiale (
                         FilialeID SERIAL PRIMARY KEY,
                         Name      VARCHAR(255),
                         Adresse   VARCHAR(255),
                         Ort       VARCHAR(100),
                         PLZ       VARCHAR(20)
);

CREATE TABLE Angebot (
                         ProduktNr VARCHAR(50) NOT NULL
                             REFERENCES Produkt(PNr)
                                 ON DELETE CASCADE
                                 ON UPDATE CASCADE,
                         FilialeID INTEGER      NOT NULL
                             REFERENCES Filiale(FilialeID)
                                 ON DELETE CASCADE
                                 ON UPDATE CASCADE,
                         Bestand   INTEGER,
                         Preis     DECIMAL(10,2),
                         PRIMARY KEY (ProduktNr, FilialeID)
);

-- 7. Kunden, Käufe und Rezensionen
CREATE TABLE Kunde (
                       KundenID   SERIAL PRIMARY KEY,
                       Nutzername VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE Kauf (
                      KaufID    SERIAL PRIMARY KEY,
                      KundenID  INTEGER NOT NULL
                          REFERENCES Kunde(KundenID)
                              ON DELETE CASCADE
                              ON UPDATE CASCADE,
                      ProduktNr VARCHAR(50) NOT NULL
                          REFERENCES Produkt(PNr)
                              ON DELETE CASCADE
                              ON UPDATE CASCADE,
                      Kaufzeit  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      Menge     INTEGER   NOT NULL,
                      Name      VARCHAR(255),
                      Adresse   VARCHAR(255),
                      Ort       VARCHAR(100),
                      PLZ       VARCHAR(20)
);

CREATE TABLE Rezension (
                           KundenID  INTEGER NOT NULL
                               REFERENCES Kunde(KundenID)
                                   ON DELETE CASCADE
                                   ON UPDATE CASCADE,
                           ProduktNr VARCHAR(50) NOT NULL
                               REFERENCES Produkt(PNr)
                                   ON DELETE CASCADE
                                   ON UPDATE CASCADE,
                           Bewertung INTEGER CHECK ( Bewertung between 1 AND 5),
                           Rezension TEXT,
                           PRIMARY KEY (KundenID, ProduktNr)
);

-- 8. Produkt-Ähnlichkeiten
CREATE TABLE Produkt_Aehnlichkeit (
                                      ProduktNr1 VARCHAR(50) NOT NULL
                                          REFERENCES Produkt(PNr)
                                              ON DELETE CASCADE
                                              ON UPDATE CASCADE,
                                      ProduktNr2 VARCHAR(50) NOT NULL
                                          REFERENCES Produkt(PNr)
                                              ON DELETE CASCADE
                                              ON UPDATE CASCADE,
                                      PRIMARY KEY (ProduktNr1, ProduktNr2)
);

-- 9. ErrorData
CREATE TABLE ErrorData (
                           Pnr           VARCHAR(50) PRIMARY KEY,
                           Fehlermeldung TEXT      NOT NULL
);