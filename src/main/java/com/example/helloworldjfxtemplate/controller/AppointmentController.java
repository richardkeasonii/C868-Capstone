package com.example.helloworldjfxtemplate.controller;

import com.example.helloworldjfxtemplate.DAO.Query;
import com.example.helloworldjfxtemplate.MainApplication;
import com.example.helloworldjfxtemplate.helper.AlertHelper;
import com.example.helloworldjfxtemplate.helper.JDBC;
import com.example.helloworldjfxtemplate.helper.TimeHelper;
import com.example.helloworldjfxtemplate.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controls the implementation of the Appointment adding and modifying FXML forms.
 * Includes validation for adding and modifying Appointments.
 */
public class AppointmentController implements Initializable {
    public TextField titleField;
    public TextField descField;
    public TextField locationField;
    public TextField typeField;
    public TextField idField;
    public ComboBox<String> contactCombo;
    public Button saveButton;
    public Button cancelButton;
    public TextField userField;
    public TextField endField;
    public TextField startField;
    public TextField customerField;
    private ObservableList<String> contacts = FXCollections.observableArrayList();
    private String title;
    private String description;
    private String location;
    private String type;
    private String contact;
    private int userId;
    private int customerId;
    private LocalDateTime start;
    private LocalDateTime end;
    private static Appointment thisToModify = null;
    private static boolean isModify;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private boolean canSave;


    /**
     * Gathers the title string from the TextField. Shows an alert and prevents saving on failure to gather a string.
     */
    public void onClickTitleField() {
        try {
            title = titleField.getText();
            if (title.isBlank()) {
                throw new Exception();
            }
        } catch (Exception e) {
            AlertHelper.showAlert("Warning", "The Title field must be populated.");
            canSave = false;
        }
    }

    /**
     * Gathers the description string from the TextField. Shows an alert and prevents saving on failure to gather a string.
     */
    public void onClickDescField() {
        try {
            description = descField.getText();
            if (description.isBlank()) {
                throw new Exception();
            }
        } catch (Exception e) {
            AlertHelper.showAlert("Warning", "The Description field must be populated.");
            canSave = false;
        }
    }

    /**
     * Gathers the location string from the TextField. Shows an alert and prevents saving on failure to gather a string.
     */
    public void onClickLocationField() {
        try {
            location = locationField.getText();
            if (location.isBlank()) {
                throw new Exception();
            }
        } catch (Exception e) {
            AlertHelper.showAlert("Warning", "The Location field must be populated.");
            canSave = false;
        }
    }

    /**
     * Gathers the type string from the TextField. Shows an alert and prevents saving on failure to gather a string.
     */
    public void onClickTypeField() {
        try {
            type = typeField.getText();
            if (type.isBlank()) {
                throw new Exception();
            }
        } catch (Exception e) {
            AlertHelper.showAlert("Warning", "The Type field must be populated.");
        }
    }


    /**
     * Gathers the selected contact from the contact ComboBox. Shows an alert and prevents saving if the ComboBox
     * was not populated.
     */
    public void onClickContact() {
        contact = contactCombo.getSelectionModel().getSelectedItem();
        if (contact == null || contact.isBlank()) {
            AlertHelper.showAlert("Warning", "Please choose a Contact to assign the Appointment to.");
            canSave = false;
        }
    }


//    public void onClickUserField() {
//        try {
//            userId = Integer.parseInt(userField.getText());
//            try {
//                String query = "SELECT User_ID FROM users WHERE EXISTS " +
//                        "(SELECT User_ID FROM users WHERE User_ID = ?)";
//                try (PreparedStatement statement = JDBC.getConnection().prepareStatement(query)) {
//                    statement.setInt(1, userId);
//                    try (ResultSet result = statement.executeQuery()) {
//                        if (!result.isClosed() && result.next()) {
//                            // User ID exists, handle as necessary
//                            return;
//                        }
//                    }
//                } catch (SQLException e) {
//                    AlertHelper.showAlert("Error", "User ID does not exist.");
//                    canSave = false;
//                }
//            }
//            } catch (NumberFormatException e) {
//                AlertHelper.showAlert("Warning", "The User ID field must be populated with an integer.");
//                canSave = false;
//            }
//    }

