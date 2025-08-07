package com.example.backendDBP;

import com.example.backendDBP.controller.ProduktController;
import com.example.backendDBP.DTOs.ProduktDTO;
import com.example.backendDBP.models.Produkt;
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
class ProduktControllerTest {

    @Mock
    private KatalogService katalogService;

    @Mock
    private SessionFactory sessionFactory;

    @InjectMocks
    private ProduktController produktController;

    @Test
    void getProdukt_Success() {
        // Arrange
        String pnr = "B00005RSCH";
        ProduktDTO expectedProdukt = new ProduktDTO();

        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getProduct(pnr)).thenReturn(expectedProdukt);

        // Act
        ResponseEntity<?> response = produktController.getProdukt(pnr);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProdukt, response.getBody());
        verify(katalogService).getProduct(pnr);
    }

    @Test
    void getProdukt_NotFound() {
        // Arrange
        String pnr = "INVALID";

        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getProduct(pnr)).thenReturn(null);

        // Act
        ResponseEntity<?> response = produktController.getProdukt(pnr);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(katalogService).getProduct(pnr);
    }

    @Test
    void getProdukt_DatabaseNotInitialized() {
        // Arrange
        String pnr = "B00005RSCH";
        when(katalogService.getSessionFactory()).thenReturn(null);

        // Act
        ResponseEntity<?> response = produktController.getProdukt(pnr);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Datenbank ist nicht initialisiert"));
        verify(katalogService, never()).getProduct(anyString());
    }

    @Test
    void getProdukt_IllegalArgumentException() {
        // Arrange
        String pnr = "B00005RSCH";
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getProduct(pnr)).thenThrow(new IllegalArgumentException("Invalid product"));

        // Act
        ResponseEntity<?> response = produktController.getProdukt(pnr);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Produkt nicht gefunden"));
    }

    @Test
    void getProdukte_Success_WithPattern() {
        // Arrange
        String pattern = "DVD";
        List<Produkt> expectedProdukte = Arrays.asList(new Produkt(), new Produkt());

        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getProducts(pattern)).thenReturn(expectedProdukte);

        // Act
        ResponseEntity<?> response = produktController.getProdukte(pattern);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProdukte, response.getBody());
        verify(katalogService).getProducts(pattern);
    }

    @Test
    void getProdukte_Success_WithoutPattern() {
        // Arrange
        List<Produkt> expectedProdukte = Arrays.asList(new Produkt(), new Produkt());

        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getProducts(null)).thenReturn(expectedProdukte);

        // Act
        ResponseEntity<?> response = produktController.getProdukte(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProdukte, response.getBody());
        verify(katalogService).getProducts(null);
    }

    @Test
    void getProdukte_DatabaseNotInitialized() {
        // Arrange
        String pattern = "DVD";
        when(katalogService.getSessionFactory()).thenReturn(null);

        // Act
        ResponseEntity<?> response = produktController.getProdukte(pattern);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Datenbank ist nicht initialisiert"));
        verify(katalogService, never()).getProducts(anyString());
    }

    @Test
    void getTopProdukte_Success() {
        // Arrange
        int lim = 10;
        List<Produkt> expectedProdukte = Arrays.asList(new Produkt(), new Produkt());

        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getTopProducts(lim)).thenReturn(expectedProdukte);

        // Act
        ResponseEntity<?> response = produktController.getTopProdukte(lim);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProdukte, response.getBody());
        verify(katalogService).getTopProducts(lim);
    }

    @Test
    void getTopProdukte_DatabaseNotInitialized() {
        // Arrange
        int lim = 10;
        when(katalogService.getSessionFactory()).thenReturn(null);

        // Act
        ResponseEntity<?> response = produktController.getTopProdukte(lim);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Datenbank ist nicht initialisiert"));
        verify(katalogService, never()).getTopProducts(anyInt());
    }

    @Test
    void getTopProdukte_ServiceException() {
        // Arrange
        int lim = 10;
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getTopProducts(lim)).thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<?> response = produktController.getTopProdukte(lim);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Fehler beim Abrufen der Top-Produkte"));
    }

    @Test
    void getBilligereProdukte_Success() {
        // Arrange
        String pnr = "B00005RSCH";
        List<Produkt> expectedProdukte = Arrays.asList(new Produkt(), new Produkt());

        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getSimilarCheaperProducts(pnr)).thenReturn(expectedProdukte);

        // Act
        ResponseEntity<?> response = produktController.getBilligereProdukte(pnr);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProdukte, response.getBody());
        verify(katalogService).getSimilarCheaperProducts(pnr);
    }

    @Test
    void getBilligereProdukte_DatabaseNotInitialized() {
        // Arrange
        String pnr = "B00005RSCH";
        when(katalogService.getSessionFactory()).thenReturn(null);

        // Act
        ResponseEntity<?> response = produktController.getBilligereProdukte(pnr);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Datenbank ist nicht initialisiert"));
        verify(katalogService, never()).getSimilarCheaperProducts(anyString());
    }

    @Test
    void getBilligereProdukte_ServiceException() {
        // Arrange
        String pnr = "B00005RSCH";
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getSimilarCheaperProducts(pnr)).thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<?> response = produktController.getBilligereProdukte(pnr);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Fehler beim Abrufen der günstigeren Produkte"));
    }
}
