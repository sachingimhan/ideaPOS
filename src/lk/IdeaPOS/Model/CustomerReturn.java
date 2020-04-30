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
public class CustomerReturn {
 
    private String orderID;
    private String itemCode;
    private String userID;
    private String retDate;
    private double returnQty;
    private double unitPrice;
    private double totalAmount;

    public CustomerReturn() {
    }

    public CustomerReturn(String orderID, String itemCode, String userID, String retDate, double returnQty, double unitPrice, double totalAmount) {
        this.orderID = orderID;
        this.itemCode = itemCode;
        this.userID = userID;
        this.retDate = retDate;
        this.returnQty = returnQty;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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

    public double getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(double returnQty) {
        this.returnQty = returnQty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "CustomerReturn{" + "orderID=" + orderID + ", itemCode=" + itemCode + ", userID=" + userID + ", retDate=" + retDate + ", returnQty=" + returnQty + ", unitPrice=" + unitPrice + ", totalAmount=" + totalAmount + '}';
    }
    
    
}
