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
public class ReturnItem {
    private String  retID;
    private String itemCode;
    private double unitPrice;
    private double returnQty;
    private double subTotal;

    public ReturnItem() {
    }

    public ReturnItem(String retID, String itemCode, double unitPrice, double returnQty, double subTotal) {
        this.retID = retID;
        this.itemCode = itemCode;
        this.unitPrice = unitPrice;
        this.returnQty = returnQty;
        this.subTotal = subTotal;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public String getRetID() {
        return retID;
    }

    public void setRetID(String retID) {
        this.retID = retID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(double returnQty) {
        this.returnQty = returnQty;
    }

    @Override
    public String toString() {
        return "ReturnItem{" + "retID=" + retID + ", itemCode=" + itemCode + ", unitPrice=" + unitPrice + ", returnQty=" + returnQty + ", subTotal=" + subTotal + '}';
    }
    
    
}
