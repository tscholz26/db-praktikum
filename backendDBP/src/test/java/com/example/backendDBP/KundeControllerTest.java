package com.example.backendDBP;

import com.example.backendDBP.controller.KundeController;
import com.example.backendDBP.DTOs.KundeDTO;
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
class KundeControllerTest {

    @Mock
    private KatalogService katalogService;

    @Mock
    private SessionFactory sessionFactory;

    @InjectMocks
    private KundeController kundeController;

    @Test
    void getTrollsKunden_Success() {
        // Arrange
        double grenzwertRating = 2.5;
        List<KundeDTO> expectedKunden = Arrays.asList(new KundeDTO(), new KundeDTO());

        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getTrolls(grenzwertRating)).thenReturn(expectedKunden);

        // Act
        ResponseEntity<?> response = kundeController.getTrollsKunden(grenzwertRating);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedKunden, response.getBody());
        verify(katalogService).getTrolls(grenzwertRating);
    }

    @Test
    void getTrollsKunden_DatabaseNotInitialized() {
        // Arrange
        double grenzwertRating = 2.5;
        when(katalogService.getSessionFactory()).thenReturn(null);

        // Act
        ResponseEntity<?> response = kundeController.getTrollsKunden(grenzwertRating);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Datenbank ist nicht initialisiert"));
        verify(katalogService, never()).getTrolls(anyDouble());
    }

    @Test
    void getTrollsKunden_DatabaseClosed() {
        // Arrange
        double grenzwertRating = 2.5;
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(true);

        // Act
        ResponseEntity<?> response = kundeController.getTrollsKunden(grenzwertRating);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Datenbank ist nicht initialisiert"));
        verify(katalogService, never()).getTrolls(anyDouble());
    }

    @Test
    void getTrollsKunden_ServiceException() {
        // Arrange
        double grenzwertRating = 2.5;
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getTrolls(grenzwertRating)).thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<?> response = kundeController.getTrollsKunden(grenzwertRating);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Fehler beim Abrufen der Troll-Kunden"));
    }

    @Test
    void getTrollsKunden_ZeroGrenzwert() {
        // Arrange
        double grenzwertRating = 0.0;
        List<KundeDTO> expectedKunden = Arrays.asList(new KundeDTO());

        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getTrolls(grenzwertRating)).thenReturn(expectedKunden);

        // Act
        ResponseEntity<?> response = kundeController.getTrollsKunden(grenzwertRating);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedKunden, response.getBody());
        verify(katalogService).getTrolls(grenzwertRating);
    }

    @Test
    void getTrollsKunden_NegativeGrenzwert() {
        // Arrange
        double grenzwertRating = -1.0;
        List<KundeDTO> expectedKunden = Arrays.asList();

        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        when(katalogService.getTrolls(grenzwertRating)).thenReturn(expectedKunden);

        // Act
        ResponseEntity<?> response = kundeController.getTrollsKunden(grenzwertRating);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedKunden, response.getBody());
        verify(katalogService).getTrolls(grenzwertRating);
    }
}
