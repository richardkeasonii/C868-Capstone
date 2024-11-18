package com.example.helloworldjfxtemplate.model;


import java.time.LocalDateTime;

/**
 * Handles the setters and getters and holding of information for the customers in the database table.
 */
public class Customer {
    private int customerId;
    private String customerName;
    private String customerAddress;
    private String customerPostal;
    private String customerPhone;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime updateDate;
    private String updatedBy;
    private int customerDivision;
    private String customerType;

    /**
     * The main constructor for Customer.
     * @param customerId the customerId to set
     * @param customerName the customerName to set
     * @param customerAddress the customerAddress to set
     * @param customerPostal the customerPostal to set
     * @param customerPhone the customerPhone to set
     * @param createDate the createDate to set
     * @param createdBy the createdBy to set
     * @param updateDate the updateDate to set
     * @param updatedBy the updatedBy to set
     * @param customerDivision the customerDivision to set
     * @param customerType the customerType to set
     */
    public Customer(int customerId, String customerName, String customerAddress, String customerPostal,
                    String customerPhone, LocalDateTime createDate, String createdBy, LocalDateTime updateDate,
                    String updatedBy, int customerDivision, String customerType) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostal = customerPostal;
        this.customerPhone = customerPhone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.updateDate = updateDate;
        this.updatedBy = updatedBy;
        this.customerDivision = customerDivision;
        this.customerType = customerType;
    }

    /**
     * @return the customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @return the customerAddress
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * @return the customerPostal
     */
    public String getCustomerPostal() {
        return customerPostal;
    }

    /**
     * @return the customerPhone
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * @return the customerDivision
     */
    public int getCustomerDivision() {
        return customerDivision;
    }

    /**
     * @return the createDate
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @return the updateDate
     */
    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    /**
     * @return the updatedBy
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
}