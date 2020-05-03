/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import lk.IdeaPOS.Model.LoginDetail;
import lk.IdeaPOS.Model.User;
import lk.IdeaPOS.Util.DBUtil;
import lk.IdeaPOS.Util.MessageBox;
import lk.IdeaPOS.Util.MessageIconType;

/**
 * FXML Controller class
 *
 * @author root
 */
public class SettingsController implements Initializable {

    @FXML
    private JFXTextField txtUseID;
    @FXML
    private JFXTextField txtFullName;
    @FXML
    private JFXTextField txtAddress;
    @FXML
    private JFXTextField txtContact;
    @FXML
    private JFXToggleButton tglActive;
    @FXML
    private JFXTextField txtUserName;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private JFXButton btnAddUser;
    @FXML
    private JFXButton btnChangePassword;
    @FXML
    private TableView<User> tblUser;
    @FXML
    private TableColumn<User, String> colUseID;
    @FXML
    private TableColumn<User, String> colName;
    @FXML
    private TableColumn<User, String> colAddress;
    @FXML
    private TableColumn<User, String> colContact;
    @FXML
    private JFXButton btnUpdate;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private Label lblMessage;
    @FXML
    private JFXRadioButton rbAdmin;
    @FXML
    private JFXRadioButton rbCashier;

    private String role;
    private boolean isActive;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colUseID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("userAddress"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("userContact"));

        loadTableView();

        ToggleGroup group = new ToggleGroup();
        rbAdmin.setToggleGroup(group);
        rbCashier.setToggleGroup(group);
        rbCashier.setSelected(true);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (rbAdmin.isSelected()) {
                    role = rbAdmin.getText();
                }
                if (rbCashier.isSelected()) {
                    role = rbCashier.getText();
                }
            }
        });

        tglActive.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (tglActive.isSelected()) {
                    isActive = true;
                } else {
                    isActive = false;
                }
            }
        });

        getUserID();
    }

    private void loadTableView() {
        tblUser.setItems(loadUser());
    }

    private void getUserID() {
        Random r = new Random(System.currentTimeMillis());
        long l = 10000 + r.nextInt(20000);
        l = l < 0 ? -l : l;
        txtUseID.setText(Long.toOctalString(l));
    }

    private ObservableList<User> loadUser() {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT * FROM User");
            ObservableList<User> list = FXCollections.observableArrayList();
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new User(rs.getString("userID"), rs.getString("userName"), rs.getString("userAddress"), rs.getString("userContact")));
            }
            return list;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return null;
    }

    private boolean insertUser(User user) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("INSERT INTO User VALUES(?,?,?,?)");
            pst.setString(1, user.getUserID());
            pst.setString(2, user.getUserName());
            pst.setString(3, user.getUserAddress());
            pst.setString(4, user.getUserContact());
            boolean b = pst.executeUpdate() > 0;
            if (b) {
                boolean insertLoginDetails = insertLoginDetails(user.getDetail());
                if (!insertLoginDetails) {
                    return false;
                }
            }
            return b;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return false;
    }

    private boolean insertLoginDetails(LoginDetail detail) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("INSERT INTO LoginDetail VALUES(?,?,md5(?),?,?)");
            pst.setString(1, detail.getUserID());
            pst.setString(2, detail.getUserName());
            pst.setString(3, detail.getPassword());
            pst.setString(4, detail.getRole());
            pst.setBoolean(5, detail.isActiveState());
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return false;
    }

    @FXML
    private void btnAddUser_OnAction(ActionEvent event) {
        if (txtUseID.getText().isEmpty() || txtFullName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtContact.getText().isEmpty() || txtUserName.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            MessageBox.show(3, lblMessage, "Please Fill the Form", MessageIconType.WARNING);
        } else {
            boolean b = insertUser(new User(
                    txtUseID.getText(),
                    txtFullName.getText(),
                    txtAddress.getText(),
                    txtContact.getText(),
                    new LoginDetail(
                            txtUseID.getText(),
                            txtUserName.getText(),
                            txtPassword.getText(),
                            role,
                            isActive
                    )
            ));
            if (b) {
                MessageBox.show(3, lblMessage, "New User has been Created.!", MessageIconType.INFORMATION);
                loadTableView();
                clear();
            }
        }
    }

    private void clear() {
        txtFullName.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        txtPassword.setText("");
        txtUserName.setText("");
        getUserID();
    }

    @FXML
    private void btnChangePassword_OnAction(ActionEvent event) {
    }

    @FXML
    private void tblUser_OnMouseClicked(MouseEvent event) {
    }

    @FXML
    private void btnUpdate_OnAction(ActionEvent event) {
    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) {
    }

    @FXML
    private void tblUser_OnMousePressed(MouseEvent event) {
        if (tblUser.getSelectionModel().getSelectedItem() != null && event.getClickCount() == 2) {
            User selectedItem = tblUser.getSelectionModel().getSelectedItem();
            txtUseID.setText(selectedItem.getUserID());
            txtFullName.setText(selectedItem.getUserName());
            txtAddress.setText(selectedItem.getUserAddress());
            txtContact.setText(selectedItem.getUserContact());
        }
    }

}
