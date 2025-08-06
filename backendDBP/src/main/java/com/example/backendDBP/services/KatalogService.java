package com.example.backendDBP.services;

import com.example.backendDBP.BackendDbpApplication;
import com.example.backendDBP.DTOs.*;
import com.example.backendDBP.api.MediastoreServiceAPI;
import com.example.backendDBP.models.*;
import com.example.backendDBP.repositories.*;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
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



    @Autowired
    public KatalogService(ProduktRepository produktRepository, RezensionRepository rezensionRepository,
                          KundeRepository kundeRepository, AngebotRepository angebotRepository, KategorieRepository kategorieRepository,
                          BuchRepository buchRepository, BuchVerlagRepository buchVerlagRepository, AutorRepository autorRepository,
                          MusikCdRepository musikCdRepository, KuenstlerRepository kuenstlerRepository, LabelRepository labelRepository, SongRepository songRepository,
                          DvdRepository dvdRepository, DirectorRepository directorRepository, CreatorRepository creatorRepository, ActorRepository actorRepository) {
        this.angebotRepository = angebotRepository;
        this.kundeRepository = kundeRepository;
        this.rezensionRepository = rezensionRepository;
        this.produktRepository = produktRepository;
        this.kategorieRepository = kategorieRepository;
        this.buchRepository = buchRepository;
        this.buchVerlagRepository = buchVerlagRepository;
        this.autorRepository = autorRepository;
        this.musikCdRepository = musikCdRepository;
        this.kuenstlerRepository = kuenstlerRepository;
        this.labelRepository = labelRepository;
        this.songRepository = songRepository;
        this.dvdRepository = dvdRepository;
        this.creatorRepository = creatorRepository;
        this.directorRepository = directorRepository;
        this.actorRepository = actorRepository;
    }

    @Override
    public String HelloWorld() {
        return "Hello World!";
    }

    @Override
    public void init() {
        if (this.sessionFactory != null && !this.sessionFactory.isClosed()) {
            throw new IllegalStateException("Es existiert bereits eine offene Session");
        }

        // application.properties ins Properties-Objekt laden
        Properties properties = new Properties();
        try (InputStream in = BackendDbpApplication.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (in == null) {
                System.err.println("ERROR: application.properties nicht gefunden im Classpath!");
                System.exit(1);
            }
            properties.load(in);
        } catch (IOException e) {
            System.err.println("ERROR beim Laden der application.properties: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // Hibernate-Konfiguration anlegen
        Configuration hibernateConfig = new Configuration();

        // Lese die Konfigurationseigenschaften aus dem Properties-Objekt
        String url = properties.getProperty("spring.datasource.url");
        String user = properties.getProperty("spring.datasource.username");
        String pass = properties.getProperty("spring.datasource.password");
        String driver = properties.getProperty("spring.datasource.driver-class-name");
        String dialect = properties.getProperty("spring.jpa.database-platform");
        String hbm2ddl = properties.getProperty("spring.jpa.hibernate.ddl-auto");

        // Setze die Konfigurationseigenschaften
        hibernateConfig.setProperty("hibernate.connection.driver_class", driver);
        hibernateConfig.setProperty("hibernate.connection.url", url);
        hibernateConfig.setProperty("hibernate.connection.username", user);
        hibernateConfig.setProperty("hibernate.connection.password", pass);
        hibernateConfig.setProperty("hibernate.dialect", dialect);
        hibernateConfig.setProperty("hibernate.hbm2ddl.auto", hbm2ddl);


        // Annotated Entity-Klassen registrieren
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Produkt.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Kategorie.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Rezension.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Kunde.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Angebot.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Actor.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Angebot.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Autor.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Dvd.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Buch.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.BuchVerlag.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Creator.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Director.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Errordata.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Filiale.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Kauf.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Kuenstler.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Label.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.MusikCd.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.ProduktAehnlichkeit.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.ProduktKategorie.class);
        hibernateConfig.addAnnotatedClass(com.example.backendDBP.models.Song.class);

        //  ServiceRegistry aufbauen
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(hibernateConfig.getProperties())
                .build();

        //  Session erzeugen
        this.sessionFactory = hibernateConfig.buildSessionFactory(registry);
        System.out.println("[INFO] Session erzeugt");
    }

    public void finish() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("[INFO] Session wurde erfolgreich geschlossen.");
        } else {
            System.out.println("[INFO] Session war bereits geschlossen oder nicht initialisiert.");
        }
    }


    @Override
    public ProduktDTO getProduct(String pnr) {
        Buch buch = buchRepository.findBuchByPnr(pnr);
        if (buch != null) {
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
            musikcddto.setKuenstler(kuenstlerRepository.findKuenstlerByPnr(pnr));
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
    public List<Produkt> getProductsByCategoryPath(List<Kategorie> kategoriePath) {
        // Implementiere die Logik, um Produkte anhand eines Kategoriepfads zu erhalten
        return null; // Platzhalter, implementiere die Logik hier
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
        List<Object> resultQuery = kundeRepository.findTrollsKunden(grenzwertRating);
        List<KundeDTO> trolls = new ArrayList<>();
        for (Object obj : resultQuery) {
            Object[] row = (Object[]) obj;
            Kunde kunde = (Kunde) row[0];
            Double durchschnittlichesRating = (Double) row[1];
            KundeDTO kundeDTO = new KundeDTO(kunde.getNutzername(), durchschnittlichesRating);
            trolls.add(kundeDTO);
        }
        if (trolls.isEmpty()) {
            return new ArrayList<>(); // Keine Troll-Kunden gefunden, leere Liste zurückgeben
        } else {
            return trolls;
        }
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

}
