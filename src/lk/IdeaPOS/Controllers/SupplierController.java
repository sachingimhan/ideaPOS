/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Controllers;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.IdeaPOS.Model.Login;
import lk.IdeaPOS.Model.Supplier;
import lk.IdeaPOS.Util.DBUtil;
import lk.IdeaPOS.Util.MessageBox;

/**
 *
 * @author root
 */
public class SupplierController implements Initializable {

    @FXML
    private TextField txtSupplierFullName;
    @FXML
    private TextField txtSupplierAddress;
    @FXML
    private TextField txtSupplierContact;
    @FXML
    private Button btnSupplierDelete;
    @FXML
    private Button btnSupplierUpdate;
    @FXML
    private Button btnSupplierSave;
    @FXML
    private TableView<Supplier> tblSupplier;
    @FXML
    private TableColumn<Supplier, String> colSupplierID;
    @FXML
    private TableColumn<Supplier, String> colSupplierName;
    @FXML
    private TableColumn<Supplier, String> colSupplierAddress;
    @FXML
    private TableColumn<Supplier, String> colSupplierContact;
    @FXML
    private Label lblSuplierID;
    @FXML
    private Button btnSupplierClear;

    private String supID;
    @FXML
    private TextField txtSearch;

    private ObservableList<Supplier> obList;
    @FXML
    private Label lblMessage;

