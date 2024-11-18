package com.example.helloworldjfxtemplate.DAO;

import com.example.helloworldjfxtemplate.helper.AlertHelper;
import com.example.helloworldjfxtemplate.helper.JDBC;
import com.example.helloworldjfxtemplate.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The DAO implementation that handles the Users.
 * This code was obtained from the Course Instructors Software Team Resources.
 */
public class UserDaoImpl {

    /**
     * Obtains a User from the database given a userName String.
     * @param userName The name of the User to be obtained.
     * @return Either the User or null if no User is found.
     */
    public static User getUser(String userName){
//        JDBC.getConnection();
//        String sqlStatement="select * FROM users WHERE User_Name  = '" + userName + "'";
//        Query.makeQuery(sqlStatement);
//        User userResult;
//        ResultSet result=Query.getResult();


        User userResult;
        String sqlStatement = "SELECT * FROM users WHERE User_Name = ?";
        try (PreparedStatement userStmt = JDBC.getConnection().prepareStatement(sqlStatement)) {

            userStmt.setString(1, userName);
            try (ResultSet result = userStmt.executeQuery()) {
                if (result.next()) {
                    int userid = result.getInt("User_ID");
                    String password = result.getString("Password");
                    userResult = new User(userid, userName, password);
                    return userResult;
                }
            }  catch (SQLException e) {
                AlertHelper.showError("Error", "Database connection failed while querying database for user login information.");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            AlertHelper.showError("Error", "Database connection failed while grabbing connection for login.");
            e.printStackTrace();
        }
        return null;
    }
}