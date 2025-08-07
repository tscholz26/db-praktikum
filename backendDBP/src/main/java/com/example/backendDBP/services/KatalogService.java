package com.example.backendDBP.services;

import com.example.backendDBP.BackendDbpApplication;
import com.example.backendDBP.DTOs.*;
import com.example.backendDBP.api.MediastoreServiceAPI;
import com.example.backendDBP.models.*;
import com.example.backendDBP.repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class KatalogService implements MediastoreServiceAPI {

    private SessionFactory sessionFactory;
    private ProduktRepository produktRepository;
    private RezensionRepository rezensionRepository;
    private KundeRepository kundeRepository;
    private AngebotRepository angebotRepository;
    private KategorieRepository kategorieRepository;
    private BuchRepository buchRepository;
    private AutorRepository autorRepository;
    private BuchVerlagRepository buchVerlagRepository;
    private MusikCdRepository musikCdRepository;
    private KuenstlerRepository kuenstlerRepository;
    private LabelRepository labelRepository;
    private SongRepository songRepository;
    private DvdRepository dvdRepository;
    private CreatorRepository creatorRepository;
    private DirectorRepository directorRepository;
    private ActorRepository actorRepository;

    private static final Object LOCK = new Object();
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(KatalogService.class);

    public KatalogService() {
        // Initialisierung in init
    }

    private void initializeRepositories() {
        this.produktRepository = new ProduktRepository(sessionFactory);
        this.rezensionRepository = new RezensionRepository(sessionFactory);
        this.kundeRepository = new KundeRepository(sessionFactory);
        this.angebotRepository = new AngebotRepository(sessionFactory);
        this.kategorieRepository = new KategorieRepository(sessionFactory);
        this.buchRepository = new BuchRepository(sessionFactory);
        this.autorRepository = new AutorRepository(sessionFactory);
        this.buchVerlagRepository = new BuchVerlagRepository(sessionFactory);
        this.musikCdRepository = new MusikCdRepository(sessionFactory);
        this.kuenstlerRepository = new KuenstlerRepository(sessionFactory);
        this.labelRepository = new LabelRepository(sessionFactory);
        this.songRepository = new SongRepository(sessionFactory);
        this.dvdRepository = new DvdRepository(sessionFactory);
        this.creatorRepository = new CreatorRepository(sessionFactory);
        this.directorRepository = new DirectorRepository(sessionFactory);
        this.actorRepository = new ActorRepository(sessionFactory);
    }

    @Override
    public void init() {
        synchronized (LOCK) {
            log.info("Initialisiere Datenbankverbindung...");
            if (this.sessionFactory != null && !this.sessionFactory.isClosed()) {
                log.warn("Es existiert bereits eine offene Session");
                throw new IllegalStateException("Es existiert bereits eine offene Session");
            }

            // Properties laden
            Properties properties = new Properties();
            try (InputStream in = BackendDbpApplication.class
                    .getClassLoader()
                    .getResourceAsStream("application.properties")) {
                if (in == null) {
                    throw new IllegalStateException("application.properties nicht gefunden im Classpath!");
                }
                properties.load(in);
            } catch (IOException e) {
                throw new IllegalStateException("Fehler beim Laden der application.properties: " + e.getMessage(), e);
            }

            // Hibernate-Konfiguration
            Configuration hibernateConfig = new Configuration();

            // Datenbank-Eigenschaften setzen
            hibernateConfig.setProperty("hibernate.connection.url", properties.getProperty("spring.datasource.url"));
            hibernateConfig.setProperty("hibernate.connection.username", properties.getProperty("spring.datasource.username"));
            hibernateConfig.setProperty("hibernate.connection.password", properties.getProperty("spring.datasource.password"));
            hibernateConfig.setProperty("hibernate.connection.driver_class", properties.getProperty("spring.datasource.driver-class-name"));
            hibernateConfig.setProperty("hibernate.dialect", properties.getProperty("spring.jpa.properties.hibernate.dialect"));
            hibernateConfig.setProperty("hibernate.hbm2ddl.auto", properties.getProperty("spring.jpa.hibernate.ddl-auto"));

            // Zusätzliche Hibernate-Eigenschaften
            hibernateConfig.setProperty("hibernate.connection.pool_size", "5");
            hibernateConfig.setProperty("hibernate.show_sql", "true");
            hibernateConfig.setProperty("hibernate.format_sql", "true");
            hibernateConfig.setProperty("hibernate.current_session_context_class", "thread");
            hibernateConfig.setProperty("hibernate.enable_lazy_load_no_trans", "true");
            hibernateConfig.setProperty("hibernate.jdbc.batch_size", "50");
            hibernateConfig.setProperty("hibernate.default_batch_fetch_size", "25");

            // Entity-Klassen registrieren
            hibernateConfig.addAnnotatedClass(Produkt.class);
            hibernateConfig.addAnnotatedClass(Kunde.class);
            hibernateConfig.addAnnotatedClass(Rezension.class);
            hibernateConfig.addAnnotatedClass(Angebot.class);
            hibernateConfig.addAnnotatedClass(Filiale.class);
            hibernateConfig.addAnnotatedClass(Kategorie.class);
            hibernateConfig.addAnnotatedClass(Buch.class);
            hibernateConfig.addAnnotatedClass(Autor.class);
            hibernateConfig.addAnnotatedClass(BuchVerlag.class);
            hibernateConfig.addAnnotatedClass(MusikCd.class);
            hibernateConfig.addAnnotatedClass(Kuenstler.class);
            hibernateConfig.addAnnotatedClass(Label.class);
            hibernateConfig.addAnnotatedClass(Song.class);
            hibernateConfig.addAnnotatedClass(Dvd.class);
            hibernateConfig.addAnnotatedClass(Creator.class);
            hibernateConfig.addAnnotatedClass(Director.class);
            hibernateConfig.addAnnotatedClass(Actor.class);
            hibernateConfig.addAnnotatedClass(ProduktKategorie.class);
            hibernateConfig.addAnnotatedClass(ProduktKategorieId.class);
            hibernateConfig.addAnnotatedClass(ProduktAehnlichkeit.class);
            hibernateConfig.addAnnotatedClass(ProduktAehnlichkeitId.class);

            // SessionFactory erstellen
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(hibernateConfig.getProperties())
                    .build();

            try {
                this.sessionFactory = hibernateConfig.buildSessionFactory(registry);
                // Nach erfolgreicher SessionFactory-Erstellung die Repositories initialisieren
                initializeRepositories();
                log.info("Datenbankverbindung erfolgreich initialisiert");
            } catch (Exception e) {
                StandardServiceRegistryBuilder.destroy(registry);
                log.error("Fehler beim Initialisieren der Datenbankverbindung: {}", e.getMessage());
                e.printStackTrace(); // Für detailliertere Fehleranalyse
                if (e instanceof org.hibernate.exception.JDBCConnectionException) {
                    throw new IllegalStateException("Keine Verbindung zur Datenbank möglich: " + e.getMessage());
                }
                throw new IllegalStateException("Fehler beim Erstellen der SessionFactory: " + e.getMessage() + "\nUrsache: " + (e.getCause() != null ? e.getCause().getMessage() : "unbekannt"));
            }
        }
    }

    @Override
    public void finish() {
        synchronized (LOCK) {
            log.info("Beende Datenbankverbindung...");
            if (sessionFactory != null) {
                try {
                    if (!sessionFactory.isClosed()) {
                        sessionFactory.close();
                        log.info("Datenbankverbindung erfolgreich geschlossen");
                    } else {
                        log.warn("SessionFactory war bereits geschlossen");
                    }
                } catch (Exception e) {
                    log.error("Fehler beim Schließen der Datenbankverbindung: {}", e.getMessage());
                    throw new IllegalStateException("Fehler beim Schließen der Datenbankverbindung: " + e.getMessage());
                } finally {
                    sessionFactory = null;
                    // Repositories auf null setzen
                    produktRepository = null;
                    rezensionRepository = null;
                    kundeRepository = null;
                    angebotRepository = null;
                    kategorieRepository = null;
                    buchRepository = null;
                    autorRepository = null;
                    buchVerlagRepository = null;
                    musikCdRepository = null;
                    kuenstlerRepository = null;
                    labelRepository = null;
                    songRepository = null;
                    dvdRepository = null;
                    creatorRepository = null;
                    directorRepository = null;
                    actorRepository = null;
                }
            } else {
                log.warn("Keine aktive SessionFactory vorhanden");
            }
        }
    }

    private <T> T executeInTransaction(Function<EntityManager, T> operation) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            T result = operation.apply(entityManager);
            entityManager.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Datenbankfehler: " + e.getMessage(), e);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    @Override
    public ProduktDTO getProduct(String pnr) {
        return executeInTransaction(session -> {
            Buch buch = buchRepository.findBuchByPnr(pnr);
            if (buch != null) {
                // Initialisiere alle benötigten Beziehungen
                BuchDTO buchdto = new BuchDTO();
                buchdto.setPnr(buch.getPnr());
                buchdto.setTitel(buch.getProdukt().getTitel());
                buchdto.setVerkaufsrang(buch.getProdukt().getVerkaufsrang());
                buchdto.setBild(buch.getProdukt().getBild());
                buchdto.setRating(buch.getProdukt().getRating());
                buchdto.setIsbn(buch.getIsbn());
                buchdto.setSeitenzahl(buch.getSeitenzahl());
                buchdto.setErscheinungsdatum(buch.getErscheinungsdatum());
                buchdto.setAuflage(buch.getAuflage());
                buchdto.setAutoren(autorRepository.findAutorNameByPnr(pnr));
                buchdto.setVerlage(buchVerlagRepository.findVerlagByPnr(pnr));
                return buchdto;
            }
            MusikCd musikcd = musikCdRepository.findMusikCdByPnr(pnr);
            if (musikcd != null) {
                MusikCdDTO musikcddto = new MusikCdDTO();
                musikcddto.setPnr(musikcd.getPnr());
                musikcddto.setTitel(musikcd.getProdukt().getTitel());
                musikcddto.setVerkaufsrang(musikcd.getProdukt().getVerkaufsrang());
                musikcddto.setBild(musikcd.getProdukt().getBild());
                musikcddto.setRating(musikcd.getProdukt().getRating());
                musikcddto.setKünstler(kuenstlerRepository.findKuenstlerByPnr(pnr));
                musikcddto.setErscheinungsdatum(musikcd.getErscheinungsdatum());
                musikcddto.setLabels(labelRepository.findLabelNameByPnr(pnr));
                musikcddto.setSongs(songRepository.findSongNameByPnr(pnr));
                return musikcddto;
            }
            Dvd dvd = dvdRepository.findDvdByPnr(pnr);
            if (dvd != null) {
                DvdDTO dvdDTO = new DvdDTO();
                dvdDTO.setPnr(dvd.getPnr());
                dvdDTO.setTitel(dvd.getProdukt().getTitel());
                dvdDTO.setVerkaufsrang(dvd.getProdukt().getVerkaufsrang());
                dvdDTO.setBild(dvd.getProdukt().getBild());
                dvdDTO.setRating(dvd.getProdukt().getRating());
                dvdDTO.setRegioncode(dvd.getRegioncode());
                dvdDTO.setFormat(dvd.getFormat());
                dvdDTO.setLaufzeit(dvd.getLaufzeit());
                dvdDTO.setDirectors(directorRepository.findDirectorNameByPnr(pnr));
                dvdDTO.setCreators(creatorRepository.findCreatorNameByPnr(pnr));
                dvdDTO.setActors(actorRepository.findActorNameByPnr(pnr));
                return dvdDTO;
            }
            Produkt produkt = produktRepository.findProduktByPnr(pnr);
            if (produkt == null) {
                throw new IllegalArgumentException("Produkt mit PNR " + pnr + " nicht gefunden.");
            }
            ProduktDTO produktDTO = new ProduktDTO(
                    produkt.getPnr(),
                    produkt.getTitel(),
                    produkt.getVerkaufsrang(),
                    produkt.getBild(),
                    produkt.getRating());
            return produktDTO;
        });
    }

    @Override
    public List<Produkt> getProducts(String pattern) {
        if (pattern == null) {
            List<Produkt> allProducts = produktRepository.findAll();
            return allProducts;
        } else {
            List<Produkt> produkteByPattern = produktRepository.findProductsByPattern(pattern);
            if (produkteByPattern == null || produkteByPattern.isEmpty()) {
                throw new IllegalArgumentException("Keine Produkte gefunden.");
            }
            return produkteByPattern;
        }
    }

    @Override
    public List<KategorieDTO> getCategorieTree(String pnr) {
        // 1. Hole alle relevanten Kategorien per CTE-Native-Query
        List<Kategorie> flatList = kategorieRepository.findCategoryTreeForProduct(pnr);

        // 2. Wandle jede Entity in ein KategorieDTO um und speichere in Map
        Map<Integer, KategorieDTO> dtoMap = flatList.stream()
                .map(cat -> new KategorieDTO(cat.getId(), cat.getName()))
                .collect(Collectors.toMap(
                        KategorieDTO::getKategorieid,
                        Function.identity(),
                        (existing, duplicate) -> existing));

        // 3. Füge jedes DTO seinem Eltern-Knoten hinzu (sofern dieser auch im Ergebnis ist)
        List<KategorieDTO> roots = new ArrayList<>();
        for (Kategorie kat : flatList) {
            KategorieDTO node = dtoMap.get(kat.getId());
            Kategorie parent = kat.getOberkategorieid();
            if (parent != null && dtoMap.containsKey(parent.getId())) {
                dtoMap.get(parent.getId()).getChildren().add(node);
            } else {
                // Keine Oberkategorie oder Oberkategorie nicht im Ergebnis → Wurzel
                roots.add(node);
            }
        }

        return roots.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<KategorieDTO> getFullCategoryTree() {
        //baum berechnen
        List<Kategorie> flatList = kategorieRepository.findFullCategoryTree();

        //kategorien auf dtos mappen (wie oben)
        Map<Integer, KategorieDTO> dtoMap = flatList.stream()
                .map(cat -> new KategorieDTO(cat.getId(), cat.getName()))
                .collect(Collectors.toMap(
                        KategorieDTO::getKategorieid,
                        Function.identity(),
                        (existing, duplicate) -> existing));

        //baumstruktur bauen (wie oben)
        List<KategorieDTO> roots = new ArrayList<>();
        for (Kategorie kat : flatList) {
            KategorieDTO node = dtoMap.get(kat.getId());
            Kategorie parent = kat.getOberkategorieid();
            if (parent != null && dtoMap.containsKey(parent.getId())) {
                dtoMap.get(parent.getId()).getChildren().add(node);
            } else {
                roots.add(node);
            }
        }

        return roots.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<ProduktDTO> getProductsByCategoryPath(String kategoriePath) {
        if (kategoriePath == null || kategoriePath.isEmpty()) {
            throw new IllegalArgumentException("Kategorie-Pfad darf nicht leer sein.");
        }
        // Konvertiere den Pfad in eine Liste von Kategorien
        String[] pathParts = kategoriePath.split("/");
        String kategorieName = pathParts[pathParts.length - 1];

        // 1. Hole die Kategorie anhand des Pfades
        Kategorie kategorie = kategorieRepository.findByKategorieName(kategorieName);
        if (kategorie == null) {
            throw new IllegalArgumentException("Kategorie mit Pfad " + kategoriePath + " nicht gefunden.");
        }

        // 2. Hole alle Produkte in dieser Kategorie und allen Unterkategorien
        List<Produkt> produkte = produktRepository.findProdukteByKategorie(kategorie);
        if (produkte == null || produkte.isEmpty()) {
            return new ArrayList<>(); // Keine Produkte gefunden, leere Liste zurückgeben
        }

        // 3. Konvertiere Produkte in ProduktDTOs
        List<ProduktDTO> produktDTOs = produkte.stream()
                .map(produkt -> new ProduktDTO(
                        produkt.getPnr(),
                        produkt.getTitel(),
                        produkt.getVerkaufsrang(),
                        produkt.getBild(),
                        produkt.getRating()))
                .toList();

        return produktDTOs;
    }

    @Override
    public List<Produkt> getTopProducts(int lim) {
        if (lim <= 0) {
            throw new IllegalArgumentException("Limit muss größer als 0 sein.");
        }
        List<Produkt> topProducts = produktRepository.findTopProducts(lim);
        if (topProducts == null || topProducts.isEmpty()) {
            throw new IllegalArgumentException("Keine Top-Produkte gefunden.");
        }
        return topProducts;
    }

    @Override
    public List<Produkt> getSimilarCheaperProducts(String pnr) {
        Produkt produkt = produktRepository.findProduktByPnr(pnr);
        if (produkt == null) {
            throw new IllegalArgumentException("Produkt mit PNR " + pnr + " nicht gefunden.");
        }
        List<Produkt> similarCheaperProducts = produktRepository.findSimilarCheaperProducts(pnr);
        if (similarCheaperProducts == null || similarCheaperProducts.isEmpty()) {
            return new ArrayList<>(); // Keine ähnlichen, günstigeren Produkte gefunden, leere Liste zurückgeben
        }
        return similarCheaperProducts;
    }

    @Override
    public List<RezensionDTO> getProductReviews(String pnr) {
        List<Rezension> reviewsPerProduct = rezensionRepository.findAllByPnr(pnr);
        if (reviewsPerProduct == null || reviewsPerProduct.isEmpty()) {
            return new ArrayList<>(); // Keine Rezensionen gefunden, leere Liste zurückgeben
        }
        // Konvertiere Rezensionen in RezensionDTOs
        List<RezensionDTO> reviewsPerProductDTO = reviewsPerProduct.stream()
                .map(rezension -> new RezensionDTO(
                        rezension.getId(),
                        rezension.getProdukt().getPnr(),
                        rezension.getProdukt().getTitel(),
                        rezension.getKunde().getNutzername(),
                        rezension.getBewertung(),
                        rezension.getRezension()))
                .toList();
        return reviewsPerProductDTO;
    }

    @Override
    public Rezension addNewReview(RezensionDTO rezensionDTO) {
        Produkt produkt = produktRepository.findProduktByPnr(rezensionDTO.getPnr());
        if (produkt == null) {
            throw new IllegalArgumentException("Produkt mit PNR " + rezensionDTO.getPnr() + " nicht gefunden.");
        }
        Kunde kunde = kundeRepository.findKundeByNutzername(rezensionDTO.getNutzername());
        if (kunde == null) {
            Kunde newKunde = new Kunde();
            newKunde.setNutzername(rezensionDTO.getNutzername());
            kundeRepository.save(newKunde);
            kunde = newKunde; // Setze den neuen Kunden als aktuellen Kunden
        }
        Rezension newRezension = new Rezension();
        newRezension.setProdukt(produkt);
        newRezension.setKunde(kunde);
        newRezension.setBewertung(rezensionDTO.getBewertung());
        newRezension.setRezension(rezensionDTO.getRezension());
        rezensionRepository.save(newRezension);
        return newRezension;
    }

    @Override
    public List<KundeDTO> getTrolls(double grenzwertRating) {
        List<Object[]> resultQuery = kundeRepository.findTrollsKunden(grenzwertRating);
        List<KundeDTO> trolls = new ArrayList<>();

        for (Object[] row : resultQuery) {
            Kunde kunde = (Kunde) row[0];
            Double durchschnittlichesRating = ((Number) row[1]).doubleValue();
            KundeDTO kundeDTO = new KundeDTO(kunde.getNutzername(), durchschnittlichesRating);
            trolls.add(kundeDTO);
        }

        return trolls.isEmpty() ? new ArrayList<>() : trolls;
    }

    @Override
    public List<AngebotDTO> getOffers(String pnr) {
        Produkt produkt = produktRepository.findProduktByPnr(pnr);
        if (produkt == null) {
            throw new IllegalArgumentException("Produkt mit PNR " + pnr + " nicht gefunden.");
        }
        List<Angebot> angebote = angebotRepository.findOffersByPnr(pnr);
        if (angebote == null || angebote.isEmpty()) {
            return new ArrayList<>(); // Keine Angebote gefunden, leere Liste zurückgeben
        }
        // Konvertiere Angebote in AngebotDTOs
        List<AngebotDTO> angeboteDTO = angebote.stream()
                .map(angebot -> new AngebotDTO(
                        angebot.getId(),
                        angebot.getProdukt().getPnr(),
                        angebot.getFiliale().getId(),
                        angebot.getFiliale().getName(),
                        angebot.getZustand(),
                        angebot.getPreis(),
                        angebot.getWaehrung()
                ))
                .toList();
        return angeboteDTO;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
