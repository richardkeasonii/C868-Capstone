package com.example.helloworldjfxtemplate.DAO;

import com.example.helloworldjfxtemplate.controller.LoginController;
import com.example.helloworldjfxtemplate.helper.JDBC;
import com.example.helloworldjfxtemplate.helper.TimeHelper;
import com.example.helloworldjfxtemplate.model.Appointment;
import com.example.helloworldjfxtemplate.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The DAO implementation that handles all the Appointments within the appointments table in the database.
 */
public class AppointmentDaoImpl {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//    public static Appointment getAppointment(String appointmentName) throws SQLException{
//        // type is name or phone, value is the name or the phone #
//        JDBC.makeConnection();
//        String sqlStatement="select * FROM appointments WHERE Title  = '" + appointmentName+ "'";
//        //  String sqlStatement="select FROM address";
//        Query.makeQuery(sqlStatement);
//        Appointment appointmentResult;
//        ResultSet result=Query.getResult();
//        while(result.next()){
//            int appointmentId = result.getInt("Appointment_ID");
//            String appointmentTitle = result.getString("Title");
//            String appointmentDesc = result.getString("Description");
//            String appointmentLocation = result.getString("Location");
//            String appointmentType = result.getString("Type");
//            LocalDateTime appointmentStart = result.getObject("Start", LocalDateTime.class);
//            LocalDateTime appointmentEnd = result.getObject("End", LocalDateTime.class);
//            LocalDateTime createDate = result.getObject("Create_Date", LocalDateTime.class);
//            String createdBy = result.getString("Created_By");
//            LocalDateTime updateDate = result.getObject("Last_Update", LocalDateTime.class);
//            String updatedBy = result.getString("Last_Updated_By");
//            int customerId = result.getInt("Customer_ID");
//            int userId = result.getInt("User_ID");
//            int contactId = result.getInt("Contact_ID");
//
//            //Changing the localdatetimes so the display matches the system time
//            LocalDateTime adjustAppointmentStart = TimeHelper.convertUTCToLocal(appointmentStart).toLocalDateTime();
//            LocalDateTime adjustAppointmentEnd = TimeHelper.convertUTCToLocal(appointmentEnd).toLocalDateTime();
//            LocalDateTime adjustCreateDate = TimeHelper.convertUTCToLocal(createDate).toLocalDateTime();
//            LocalDateTime adjustUpdateDate = TimeHelper.convertUTCToLocal(updateDate).toLocalDateTime();
//
//            appointmentResult= new Appointment(appointmentId, appointmentTitle, appointmentDesc, appointmentLocation,
//                    appointmentType, adjustAppointmentStart, adjustAppointmentEnd, adjustCreateDate, createdBy,
//                    adjustUpdateDate, updatedBy, customerId,  userId, contactId);
//            return appointmentResult;
//        }
//        JDBC.closeConnection();
//        return null;
//    }

    /**
     * Returns an ObservableList of all the Appointments found within the database, used to populate the TableView.
     * @return ObservableList<Appointment> containing all the appointments within the database.
     * @throws SQLException Exception thrown due to Querying the database.
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException{
        ObservableList<Appointment> allAppointments= FXCollections.observableArrayList();
        JDBC.getConnection();



        String sqlStatement="SELECT appointments.*, customers.Customer_Type FROM appointments " +
                "JOIN customers ON appointments.Customer_ID = customers.Customer_ID;";
        Query.makeQuery(sqlStatement);
        ResultSet result=Query.getResult();
        while(result.next()){

            int appointmentId = result.getInt("Appointment_ID");
            String appointmentTitle = result.getString("Title");
            String appointmentDesc = result.getString("Description");
            String appointmentLocation = result.getString("Location");
            String appointmentType = result.getString("Type");
            LocalDateTime appointmentStart = result.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = result.getTimestamp("End").toLocalDateTime();
            LocalDateTime createDate = result.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = result.getString("Created_By");
            LocalDateTime updateDate = result.getTimestamp("Last_Update").toLocalDateTime();
            String updatedBy = result.getString("Last_Updated_By");
            int customerId = result.getInt("Customer_ID");
            int userId = result.getInt("User_ID");
            int contactId = result.getInt("Contact_ID");
            String customerType = result.getString("Customer_Type");

//            LocalDateTime adjustAppointmentStart = TimeHelper.convertUTCToLocal(appointmentStart).toLocalDateTime();
//            LocalDateTime adjustAppointmentEnd = TimeHelper.convertUTCToLocal(appointmentEnd).toLocalDateTime();
//            LocalDateTime adjustCreateDate = TimeHelper.convertUTCToLocal(createDate).toLocalDateTime();
//            LocalDateTime adjustUpdateDate = TimeHelper.convertUTCToLocal(updateDate).toLocalDateTime();


            if (LoginController.getUsername().equals("admin")) {
                Appointment appointmentResult = new Appointment(appointmentId, appointmentTitle, appointmentDesc, appointmentLocation,
                        appointmentType, appointmentStart, appointmentEnd, createDate, createdBy,
                        updateDate, updatedBy, customerId, userId, contactId);
                allAppointments.add(appointmentResult);
            } else if (userId != 2 && !customerType.equals("Company")) {
                Appointment appointmentResult = new Appointment(appointmentId, appointmentTitle, appointmentDesc, appointmentLocation,
                        appointmentType, appointmentStart, appointmentEnd, createDate, createdBy,
                        updateDate, updatedBy, customerId, userId, contactId);
                allAppointments.add(appointmentResult);
            }
        }
        return allAppointments;
    }
}