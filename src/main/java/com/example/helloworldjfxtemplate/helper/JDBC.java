package com.example.helloworldjfxtemplate.helper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Handles database connection.
 * Code obtained from the Course Instructor Software Team Resources.
 */
//public abstract class JDBC {
//
//    private static final String protocol = "jdbc";
//    private static final String socketFactory = "&socketFactory=com.google.cloud.sql.mysql.SocketFactory";
//    private static final String instanceName = "key-chalice-441123-m4:us-central1:eason-capstone-project-c868";
//    private static final String vendor = ":mysql:";
////    private static final String location = "//localhost/";
////    private static final String location = "//34.55.237.46/";
//    private static final String databaseName = "///client_schedule?";
//    private static final String jdbcUrl = protocol + vendor + databaseName + instanceName + socketFactory;
//    private static final String driver = "com.mysql.cj.jdbc.Driver";
//    private static final String userName = "&user=sqlUser";
//    private static String password = "&password=Passw0rd!";
//    public static Connection connection;
//    private static PreparedStatement preparedStatement;
//    public static String connectionStatus;
//
//    /**
//     * Makes the connection with the database using the information provided in the variables.
//     */
//    public static void makeConnection() {
//        try {
//            Class.forName(driver); // Locate Driver
//            connection = DriverManager.getConnection(jdbcUrl, userName, password); // reference Connection object
//            connectionStatus = "Database Connection successful!";
//            System.out.println(connectionStatus);
//        }
//        catch(ClassNotFoundException e) {
//            System.out.println("Error:" + e.getMessage());
//        }
//        catch(SQLException e) {
//            System.out.println("Error:" + e.getMessage());
//        }
//    }
//
//    public static Connection getConnection() {
//        if (connection == null) {
//            makeConnection();
//        }
//        return connection;
//    }
//
//    /**
//     * Closes the connection with the database.
//     */
//    public static void closeConnection() {
//        try {
//            connection.close();
//            connectionStatus = "Database Connection closed!";
//            System.out.println(connectionStatus);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//
//
//}


public abstract class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//34.55.237.46/"; // Replace with actual IP address
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone=SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser"; // Replace with actual username
    private static String password = "Passw0rd!";     // Replace with actual password
    public static Connection connection;
    private static PreparedStatement preparedStatement;
    public static String connectionStatus;

    /**
     * Establishes a connection to the database.
     */
    public static void makeConnection() {
        try {
            Class.forName(driver); // Load MySQL driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Create connection
            connectionStatus = "Database connection successful!";
            System.out.println(connectionStatus);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            makeConnection();
        }
        return connection;
    }

    /**
     * Closes the connection to the database.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connectionStatus = "Database connection closed!";
                System.out.println(connectionStatus);
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
