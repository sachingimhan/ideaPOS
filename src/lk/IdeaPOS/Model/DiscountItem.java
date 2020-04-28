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
public class DiscountItem {

    private int disCode;
    private String itemCode;
    private String itemName;
    private double costPrice;
    private String discount;
    private double discountAmount;
    private String disDate;

    public DiscountItem() {
    }

    public DiscountItem(int disCode, String itemCode, String itemName, double costPrice, String discount, double discountAmount, String disDate) {
        this.disCode = disCode;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.costPrice = costPrice;
        this.discount = discount;
        this.discountAmount = discountAmount;
        this.disDate = disDate;
    }

    public DiscountItem(int disCode, String itemCode, double costPrice, String discount, double discountAmount, String disDate) {
        this.disCode = disCode;
        this.itemCode = itemCode;
        this.costPrice = costPrice;
        this.discount = discount;
        this.discountAmount = discountAmount;
        this.disDate = disDate;
    }

    
    
    public DiscountItem(String itemCode, double costPrice, String discount, double discountAmount, String disDate) {
        this.itemCode = itemCode;
        this.costPrice = costPrice;
        this.discount = discount;
        this.discountAmount = discountAmount;
        this.disDate = disDate;
    }

    public int getDisCode() {
        return disCode;
    }

    public void setDisCode(int disCode) {
        this.disCode = disCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDisDate() {
        return disDate;
    }

    public void setDisDate(String disDate) {
        this.disDate = disDate;
    }

    @Override
    public String toString() {
        return "DiscountItem{" + "disCode=" + disCode + ", itemCode=" + itemCode + ", itemName=" + itemName + ", costPrice=" + costPrice + ", discount=" + discount + ", discountAmount=" + discountAmount + ", disDate=" + disDate + '}';
    }

    
}
