/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Model;

/**
 *
 * @author root
 */
public class Customer {

    private String custID;
    private String custName;
    private String custAddress;
    private String custContact;

    public Customer() {
    }

    public Customer(String custID, String custName, String custAddress, String custContact) {
        this.custID = custID;
        this.custName = custName;
        this.custAddress = custAddress;
        this.custContact = custContact;
    }

    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getCustContact() {
        return custContact;
    }

    public void setCustContact(String custContact) {
        this.custContact = custContact;
    }

    @Override
    public String toString() {
        return "Customer{" + "custID=" + custID + ", custName=" + custName + ", custAddress=" + custAddress + ", custContact=" + custContact + '}';
    }
    
}