    /**
     * Gathers the user id from the TextField. Shows an alert and prevents saving if the input was not an integer.
     */
    public void onClickUserField() {
        try {
            userId = Integer.parseInt(userField.getText());
            String query = "SELECT User_ID FROM users WHERE EXISTS " +
                    "(SELECT User_ID FROM users WHERE User_ID = ?)";

            try (PreparedStatement statement = JDBC.getConnection().prepareStatement(query)) {
                statement.setInt(1, userId);

                try (ResultSet result = statement.executeQuery()) {
                    if (!result.isClosed() && result.next()) {
                        // User ID exists, handle as necessary
                        return;
                    } else {
                        AlertHelper.showError("Error", "User ID does not exist.");
                        canSave = false;
                    }
                }
            } catch (SQLException e) {
                AlertHelper.showError("Error", "Database error occurred while checking User ID.");
                canSave = false;
            }
        } catch (NumberFormatException e) {
            AlertHelper.showAlert("Warning", "The User ID field must be populated with an integer.");
            canSave = false;
        }
    }

    /**
     * Gathers the start time and date from the TextField. Shows an alert and prevents saving if the time is in
     * an incorrect format or if the time is not within office hours.
     */
    public void onClickStartField() {
        try {
            // Parse the input using the custom formatter
            start = LocalDateTime.parse(startField.getText(), formatter);
            if (!TimeHelper.isInOfficeHours(start)) {
                canSave = false;
                AlertHelper.showError("Error", "Start time is not within the office hours of 8 a.m. to 10 p.m. ET.");
            }
        } catch (DateTimeParseException e) {
            // Handle the error (e.g., show an alert or log the error)
            AlertHelper.showAlert("Warning", "Invalid date and time format for Start time. Please use 'yyyy-MM-dd HH:mm'.");
            canSave = false;
        }
    }

    /**
     * Gathers the end time and date from the TextField. Shows an alert and prevents saving if the time is in
     * an incorrect format or if the time is not within office hours.
     */
    public void onClickEndField() {
        try {
            // Parse the input using the custom formatter
            end = LocalDateTime.parse(endField.getText(), formatter);
            if (!TimeHelper.isInOfficeHours(end)) {
                canSave = false;
                AlertHelper.showAlert("Warning", "End time is not within the office hours of 8 a.m. to 10 p.m. ET.");
            }
        } catch (DateTimeParseException e) {
            AlertHelper.showAlert("Warning", "Invalid date and time format for End time. Please use 'yyyy-MM-dd HH:mm'.");
            canSave = false;
        }
    }

    /**
     * Gathers the customer id from the TextField. Shows an alert and prevents saving if the input was not an integer.
     */
    public void onClickCustomer() {
//        try {
//            customerId = Integer.parseInt(customerField.getText());
//            try {
//                JDBC.makeConnection();
//                Query.makeQuery("SELECT Customer_ID FROM customers WHERE EXISTS " +
//                        "(SELECT Customer_ID FROM customers WHERE Customer_ID = " + customerId + ")");
//                ResultSet result = Query.getResult();
//                if (!result.isClosed() && result.next()) {
//                    return;
//                }
//            } catch (SQLException e) {
//                AlertHelper.showAlert("Error", "Customer ID does not exist.");
//                canSave = false;
//            }
//        } catch (NumberFormatException e) {
//            AlertHelper.showAlert("Warning", "The Customer ID field must be populated with an integer.");
//            canSave = false;
//        }
        try {
            customerId = Integer.parseInt(customerField.getText());
            String query = "SELECT Customer_ID FROM customers WHERE Customer_ID = ?";
            try (PreparedStatement statement = JDBC.getConnection().prepareStatement(query)) {
                statement.setInt(1, customerId);
                try (ResultSet result = statement.executeQuery()) {
                    if (!result.next()) {
                        AlertHelper.showError("Error", "Customer ID does not exist.");
                        canSave = false;
                    }
                }
            } catch (SQLException e) {
                AlertHelper.showError("Error", "Database error occurred while checking Customer ID.");
                canSave = false;
            }
        } catch (NumberFormatException e) {
            AlertHelper.showAlert("Warning", "The Customer ID field must be populated with an integer.");
            canSave = false;
        }
    }

