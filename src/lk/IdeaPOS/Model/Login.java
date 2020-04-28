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
public class Login {

    private String userID;
    private String userName;
    private String role;
    private boolean activeState;

    public Login() {
    }

    public Login(String userID, String userName, String role, boolean activeState) {
        this.userID = userID;
        this.userName = userName;
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

    @Override
    public String toString() {
        return "Login{" + "userID=" + userID + ", userName=" + userName + ", role=" + role + ", activeState=" + activeState + '}';
    }

}
