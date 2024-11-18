package com.example.helloworldjfxtemplate.controller;

import com.example.helloworldjfxtemplate.DAO.Query;
import com.example.helloworldjfxtemplate.helper.AlertHelper;
import com.example.helloworldjfxtemplate.helper.DivisionHelper;
import com.example.helloworldjfxtemplate.helper.JDBC;
import com.example.helloworldjfxtemplate.helper.TimeHelper;
import com.example.helloworldjfxtemplate.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controls the implementation of the Customer adding and modifying FXML forms.
 * Includes validation for adding and modifying Customers.
 */
public class CustomerController implements Initializable {
    private static boolean isModify;
    @FXML
    public TextField nameField;
    @FXML
    public TextField addressField;
    @FXML
    public TextField postalField;
    @FXML
    public TextField phoneField;
    @FXML
    public TextField idField;
    @FXML
    public ComboBox<String> countryCombo;
    @FXML
    public ComboBox<String> divisionCombo;
    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;
    @FXML
    public RadioButton individualRadio;
    @FXML
    public RadioButton companyRadio;
    @FXML
    public ComboBox companyCombo;
    @FXML
    public Button companyButton;
    @FXML
    public Label nameLabel;
    private ObservableList<String> countries = FXCollections.observableArrayList();
    private ObservableList<String> divisions = FXCollections.observableArrayList();
    private static Customer thisToModify = null;
    private String name;
    private String address;
    private String postal;
    private String phone;
    private int id;
    private String customerType;
    private boolean canSave;

    /**
     * Gives the CustomerController access to the Customer to be modified.
     * @param toModify Customer passed by the MainController to be modified.
     */
    public static void setCustomerToModify(Customer toModify) {
        thisToModify = toModify;
    }

    /**
     * Initializes the CustomerController by obtaining Country information from the database to populate the ComboBox.
     * If the user is modifying a Customer, the fields will be prefilled with the relevant information.
     * @param url The URL of the FXML file.
     * @param resourceBundle Used for localized resources such as language files and images.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        JDBC.getConnection();
//        Query.makeQuery("SELECT Country FROM Countries");
//        ResultSet result = Query.getResult();
//        try {
//            while (result.next()) {
//                countries.add(result.getString("Country"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        countryCombo.setItems(countries);

        ToggleGroup group = new ToggleGroup();
        companyRadio.setToggleGroup(group);
        individualRadio.setToggleGroup(group);

        String countryQuery = "SELECT Country FROM countries";
        try (PreparedStatement countryStmt = JDBC.getConnection().prepareStatement(countryQuery);
             ResultSet result = countryStmt.executeQuery()) {
            countries.clear();
            while (result.next()) {
                countries.add(result.getString("Country"));
            }
            countryCombo.setItems(countries);
        } catch (SQLException e) {
            AlertHelper.showError("Error", "There was a database connection error while populating the Country Combo Box.");
            e.printStackTrace();
        }

        onClickIndividualRadio();


        if (isModify) {
            if (thisToModify.getCustomerType().equals("Individual")) {
                onClickIndividualRadio();
            } else {
                onClickCompanyRadio();
            }
            nameField.setText(thisToModify.getCustomerName());
            addressField.setText(thisToModify.getCustomerAddress());
            postalField.setText(thisToModify.getCustomerPostal());
            phoneField.setText(thisToModify.getCustomerPhone());
            idField.setText(((Integer)thisToModify.getCustomerId()).toString());
            try {
                countryCombo.getSelectionModel().select(DivisionHelper.divisionIdToCountry(thisToModify.getCustomerDivision()));
                onClickCountry();
                divisionCombo.getSelectionModel().select(DivisionHelper.divisionIdToString(thisToModify.getCustomerDivision()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }
    /**
     * Gathers the name string from the TextField. Shows an alert and prevents saving on failure to gather a string.
     */
    public void onClickNameField() {
        try {
            name = nameField.getText();
            if (name.isBlank()){
                throw new Exception();
            }
        } catch (Exception e) {
            AlertHelper.showAlert("Warning", "The Name field must be populated.");
            canSave = false;
        }
    }

    /**
     * Gathers the address string from the TextField. Shows an alert and prevents saving on failure to gather a string.
     */
    public void onClickAddressField() {
        try {
            address = addressField.getText();
            if (address.isBlank()){
                throw new Exception();
            }
        } catch (Exception e) {
            AlertHelper.showAlert("Warning", "The Address field must be populated.");
            canSave = false;
        }
    }

