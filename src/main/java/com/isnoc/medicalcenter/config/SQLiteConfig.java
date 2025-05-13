package com.isnoc.medicalcenter.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * SQLite configuration to ensure database file exists
 */
@Configuration
public class SQLiteConfig {
    
    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    
    /**
     * Initialize the SQLite database file if it doesn't exist
     */
    @PostConstruct
    public void initializeDatabase() {
        try {
            // Extract database file path from JDBC URL
            if (jdbcUrl.startsWith("jdbc:sqlite:")) {
                String dbFilePath = jdbcUrl.substring("jdbc:sqlite:".length());
                
                // Create database directory if it doesn't exist
                Path dbPath = Paths.get(dbFilePath);
                File parentDir = dbPath.getParent() != null ? 
                        dbPath.getParent().toFile() : new File(".");
                
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                
                // Check if the file exists
                File dbFile = dbPath.toFile();
                if (!dbFile.exists()) {
                    // Create an empty SQLite database by establishing a connection
                    Class.forName("org.sqlite.JDBC");
                    Connection conn = DriverManager.getConnection(jdbcUrl);
                    if (conn != null) {
                        System.out.println("Created database file: " + dbFilePath);
                        conn.close();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error initializing SQLite database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
