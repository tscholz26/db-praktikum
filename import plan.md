# SCHRITT 1: dresden.xml und leipzig_transformed.xml
#### -> Erstellte Tables: Filiale, Produkt, Buch, DVD, Musik-CD, Angebot
## Schritt 1.1: Filialen erstellen
- die XML files sind so aufgebaut, das als größte oberkategorie \<shop> steht, anhand dessen kann man die Filialen erstellen
- später kann man dann die Produkte zu den Filialen zufügen (=Tabelle Angebot)

## Schritt 1.2: Produkte erstellen
- XML Zeile für Zeile durchlaufen, jedes Produkt einzeln erstellen, erstmal unabhängig von Filiale
- PROBLEM: wann werden die ganzen Künstler/Autoren/... erstellt?
- nach dem "ersten" durchlauf kann die relation Produkt_Aehnlicheit erstellt werden, da erstmal geschaut werden muss welche items wirklich erstellt werden konnten

## Schritt 1.3: Angebot erstellen
- nochmal die zwei XMLS durchlaufen und die Produkte, die erstellt wurden, zu den Filialen zufügen

# SCHRITT 2: categories.xml
#### -> Erstellte Tables: Kategorie, Produkt_Kategorie
- für jede \<category> ist hier eine Liste mit den IDs der Artikel zu finden
- Vorgehen: für jede \<category> eine Kategorie in der Relation Kategorie erstellen (ID: neu generieren)
- danach für jede Produkt ID (die wirklich existiert) einen eintrag in der Table Produkt_Kategorie hinzufügen
- woran erkennt man die unterkategorien?????


# SCHRITT 3: reviews.csv
#### -> Erstellte Tables: Rezension, Kunde
- für jeden neuen Username einen Kunde in der Relation anlegen
- Reviews mit laufender ID speichern, bei Kunde null-wert möglich (guest)
- mehr Kundendaten als die Usernames gibt es nicht, da keine Daten über Verkäufe/Kontonummern/... existieren
