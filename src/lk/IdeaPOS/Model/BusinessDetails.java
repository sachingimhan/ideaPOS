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
public class BusinessDetails {
    private String name;
    private String address;
    private String contact;
    private String regNo;

    public BusinessDetails() {
    }

    public BusinessDetails(String name, String address, String contact, String regNo) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "BusinessDetails{" + "name=" + name + ", address=" + address + ", contact=" + contact + ", regNo=" + regNo + '}';
    }
    
    
}
