package com.example.backendDBP;

import com.example.backendDBP.services.KatalogService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
public class BackendDbpApplication {

	public static void main(String[] args) {
		// 1) application.properties ins Properties-Objekt laden
		Properties props = new Properties();
		try (InputStream in = BackendDbpApplication.class
				.getClassLoader()
				.getResourceAsStream("application.properties")) {
			if (in == null) {
				System.err.println("ERROR: application.properties nicht gefunden im Classpath!");
				System.exit(1);
			}
			props.load(in);
		} catch (IOException e) {
			System.err.println("ERROR beim Laden der application.properties: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		// 2) KatalogService initialisieren
		KatalogService katalogService = new KatalogService();
		try {
			katalogService.init(props);
			System.out.println("\u001B[32m[SUCCESS] Connected to the database!\u001B[0m");
		} catch (Exception e) {
			System.err.println("[ERROR] Failed to establish connection to the database." + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} finally {
			// Sicherstellen, dass der KatalogService am Ende geschlossen wird
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					katalogService.finish();
					System.out.println("\u001B[32m[SUCCESS] KatalogService finished successfully.\u001B[0m");
				} catch (Exception e) {
					System.err.println("[ERROR] Failed to finish KatalogService: " + e.getMessage());
					e.printStackTrace();
				}
			}));
		}

		// 3) Spring Boot Anwendung starten
		SpringApplication.run(BackendDbpApplication.class, args);
	}
}
