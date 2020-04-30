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
public class Item {

    private String itemCode;
    private String itemName;
    private double costPrice;
    private double retailPrice;
    private double wholeSalePrice;
    private double itemQty;
    private double discount;
    private int reorderLevel;

    public Item() {
    }

    public Item(String itemCode, String itemName, double costPrice, double retailPrice, double wholeSalePrice, double itemQty, int reorderLevel) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.costPrice = costPrice;
        this.retailPrice = retailPrice;
        this.wholeSalePrice = wholeSalePrice;
        this.itemQty = itemQty;
        this.reorderLevel = reorderLevel;
    }

    public Item(String itemCode, String itemName, double costPrice, double retailPrice, double wholeSalePrice, double itemQty, double discount, int reorderLevel) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.costPrice = costPrice;
        this.retailPrice = retailPrice;
        this.wholeSalePrice = wholeSalePrice;
        this.itemQty = itemQty;
        this.discount = discount;
        this.reorderLevel = reorderLevel;
    }
    
    public Item(String itemCode, String itemName, double retailPrice, double discount, double itemQty) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.retailPrice = retailPrice;
        this.discount = discount;
        this.itemQty = itemQty;
    }

    public Item(String itemCode, String itemName, double retailPrice) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.retailPrice = retailPrice;
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

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getWholeSalePrice() {
        return wholeSalePrice;
    }

    public void setWholeSalePrice(double wholeSalePrice) {
        this.wholeSalePrice = wholeSalePrice;
    }

    public double getItemQty() {
        return itemQty;
    }

    public void setItemQty(double itemQty) {
        this.itemQty = itemQty;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    @Override
    public String toString() {
        return "Item{" + "itemCode=" + itemCode + ", itemName=" + itemName + ", costPrice=" + costPrice + ", retailPrice=" + retailPrice + ", wholeSalePrice=" + wholeSalePrice + ", itemQty=" + itemQty + ", discount=" + discount + ", reorderLevel=" + reorderLevel + '}';
    }

}
