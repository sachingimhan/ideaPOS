/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Controllers;

import animatefx.animation.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lk.IdeaPOS.Model.LoginDetail;
import lk.IdeaPOS.Util.DBUtil;
import lk.IdeaPOS.Util.Loader;
import lk.IdeaPOS.Util.MessageBox;
import lk.IdeaPOS.Util.MessageIconType;

/**
 * FXML Controller class
 *
 * @author root
 */
public class LoginController implements Initializable {

    @FXML
    private TextField txtUserName;
    @FXML
    private Button btnLogin;
    @FXML
    private StackPane loginStack;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private Pane loginPane;

    private PauseTransition p = new PauseTransition(Duration.seconds(3));
    @FXML
    private JFXButton btnExit;
    @FXML
    private Label lblError;

    private LoginDetail checkLogin;
    @FXML
    private AnchorPane AnchorLogin;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            FXMLLoader loadeFXML = new Loader().loadeFXML("View/SplashScreen.fxml");
            StackPane load = loadeFXML.load();
            loginStack.getChildren().add(load);
            SplashScreenController controller = loadeFXML.<SplashScreenController>getController();
            controller.run();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnLogin_OnAction(ActionEvent event) throws InterruptedException, IOException {
        if (txtUserName.getText().isEmpty()) {
            new Shake(txtUserName).play();
            txtUserName.setStyle("-jfx-unfocus-color:#ff0000;-fx-prompt-text-fill:#ffffff;-fx-text-fill:#ffffff;-jfx-focus-color:#ffffff;");
            p.setOnFinished(evt -> txtUserName.setStyle("-jfx-unfocus-color: #ffffff;-fx-prompt-text-fill:#ffffff;-fx-text-fill:#ffffff;-jfx-focus-color:#ffffff;"));
            p.play();
        } else if (txtPassword.getText().isEmpty()) {
            new Shake(txtPassword).play();
            txtPassword.setStyle("-jfx-unfocus-color:#ff0000;-fx-prompt-text-fill:#ffffff;-fx-text-fill:#ffffff;");
            p.setOnFinished(evt -> txtPassword.setStyle("-jfx-unfocus-color: #ffffff;-fx-prompt-text-fill:#ffffff;-fx-text-fill:#ffffff;"));
            p.play();
        } else {
            checkLogin = checkLogin(txtUserName.getText(), txtPassword.getText());
            if (checkLogin != null) {
                if (checkLogin.isActiveState()) {
                    new SlideOutDown(AnchorLogin).play();
                    setLogin(checkLogin);
                    p.setOnFinished(evt -> AnchorLogin.toBack());
                    p.play();
                } else {
                    lblError.setText("User Not Actived.!");
                    p.setOnFinished(evt -> lblError.setText(""));
                    p.play();
                    txtUserName.requestFocus();
                }

            } else {
                clearField();
                lblError.setText("UserName or Password Incorrect.!");
                new Shake(loginPane).play();
                p.setOnFinished(evt -> lblError.setText(""));
                p.play();
                txtUserName.requestFocus();
            }
        }

    }

    private void setLogin(LoginDetail login) throws IOException {
        FXMLLoader loadeFXML = new Loader().loadeFXML("View/Dashboard.fxml");
        StackPane load = loadeFXML.load();
        loginStack.getChildren().add(load);
        DashboardController controller = loadeFXML.<DashboardController>getController();
        controller.setLogin(login);
    }

    private void clearField() {
        txtPassword.setText("");
        txtUserName.setText("");
    }

    private LoginDetail checkLogin(String useName, String password) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT u.userID,u.userName,l.role,l.activeState FROM User u,LoginDetail l WHERE u.userID=l.userID AND l.userName=? AND l.password=md5(?)");
            pst.setString(1, useName);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new LoginDetail(rs.getString("userID"), rs.getString("userName"), rs.getString("role"), rs.getBoolean("activeState"));
            }
        } catch (NullPointerException | ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblError, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return null;
    }

    @FXML
    private void txtUserName_OnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }

    @FXML
    private void btnExit_OnAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void txtPassword_OnAction(ActionEvent event) {
        btnLogin.fire();
    }

}
