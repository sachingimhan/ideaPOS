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
public class Supplier {
    private String supplierID;
    private String name;
    private String address;
    private String contact;

    public Supplier() {
    }

    public Supplier(String supplierID, String name, String address, String contact) {
        this.supplierID = supplierID;
        this.name = name;
        this.address = address;
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
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

    @Override
    public String toString() {
        return "Supplier{" + "supplierID=" + supplierID + ", name=" + name + ", address=" + address + ", contact=" + contact + '}';
    }
    
}
