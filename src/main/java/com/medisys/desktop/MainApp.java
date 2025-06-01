package com.medisys.desktop;

import com.medisys.desktop.config.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class MainApp extends Application {
    private static AnnotationConfigApplicationContext context;

    @Override
    public void init() {
        // Initialize Spring context
        context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Initialize database with Flyway
        // TODO: Fix Flyway compatibility with PostgreSQL 16.9
        // Flyway flyway = context.getBean(Flyway.class);
        // flyway.migrate();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        loader.setControllerFactory(context::getBean);
        Scene scene = new Scene(loader.load(), 400, 300);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        primaryStage.setTitle("MediSys - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Close Spring context
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}