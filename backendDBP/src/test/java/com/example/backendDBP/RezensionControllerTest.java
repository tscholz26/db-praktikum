package com.example.backendDBP;

import com.example.backendDBP.controller.RezensionController;
import com.example.backendDBP.DTOs.RezensionDTO;
import com.example.backendDBP.models.Rezension;
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
class RezensionControllerTest {

    @Mock
    private KatalogService katalogService;

    @Mock
    private SessionFactory sessionFactory;

    @InjectMocks
    private RezensionController rezensionController;

    @Test
    void getRezensionen_Success() {
        // Arrange
        String pnr = "B00005RSCH";
        List<RezensionDTO> expectedRezensionen = Arrays.asList(new RezensionDTO(), new RezensionDTO());

        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getProductReviews(pnr)).thenReturn(expectedRezensionen);

        // Act
        ResponseEntity<?> response = rezensionController.getProduktRezensionen(pnr);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRezensionen, response.getBody());
        verify(katalogService).getProductReviews(pnr);
    }

    @Test
    void getRezensionen_DatabaseNotInitialized() {
        // Arrange
        String pnr = "B00005RSCH";
        when(katalogService.getSessionFactory()).thenReturn(null);

        // Act
        ResponseEntity<?> response = rezensionController.getProduktRezensionen(pnr);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Datenbank ist nicht initialisiert"));
        verify(katalogService, never()).getProductReviews(anyString());
    }

    @Test
    void getRezensionen_DatabaseClosed() {
        // Arrange
        String pnr = "B00005RSCH";
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(true);

        // Act
        ResponseEntity<?> response = rezensionController.getProduktRezensionen(pnr);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Datenbank ist nicht initialisiert"));
        verify(katalogService, never()).getProductReviews(anyString());
    }

    @Test
    void getRezensionen_ServiceException() {
        // Arrange
        String pnr = "B00005RSCH";
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getProductReviews(pnr)).thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<?> response = rezensionController.getProduktRezensionen(pnr);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Fehler beim Abrufen der Rezensionen"));
    }

    @Test
    void addRezension_Success() {
        // Arrange
        RezensionDTO rezensionDTO = new RezensionDTO();
        Rezension expectedResult = new Rezension();

        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.addNewReview(rezensionDTO)).thenReturn(expectedResult);

        // Act
        ResponseEntity<?> response = rezensionController.addRezension(rezensionDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
        verify(katalogService).addNewReview(rezensionDTO);
    }

    @Test
    void addRezension_DatabaseNotInitialized() {
        // Arrange
        RezensionDTO rezensionDTO = new RezensionDTO();
        when(katalogService.getSessionFactory()).thenReturn(null);

        // Act
        ResponseEntity<?> response = rezensionController.addRezension(rezensionDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Datenbank ist nicht initialisiert"));
        verify(katalogService, never()).addNewReview(any(RezensionDTO.class));
    }

    @Test
    void addRezension_DatabaseClosed() {
        // Arrange
        RezensionDTO rezensionDTO = new RezensionDTO();
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(true);

        // Act
        ResponseEntity<?> response = rezensionController.addRezension(rezensionDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Datenbank ist nicht initialisiert"));
        verify(katalogService, never()).addNewReview(any(RezensionDTO.class));
    }

    @Test
    void addRezension_ServiceException() {
        // Arrange
        RezensionDTO rezensionDTO = new RezensionDTO();
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.addNewReview(rezensionDTO)).thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<?> response = rezensionController.addRezension(rezensionDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Fehler beim Hinzufügen der Rezension"));
    }

    @Test
    void addRezension_NullRezension() {
        // Arrange
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.addNewReview(null)).thenThrow(new IllegalArgumentException("Rezension darf nicht null sein"));

        // Act
        ResponseEntity<?> response = rezensionController.addRezension(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Fehler beim Hinzufügen der Rezension"));
    }
}
