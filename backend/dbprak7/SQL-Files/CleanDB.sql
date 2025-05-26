-- Löscht public-Schema inkl. aller Objekte (Tabellen, Views, Funktionen…)
DROP SCHEMA public CASCADE;

-- Legt das public-Schema wieder neu an
CREATE SCHEMA public;