    /**
     * Handles the saving of Appointments created or modified within the form. They are then stored within the database.
     *
     * @param actionEvent This ActionEvent is generated by the save button, and is used for the context to go back to the main form.
     * @throws SQLException Exception thrown in cases where the Query and ResultSet handling fails.
     * @throws IOException  Exception thrown in cases where the destination of the main-menu is inaccessible.
     */
    public void onClickSave(ActionEvent actionEvent) throws SQLException, IOException {
        canSave = true;
        onClickContact();
        onClickDescField();
        onClickEndField();
        onClickStartField();
        onClickLocationField();
        onClickTitleField();
        onClickTypeField();
        onClickUserField();
        onClickCustomer();
        if (canSave) {
            if (end.isBefore(start)) {
                AlertHelper.showAlert("Warning", "The End time for the appointment should be after the Start time.");
                return;
            }



            ZonedDateTime utcStart = start.atZone(MainApplication.zoneId).withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime utcEnd = end.atZone(MainApplication.zoneId).withZoneSameInstant(ZoneId.of("UTC"));

            String utcStartString = utcStart.format(formatter);
            String utcEndString = utcEnd.format(formatter);

//            System.out.println("SELECT Start, End FROM Appointments WHERE Customer_ID = " + customerId + " AND (Start <= '" +
//                    utcEndString + "' AND End >= '" + utcStartString + "')");


//            JDBC.getConnection();
//            Query.makeQuery("SELECT Start, End FROM Appointments WHERE Customer_ID = " + customerId + " AND (Start <= '" +
//                    utcEndString + "' AND End >= '" + utcStartString + "')");
//
//            ResultSet result = Query.getResult();
//            if (!result.isClosed() && result.next()) {
//                AlertHelper.showAlert("Error", "Customer already has appointment during this time.");
//                return;
//            }



            String query = "SELECT Start, End FROM appointments WHERE Customer_ID = ? AND (Start <= ? AND End >= ?)";

            try (PreparedStatement statement = JDBC.getConnection().prepareStatement(query)) {
                statement.setInt(1, customerId);
                statement.setString(2, utcEndString);
                statement.setString(3, utcStartString);

                try (ResultSet result = statement.executeQuery()) {
                    if (!result.isClosed() && result.next()) {
                        AlertHelper.showAlert("Warning", "Customer already has an appointment during this time.");
                        return;
                    }
                }
            } catch (SQLException e) {
                AlertHelper.showError("Error", "There was a database connection error while checking customer appointments.");
                e.printStackTrace();
                return;
            }



//            Query.makeQuery("SELECT Contact_ID FROM Contacts WHERE Contact_Name = '" + contact + "'");
//            result = Query.getResult();
//            result.next();
//            int contactId = result.getInt("Contact_ID");
//            if (isModify) {
//                Query.makeQuery("UPDATE Appointments SET Title = '" + title + "', Description = '" + description +
//                        "', Location = '" + location + "', Type = '" + type + "', Start = '" + utcStartString + "', End = '" +
//                       utcEndString + "', Last_Update = '" + TimeHelper.convertNowToUTC().format(formatter) + "', Last_Updated_By = '" +
//                        LoginController.getUsername() + "', Customer_ID = " + customerId + ", User_ID = " + userId +
//                        ", Contact_ID = " + contactId + "WHERE Appointment_ID = " + thisToModify.getAppointmentId());
//            } else {
//                Query.makeQuery("INSERT INTO appointments VALUES (" + MainController.nextAppointmentId() + ", '" +
//                        title + "', '" + description + "', '" + location + "', '" + type + "', '" + utcStartString + "', '" +
//                        utcEndString + "', '" + TimeHelper.convertNowToUTC().format(formatter) +
//                        "', '" + LoginController.getUsername() + "', '" +
//                        TimeHelper.convertNowToUTC().format(formatter) + "', '" + LoginController.getUsername() + "', " +
//                        customerId + ", " + userId + ", " + contactId + ")");
//            }


            String contactQuery = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?";
            int contactId;

            try (PreparedStatement contactStatement = JDBC.getConnection().prepareStatement(contactQuery)) {
                contactStatement.setString(1, contact);
                try (ResultSet result = contactStatement.executeQuery()) {
                    if (result.next()) {
                        contactId = result.getInt("Contact_ID");
                    } else {
                        AlertHelper.showAlert("Warning", "Contact not found.");
                        return;
                    }
                }
            } catch (SQLException e) {
                AlertHelper.showError("Error", "There was a database connection error while turning contact data into relevant id.");
                return;
            }

            if (isModify) {
                // Update existing appointment
                String updateQuery = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, "
                        + "Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? "
                        + "WHERE Appointment_ID = ?";

                try (PreparedStatement updateStatement = JDBC.getConnection().prepareStatement(updateQuery)) {
                    updateStatement.setString(1, title);
                    updateStatement.setString(2, description);
                    updateStatement.setString(3, location);
                    updateStatement.setString(4, type);
                    updateStatement.setString(5, utcStartString);
                    updateStatement.setString(6, utcEndString);
                    updateStatement.setString(7, TimeHelper.convertNowToUTC().format(formatter));
                    updateStatement.setString(8, LoginController.getUsername());
                    updateStatement.setInt(9, customerId);
                    updateStatement.setInt(10, userId);
                    updateStatement.setInt(11, contactId);
                    updateStatement.setInt(12, thisToModify.getAppointmentId());

                    updateStatement.executeUpdate();
                } catch (SQLException e) {
                    AlertHelper.showError("Error", "There was a database connection error while updating an existing appointment.");
                    return;
                }

            } else {
                // Insert new appointment
                String insertQuery = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, "
                        + "Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement insertStatement = JDBC.getConnection().prepareStatement(insertQuery)) {
                    insertStatement.setInt(1, MainController.nextAppointmentId());
                    insertStatement.setString(2, title);
                    insertStatement.setString(3, description);
                    insertStatement.setString(4, location);
                    insertStatement.setString(5, type);
                    insertStatement.setString(6, utcStartString);
                    insertStatement.setString(7, utcEndString);
                    insertStatement.setString(8, TimeHelper.convertNowToUTC().format(formatter));
                    insertStatement.setString(9, LoginController.getUsername());
                    insertStatement.setString(10, TimeHelper.convertNowToUTC().format(formatter));
                    insertStatement.setString(11, LoginController.getUsername());
                    insertStatement.setInt(12, customerId);
                    insertStatement.setInt(13, userId);
                    insertStatement.setInt(14, contactId);

                    insertStatement.executeUpdate();
                } catch (SQLException e) {
                    AlertHelper.showError("Error", "There was a database connection error while inserting a new appointment.");
                    return;
                }
            }

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/main-menu.fxml")));
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("Main Scene");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }

