# DBPraktikum MediaStore

## Übersicht

Das Praktikum beinhaltet den Entwurf einer Datenbank sowie die Realisierung von Datenbankanwendungen unter praxisnahen Bedingungen. Die Aufgaben sind mit einem einer lokalen postgres Datenbank zu bearbeiten. Im einzelnen sind folgende Teilaufgaben zu lösen:

  
1.   Entwurf eines konzeptionellen Schemas für eine Beispiel-Miniwelt. Dazu ist ein Entity-Relationship-Modell zu erstellen und in ein relationales Schema zu transformieren. Vollständige Generierung der Datenbank, d.h. Einlesen der zur Verfügung gestellten Daten (XML, CSV) in die DB mittels Java bzw. SQL/XML. 
2. Es sollen verschiedene Anfragen formuliert werden.
3.  Realisierung einer Middleware in Java unter Verwendung von Hibernate zur Anbindung einer Konsolenanwendung oder GUI-Anwendung.

## Lösungen

### Teil 1
- Beschreibung [Teil 1](teil_1.md)
- SQL-Skript zum Erstellen [sql/create.sql](sql/create.sql)
- Import Klassen: Für die Shops und Kategorie XML-Dateien existieren die Klassen   `de.uni_leipzig.dbs.io.handler.ShopHandler` und `de.uni_leipzig.dbs.io.handler.CategoryHandler`. Für das Parsen der Reviews wird eine CSV-Datei ausgelesen. Hierfür folgende Klasse `de.uni_leipzig.dbs.io.handler.ReviewParser`. Der Import erfolgt mittels jdbc und der `de.uni_leipzig.dbs.io.DBWriter` Klasse.

### Teil 2
[Teil 2](teil_2.md)



### Teil 3
[Teil 3](teil_3.md)
