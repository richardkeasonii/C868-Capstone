package com.example.helloworldjfxtemplate.controller;

import com.example.helloworldjfxtemplate.DAO.AppointmentDaoImpl;
import com.example.helloworldjfxtemplate.DAO.CustomerDaoImpl;
import com.example.helloworldjfxtemplate.DAO.Query;
import com.example.helloworldjfxtemplate.helper.AlertHelper;
import com.example.helloworldjfxtemplate.helper.JDBC;
import com.example.helloworldjfxtemplate.model.Appointment;
import com.example.helloworldjfxtemplate.model.Customer;
import javafx.application.Platform;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controls the implementation of the Main scene, including validating deletion of Customers and Appointments.
 * Validates that Appointments or Customers are selected for modification.
 */
public class MainController implements Initializable {

    public RadioButton monthRadio;
    public RadioButton weekRadio;
    public Spinner<Integer> timeSpinner;
    public ChoiceBox<String> reportChoice;
    public TextArea reportText;
    public TextField searchField;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Customer, Integer> customerID;
    @FXML
    private TableColumn<Customer, String> customerName;
    @FXML
    private TableColumn<Customer, String> customerAddress;
    @FXML
    private TableColumn<Customer, String> customerPostal;
    @FXML
    private TableColumn<Customer, String> customerPhone;
    @FXML
    private TableColumn<Customer, LocalDateTime> customerCreated;
    @FXML
    private TableColumn<Customer, String> customerCreatedBy;
    @FXML
    private TableColumn<Customer, LocalDateTime> customerUpdated;
    @FXML
    private TableColumn<Customer, String> customerUpdatedBy;
    @FXML
    private TableColumn<Customer, String> customerDivision;
    @FXML
    private TableColumn<Customer, String> customerType;
    @FXML
    private TableColumn<Appointment, Integer> appointmentID;
    @FXML
    private TableColumn<Appointment, String> appointmentTitle;
    @FXML
    private TableColumn<Appointment, String> appointmentDesc;
    @FXML
    private TableColumn<Appointment, String> appointmentLocation;
    @FXML
    private TableColumn<Appointment, String> appointmentType;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentStart;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentEnd;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentCreated;
    @FXML
    private TableColumn<Appointment, String> appointmentCreatedBy;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentUpdated;
    @FXML
    private TableColumn<Appointment, String> appointmentUpdatedBy;
    @FXML
    private TableColumn<Appointment, Integer> appointmentCustomer;
    @FXML
    private TableColumn<Appointment, Integer> appointmentUser;
    @FXML
    private TableColumn<Appointment, Integer> appointmentContact;

    static ObservableList<Customer> AllCustomers = FXCollections.observableArrayList();
    ObservableList<Customer> LimitedCustomers = FXCollections.observableArrayList();
    ObservableList<Customer> SearchedCustomers = FXCollections.observableArrayList();
    ObservableList<Appointment> Appointments = FXCollections.observableArrayList();
    ObservableList<Appointment> FilteredAppointments = FXCollections.observableArrayList();

