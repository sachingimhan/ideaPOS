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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
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
import javafx.scene.layout.AnchorPane;
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
    @FXML
    private JFXButton btnSetPassword;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXRadioButton rbDeactive;
    @FXML
    private JFXRadioButton rbActive;
    @FXML
    private JFXTextField txtBusinessName;
    @FXML
    private JFXTextField txtBissReg;
    private JFXTextField txtLogoPath;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXTextField txtBizAddress;
    @FXML
    private JFXTextField txtBizContactNo;
    @FXML
    private AnchorPane root;
    @FXML
    private JFXTextField txtDbHost;
    @FXML
    private JFXTextField txtDbUser;
    @FXML
    private JFXPasswordField txtDbPassword;
    @FXML
    private JFXButton btnConfigDb;
    @FXML
    private JFXTextField txtDbName;
    @FXML
    private JFXTextField txtDbPort;

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
        //---------------------------------
        ToggleGroup group = new ToggleGroup();
        rbAdmin.setToggleGroup(group);
        rbCashier.setToggleGroup(group);

        ToggleGroup state = new ToggleGroup();
        rbActive.setToggleGroup(state);
        rbDeactive.setToggleGroup(state);
        //-----------------------------------------
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (rbAdmin.isSelected()) {
                    role = rbAdmin.getText();
                }
                if (rbCashier.isSelected()) {
                    role = rbCashier.getText();
                }
                if (tblUser.getSelectionModel().getSelectedItem() != null && role != null) {
                    if (MessageBox.showConfMessage("Are you sure?", "Confirmation")) {
                        boolean updatePrivilege = updatePrivilege(tblUser.getSelectionModel().getSelectedItem().getUserID());
                        if (updatePrivilege) {
                            MessageBox.show(3, lblMessage, tblUser.getSelectionModel().getSelectedItem().getUserID() + " User Privilege has been Changed to " + role, MessageIconType.INFORMATION);
                            loadTableView();
                            clear();
                        }
                    }
                }
            }
        });
        state.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (rbActive.isSelected()) {
                    isActive = true;
                }
                if (rbDeactive.isSelected()) {
                    isActive = false;
                }

                if (tblUser.getSelectionModel().getSelectedItem() != null && (rbActive.isSelected() || rbDeactive.isSelected())) {
                    if (MessageBox.showConfMessage("Are you sure?", "Confirmation")) {
                        boolean activeStateChange = activeStateChange(tblUser.getSelectionModel().getSelectedItem().getUserID());
                        if (activeStateChange) {
                            MessageBox.show(3, lblMessage, tblUser.getSelectionModel().getSelectedItem().getUserID() + " User Account has been : " + (isActive == true ? "Activated!" : "Deactivated!"), MessageIconType.INFORMATION);
                            loadTableView();
                            clear();
                        }
                    }
                }
            }
        });
        //---------------------------
        getUserID();
        readProperties();
    }

    private void readProperties() {
        //get currnt path
        String pwd = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        try {
            //
            InputStream inputStream = new FileInputStream(pwd + "/Settings/BusinessData.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            txtBusinessName.setText(properties.getProperty("biz.Name"));
            txtBizAddress.setText(properties.getProperty("biz.Address"));
            txtBizContactNo.setText(properties.getProperty("biz.Contact"));
            txtBissReg.setText(properties.getProperty("biz.RegNo"));
        } catch (IOException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
    }

    private boolean activeStateChange(String userID) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("UPDATE LoginDetail SET activeState=? WHERE userID=?");
            pst.setBoolean(1, isActive);
            pst.setString(2, userID);
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return false;
    }

    private boolean updatePrivilege(String userID) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("UPDATE LoginDetail SET role=? WHERE userID=?");
            pst.setString(1, role);
            pst.setString(2, userID);
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return false;
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
            return pst.executeUpdate() > 0;
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
        if (txtUseID.getText().isEmpty() || txtFullName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtContact.getText().isEmpty()) {
            MessageBox.show(3, lblMessage, "Please Fill the Form", MessageIconType.WARNING);
        } else {
            boolean b = insertUser(new User(
                    txtUseID.getText(),
                    txtFullName.getText(),
                    txtAddress.getText(),
                    txtContact.getText()
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

    private boolean updateUser(User user) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("UPDATE User SET userName=?, userAddress=?, userContact=? WHERE userID=?");
            pst.setString(1, user.getUserName());
            pst.setString(2, user.getUserAddress());
            pst.setString(3, user.getUserContact());
            pst.setString(4, user.getUserID());
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return false;
    }

    @FXML
    private void btnChangePassword_OnAction(ActionEvent event) {
        if (tblUser.getItems().size() > 0 && tblUser.getSelectionModel().getSelectedItem() != null && !txtPassword.getText().isEmpty()) {
            if (MessageBox.showConfMessage("Are you sure?", "Confirmation")) {
                boolean changePassword = changePassword(txtPassword.getText(), tblUser.getSelectionModel().getSelectedItem().getUserID());
                if (changePassword) {
                    MessageBox.show(3, lblMessage, "User Password has been Changed.!", MessageIconType.INFORMATION);
                    loadTableView();
                    clear();
                }
            }
        } else {
            MessageBox.show(3, lblMessage, "Please Select User or type password.!", MessageIconType.WARNING);
        }
    }

    private boolean changePassword(String password, String useID) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("LoginDetail SET password=md5(?) WHERE userID=?");
            pst.setString(1, password);
            pst.setString(2, useID);
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return false;
    }

    @FXML
    private void tblUser_OnMouseClicked(MouseEvent event) {
        if (tblUser.getSelectionModel().getSelectedItem() != null && event.getClickCount() == 2) {
            User selectedItem = tblUser.getSelectionModel().getSelectedItem();
            txtUseID.setText(selectedItem.getUserID());
            txtFullName.setText(selectedItem.getUserName());
            txtAddress.setText(selectedItem.getUserAddress());
            txtContact.setText(selectedItem.getUserContact());
        }
    }

    @FXML
    private void btnUpdate_OnAction(ActionEvent event) {
        if (tblUser.getItems().size() > 0 && tblUser.getSelectionModel().getSelectedItem() != null) {
            if (txtUseID.getText().isEmpty() || txtFullName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtContact.getText().isEmpty()) {
                MessageBox.show(3, lblMessage, "Please Fill the Form", MessageIconType.WARNING);
            } else {
                boolean b = updateUser(new User(
                        txtUseID.getText(),
                        txtFullName.getText(),
                        txtAddress.getText(),
                        txtContact.getText()
                ));
                if (b) {
                    MessageBox.show(3, lblMessage, "User has been Updated.!", MessageIconType.INFORMATION);
                    loadTableView();
                    clear();
                }
            }
        } else {
            MessageBox.show(3, lblMessage, "Please Select User.!", MessageIconType.WARNING);
        }

    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) {
        if (tblUser.getItems().size() > 0 && tblUser.getSelectionModel().getSelectedItem() != null) {
            if (MessageBox.showConfMessage("Are you sure?", "Confirmation")) {
                boolean deleteUser = deleteUser(tblUser.getSelectionModel().getSelectedItem().getUserID());
                if (deleteUser) {
                    MessageBox.show(3, lblMessage, "User has been Deleted.!", MessageIconType.INFORMATION);
                    loadTableView();
                    clear();
                }
            }
        } else {
            MessageBox.show(3, lblMessage, "Please Select User.!", MessageIconType.WARNING);
        }
    }

    private boolean deleteUser(String userID) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("DELETE FROM User WHERE userID=?");
            pst.setString(1, userID);
            return pst.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return false;
    }

    @FXML
    private void btnSetPassword_OnAction(ActionEvent event) {
        if (tblUser.getItems().size() > 0 && tblUser.getSelectionModel().getSelectedItem() != null) {
            if (txtUserName.getText().isEmpty() || txtPassword.getText().isEmpty()) {
                MessageBox.show(3, lblMessage, "User Name or Password Empty.!", MessageIconType.WARNING);
            } else {
                boolean b = insertLoginDetails(new LoginDetail(
                        tblUser.getSelectionModel().getSelectedItem().getUserID(),
                        txtUserName.getText(),
                        txtPassword.getText(),
                        role,
                        isActive
                ));
                if (b) {
                    MessageBox.show(3, lblMessage, "Password added to User.!", MessageIconType.INFORMATION);
                    loadTableView();
                    clear();
                }
            }
        } else {
            MessageBox.show(3, lblMessage, "Please Select User.!", MessageIconType.WARNING);
        }
    }

    @FXML
    private void btnClear_OnAction(ActionEvent event) {
        clear();
    }
    //----------------------------General Setting---

    @FXML
    private void btnSave_OnAction(ActionEvent event) {
        createProperties();
        readProperties();
    }

    private void createProperties() {
        //get currnt path
        String pwd = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        // Create Settings Folder
        File file = new File("Settings");
        file.mkdir();
        try {
            //
            OutputStream outputStream = new FileOutputStream(pwd + "/Settings/BusinessData.properties");
            Properties properties = new Properties();
            properties.setProperty("biz.Name", txtBusinessName.getText());
            properties.setProperty("biz.Address", txtBizAddress.getText());
            properties.setProperty("biz.Contact", txtBizContactNo.getText());
            properties.setProperty("biz.RegNo", txtBissReg.getText());
            properties.store(outputStream, LocalDate.now().toString());
            MessageBox.show(3, lblMessage, "Data Saved.!", MessageIconType.INFORMATION);
        } catch (IOException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
    }

    @FXML
    private void btnConfigDb_OnAction(ActionEvent event) {
        ConfigDB();
    }

    private void ConfigDB() {
        //get currnt path
        String pwd = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        // Create Settings Folder
        File file = new File("Settings");
        file.mkdir();
        try {
            //
            if (MessageBox.showConfMessage("Warning: This will Break Your System. Continue?", "Warning.!")) {
                OutputStream outputStream = new FileOutputStream(pwd + "/Settings/Config.properties");
                Properties properties = new Properties();
                properties.setProperty("db.Host", txtDbHost.getText());
                properties.setProperty("db.User", txtDbUser.getText());
                properties.setProperty("db.Pass", txtDbPassword.getText());
                properties.setProperty("db.Port", txtDbPort.getText());
                properties.setProperty("db.DbName", txtDbName.getText());
                properties.store(outputStream, LocalDate.now().toString());
                MessageBox.show(3, lblMessage, "Data Saved.!", MessageIconType.INFORMATION);
            }
        } catch (IOException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
    }

}
