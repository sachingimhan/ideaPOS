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
public class LoginDetail {

    private String userID;
    private String userName;
    private String password;
    private String role;
    private boolean activeState;

    public LoginDetail() {
    }

    public LoginDetail(String userID, String userName, String role, boolean activeState) {
        this.userID = userID;
        this.userName = userName;
        this.role = role;
        this.activeState = activeState;
    }

    public LoginDetail(String userID, String userName, String password, String role, boolean activeState) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.activeState = activeState;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActiveState() {
        return activeState;
    }

    public void setActiveState(boolean activeState) {
        this.activeState = activeState;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Login{" + "userID=" + userID + ", userName=" + userName + ", password=" + password + ", role=" + role + ", activeState=" + activeState + '}';
    }

}
