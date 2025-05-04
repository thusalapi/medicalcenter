package com.isnoc.medicalcenter;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import java.awt.Desktop;
import java.net.URI;

/**
 * This class serves as a launcher for the application when packaged as an executable JAR/EXE.
 * It extends SpringBootServletInitializer to allow for deployment as a WAR if needed.
 */
public class LauncherApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MedicalcenterApplication.class);
    }

    /**
     * Main method that launches the Spring Boot application and opens a browser window
     */
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(MedicalcenterApplication.class, args);
        
        // After application starts, open the default browser to the application URL
        try {
            // Sleep briefly to allow the application to start up completely
            Thread.sleep(2000);
            
            // Check if Desktop is supported (most modern OS support it)
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                // Open the browser with the app's URL
                Desktop.getDesktop().browse(new URI("http://localhost:8080"));
            }
        } catch (Exception e) {
            System.out.println("Failed to open browser automatically: " + e.getMessage());
            // This is not critical, application will still run even if browser doesn't open
        }
        
        System.out.println("Medical Center Management System is running at http://localhost:8080");
    }
}