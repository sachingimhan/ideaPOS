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
import java.util.Random;
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
import javafx.scene.layout.AnchorPane;
import lk.IdeaPOS.Model.Customer;
import lk.IdeaPOS.Model.Login;
import lk.IdeaPOS.Util.DBUtil;
import lk.IdeaPOS.Util.MessageBox;

/**
 *
 * @author root
 */
public class CustomerController implements Initializable {

    @FXML
    private TextField txtFullName;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtContact;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnSave;
    @FXML
    private TableView<Customer> tblCustomer;
    @FXML
    private TableColumn<Customer, String> colID;
    @FXML
    private TableColumn<Customer, String> colFullName;
    @FXML
    private TableColumn<Customer, String> colAddress;
    @FXML
    private TableColumn<Customer, String> colContact;
    @FXML
    private Label lblCustID;
    @FXML
    private Button btnClear;

    private String custID;
    @FXML
    private AnchorPane customer;
    @FXML
    private TextField txtSearch;

    private ObservableList<Customer> dataList;
    @FXML
    private Label lblMessage;

    private Login login;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colID.setCellValueFactory(new PropertyValueFactory<>("custID"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("custName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("custAddress"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("custContact"));
        getCustomerID();
        loadDataTable();
        // validate Contact feild
        txtContact.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!txtContact.getText().matches("[0-9]+") && !txtContact.getText().isEmpty()) {
                txtContact.setText(oldValue);
                txtContact.positionCaret(txtContact.getText().length());
            }
        });
        //validate Name feild
        txtFullName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!txtFullName.getText().matches("[a-z A-Z.]+") && !txtFullName.getText().isEmpty()) {
                txtFullName.setText(oldValue);
                txtFullName.positionCaret(txtFullName.getText().length());
            }
        });
        //Filter Data
        FilteredList<Customer> flist = new FilteredList<>(dataList, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            tblCustomer.setItems(searchCustomer(newValue));
        });

    }

    public void setLogin(Login login) {
        this.login = login;
        if (!this.login.getRole().equals("Administrator")) {
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
        }
    }

    private ObservableList<Customer> searchCustomer(String filter) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT * FROM Customer WHERE CONCAT(custID,custName,custContact) LIKE '%" + filter + "%'");
            ResultSet rs = pst.executeQuery();
            ObservableList<Customer> list = FXCollections.observableArrayList();
            while (rs.next()) {
                list.add(new Customer(
                        rs.getString("custID"),
                        rs.getString("custName"),
                        rs.getString("custAddress"),
                        rs.getString("custContact")
                ));
            }
            return list;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return null;
    }

    private void getCustomerID() {
        Random r = new Random(System.currentTimeMillis());
        long l = 100000000 + r.nextInt(200000000);
        l = l < 0 ? -l : l;
        lblCustID.setText(Long.toOctalString(l));
    }

    private void loadDataTable() {
        dataList = loardDataTable();
        tblCustomer.setItems(dataList);
    }

    private void clearCustomerFeild() {
        txtFullName.setText("");
        txtContact.setText("");
        txtAddress.setText("");
    }

    private String getLastCustomerID() {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT custID FROM Customer ORDER BY custID DESC LIMIT 1");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return null;
    }

    private boolean insertCustomer(Customer cust) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("INSERT INTO Customer(custID,custName,custAddress,custContact) VALUES(?,?,?,?)");
            pst.setString(1, cust.getCustID());
            pst.setString(2, cust.getCustName());
            pst.setString(3, cust.getCustAddress());
            pst.setString(4, cust.getCustContact());
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return false;
    }

    private boolean updateCustomer(Customer cust) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("UPDATE Customer SET custName=?,custAddress=?,custContact=? WHERE custID=? AND isLocked=false");
            pst.setString(1, cust.getCustName());
            pst.setString(2, cust.getCustAddress());
            pst.setString(3, cust.getCustContact());
            pst.setString(4, cust.getCustID());
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return false;
    }

    private ObservableList<Customer> loardDataTable() {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT * FROM Customer");
            ResultSet rs = pst.executeQuery();
            ObservableList<Customer> obList = FXCollections.observableArrayList();
            while (rs.next()) {
                obList.add(new Customer(
                        rs.getString("custID"),
                        rs.getString("custName"),
                        rs.getString("custAddress"),
                        rs.getString("custContact")
                ));
            }
            return obList;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return null;
    }

    private boolean deleteCustomer(String custID) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("DELETE FROM Customer WHERE custID=? AND isLocked=false");
            pst.setString(1, custID);
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return false;
    }

    @FXML
    private void btnDelete_handleButtonAction(ActionEvent event) {
        if (tblCustomer.getItems().size() > 0) {
            if (tblCustomer.getSelectionModel().getSelectedItem() != null) {
                if (MessageBox.showConfMessage("Are you sure?", "Delete")) {
                    boolean del = deleteCustomer(tblCustomer.getSelectionModel().getSelectedItem().getCustID());
                    if (del) {
                        MessageBox.messageLable(3, lblMessage, "Success: Customer has been Deleted.!", "", "-fx-text-fill:#2ecc71");
                    }
                    clearCustomerFeild();
                    getCustomerID();
                    loadDataTable();
                }
            } else {
                MessageBox.messageLable(3, lblMessage, "Warning: Please select Item to Delete", "", "-fx-text-fill:#e67e22");
            }

        } else {
            MessageBox.messageLable(3, lblMessage, "Warning: Database Empty.!", "", "-fx-text-fill:#e67e22");
        }
    }

    @FXML
    private void btnUpdate_handleButtonAction(ActionEvent event) {
        if (txtFullName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtContact.getText().isEmpty()) {
            MessageBox.messageLable(3, lblMessage, "Warning: Please Fill the form", "", "-fx-text-fill:#e67e22");
        } else {
            boolean updateCustomer = updateCustomer(new Customer(lblCustID.getText(), txtFullName.getText(), txtAddress.getText(), txtContact.getText()));
            if (updateCustomer) {
                MessageBox.messageLable(3, lblMessage, "Success: Customer has been Updated.!", "", "-fx-text-fill:#2ecc71");
            }
            loadDataTable();
            clearCustomerFeild();
            getCustomerID();
        }
    }

    @FXML
    private void btnSave_handleButtonAction(ActionEvent event) {
        if (txtFullName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtContact.getText().isEmpty()) {
            MessageBox.messageLable(3, lblMessage, "Warning: Please Fill the form", "", "-fx-text-fill:#e67e22");
        } else {
            boolean result = insertCustomer(new Customer(lblCustID.getText(), txtFullName.getText(), txtAddress.getText(), txtContact.getText()));
            if (result) {
                MessageBox.messageLable(3, lblMessage, "Success: Customer has been Added.!", "", "-fx-text-fill:#2ecc71");
            }
            loadDataTable();
            getCustomerID();
            clearCustomerFeild();
        }
    }

    @FXML
    private void getSelectedData(MouseEvent event) {
        Customer selectedItem = tblCustomer.getSelectionModel().getSelectedItem();
        if (selectedItem != null && event.getClickCount() == 2) {
            lblCustID.setText(selectedItem.getCustID());
            txtFullName.setText(selectedItem.getCustName());
            txtAddress.setText(selectedItem.getCustAddress());
            txtContact.setText(selectedItem.getCustContact());
        }
    }

    @FXML
    private void btnClear_handleButtonAction(ActionEvent event) {
        clearCustomerFeild();
        getCustomerID();
    }

}
