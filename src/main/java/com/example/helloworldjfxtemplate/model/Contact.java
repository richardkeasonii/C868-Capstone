package com.example.helloworldjfxtemplate.model;

/**
 * Handles the contacts within the databse table.
 */
public class Contact {
    private int contactId;
    private String contactName;
    private String contactEmail;

    /**
     * Main constructor for Contact.
     * @param id the id to be set
     * @param name the name to be set
     * @param email the email to be set
     */
    public Contact (int id, String name, String email) {
        contactId = id;
        contactName = name;
        contactEmail = email;
    }

}