    /**
     * Initializes the FXML fields, most importantly gathering the relevant Customer and Appointment information,
     * populating their TableViews, and displaying an Alert whether there is an appointment within 15 minutes or not.
     * The Lambda expression here is used as an event listener to change the report output based on a selection within
     * the ChoiceBox.
     * @param url The URL of the FXML file.
     * @param rb Used for localized resources like language files and images.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ToggleGroup group = new ToggleGroup();
        monthRadio.setToggleGroup(group);
        weekRadio.setToggleGroup(group);
        timeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

        reportChoice.getItems().addAll("# by Type and Month", "Contact Schedules", "Appointment Creation/Updating");

        reportChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Update the TextArea based on the selected report
            if (newValue != null) {
                try {
                    reportText.setText(generateReport(newValue));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        customerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostal.setCellValueFactory(new PropertyValueFactory<>("customerPostal"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customerCreated.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        customerCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        customerUpdated.setCellValueFactory(new PropertyValueFactory<>("updateDate"));
        customerUpdatedBy.setCellValueFactory(new PropertyValueFactory<>("updatedBy"));
        customerDivision.setCellValueFactory(new PropertyValueFactory<>("customerDivision"));
        customerType.setCellValueFactory(new PropertyValueFactory<>("customerType"));

        try {
            LimitedCustomers.addAll(CustomerDaoImpl.getLimitedCustomers());
            AllCustomers.clear();
            AllCustomers.addAll(CustomerDaoImpl.getAllCustomers());

        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        customerTable.setItems(LimitedCustomers);


        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentDesc.setCellValueFactory(new PropertyValueFactory<>("appointmentDesc"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        appointmentCreated.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        appointmentCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        appointmentUpdated.setCellValueFactory(new PropertyValueFactory<>("updateDate"));
        appointmentUpdatedBy.setCellValueFactory(new PropertyValueFactory<>("updatedBy"));
        appointmentCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentUser.setCellValueFactory(new PropertyValueFactory<>("userId"));
        appointmentContact.setCellValueFactory(new PropertyValueFactory<>("contactId"));


        try {
            Appointments.addAll(AppointmentDaoImpl.getAllAppointments());


        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        appointmentTable.setItems(Appointments);

        boolean isWithinFifteen = false;

        for (Appointment appointment : Appointments) {
            Duration startDuration = Duration.between(LocalDateTime.now(), appointment.getAppointmentStart());

            // Get the absolute value of the difference to ensure it's positive
            long startDifference = Math.abs(startDuration.toMinutes());

            if (startDifference <= 15) {
                isWithinFifteen = true;
                LocalDate appointmentDate = appointment.getAppointmentStart().toLocalDate();
                LocalTime appointmentTime = appointment.getAppointmentStart().toLocalTime();
                AlertHelper.showAlert("Appointment", "You have an upcoming appointment.\nAppointment ID: " +
                        appointment.getAppointmentId() + "\nAppointment Date: " + appointmentDate +
                        "\nAppointment Time: " + appointmentTime);
            }
        }

        if (!isWithinFifteen) {
            AlertHelper.showAlert("No Appointments", "There are no appointments within the next 15 minutes.");
        }

        addSpinnerListeners();
        onClickMonth();

    }

    /**
     * Handles the implementation of the Spinner which is used to progress the number of months/weeks to show on the
     * Appointments TableView.
     * The Lambda expression here is used as a listener for a change in the value of the Spinner.
     */
    private void addSpinnerListeners() {
        timeSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (monthRadio.isSelected()) {
                onClickMonth();
            } else {
                onClickWeek();
            }
        });
    }

    /**
     * Handles the gathering of data for three unique reports. Constructs an appropriate String and returns it to
     * be displayed within the TextArea. Queries the database for the information to display.
     * @param reportType The choice the user selected within the ChoiceBox.
     * @return Returns a String, which is a fully formatted report to display in the TextArea.
     * @throws SQLException Exception thrown due to accessing the database through Queries.
     */
    private String generateReport(String reportType) throws SQLException {
        StringBuilder report = new StringBuilder();
        JDBC.getConnection();
        ResultSet result;

        switch (reportType) {
            case "# by Type and Month":
                Query.makeQuery("SELECT Type, MONTH(Start) AS Month, COUNT(*) AS TotalAppointments " +
                        "FROM appointments GROUP BY Type, MONTH(Start)");
                report.append("Appointment Report by Type and Month:\n");
                report.append("Type\tMonth\tTotal Appointments\n");
                result = Query.getResult();
                while (result.next()) {
                    String type = result.getString("Type");
                    int month = result.getInt("Month");
                    int totalAppointments = result.getInt("TotalAppointments");

                    report.append(type).append("\t")
                            .append(month).append("\t")
                            .append(totalAppointments).append("\n");
                }
                return report.toString();
            case "Contact Schedules":
                Query.makeQuery("SELECT a.Appointment_ID, a.Title, a.Type, a.Description, a.Start, a.End, " +
                        "a.Customer_ID, c.Contact_Name " + "FROM appointments a JOIN contacts c " +
                        "ON a.Contact_ID = c.Contact_ID ORDER BY c.Contact_Name, a.Start");
                result = Query.getResult();

                report.append("Schedule for Each Contact:\n\n");

                String currentContact = "";
                while (result.next()) {
                    String contactName = result.getString("Contact_Name");

                    // If the contact has changed, add a new header
                    if (!contactName.equals(currentContact)) {
                        currentContact = contactName;
                        report.append("Contact: ").append(contactName).append("\n");
                        report.append(String.format("%-15s %-10s %-15s %-20s %-20s %-20s %s\n",
                                "Appointment ID", "Title", "Type", "Description", "Start Date/Time", "End Date/Time", "Customer ID"));
                        report.append("------------------------------------------------------------------------------------------------------\n");
                    }

                    // Append appointment details for the current contact
                    int appointmentID = result.getInt("Appointment_ID");
                    String title = result.getString("Title");
                    String type = result.getString("Type");
                    String description = result.getString("Description");
                    String start = result.getString("Start");
                    String end = result.getString("End");
                    int customerID = result.getInt("Customer_ID");

                    report.append(String.format("%-15d %-10s %-15s %-20s %-20s %-20s %d\n",
                            appointmentID, title, type, description, start, end, customerID));
                }

                return report.toString();

            case "Appointment Creation/Updating":
                Query.makeQuery("SELECT Created_By, COUNT(*) AS Created_Count FROM appointments GROUP BY Created_By");
                result = Query.getResult();

                report.append("Appointment Create/Update Report:\n\n");
                report.append(String.format("%-20s %-15s\n", "Creator Name", "Created Count"));
                report.append("----------------------------------------\n");

                while (result.next()) {
                    String creator = result.getString("Created_By");
                    int count = result.getInt("Created_Count");
                    report.append(String.format("%-20s %-15d\n", creator, count));
                }

                Query.makeQuery("SELECT Last_Updated_By, COUNT(*) AS Updated_Count FROM appointments GROUP BY Last_Updated_By");
                result = Query.getResult();
                report.append(String.format("\n%-20s %-15s\n", "Updater Name", "Update Count"));
                report.append("----------------------------------------\n");

                while (result.next()) {
                    String updater = result.getString("Last_Updated_By");
                    int count = result.getInt("Updated_Count");
                    report.append(String.format("%-20s %-15d\n", updater, count));
                }

                return report.toString();

            default:
                return "";
        }
    }

    /**
     * Method used by the CustomerController for generating the next Customer ID.
     * @return Returns the next available customer id.
     * @throws SQLException Exception thrown due to Querying the database.
     */
    public static int nextCustomerId() throws SQLException {
        JDBC.makeConnection();
        Query.makeQuery("SELECT MAX(Customer_ID) FROM customers");
        ResultSet result = Query.getResult();
        result.next();
        return (result.getInt("MAX(Customer_ID)") + 1);
    }

    /**
     * Method used by the AppointmentController for generating the next Appointment ID.
     * @return Returns the next available appointment id.
     * @throws SQLException Exception thrown due to Querying the database.
     */
    public static int nextAppointmentId() throws SQLException {
        JDBC.makeConnection();
        Query.makeQuery("SELECT MAX(Appointment_ID) FROM appointments");
        ResultSet result = Query.getResult();
        result.next();
        return (result.getInt("MAX(Appointment_ID)") + 1);
    }

    /**
     * Handler for the Exit button, which will properly close out the application.
     */
    public void onExit() {
        Platform.exit();
    }

    /**
     * Navigates to the Customer Form after setting the modify boolean to false, indicating creating instead of updating.
     * @param actionEvent ActionEvent given by the Add Customer button, used to navigate to the customer form.
     * @throws IOException Exception thrown in cases where the FXML file is unreachable.
     */
    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        CustomerController.setModify(false);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/customer-form.fxml")));
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Add Customer Scene");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Navigates to the Customer Form after setting the modify boolean to true, indicating updating instead of creating.
     * First validates that the user has selected a Customer to be modified, and if so, passes it along to the controller.
     * @param actionEvent ActionEvent given by the Modify Customer button, used to navigate to the customer form.
     * @throws IOException Exception thrown in cases where the FXML file is unreachable.
     */
    public void onModifyCustomer(ActionEvent actionEvent) throws IOException {
        Customer toModify = customerTable.getSelectionModel().getSelectedItem();
        if (toModify != null) {
            CustomerController.setModify(true);
            CustomerController.setCustomerToModify(toModify);
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/customer-form.fxml")));
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("Modify Customer Scene");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } else {
            AlertHelper.showError(null, "No customer is selected.");
        }
    }

    /**
     * Validates that the user has selected a customer, and checks whether the customer has existing appointments. In
     * the cases where the customer does, the deletion is not allowed.
     * Lambda expression used to delete the Customer from the TableView with the matching customer id.
     * @throws SQLException Exception thrown due to Querying the database.
     */
    public void onDeleteCustomer() throws SQLException {
        Customer toDelete = customerTable.getSelectionModel().getSelectedItem();
        if (toDelete != null) {
            JDBC.getConnection();
            Query.makeQuery("SELECT Appointment_ID FROM appointments WHERE Customer_ID = " + toDelete.getCustomerId());
            ResultSet result = Query.getResult();
            if (result.next()) {
                AlertHelper.showError("Unable to Delete", "The selected Customer has existing appointments " +
                        "and cannot be deleted.");
            } else {
                Query.makeQuery("DELETE FROM customers WHERE Customer_ID = " + toDelete.getCustomerId());
                LimitedCustomers.removeIf(customer -> customer.getCustomerId() == toDelete.getCustomerId());
                customerTable.refresh();
                AlertHelper.showAlert("Deletion Successful", "Customer was successfully deleted.");
            }
        } else {
            AlertHelper.showError(null, "No customer is selected.");
        }
    }

    /**
     * Navigates to the Appointment form after setting the modify boolean to false, indicating creating instead of updating.
     * @param actionEvent ActionEvent given by the Add Appointment button, used to navigate to the appointment form.
     * @throws IOException
     */
    public void onAddAppointment(ActionEvent actionEvent) throws IOException {
        AppointmentController.setModify(false);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/appointment-form.fxml")));
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Add Appointment Scene");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Navigates to the Appointment Form after setting the modify boolean to true, indicating updating instead of creating.
     * First validates that the user has selected an Appointment to be modified, and if so, passes it along to the controller.
     * @param actionEvent ActionEvent given by the Modify Appointment button, used to navigate to the appointment form.
     * @throws IOException Exception thrown in cases where the FXML file is unreachable.
     */
    public void onModifyAppointment(ActionEvent actionEvent) throws IOException {
        Appointment toModify = appointmentTable.getSelectionModel().getSelectedItem();
        if (toModify != null) {
            AppointmentController.setModify(true);
            AppointmentController.setAppointmentToModify(toModify);
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/appointment-form.fxml")));
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setTitle("Modify Appointment Scene");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } else {
            AlertHelper.showError(null, "No appointment is selected.");
        }
    }

    /**
     * First validates that the user has selected an Appointment from the TableView, then Queries the database to delete
     * it if so. Lambda expression used to select the appointment to delete that has the matching ID from the TableView.
     */
    public void onDeleteAppointment() {
        Appointment toDelete = appointmentTable.getSelectionModel().getSelectedItem();
        if (toDelete != null) {
            JDBC.getConnection();
            Query.makeQuery("DELETE FROM appointments WHERE Appointment_ID = " + toDelete.getAppointmentId());
            Appointments.removeIf(appointment -> appointment.getAppointmentId() == toDelete.getAppointmentId());
            FilteredAppointments.removeIf(appointment -> appointment.getAppointmentId() == toDelete.getAppointmentId());
            appointmentTable.refresh();
            AlertHelper.showAlert("Deletion Successful", "Appointment was successfully deleted." +
                    "\nAppointment ID: " + toDelete.getAppointmentId() + "\nAppointment Type: " +
                    toDelete.getAppointmentType());
        } else {
            AlertHelper.showError(null, "No appointment is selected.");
        }
    }

    /**
     * Handles the case where the Month radio button is selected. Filters the appointments to be displayed based on
     * if they fall within the given timespan.
     * FIX: Was previously showing multiple months ahead instead of the current month.
     * Lambda expression used to handle the filtering logic of Appointments.
     */
    public void onClickMonth() {
//        int timeChange = timeSpinner.getValue();
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime chosenTime = now.plus(timeChange - 1, ChronoUnit.MONTHS);
//        LocalDateTime oneMonthLater = now.plus(timeChange, ChronoUnit.MONTHS);
//        FilteredAppointments.setAll(Appointments.stream().filter(appointment -> appointment.getAppointmentStart().isAfter(chosenTime)
//                && appointment.getAppointmentStart().isBefore(oneMonthLater)).collect(Collectors.toList()));
//        appointmentTable.setItems(FilteredAppointments);

        LocalDateTime now = LocalDateTime.now();
        YearMonth currentYearMonth = YearMonth.from(now);

        // Filter the appointments to only include those in the current month
        FilteredAppointments.setAll(
                Appointments.stream()
                        .filter(appointment -> {
                            YearMonth appointmentYearMonth = YearMonth.from(appointment.getAppointmentStart());
                            return appointmentYearMonth.equals(currentYearMonth);
                        })
                        .toList()
        );
        appointmentTable.setItems(FilteredAppointments);

    }

    /**
     * Handles the case where the Week radio button is selected. Filters the appointments to be displayed based on
     * if they fall within the given timespan.
     * FIX: Was previously only increasing the amount of weeks shown instead of shifting by a week.
     * Lambda expression used to handle the filtering logic of Appointments.
     */
    public void onClickWeek() {
        int timeChange = timeSpinner.getValue();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime chosenTime = now.plus(timeChange - 1, ChronoUnit.WEEKS);
        LocalDateTime oneWeekLater = now.plus(timeChange, ChronoUnit.WEEKS);

        // Filter appointments that fall within the chosen week
        FilteredAppointments.setAll(Appointments.stream().filter(appointment -> appointment.getAppointmentStart().isAfter(chosenTime)
                && appointment.getAppointmentStart().isBefore(oneWeekLater)).collect(Collectors.toList()));

        // Update the TableView
        appointmentTable.setItems(FilteredAppointments);
    }

    /**
     * Handles the Search by Customer Name functionality. Filters the customers to be displayed based on if they
     * contain the entered string.
     */
    public void onClickSearchField() {
        String customerName = searchField.getText().toLowerCase();

        //Searches for Customers that non-case-sensitively contain the given string
        SearchedCustomers.setAll(LimitedCustomers.stream().filter(customer -> customer.getCustomerName().toLowerCase().contains(customerName)).collect(Collectors.toList()));

        //Updates the TableView
        customerTable.setItems(SearchedCustomers);
    }
    public static ObservableList<Customer> getAllCustomers() {
        return AllCustomers;
    }

}