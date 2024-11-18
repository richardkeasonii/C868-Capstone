package com.example.helloworldjfxtemplate.model;

import java.time.LocalDateTime;

public class Company extends Customer{
    /**
     * The main constructor for Customer.
     *
     * @param customerId       the customerId to set
     * @param customerName     the customerName to set
     * @param customerAddress  the customerAddress to set
     * @param customerPostal   the customerPostal to set
     * @param customerPhone    the customerPhone to set
     * @param createDate       the createDate to set
     * @param createdBy        the createdBy to set
     * @param updateDate       the updateDate to set
     * @param updatedBy        the updatedBy to set
     * @param customerDivision the customerDivision to set
     * @param customerType the customerType to set
     */
    public Company(int customerId, String customerName, String customerAddress, String customerPostal, String customerPhone, LocalDateTime createDate, String createdBy, LocalDateTime updateDate, String updatedBy, int customerDivision, String customerType) {
        super(customerId, customerName, customerAddress, customerPostal, customerPhone, createDate, createdBy, updateDate, updatedBy, customerDivision, customerType);
    }


}
