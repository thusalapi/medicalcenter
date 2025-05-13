package com.isnoc.medicalcenter.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * SQLite configuration to ensure database file exists
 */
@Configuration
public class SQLiteConfig {

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        String url = properties.getUrl();
        
        // Extract database file path from JDBC URL
        if (url.startsWith("jdbc:sqlite:")) {
            String dbFilePath = url.substring("jdbc:sqlite:".length());
            
            // Create database directory if it doesn't exist
            Path dbPath = Paths.get(dbFilePath);
            File parentDir = dbPath.getParent() != null ? 
                    dbPath.getParent().toFile() : new File(".");
            
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            try {
                // Check if the file exists
                File dbFile = dbPath.toFile();
                if (!dbFile.exists()) {
                    // Create an empty file to ensure SQLite can connect
                    dbFile.createNewFile();
                    System.out.println("Created database file: " + dbFilePath);
                }
            } catch (Exception e) {
                System.err.println("Error creating SQLite database file: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        return DataSourceBuilder
                .create()
                .driverClassName(properties.getDriverClassName())
                .url(properties.getUrl())
                .build();
    }
}
