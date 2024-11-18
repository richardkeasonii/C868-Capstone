package com.example.helloworldjfxtemplate.DAO;

import com.example.helloworldjfxtemplate.helper.JDBC;
import com.example.helloworldjfxtemplate.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The DAO implementation for the Contacts in the database. Unused.
 */
    public class ContactDaoImpl {
//    public static Contact getContact(String contactName) throws SQLException, Exception{
//        // type is name or phone, value is the name or the phone #
//        JDBC.makeConnection();
//        String sqlStatement="select * FROM contacts WHERE Contact_Name  = '" + contactName+ "'";
//        //  String sqlStatement="select FROM address";
//        Query.makeQuery(sqlStatement);
//        Contact contactResult;
//        ResultSet result=Query.getResult();
//        while(result.next()){
//            int contactId=result.getInt("Contact_ID");
//            String contactNameG=result.getString("Contact_Name");
//            String email=result.getString("Email");
//            contactResult= new Contact(contactId, contactNameG, email);
//            return contactResult;
//        }
//        JDBC.closeConnection();
//        return null;
//    }
//
//    public static ObservableList<Contact> getAllContacts() throws SQLException, Exception{
//        ObservableList<Contact> allContacts= FXCollections.observableArrayList();
//        JDBC.makeConnection();
//        String sqlStatement="select * from contacts";
//        Query.makeQuery(sqlStatement);
//        ResultSet result=Query.getResult();
//        while(result.next()){
//            int contactId=result.getInt("Contact_ID");
//            String contactNameG=result.getString("Contact_Name");
//            String email=result.getString("Email");
//            Contact contactResult= new Contact(contactId, contactNameG, email);
//            allContacts.add(contactResult);
//        }
//        JDBC.closeConnection();
//        return allContacts;
//    }

    }
