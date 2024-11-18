package com.example.helloworldjfxtemplate.helper;

import com.example.helloworldjfxtemplate.DAO.Query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Helper that handles information involving the First-Level Divisions within the database.
 */
public class DivisionHelper {
    /**
     * Converts a Division ID to the String related to it.
     *
     * @param divisionId The Division ID to convert.
     * @return The String related to the Division.
     * @throws SQLException Exception thrown due to Querying the database.
     */
    public static String divisionIdToString(int divisionId) throws SQLException {
//        JDBC.makeConnection();
//        Query.makeQuery("SELECT Division FROM first_level_divisions WHERE Division_ID = " + divisionId);
//        ResultSet result = Query.getResult();
//        result.next();
//        return result.getString("Division");

        String sqlStatement = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
        try (PreparedStatement stmt = JDBC.getConnection().prepareStatement(sqlStatement)) {

            stmt.setInt(1, divisionId); // Set the Division_ID parameter
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return result.getString("Division");
                } else {
                    AlertHelper.showError("Error", "Division ID does not exist.");
                }
            }

        } catch (SQLException e) {
            AlertHelper.showError("Error", "There was a database connection error while obtaining the String for the Division ID.");
            return null;
        }

    return null;
    }

    /**
     * Takes a Division ID and returns the Country that matches the relevant Country_ID to that Division.
     *
     * @param divisionId The given Division ID.
     * @return The String that represents the relevant Country.
     * @throws SQLException Exception thrown due to Querying the database.
     */
    public static String divisionIdToCountry(int divisionId) throws SQLException {
//        JDBC.makeConnection();
//        Query.makeQuery("SELECT Country_ID FROM first_level_divisions WHERE Division_ID = " + divisionId);
//        ResultSet result = Query.getResult();
//        result.next();
//        int countryId = result.getInt("Country_ID");
//        System.out.println(countryId);
//        Query.makeQuery("SELECT Country FROM countries WHERE Country_ID = " + countryId);
//        result = Query.getResult();
//        result.next();
//        return result.getString("Country");

        String sqlStatement = "SELECT Country_ID FROM first_level_divisions WHERE Division_ID = ?";
        try (PreparedStatement stmt = JDBC.getConnection().prepareStatement(sqlStatement)) {
            stmt.setInt(1, divisionId); // Set the Division_ID parameter
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    int countryId = result.getInt("Country_ID");
                    System.out.println(countryId);

                    // Now query the 'countries' table using the countryId obtained above
                    String sqlCountry = "SELECT Country FROM countries WHERE Country_ID = ?";
                    try (PreparedStatement stmtCountry = JDBC.getConnection().prepareStatement(sqlCountry)) {
                        stmtCountry.setInt(1, countryId); // Set the Country_ID parameter
                        try (ResultSet resultCountry = stmtCountry.executeQuery()) {
                            if (resultCountry.next()) {
                                return resultCountry.getString("Country");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            AlertHelper.showError("Error", "There was a database connection error while obtaining the Country for the Division ID.");
            return null;
        }
    return null;
    }


    /**
     * Takes a String containing a division's name and converts it to the relevant ID.
     * @param divisionString The String to be converted.
     * @return The integer ID of the division.
     */
    public static int divisionStringToId(String divisionString){
//        JDBC.makeConnection();
//        Query.makeQuery("SELECT Division_ID FROM first_level_divisions WHERE Division = " + "'" + divisionString + "'");
//        ResultSet result = Query.getResult();
//        result.next();
//        return result.getInt("Division_ID");

        String sqlStatement = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        try (PreparedStatement stmt = JDBC.getConnection().prepareStatement(sqlStatement)) {
            stmt.setString(1, divisionString); // Set the Division string parameter
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return result.getInt("Division_ID");
                }
            }
        } catch (SQLException e) {
            AlertHelper.showError("Error", "There was a database connection error while obtaining the Division String's related ID.");
            return -1;
        }
    return -1;
    }

}
