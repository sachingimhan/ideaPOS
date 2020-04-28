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
public class OrderItem {

    private String orderID;
    private String itemCode;
    private String description;
    private double unitPrice;
    private String qty;
    private double discount;
    private double subTotal;

    public OrderItem() {
    }

    public OrderItem(String orderID, String itemCode, String description, double unitPrice, String qty, double discount, double subTotal) {
        this.orderID = orderID;
        this.itemCode = itemCode;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.discount = discount;
        this.subTotal = subTotal;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public String toString() {
        return "OrderItem{" + "orderID=" + orderID + ", itemCode=" + itemCode + ", description=" + description + ", unitPrice=" + unitPrice + ", qty=" + qty + ", discount=" + discount + ", subTotal=" + subTotal + '}';
    }
    

}
