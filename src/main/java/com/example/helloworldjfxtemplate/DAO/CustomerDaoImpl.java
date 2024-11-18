package com.example.helloworldjfxtemplate.DAO;

import com.example.helloworldjfxtemplate.controller.LoginController;
import com.example.helloworldjfxtemplate.helper.JDBC;
import com.example.helloworldjfxtemplate.model.Company;
import com.example.helloworldjfxtemplate.model.Customer;
import com.example.helloworldjfxtemplate.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * The Customer DAO implementation. Queries the database and obtains the user information.
 */
public class CustomerDaoImpl {
//    public static Customer getCustomer(String userName) throws SQLException, Exception{
//        // type is name or phone, value is the name or the phone #
//        JDBC.makeConnection();
//        String sqlStatement="select * FROM customers";
//        //  String sqlStatement="select FROM address";
//        Query.makeQuery(sqlStatement);
//        Customer customerResult;
//        ResultSet result=Query.getResult();
//        while(result.next()){
//            int customerId = result.getInt("Customer_ID");
//            String customerName = result.getString("Customer_Name");
//            String customerAddress = result.getString("Address");
//            String customerPostal = result.getString("Postal_Code");
//            String customerPhone = result.getString("Phone");
//            LocalDateTime createDate = result.getObject("Create_Date", LocalDateTime.class);
//            String createdBy = result.getString("Created_By");
//            LocalDateTime updateDate = result.getObject("Last_Update", LocalDateTime.class);
//            String updatedBy = result.getString("Last_Updated_By");
//            int customerDivision = result.getInt("Division_ID");
//
//            customerResult= new Customer(customerId, customerName, customerAddress, customerPostal,
//                    customerPhone, createDate, createdBy, updateDate, updatedBy, customerDivision);
//            return customerResult;
//        }
//        JDBC.closeConnection();
//        return null;
//    }

    /**
     * Returns a List of all the Customers within the customers table of the database.
     * @return ObservableList<Customer> that contains all the customers in the database.
     * @throws SQLException Exception thrown due to Querying the database.
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException{
        ObservableList<Customer> allCustomers= FXCollections.observableArrayList();
        JDBC.getConnection();
        String sqlStatement="SELECT * FROM customers";
        Query.makeQuery(sqlStatement);
        ResultSet result=Query.getResult();
        while(result.next()){

            int customerId = result.getInt("Customer_ID");
            String customerName = result.getString("Customer_Name");
            String customerAddress = result.getString("Address");
            String customerPostal = result.getString("Postal_Code");
            String customerPhone = result.getString("Phone");
            LocalDateTime createDate = result.getObject("Create_Date", LocalDateTime.class);
            String createdBy = result.getString("Created_By");
            LocalDateTime updateDate = result.getObject("Last_Update", LocalDateTime.class);
            String updatedBy = result.getString("Last_Updated_By");
            int customerDivision = result.getInt("Division_ID");
            String customerType = result.getString("Customer_Type");

            if (customerType.equals("Individual")) {
                Customer customerResult= new Customer(customerId, customerName, customerAddress, customerPostal,
                        customerPhone, createDate, createdBy, updateDate, updatedBy, customerDivision, customerType);
                allCustomers.add(customerResult);
            } else if (LoginController.getUsername().equals("admin")) {
                Company companyResult = new Company(customerId, customerName, customerAddress, customerPostal,
                        customerPhone, createDate, createdBy, updateDate, updatedBy, customerDivision, customerType);
                allCustomers.add(companyResult);
            }
        }
        return allCustomers;
    }
}