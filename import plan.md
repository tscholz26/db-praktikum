# SCHRITT 1: dresden.xml und leipzig_transformed.xml
#### -> Erstellte Tables: Filiale, Produkt, Buch, DVD, Musik-CD, Angebot, Autoren, Produkt_Aehnlicheit
## Schritt 1.1: Filialen erstellen
- die XML files sind so aufgebaut, das als größte oberkategorie \<shop> steht, anhand dessen kann man die Filialen erstellen
- später kann man dann die Produkte zu den Filialen zufügen (wird in Tabelle Angebot gespeichert, siehe Schritt 1.4)

## Schritt 1.2: Produkte erstellen
- XML Zeile für Zeile durchlaufen und Produkte unabhängig von Filiale erstellen, und zwar wie folgt:
- ASIN suchen, falls nicht vorhanden: FEHLERMELDUNG, falls vorhanden: prüfen, ob ASIN bereits existiert
  - falls ASIN existiert: FALL 1: fehlerhafte ASIN, FALL 2: produkt ist schon vorhanden, aber in einem anderen Zustand in der filiale vorrätig
  - falls ASIN noch nicht existiert: neuen Eintrag in Tabelle Produkt erstellen mit ASIN als Primary Key 
  - bei Erstellung können salesrank, picture und Bild direkt mit eingetragen werden
  - nun muss jeweils noch ein Datensatz in der Tabelle Book/Music/DVD erstellt werden, in der der PK (also die ASIN) eingetragen wird, die Datensätze werden dann wie folgt behandelt:
  
- Fall 1: erstelles Objekt hat pgroup="Book"
  - item.author-Spalte auslesen, falls vorhanden:  in Tabelle Autoren den Dateynsatz (ASIN, author) erstellen, falls nicht vorhanden: FEHLER WERFEN
  - Folgende Spalten kann man auslesen und 1:1 so in die Tabelle eintragen falls vorhanden, falls nicht WIRD EIN FEHLER GEWORFEN UND DER DATENSATZ ZURÜCKGEWIESEN:
    - bookspec.isbn
    - item.publisher (=Verlag)
  - Folgende Spalten kann man auslesen und 1:1 so in die Tabelle eintragen falls vorhanden, falls nicht ist es kein Problem und wir werfen keinen Fehler:
    - bookspec.pages
    - bookspec.edition
    - bookspec.publication
  - Folgende Buch-spezifische Spalten werden (bis auf weiteres) ignoriert:  
    - bookspec.binding
    - bookspec.package
  
  
- Fall 2: erstelles Objekt hat pgroup="Music"
  - Folgende Spalten kann man auslesen und 1:1 so in die Tabelle eintragen falls vorhanden, falls nicht WIRD EIN FEHLER GEWORFEN UND DER DATENSATZ ZURÜCKGEWIESEN:
  - Folgende Spalten kann man auslesen und 1:1 so in die Tabelle eintragen falls vorhanden, falls nicht ist es kein Problem und wir werfen keinen Fehler:
  - Folgende Music-spezifische Spalten werden (bis auf weiteres) ignoriert:


- Fall 3: erstelles Objekt hat pgroup="DVD"
  - Folgende Spalten kann man auslesen und 1:1 so in die Tabelle eintragen falls vorhanden, falls nicht WIRD EIN FEHLER GEWORFEN UND DER DATENSATZ ZURÜCKGEWIESEN:
  - Folgende Spalten kann man auslesen und 1:1 so in die Tabelle eintragen falls vorhanden, falls nicht ist es kein Problem und wir werfen keinen Fehler:
  - Folgende DVD-spezifische Spalten werden (bis auf weiteres) ignoriert:
- Fall 4: erstelles Objekt hat Typ anderen Typ
  - FEHLER WERFEN 
- PROBLEM: wann werden die ganzen Künstler/Autoren/... erstellt?
- nach dem "ersten" durchlauf kann die relation Produkt_Aehnlicheit erstellt werden, da erstmal geschaut werden muss welche items wirklich erstellt werden konnten

## Schritt 1.3: Relation "Similar Product"
- hierfür muss man die Datensätze durchgehen und in Spalte similars.sim_product die ASINs der ähnlichen Produkte eintragen (hierfür wird die tabelle Produkt_Aehnlicheit benutzt)

## Schritt 1.4: Angebot erstellen
- nochmal die zwei XMLS durchlaufen und die Produkte, die erstellt wurden, zu den Filialen zufügen

# SCHRITT 2: categories.xml
#### -> Erstellte Tables: Kategorie, Produkt_Kategorie
- für jede \<category> ist hier eine Liste mit den IDs der Artikel zu finden
- Vorgehen: für jede \<category> eine Kategorie in der Relation Kategorie erstellen (ID: neu generieren)
- für jedes \<item> in der \<category> einen Eintrag in der Relation Produkt_Kategorie erstellen, FALLS ZUR ID EIN PRODUKT EXISTIERT
- wenn man in der XML jeweils eine Stufe tiefer geht: neu erstellte Kategorie der Oberkategorie zuweisen (indem man OberkategorieID der Unterkategorie setzt)


# SCHRITT 3: reviews.csv
#### -> Erstellte Tables: Rezension, Kunde
- für jeden neuen Username einen Kunde in der Relation anlegen
- Reviews mit laufender ID speichern, bei Kunde null-wert möglich (guest)
- mehr Kundendaten als die Usernames gibt es nicht, da keine Daten über Verkäufe/Kontonummern/... existieren
