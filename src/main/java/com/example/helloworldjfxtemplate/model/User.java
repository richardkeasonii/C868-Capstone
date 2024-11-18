package com.example.helloworldjfxtemplate.model;

/**
 * User class to handle retrieving user information.
 */
public class User {
    private int userId;
    private String userName;
    private String password;

    /**
     *
     * @param userId the userId to set
     * @param userName the userName to set
     * @param password the password to set
     */
    public User(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;

    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }


}