    private Login login;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colSupplierID.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSupplierAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSupplierContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        getSupplierID();
        loadSupplierDataView();
        //validate contact feild
        txtSupplierContact.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!txtSupplierContact.getText().matches("[0-9]+") && !txtSupplierContact.getText().isEmpty()) {
                txtSupplierContact.setText(oldValue);
                txtSupplierContact.positionCaret(txtSupplierContact.getText().length());
            }
        });
        //validate Name feild
        txtSupplierFullName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!txtSupplierFullName.getText().matches("[a-z A-Z.]+") && !txtSupplierFullName.getText().isEmpty()) {
                txtSupplierFullName.setText(oldValue);
                txtSupplierFullName.positionCaret(txtSupplierFullName.getText().length());
            }
        });
        //filtering Suppliers
        FilteredList<Supplier> flist = new FilteredList<>(obList, s -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            tblSupplier.setItems(searchSupplier(newValue));
        });
    }
    
    public void setLogin(Login login) {
        this.login = login;
        if (!this.login.getRole().equals("Administrator")) {
            btnSupplierUpdate.setDisable(true);
            btnSupplierDelete.setDisable(true);
        }
    }
    
    private ObservableList<Supplier> searchSupplier(String filter) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT * FROM Supplier WHERE CONCAT(supplierID,name,contact) LIKE '%" + filter + "%'");
            ResultSet rs = pst.executeQuery();
            ObservableList<Supplier> list = FXCollections.observableArrayList();
            while (rs.next()) {
                list.add(new Supplier(
                        rs.getString("supplierID"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("contact")
                ));
            }
            return list;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return null;
    }

    private void loadSupplierDataView() {
        obList = loardSupplierDataTable();
        tblSupplier.setItems(obList);
    }

    private void getSupplierID() {
        String last = getLastSupplierID();
        if (last != null) {
            String[] split = last.split("-");
            int num = Integer.parseInt(split[1]);
            num++;
            if (num < 10) {
                supID = "SUP-000" + num;
            } else if (num < 100) {
                supID = "SUP-00" + num;
            } else if (num < 1000) {
                supID = "SUP-0" + num;
            } else {
                supID = "SUP-" + num;
            }
        } else {
            supID = "SUP-0001";
        }
        lblSuplierID.setText(supID);
    }

    private void clearSupplierFeild() {
        txtSupplierAddress.setText("");
        txtSupplierContact.setText("");
        txtSupplierFullName.setText("");
        getSupplierID();
    }

    private String getLastSupplierID() {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT supplierID FROM Supplier ORDER BY supplierID DESC LIMIT 1");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
           MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return null;
    }

    private boolean insertSupplier(Supplier sup) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("INSERT INTO Supplier VALUES(?,?,?,?)");
            pst.setString(1, sup.getSupplierID());
            pst.setString(2, sup.getName());
            pst.setString(3, sup.getAddress());
            pst.setString(4, sup.getContact());
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return false;
    }

    private ObservableList<Supplier> loardSupplierDataTable() {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT * FROM Supplier");
            ResultSet rs = pst.executeQuery();
            ObservableList<Supplier> obList = FXCollections.observableArrayList();
            while (rs.next()) {
                obList.add(new Supplier(
                        rs.getString("supplierID"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("contact")
                ));
            }
            return obList;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return null;
    }

    private boolean updateSupplier(Supplier sup) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("UPDATE Supplier SET name=?,address=?,contact=? WHERE supplierID=?");
            pst.setString(1, sup.getName());
            pst.setString(2, sup.getAddress());
            pst.setString(3, sup.getContact());
            pst.setString(4, sup.getSupplierID());
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return false;
    }

    private boolean deleteSupplier(String supID) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("DELETE FROM Supplier WHERE supplierID=?");
            pst.setString(1, supID);
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return false;
    }

    @FXML
    private void btnSupplierDelete_OnAction(ActionEvent event) {
        if (tblSupplier.getItems().size() > 0) {
            if (tblSupplier.getSelectionModel().getSelectedItem() != null) {
                if (MessageBox.showConfMessage("Are you sure?", "Delete Confirmation")) {
                    boolean flag = deleteSupplier(tblSupplier.getSelectionModel().getSelectedItem().getSupplierID());
                    if (flag) {
                        MessageBox.messageLable(3, lblMessage, "Success: Supplier has been Deleted.!", "", "-fx-text-fill:#2ecc71");
                    }
                    clearSupplierFeild();
                    loadSupplierDataView();
                }
            } else {
                MessageBox.messageLable(3, lblMessage, "Warning: Please select Item to Delete.!", "", "-fx-text-fill:#e67e22");
            }
        } else {
            MessageBox.messageLable(3, lblMessage, "Warning: Database Empty.!", "", "-fx-text-fill:#e67e22");
        }
    }

    @FXML
    private void btnSupplierUpdate_OnAction(ActionEvent event) {
        if (txtSupplierFullName.getText().isEmpty() || txtSupplierAddress.getText().isEmpty() || txtSupplierContact.getText().isEmpty()) {
            MessageBox.messageLable(3, lblMessage, "Warning: Please Fill the Form.!", "", "-fx-text-fill:#e67e22");
        } else if (MessageBox.showConfMessage("Are you sure?", "Update Confirmation")) {
            boolean flag = updateSupplier(new Supplier(lblSuplierID.getText(), txtSupplierFullName.getText(), txtSupplierAddress.getText(), txtSupplierContact.getText()));
            if (flag) {
                MessageBox.messageLable(3, lblMessage, "Success: Supplier has been Updated.!", "", "-fx-text-fill:#2ecc71");
            }
            clearSupplierFeild();
            loadSupplierDataView();
        }
    }

    @FXML
    private void btnSupplierSave_OnAction(ActionEvent event) {
        if (txtSupplierFullName.getText().isEmpty() || txtSupplierAddress.getText().isEmpty() || txtSupplierContact.getText().isEmpty()) {
            MessageBox.messageLable(3, lblMessage, "Warning: Please Fill the Form.!", "", "-fx-text-fill:#e67e22");
        } else {
            boolean suppler = insertSupplier(new Supplier(
                    lblSuplierID.getText(),
                    txtSupplierFullName.getText(),
                    txtSupplierAddress.getText(),
                    txtSupplierContact.getText()
            ));
            if (suppler) {
                MessageBox.messageLable(3, lblMessage, "Success: Supplier has been Added.!", "", "-fx-text-fill:#2ecc71");
            }
            clearSupplierFeild();
            loadSupplierDataView();
        }
    }

    @FXML
    private void tblSupplier_OnMouseClicked(MouseEvent event) {
        Supplier item = tblSupplier.getSelectionModel().getSelectedItem();
        if (item != null && event.getClickCount() == 2) {
            lblSuplierID.setText(item.getSupplierID());
            txtSupplierFullName.setText(item.getName());
            txtSupplierAddress.setText(item.getAddress());
            txtSupplierContact.setText(item.getContact());
        }
    }

    @FXML
    private void btnSupplierClear_OnAction(ActionEvent event) {
        clearSupplierFeild();
    }

}
