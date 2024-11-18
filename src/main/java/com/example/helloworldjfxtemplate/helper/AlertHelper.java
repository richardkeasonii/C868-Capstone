package com.example.helloworldjfxtemplate.helper;

import javafx.scene.control.Alert;

/**
 * Helper class that handles generating alerts.
 */
public class AlertHelper {

    /**
     * Shows a normal information alert.
     * @param title The title of the alert.
     * @param text The main text of the alert.
     */
    public static void showAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * Shows an error alert.
     * @param title The title of the alert.
     * @param text The main text of the alert.
     */
    public static void showError(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }


}
