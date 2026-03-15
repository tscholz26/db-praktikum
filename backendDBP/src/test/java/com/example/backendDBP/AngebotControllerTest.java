package com.example.backendDBP;

import com.example.backendDBP.controller.AngebotController;
import com.example.backendDBP.DTOs.AngebotDTO;
import com.example.backendDBP.services.KatalogService;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
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
class AngebotControllerTest {

    @Mock
    private KatalogService katalogService;

    @Mock
    private SessionFactory sessionFactory;

    @InjectMocks
    private AngebotController angebotController;

    @Test
    void getAngebote_Success() {
        // Arrange
        String pnr = "B00005RSCH";
        List<AngebotDTO> expectedAngebote = Arrays.asList(
            new AngebotDTO(), new AngebotDTO()
        );

        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getOffers(pnr)).thenReturn(expectedAngebote);

        // Act
        ResponseEntity<?> response = angebotController.getAngebot(pnr);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAngebote, response.getBody());
        verify(katalogService).getOffers(pnr);
    }

    @Test
    void getAngebote_DatabaseNotInitialized() {
        // Arrange
        String pnr = "B00005RSCH";
        when(katalogService.getSessionFactory()).thenReturn(null);

        // Act
        ResponseEntity<?> response = angebotController.getAngebot(pnr);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Datenbank ist nicht initialisiert"));
        verify(katalogService, never()).getOffers(anyString());
    }

    @Test
    void getAngebote_DatabaseClosed() {
        // Arrange
        String pnr = "B00005RSCH";
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(true);

        // Act
        ResponseEntity<?> response = angebotController.getAngebot(pnr);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Datenbank ist nicht initialisiert"));
        verify(katalogService, never()).getOffers(anyString());
    }

    @Test
    void getAngebote_ServiceException() {
        // Arrange
        String pnr = "B00005RSCH";
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getOffers(pnr)).thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<?> response = angebotController.getAngebot(pnr);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Fehler beim Abrufen der Angebote"));
    }
}
