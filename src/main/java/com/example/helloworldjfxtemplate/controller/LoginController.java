package com.example.helloworldjfxtemplate.controller;

import com.example.helloworldjfxtemplate.DAO.UserDaoImpl;
import com.example.helloworldjfxtemplate.MainApplication;
import com.example.helloworldjfxtemplate.helper.AlertHelper;
import com.example.helloworldjfxtemplate.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Handles the Login function of the first scene. Validates user input based on given information in the database.
 */
public class LoginController implements Initializable {
    @FXML
    public TextField usernameField;
    @FXML
    public TextField passwordField;
    @FXML
    public Label loginLabel;
    @FXML
    public Label zoneLabel;
    @FXML
    public Button submitButton;
    private static String username;
    private String password;
    boolean submittable;
    boolean isFrench = false;


    /**
     * Gathers the username string from the TextField and only checks whether the username field is not populated.
     * Gives an alert, which will be in French if the user's system language is French.
     */
    public void onClickUsername() {
        try {
            username = usernameField.getText();
            if (username.isBlank()) {
                throw new Exception();
            }
        } catch (Exception e) {
            if (isFrench) {
                AlertHelper.showError("Erreur", "Le champ nom d'utilisateur doit être renseigné.");
            } else {
                AlertHelper.showError("Error", "The Username field must be populated.");
            }
            submittable = false;
        }
    }

    /**
     * Gathers the password string from the TextField and only checks whether the password field is not populated.
     * Gives an alert, which will be in French if the user's system language is French.
     */
    public void onClickPassword() {
        System.out.println("Entered Password");
        try {
            password = passwordField.getText();
            if (password.isBlank()) {
                throw new Exception();
            }
        } catch (Exception e) {
            if (isFrench) {
                AlertHelper.showError("Erreur", "Le champ mot de passe doit être renseigné.");
            } else {
                AlertHelper.showError("Error", "The Password field must be populated.");
            }
            submittable = false;
        }
    }

    /**
     * Handles the validation of the username and password, as well as logging the failure or success of the user to
     * log in to the program. Displays alerts if the password or username is incorrect, and displays those alerts in
     * French is the user's system language is French.
     *
     * @param actionEvent ActionEvent given by the Submit button which is used to navigate to the main-menu FXML form.
     * @throws SQLException Exception thrown by the UserDaoImpl if the User does not exist.
     */
    public void onClickSubmit(ActionEvent actionEvent) throws SQLException {
        submittable = true;
        String log;
        onClickUsername();
        onClickPassword();
        if (submittable) {
            User current = UserDaoImpl.getUser(username);
            if (current != null) {
                if (current.getPassword().equals(password)) {
                    log = "Username: " + username + "\tPassword: " + password + "\tSuccessful Login.";
                    logToFile(log);
                    try {
                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/main-menu.fxml")));
                        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        primaryStage.setTitle("Main Menu");
                        primaryStage.setScene(new Scene(root));
                        primaryStage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    log = "Username: " + username + "\tPassword: " + password + "\tFailure to log in: Wrong Password.";
                    logToFile(log);
                    if (isFrench) {
                        AlertHelper.showError("Erreur", "Le nom d'utilisateur et le mot de passe ne correspondent pas.");
                    } else {
                        AlertHelper.showError("Error", "The Username and Password do not match.");
                    }
                }
            } else {
                log = "Username: " + username + "\tPassword: " + password + "\tFailure to log in: Wrong Username.";
                logToFile(log);
                if (isFrench) {
                    AlertHelper.showError("Erreur", "Nom d'utilisateur invalide.");
                } else {
                    AlertHelper.showError("Error", "Invalid Username.");
                }
            }
        }
    }

    /**
     * Displays the user's current location in a Label, and if the user's System Language is French, sets the
     * appropriate Strings to their French equivalents and sets the isFrench boolean for reference in alerts.
     *
     * @param url            The URL of the login FXML file.
     * @param resourceBundle Used for localized resources like language files or images.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String display = MainApplication.zoneId.toString();
        zoneLabel.setText("Location: " + display);
        if (MainApplication.locale.getLanguage().equals(Locale.FRENCH.getLanguage())) {
            isFrench = true;
            zoneLabel.setText("Emplacement: " + display);
            loginLabel.setText("Se Connecter");
            submitButton.setText("Soumettre");
            usernameField.setPromptText("Nom d'utilisateur");
            passwordField.setPromptText("Mot de passe");
        }
    }

    /**
     * Handles the recording of user logs in a login_activity.txt file within the root folder.
     *
     * @param log User activity logs given by the Save functionality.
     */
    public static void logToFile(String log) {
        try {
            Path logFilePath = Paths.get("login_activity.txt");

            String logEntry = LocalDateTime.now() + ": " + log + System.lineSeparator();

            Files.write(logFilePath, logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to reference which user is creating or updating a given Customer or Appointment.
     *
     * @return Returns the Username given by the User when they successfully log in.
     */
    public static String getUsername() {
        return username;
    }
}
