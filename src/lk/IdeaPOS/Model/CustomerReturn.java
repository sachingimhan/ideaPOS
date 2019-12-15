/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Model;

import javafx.collections.ObservableList;

/**
 *
 * @author root
 */
public class CustomerReturn {

    private String retID;
    private String orderID;
    private String userID;
    private String retDate;
    private String billDate;
    private double totalAmount;
    private ObservableList<ReturnItem> list;

    public CustomerReturn() {
    }

    public CustomerReturn(String retID, String orderID, String userID, String retDate, String billDate, double totalAmount, ObservableList<ReturnItem> list) {
        this.retID = retID;
        this.orderID = orderID;
        this.userID = userID;
        this.retDate = retDate;
        this.billDate = billDate;
        this.totalAmount = totalAmount;
        this.list = list;
    }

    public String getRetID() {
        return retID;
    }

    public void setRetID(String retID) {
        this.retID = retID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRetDate() {
        return retDate;
    }

    public void setRetDate(String retDate) {
        this.retDate = retDate;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ObservableList<ReturnItem> getList() {
        return list;
    }

    public void setList(ObservableList<ReturnItem> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "CustomerReturn{" + "retID=" + retID + ", orderID=" + orderID + ", userID=" + userID + ", retDate=" + retDate + ", billDate=" + billDate + ", totalAmount=" + totalAmount + ", list=" + list + '}';
    }

    
}
