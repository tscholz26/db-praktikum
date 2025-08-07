package com.example.backendDBP;

import com.example.backendDBP.controller.KategorieController;
import com.example.backendDBP.DTOs.KategorieDTO;
import com.example.backendDBP.DTOs.ProduktDTO;
import com.example.backendDBP.services.KatalogService;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KategorieControllerTest {

    @Mock
    private KatalogService katalogService;

    @Mock
    private SessionFactory sessionFactory;

    @InjectMocks
    private KategorieController kategorieController;

    @Test
    void getKategorieBaum_Success() {
        // Arrange
        String pnr = "B00005RSCH";
        List<KategorieDTO> expectedKategorien = Arrays.asList(new KategorieDTO(), new KategorieDTO());

        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getCategorieTree(pnr)).thenReturn(expectedKategorien);

        // Act
        ResponseEntity<?> response = kategorieController.getKategorieBaum(pnr);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedKategorien, response.getBody());
        verify(katalogService).getCategorieTree(pnr);
    }

    @Test
    void getKategorieBaum_EmptyPnr() {
        // Arrange
        String pnr = "";
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);

        // Act
        ResponseEntity<?> response = kategorieController.getKategorieBaum(pnr);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Produktnummer (pnr) darf nicht leer sein.", response.getBody());
        verify(katalogService, never()).getCategorieTree(anyString());
    }

    @Test
    void getKategorieBaum_NullPnr() {
        // Arrange
        String pnr = null;
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);

        // Act
        ResponseEntity<?> response = kategorieController.getKategorieBaum(pnr);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Produktnummer (pnr) darf nicht leer sein.", response.getBody());
        verify(katalogService, never()).getCategorieTree(anyString());
    }

    @Test
    void getKategorieBaum_DatabaseNotInitialized() {
        // Arrange
        String pnr = "B00005RSCH";
        when(katalogService.getSessionFactory()).thenReturn(null);

        // Act
        ResponseEntity<?> response = kategorieController.getKategorieBaum(pnr);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Datenbank ist nicht initialisiert"));
        verify(katalogService, never()).getCategorieTree(anyString());
    }

    @Test
    void getKategorieBaum_ServiceException() {
        // Arrange
        String pnr = "B00005RSCH";
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getCategorieTree(pnr)).thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<?> response = kategorieController.getKategorieBaum(pnr);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Fehler beim Abrufen des Kategoriebaums"));
    }

    @Test
    void getKompletterKategorieBaum_Success() {
        // Arrange
        List<KategorieDTO> expectedKategorien = Arrays.asList(new KategorieDTO(), new KategorieDTO());

        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getFullCategoryTree()).thenReturn(expectedKategorien);

        // Act
        ResponseEntity<?> response = kategorieController.getKompletterKategorieBaum();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedKategorien, response.getBody());
        verify(katalogService).getFullCategoryTree();
    }

    @Test
    void getKompletterKategorieBaum_DatabaseNotInitialized() {
        // Arrange
        when(katalogService.getSessionFactory()).thenReturn(null);

        // Act
        ResponseEntity<?> response = kategorieController.getKompletterKategorieBaum();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Datenbank ist nicht initialisiert"));
        verify(katalogService, never()).getFullCategoryTree();
    }

    @Test
    void getKompletterKategorieBaum_ServiceException() {
        // Arrange
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getFullCategoryTree()).thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<?> response = kategorieController.getKompletterKategorieBaum();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Fehler beim Abrufen des kompletten Kategoriebaums"));
    }

    @Test
    void getProdukteInKategorie_Success() {
        // Arrange
        String kategoriePfad = "Formate/Limited Editions/TV-Werbung";
        List<ProduktDTO> expectedProdukte = Arrays.asList(new ProduktDTO(), new ProduktDTO());

        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getProductsByCategoryPath(kategoriePfad)).thenReturn(expectedProdukte);

        // Act
        ResponseEntity<?> response = kategorieController.getProdukteInKategorie(kategoriePfad);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProdukte, response.getBody());
        verify(katalogService).getProductsByCategoryPath(kategoriePfad);
    }

    @Test
    void getProdukteInKategorie_EmptyPath() {
        // Arrange
        String kategoriePfad = "";
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);

        // Act
        ResponseEntity<?> response = kategorieController.getProdukteInKategorie(kategoriePfad);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Kategorie-Pfad darf nicht leer sein.", response.getBody());
        verify(katalogService, never()).getProductsByCategoryPath(anyString());
    }

    @Test
    void getProdukteInKategorie_NullPath() {
        // Arrange
        String kategoriePfad = null;
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);

        // Act
        ResponseEntity<?> response = kategorieController.getProdukteInKategorie(kategoriePfad);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Kategorie-Pfad darf nicht leer sein.", response.getBody());
        verify(katalogService, never()).getProductsByCategoryPath(anyString());
    }

    @Test
    void getProdukteInKategorie_DatabaseNotInitialized() {
        // Arrange
        String kategoriePfad = "Formate/Limited Editions";
        when(katalogService.getSessionFactory()).thenReturn(null);

        // Act
        ResponseEntity<?> response = kategorieController.getProdukteInKategorie(kategoriePfad);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Datenbank ist nicht initialisiert"));
        verify(katalogService, never()).getProductsByCategoryPath(anyString());
    }

    @Test
    void getProdukteInKategorie_ServiceException() {
        // Arrange
        String kategoriePfad = "Formate/Limited Editions";
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getProductsByCategoryPath(kategoriePfad)).thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<?> response = kategorieController.getProdukteInKategorie(kategoriePfad);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Fehler beim Abrufen der Produkte in der Kategorie"));
    }
}
