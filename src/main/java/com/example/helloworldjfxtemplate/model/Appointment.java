package com.example.helloworldjfxtemplate.model;

import java.time.LocalDateTime;

/**
 * Handles the storage and setters and getters for the appointments in the appointments table in the database.
 */
public class Appointment {
    private int appointmentId;
    private String appointmentTitle;
    private String appointmentDesc;
    private String appointmentLocation;
    private String appointmentType;
    private LocalDateTime appointmentStart;
    private LocalDateTime appointmentEnd;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime updateDate;
    private String updatedBy;
    private int customerId;
    private int userId;
    private int contactId;

    /**
     * Main Constructor for Appointments.
     * @param appointmentId the appointmentId to be set
     * @param appointmentTitle the appointmentTitle to be set
     * @param appointmentDesc the appointmentDesc to be set
     * @param appointmentLocation the appointmentLocation to be set
     * @param appointmentType the appointmentType to be set
     * @param appointmentStart the appointmentStart to be set
     * @param appointmentEnd the appointmentEnd to be set
     * @param createDate the createDate to be set
     * @param createdBy the createdBy to be set
     * @param updateDate the updateDate to be set
     * @param updatedBy the updatedBy to be set
     * @param customerId the customerId to be set
     * @param userId the userId to be set
     * @param contactId the contactId to be set
     */
    public Appointment(int appointmentId, String appointmentTitle, String appointmentDesc, String appointmentLocation,
                       String appointmentType, LocalDateTime appointmentStart, LocalDateTime appointmentEnd,
                       LocalDateTime createDate, String createdBy, LocalDateTime updateDate, String updatedBy,
                       int customerId, int userId, int contactId) {
        this.appointmentId = appointmentId;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDesc = appointmentDesc;
        this.appointmentLocation = appointmentLocation;
        this.appointmentType = appointmentType;
        this.appointmentStart = appointmentStart;
        this.appointmentEnd = appointmentEnd;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.updateDate = updateDate;
        this.updatedBy = updatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;

    }

    /**
     * @return the appointmentId
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * @return the appointmentTitle
     */
    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    /**
     * @return the appointmentDesc
     */
    public String getAppointmentDesc() {
        return appointmentDesc;
    }

    /**
     * @return the appointmentLocation
     */
    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    /**
     * @return the appointmentType
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * @return the appointmentStart
     */
    public LocalDateTime getAppointmentStart() {
        return appointmentStart;
    }

    /**
     * @return the appointmentEnd
     */
    public LocalDateTime getAppointmentEnd() {
        return appointmentEnd;
    }


    /**
     * @return the customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @return the contactId
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * @return the createDate
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }
}