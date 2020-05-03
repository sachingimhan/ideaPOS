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
public class User {
    private String userID;
    private String userName;
    private String userAddress;
    private String userContact;
    private LoginDetail detail;

    public User() {
    }

    public User(String userID, String userName, String userAddress, String userContact) {
        this.userID = userID;
        this.userName = userName;
        this.userAddress = userAddress;
        this.userContact = userContact;
    }

    public User(String userID, String userName, String userAddress, String userContact, LoginDetail detail) {
        this.userID = userID;
        this.userName = userName;
        this.userAddress = userAddress;
        this.userContact = userContact;
        this.detail = detail;
    }
    
    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public LoginDetail getDetail() {
        return detail;
    }

    public void setDetail(LoginDetail detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "User{" + "userID=" + userID + ", userName=" + userName + ", userAddress=" + userAddress + ", userContact=" + userContact + ", detail=" + detail + '}';
    }

    
}
