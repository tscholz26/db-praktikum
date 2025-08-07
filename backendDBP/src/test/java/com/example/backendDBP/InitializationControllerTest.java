package com.example.backendDBP;

import com.example.backendDBP.controller.InitializationController;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InitializationControllerTest {

    @Mock
    private KatalogService katalogService;

    @Mock
    private SessionFactory sessionFactory;

    @InjectMocks
    private InitializationController initializationController;

    @Test
    void init_Success() {
        // Arrange
        doNothing().when(katalogService).init();

        // Act
        ResponseEntity<String> response = initializationController.init();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Datenbankverbindung erfolgreich hergestellt", response.getBody());
        verify(katalogService).init();
    }

    @Test
    void init_Exception() {
        // Arrange
        doThrow(new RuntimeException("Connection error")).when(katalogService).init();

        // Act
        ResponseEntity<String> response = initializationController.init();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Fehler beim Aufbau der Datenbankverbindung"));
    }

    @Test
    void finish_Success() {
        // Arrange
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        doNothing().when(katalogService).finish();

        // Act
        ResponseEntity<String> response = initializationController.finish();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Datenbankverbindung erfolgreich geschlossen", response.getBody());
        verify(katalogService).finish();
    }

    @Test
    void finish_NoActiveConnection() {
        // Arrange
        when(katalogService.getSessionFactory()).thenReturn(null);

        // Act
        ResponseEntity<String> response = initializationController.finish();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Keine aktive Datenbankverbindung vorhanden", response.getBody());
        verify(katalogService, never()).finish();
    }

    @Test
    void finish_ConnectionClosed() {
        // Arrange
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(true);

        // Act
        ResponseEntity<String> response = initializationController.finish();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Keine aktive Datenbankverbindung vorhanden", response.getBody());
        verify(katalogService, never()).finish();
    }

    @Test
    void finish_Exception() {
        // Arrange
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);
        doThrow(new RuntimeException("Close error")).when(katalogService).finish();

        // Act
        ResponseEntity<String> response = initializationController.finish();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Fehler beim Schließen der Datenbankverbindung"));
    }

    @Test
    void getDatabaseStatus_NotInitialized() {
        // Arrange
        when(katalogService.getSessionFactory()).thenReturn(null);

        // Act
        ResponseEntity<String> response = initializationController.getDatabaseStatus();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status: Nicht initialisiert", response.getBody());
    }

    @Test
    void getDatabaseStatus_Connected() {
        // Arrange
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(false);

        // Act
        ResponseEntity<String> response = initializationController.getDatabaseStatus();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status: Verbunden", response.getBody());
    }

    @Test
    void getDatabaseStatus_Closed() {
        // Arrange
        when(katalogService.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.isClosed()).thenReturn(true);

        // Act
        ResponseEntity<String> response = initializationController.getDatabaseStatus();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status: Geschlossen", response.getBody());
    }

    @Test
    void getDatabaseStatus_Exception() {
        // Arrange
        when(katalogService.getSessionFactory()).thenThrow(new RuntimeException("Status error"));

        // Act
        ResponseEntity<String> response = initializationController.getDatabaseStatus();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Fehler beim Abrufen des Datenbankstatus"));
    }
}
