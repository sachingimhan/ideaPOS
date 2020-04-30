/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Model;

import java.math.BigDecimal;
import javafx.collections.ObservableList;

/**
 *
 * @author root
 */
public class Order {

    private String orderID;
    private String custID;
    private String userID;
    private String orderDate;
    private String paymentMethod;
    private double grossAmount;
    private double netAmount;
    private double cash;
    private double balance;
    private ObservableList<OrderItem> orderItems;

    public Order() {
    }

    public Order(String custID, double netAmount) {
        this.custID = custID;
        this.netAmount = netAmount;
    }
    
    public Order(String orderID, String custID, String userID, String orderDate, String paymentMethod, double grossAmount, double netAmount, double cash, double balance, ObservableList<OrderItem> orderItems) {
        this.orderID = orderID;
        this.custID = custID;
        this.userID = userID;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
        this.grossAmount = grossAmount;
        this.netAmount = netAmount;
        this.cash = cash;
        this.balance = balance;
        this.orderItems = orderItems;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(double grossAmount) {
        this.grossAmount = grossAmount;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public ObservableList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ObservableList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "Order{" + "orderID=" + orderID + ", custID=" + custID + ", userID=" + userID + ", orderDate=" + orderDate + ", paymentMethod=" + paymentMethod + ", grossAmount=" + grossAmount + ", netAmount=" + netAmount + ", cash=" + cash + ", balance=" + balance + ", orderItems=" + orderItems + '}';
    }

   
}