    /**
     * Handles the Cancel button which allows the user to discard any input on the form and return to the main-menu scene.
     *
     * @param actionEvent ActionEvent given by the cancel button, used to navigate back to the main-menu scene.
     * @throws IOException Exception thrown in cases where the main-menu fxml is unreachable.
     */
    public void onClickCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/main-menu.fxml")));
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Main Scene");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    /**
     * Method to instantiate the ComboBox selection, and in cases where the user is modifying an existing Appointment,
     * the form is pre-filled with the given Appointment's information.
     *
     * @param url            The URL to the FXML file.
     * @param resourceBundle Used for localized resources such as language files and images.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        JDBC.getConnection();
//        Query.makeQuery("SELECT * FROM contacts");
//        ResultSet result = Query.getResult();
//        try {
//            while (result.next()) {
//                contacts.add(result.getString("Contact_Name"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        String sqlStatement = "SELECT * FROM contacts";
        try (PreparedStatement stmt = JDBC.getConnection().prepareStatement(sqlStatement);
             ResultSet result = stmt.executeQuery()) {

            while (result.next()) {
                contacts.add(result.getString("Contact_Name"));
            }

        } catch (SQLException e) {
            AlertHelper.showError("Error", "There was a database connection while populating the Contacts ComboBox.");
            e.printStackTrace();
        }

        contactCombo.setItems(contacts);
        if (isModify) {
            //POPULATE FIELDS HERE
            String startText = thisToModify.getAppointmentStart().toString().replace("T", " ");
            String endText = thisToModify.getAppointmentEnd().toString().replace("T", " ");
            descField.setText(thisToModify.getAppointmentDesc());
            endField.setText(endText);
            startField.setText(startText);
            customerField.setText(((Integer) thisToModify.getCustomerId()).toString());
            locationField.setText(thisToModify.getAppointmentLocation());
            titleField.setText(thisToModify.getAppointmentTitle());
            typeField.setText(thisToModify.getAppointmentType());
            userField.setText(((Integer) thisToModify.getUserId()).toString());
            idField.setText(((Integer) thisToModify.getAppointmentId()).toString());

            int contactId = thisToModify.getContactId();
            String query = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?";
            try (PreparedStatement statement = JDBC.getConnection().prepareStatement(query)) {
                statement.setInt(1, contactId);

                ResultSet result = statement.executeQuery();
                result.next();
                String thisContact = result.getString("Contact_Name");
                contactCombo.getSelectionModel().select(thisContact);
                onClickContact();
            } catch (SQLException e) {
                AlertHelper.showError("Error", "Database error occurred while getting contacts.");
            }

//            try {
//                int contactId = thisToModify.getContactId();
//                Query.makeQuery("SELECT Contact_Name FROM contacts WHERE Contact_ID = " + contactId);
//                result = Query.getResult();
//                result.next();
//                String thisContact = result.getString("Contact_Name");
//                contactCombo.getSelectionModel().select(thisContact);
//                onClickContact();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
        }
    }

    /**
     * Tells the AppointmentController if the user is creating or updating an Appointment.
     * True when the user is modifying an Appointment.
     *
     * @param modify Boolean passed by the MainController.
     */
    public static void setModify(boolean modify) {
        isModify = modify;
    }

    /**
     * Tells the AppointmentController which appointment is to be updated.
     *
     * @param toModify Appointment passed by the MainController.
     */
    public static void setAppointmentToModify(Appointment toModify) {
        thisToModify = toModify;
    }


}
