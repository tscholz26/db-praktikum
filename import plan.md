# SCHRITT 1: dresden.xml und leipzig_transformed.xml
#### -> Erstellte Tables: Filiale, Produkt, Buch, DVD, Musik-CD, Angebot, Autoren, Produkt_Aehnlicheit, Songs, Künstler
## Schritt 1.1: Filialen erstellen
- die XML files sind so aufgebaut, das als größte oberkategorie \<shop> steht, anhand dessen kann man die Filialen erstellen
- später kann man dann die Produkte zu den Filialen zufügen (wird in Tabelle Angebot gespeichert, siehe Schritt 1.4)

## Schritt 1.2: Produkte erstellen
- XML Zeile für Zeile durchlaufen und Produkte unabhängig von Filiale erstellen, und zwar wie folgt:
- ASIN suchen, falls nicht vorhanden: FEHLERMELDUNG, falls vorhanden: prüfen, ob ASIN bereits existiert
  - falls ASIN existiert: FALL 1: fehlerhafte ASIN, FALL 2: produkt ist schon vorhanden, aber in einem anderen Zustand in der filiale vorrätig
  - falls ASIN noch nicht existiert: neuen Eintrag in Tabelle Produkt erstellen mit ASIN als Primary Key 
  - titel wird übernommen, bei NULL-Wert: FEHLER WERFEN
  - picture wird übernommen, bei NULL-Wert: kein fehler
  - salesrank wird übernommen, !!!!WAS PASSIERT BEI NULLWERT??!!!! (ist bei sehr vielen datensätzen vorhanden, sollte also ignoriert werden)
  - bei Erstellung können salesrank, picture und titel direkt mit eingetragen werden (bei picture wird NULL-wert akzeptiert, bei titel NICHT)
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
  - item.tracks auslesen:
    - falls nicht vorhanden: kein problem
    - falls vorhanden: für jeden Track zur Tabelle "Songs" den Datensatz (ASIN, Songtitel) hinzufügen 
  - item.artists auslesen:
    - falls nicht vorhanden: item.creators auslesen:
      - falls nicht vorhanden: FEHLER, DATENSATZ ZURÜCKWEISEN
      - falls vorhanden: für jeden Creator zur Tabelle "Künstler" den Datensatz (ASIN, Künstlername) hinzufügen
    - falls vorhanden: für jeden Artist zur Tabelle "Künstler" den Datensatz (ASIN, Künstlername) hinzufügen
  - item.label auslesen:
    - falls nicht vorhanden: FEHLER WERFEN, DATENSATZ ZURÜCKWEISEN
    - falls vorhanden: in Tabelle CD_Label den Datensatz (ASIN,Labelname) hinzufügen
  - Folgende Spalten kann man auslesen und 1:1 so in die Tabelle eintragen falls vorhanden, falls nicht ist es kein Problem und wir werfen keinen Fehler:
    - musicspec.releasedate
  - Folgende Music-spezifische Spalten werden (bis auf weiteres) ignoriert:
    - musicspec.binding
    - musicspec.format
    - musicspec.num_discs
    - musicspec.upc


- Fall 3: erstelles Objekt hat pgroup="DVD"
  - Folgende Spalten kann man auslesen und 1:1 so in die Tabelle eintragen falls vorhanden, falls nicht ist es kein Problem und wir werfen keinen Fehler:
    - dvdspec.format    -> DEFAULT WERT = NOT SPECIFIED
    - dvdspec.regioncode
    - dvdspec.runningtime
  - Folgende DVD-spezifische Spalten werden (bis auf weiteres) ignoriert:
    - dvdspec.aspectratio
    - dvdspec.releasedate
    - dvdspec.theatr_release
    - dvdspec.upc
    - item.audiotext
    - studio
  - actor/creator/director auslesen:
    - falls nicht vorhanden: kein problem
    - falls vorhanden: in tabelle actor/creator/director eine zeile (ASIN,Name der Person) schreiben    
  

- Fall 4: erstelles Objekt hat anderen Typ
  - FEHLER WERFEN 

## Schritt 1.3: Relation "Similar Product"
- hierfür muss man die Datensätze durchgehen und in Spalte similars.sim_product die ASINs der ähnlichen Produkte eintragen (hierfür wird die tabelle Produkt_Aehnlicheit benutzt)
- relation soll symmetrisch sein, also muss man bei der Suche nach ähnlichen Produkten zu Produkt X in beiden Spalten nach X suchen -> Index für beide Spalten anlegen
- eventuell prüfen, ob angegebener titel in der relation sim_product auch mit titel übereinstimmt der in produkt relation hinterlegt ist

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
