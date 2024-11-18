package com.example.helloworldjfxtemplate;

import com.example.helloworldjfxtemplate.helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;

/**
 * The Main Application, which creates the Stage and loads the login form.
 */
public class MainApplication extends Application {
    public static ZoneId zoneId;
    public static Locale locale;


    /**
     * @param stage The stage to load a scene within.
     * @throws IOException Exception thrown in cases where the FXML file is unreachable.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Log In");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method, launches the application. Sets the zoneId and locale to the relevant system defaults.
     * @param args Additional arguments given on execution.
     */
    public static void main(String[] args) {
        zoneId = ZoneId.systemDefault();
        locale = Locale.getDefault();
        JDBC.makeConnection();
        launch();
        JDBC.closeConnection();
    }
}