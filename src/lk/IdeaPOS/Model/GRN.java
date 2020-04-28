/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Model;

import com.jfoenix.controls.JFXCheckBox;

/**
 *
 * @author root
 */
public class GRN {

    private String grnID;
    private String itemCode;
    private String supplierID;
    private String userID;
    private String batchNo;
    private String grnDate;
    private int qty;
    private double unitPrice;
    private double totalCost;
    private String mdf;
    private String expiryDate;
    private boolean isConformed;
    private JFXCheckBox checkBox;

    public GRN() {
    }

    public GRN(String grnID, String itemCode, String supplierID, String userID, String batchNo, String grnDate, int qty, double unitPrice, double totalCost, String mdf, String expiryDate) {
        this.grnID = grnID;
        this.itemCode = itemCode;
        this.supplierID = supplierID;
        this.userID = userID;
        this.batchNo = batchNo;
        this.grnDate = grnDate;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.totalCost = totalCost;
        this.mdf = mdf;
        this.expiryDate = expiryDate;
    }

    public GRN(String grnID, String itemCode, String supplierID, String batchNo, String grnDate, int qty, double unitPrice, double totalCost, String mdf, String expiryDate) {
        this.grnID = grnID;
        this.itemCode = itemCode;
        this.supplierID = supplierID;
        this.batchNo = batchNo;
        this.grnDate = grnDate;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.totalCost = totalCost;
        this.mdf = mdf;
        this.expiryDate = expiryDate;
    }

    public GRN(String grnID, String itemCode, String supplierID, String userID, int qty, double unitPrice) {
        this.grnID = grnID;
        this.itemCode = itemCode;
        this.supplierID = supplierID;
        this.userID = userID;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.checkBox = new JFXCheckBox();
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getGrnID() {
        return grnID;
    }

    public void setGrnID(String grnID) {
        this.grnID = grnID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getGrnDate() {
        return grnDate;
    }

    public void setGrnDate(String grnDate) {
        this.grnDate = grnDate;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getMdf() {
        return mdf;
    }

    public void setMdf(String mdf) {
        this.mdf = mdf;
    }

    public boolean isConformed() {
        return isConformed;
    }

    public void setIsConformed(boolean isConformed) {
        this.isConformed = isConformed;
    }

    public JFXCheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(JFXCheckBox checkBox) {
        this.checkBox = checkBox;
    }

    @Override
    public String toString() {
        return "GRN{" + "grnID=" + grnID + ", itemCode=" + itemCode + ", supplierID=" + supplierID + ", userID=" + userID + ", batchNo=" + batchNo + ", grnDate=" + grnDate + ", qty=" + qty + ", unitPrice=" + unitPrice + ", totalCost=" + totalCost + ", mdf=" + mdf + ", expiryDate=" + expiryDate + ", isConformed=" + isConformed + '}';
    }

}