    public void onClickPostalField() {
        try {
            postal = postalField.getText();
            if (postal.isBlank()){
                throw new Exception();
            }
        } catch (Exception e) {
            AlertHelper.showAlert("Warning", "The Postal field must be populated.");
            canSave = false;
        }
    }

    /**
     * Gathers the phone string from the TextField. Shows an alert and prevents saving on failure to gather a string.
     */
    public void onClickPhoneField() {
        try {
            phone = phoneField.getText();
            if (phone.isBlank()){
                throw new Exception();
            }
        } catch (Exception e) {
            AlertHelper.showAlert("Warning", "The Phone Number field must be populated.");
            canSave = false;
        }
    }

    /**
     * Gathers the selected country from the country ComboBox. Shows an alert and prevents saving if the ComboBox
     * was not populated. Depending on the selected country, it also sets the items for the division ComboBox.
     */
    public void onClickCountry() {
//        int id = -1;
//        String currentCountry = countryCombo.getSelectionModel().getSelectedItem();
//        JDBC.getConnection();
//        Query.makeQuery("SELECT Country_ID FROM Countries WHERE Country = '" + currentCountry + "'");
//        ResultSet result = Query.getResult();
//        try {
//            while (result.next()) {
//                id = result.getInt("Country_ID");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        if (id != -1) {
//            Query.makeQuery("SELECT Division FROM first_level_divisions WHERE Country_ID = " + id);
//            result = Query.getResult();
//            divisions.clear();
//            try {
//                while (result.next()) {
//                    divisions.add(result.getString("Division"));
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            divisionCombo.setItems(divisions);
//        } else {
//            AlertHelper.showError("Error", "Invalid Country selection (ID of -1).");
//        }
//
//        divisionCombo.getSelectionModel().clearSelection();

        int id = -1;
        String currentCountry = countryCombo.getSelectionModel().getSelectedItem();
        String countryQuery = "SELECT Country_ID FROM countries WHERE Country = ?";
        String divisionQuery = "SELECT Division FROM first_level_divisions WHERE Country_ID = ?";
        try (PreparedStatement countryStmt = JDBC.getConnection().prepareStatement(countryQuery)) {
            // Get the Country_ID for the selected country
            countryStmt.setString(1, currentCountry);
            try (ResultSet result = countryStmt.executeQuery()) {
                if (result.next()) {
                    id = result.getInt("Country_ID");
                }
            }
            // If Country_ID is valid, retrieve and populate divisions
            if (id != -1) {
                try (PreparedStatement divisionStmt = JDBC.getConnection().prepareStatement(divisionQuery)) {
                    divisionStmt.setInt(1, id);
                    try (ResultSet divisionResult = divisionStmt.executeQuery()) {
                        divisions.clear();
                        while (divisionResult.next()) {
                            divisions.add(divisionResult.getString("Division"));
                        }
                    }
                }
                divisionCombo.setItems(divisions);
            } else {
                AlertHelper.showError("Error", "Invalid Country selection (ID of -1).");
            }
            divisionCombo.getSelectionModel().clearSelection();
        } catch (SQLException e) {
            AlertHelper.showError("Error", "There was a database connection error while populating the Division ComboBox.");
        }

    }

    /**
     * Handles the saving of Customers created or modified within the form. They are then stored within the database.
     * @param actionEvent This ActionEvent is generated by the save button, and is used for the context to go back to the main form.
     * @throws SQLException Exception thrown in cases where the Query and ResultSet handling fails.
     * @throws IOException Exception thrown in cases where the destination of the main-menu is inaccessible.
     */
    public void onClickSave(ActionEvent actionEvent) throws SQLException, IOException {
        canSave = true;
        onClickNameField();
        onClickAddressField();
        onClickPostalField();
        onClickPhoneField();
        if ((countryCombo.getSelectionModel().getSelectedItem() == null ||
                countryCombo.getSelectionModel().getSelectedItem().isBlank() )||
                (divisionCombo.getSelectionModel().getSelectedItem() == null ||
                        divisionCombo.getSelectionModel().getSelectedItem().isBlank())) {
            canSave = false;
            AlertHelper.showError("Error", "The Country and Division combo boxes must be populated.");
        }


        if (canSave) {

            int divisionId = DivisionHelper.divisionStringToId(divisionCombo.getSelectionModel().getSelectedItem());

            try {
                if (isModify) {
                    String updateQuery = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, "
                            + "Last_Update = ?, Last_Updated_By = ?, Division_ID = ?, Customer_Type = ? WHERE Customer_ID = ?";

                    try (PreparedStatement updateStatement = JDBC.getConnection().prepareStatement(updateQuery)) {
                        updateStatement.setString(1, name);
                        updateStatement.setString(2, address);
                        updateStatement.setString(3, postal);
                        updateStatement.setString(4, phone);
                        updateStatement.setTimestamp(5, Timestamp.valueOf(TimeHelper.convertNowToUTC()));
                        updateStatement.setString(6, LoginController.getUsername());
                        updateStatement.setInt(7, divisionId);
                        updateStatement.setInt(8, thisToModify.getCustomerId());
                        updateStatement.setString(9, customerType);

                        updateStatement.executeUpdate();
                    }

                } else {
                    String insertQuery = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, "
                            + "Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID, Customer_Type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    try (PreparedStatement insertStatement = JDBC.getConnection().prepareStatement(insertQuery)) {
                        insertStatement.setInt(1, MainController.nextCustomerId());
                        insertStatement.setString(2, name);
                        insertStatement.setString(3, address);
                        insertStatement.setString(4, postal);
                        insertStatement.setString(5, phone);
                        insertStatement.setTimestamp(6, Timestamp.valueOf(TimeHelper.convertNowToUTC()));
                        insertStatement.setString(7, LoginController.getUsername());
                        insertStatement.setTimestamp(8, Timestamp.valueOf(TimeHelper.convertNowToUTC()));
                        insertStatement.setString(9, LoginController.getUsername());
                        insertStatement.setInt(10, divisionId);
                        insertStatement.setString(11, customerType);

                        insertStatement.executeUpdate();
                    }
                }

            } catch (SQLException e) {
                AlertHelper.showError("Error", "Failed to save customer information to the database.");
                e.printStackTrace();
            }





//            int divisionId = DivisionHelper.divisionStringToId(divisionCombo.getSelectionModel().getSelectedItem());
//            JDBC.getConnection();
//            if (isModify) {
//                Query.makeQuery("UPDATE customers SET Customer_Name = '" + name + "', Address = '" + address
//                        + "', Postal_Code = '" + postal + "', Phone = '" + phone + "', Last_Update = '" +
//                        TimeHelper.convertNowToUTC() + "', Last_Updated_By = '" + LoginController.getUsername() +
//                        "', Division_ID = " + divisionId + " WHERE Customer_ID = " + thisToModify.getCustomerId());
//            } else {
//                Query.makeQuery("INSERT INTO customers VALUES (" + MainController.nextCustomerId() + ", '" + name +
//                        "', '" + address + "', '" + postal + "', '" + phone + "', '" + TimeHelper.convertNowToUTC() +
//                        "', '" + LoginController.getUsername() + "', '" + TimeHelper.convertNowToUTC() +
//                        "', '" + LoginController.getUsername() + "', " + divisionId + ")");
//            }
//

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/main-menu.fxml")));
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("Main Scene");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }

    /**
     * Handles the Cancel button which allows the user to discard any input on the form and return to the main-menu scene.
     * @param actionEvent ActionEvent given by the cancel button, used to navigate back to the main-menu scene.
     * @throws IOException Exception thrown in cases where the main-menu fxml is unreachable.
     */
    public void onClickCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/main-menu.fxml")));
        Stage primaryStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Main Scene");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Boolean that indicates whether the form should be blank for adding a Customer, or populated for modification.
     * @param modify Boolean passed by the Main Scene.
     */
    public static void setModify(boolean modify) {
        isModify = modify;
    }

    public void onClickIndividualRadio() {
        nameLabel.setText("Customer Name");
        individualRadio.setSelected(true);
        customerType = "Individual";
    }

    public void onClickCompanyRadio() {
        customerType = "Company";
        if (!LoginController.getUsername().equals("admin")) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Admin Authorization");
            dialog.setHeaderText("Administrator Access Required");
            dialog.setContentText("Enter Admin Password:");

            // Show the dialog and capture the result
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals("admin")) {
                nameLabel.setText("Company Name");
            } else {
                AlertHelper.showError("Authorization Unsuccessful", "Incorrect Password.");
                onClickIndividualRadio();
            }
        } else {
            nameLabel.setText("Company Name");
        }
    }

}
