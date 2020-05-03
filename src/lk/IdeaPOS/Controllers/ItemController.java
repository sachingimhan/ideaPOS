/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Controllers;

import com.jfoenix.controls.JFXTextField;
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
import lk.IdeaPOS.Model.Item;
import lk.IdeaPOS.Model.Login;
import lk.IdeaPOS.Util.DBUtil;
import lk.IdeaPOS.Util.MessageBox;

/**
 * FXML Controller class
 *
 * @author root
 */
public class ItemController implements Initializable {

    @FXML
    private TextField txtItemName;
    @FXML
    private TextField txtCostPrice;
    @FXML
    private TextField txtItemQty;
    @FXML
    private TextField txtRetailPrice;
    @FXML
    private TextField txtWholeSalePrice;
    @FXML
    private TextField txtReorderLevel;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClear;
    @FXML
    private TableView<Item> tblItems;
    @FXML
    private TableColumn<Item, String> colItemID;
    @FXML
    private TableColumn<Item, String> colItemName;
    @FXML
    private TableColumn<Item, Double> colCostPrice;
    @FXML
    private TableColumn<Item, Integer> colItemQty;
    @FXML
    private TableColumn<Item, Double> colRetailPrice;
    @FXML
    private TableColumn<Item, Double> colWhPrice;
    @FXML
    private TableColumn<Item, Integer> colOdLevel;
    @FXML
    private TextField txtSearch;

    String itemCode;

    private ObservableList<Item> allDataList;
    @FXML
    private Label lblMessage;
    @FXML
    private JFXTextField txtItemCode;
    @FXML
    private Button btnRandomItemCode;

    private Login login;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        colItemID.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colCostPrice.setCellValueFactory(new PropertyValueFactory<>("costPrice"));
        colItemQty.setCellValueFactory(new PropertyValueFactory<>("itemQty"));
        colRetailPrice.setCellValueFactory(new PropertyValueFactory<>("retailPrice"));
        colWhPrice.setCellValueFactory(new PropertyValueFactory<>("wholeSalePrice"));
        colOdLevel.setCellValueFactory(new PropertyValueFactory<>("reorderLevel"));
        loadTableView();
        FilteredList<Item> filteredList = new FilteredList<>(allDataList, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            tblItems.setItems(searchItem(newValue));
        });
    }

    private ObservableList<Item> searchItem(String filter) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE CONCAT(itemCode,itemName) LIKE '%" + filter + "%'");
            ResultSet rs = pst.executeQuery();
            ObservableList<Item> list = FXCollections.observableArrayList();
            while (rs.next()) {
                list.add(new Item(
                        rs.getString("itemCode"),
                        rs.getString("itemName"),
                        rs.getDouble("costPrice"),
                        rs.getDouble("retailPrice"),
                        rs.getDouble("wholeSalePrice"),
                        rs.getDouble("itemQty"),
                        rs.getInt("reorderLevel")
                ));
            }
            return list;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return null;
    }

     public void setLogin(Login login) {
        this.login = login;
        if (!this.login.getRole().equals("Administrator")) {
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
        }
    }
    
    private long genRandom() {
        Random r = new Random(System.currentTimeMillis());
        long l = 1000000000 + r.nextInt(2000000000);
        l = l < 0 ? -l : l;
        return l;
    }

    private String getLastItemID() {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT itemCode FROM Item ORDER BY itemCode DESC LIMIT 1");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return null;
    }

    private boolean insertItem(Item itm) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("INSERT INTO Item (itemCode,itemName,costPrice,retailPrice,wholeSalePrice,itemQty,reorderLevel) VALUES(?,?,?,?,?,?,?)");
            pst.setString(1, itm.getItemCode());
            pst.setString(2, itm.getItemName());
            pst.setDouble(3, itm.getCostPrice());
            pst.setDouble(4, itm.getRetailPrice());
            pst.setDouble(5, itm.getWholeSalePrice());
            pst.setDouble(6, itm.getItemQty());
            pst.setInt(7, itm.getReorderLevel());
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return false;
    }

    private void clearFeilds() {
        txtCostPrice.setText("");
        txtItemName.setText("");
        txtItemQty.setText("");
        txtReorderLevel.setText("");
        txtRetailPrice.setText("");
        txtWholeSalePrice.setText("");
        txtItemCode.setText("");
        if (txtItemQty.isDisable()) {
            txtItemQty.setDisable(false);
        }
    }

    private ObservableList<Item> loadAllItems() {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection()
                    .prepareStatement("SELECT * FROM Item");
            ResultSet rs = pst.executeQuery();
            ObservableList<Item> list = FXCollections.observableArrayList();
            while (rs.next()) {
                list.add(new Item(
                        rs.getString("itemCode"),
                        rs.getString("itemName"),
                        rs.getDouble("costPrice"),
                        rs.getDouble("retailPrice"),
                        rs.getDouble("wholeSalePrice"),
                        rs.getDouble("itemQty"),
                        rs.getInt("reorderLevel")
                ));
            }
            return list;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return null;
    }

    private void loadTableView() {
        allDataList = loadAllItems();
        tblItems.setItems(loadAllItems());
    }

    private boolean updateItem(Item item) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection()
                    .prepareStatement("UPDATE Item SET itemName=?, costPrice=?, retailPrice=?, wholeSalePrice=?,reorderLevel=? WHERE itemCode=?");
            pst.setString(1, item.getItemName());
            pst.setDouble(2, item.getCostPrice());
            pst.setDouble(3, item.getRetailPrice());
            pst.setDouble(4, item.getWholeSalePrice());
            pst.setInt(5, item.getReorderLevel());
            pst.setString(6, item.getItemCode());
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return false;
    }

    private boolean deleteItem(String itemCode) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection()
                    .prepareStatement("DELETE FROM Item WHERE itemCode=?");
            pst.setString(1, itemCode);
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return false;
    }

    @FXML
    private void btnDelete_handleButtonAction(ActionEvent event) {
        if (tblItems.getItems().size() > 0) {
            if (tblItems.getSelectionModel().getSelectedItem() != null) {
                if (MessageBox.showConfMessage("Are you sure?", "Delete")) {
                    boolean flag = deleteItem(tblItems.getSelectionModel().getSelectedItem().getItemCode());
                    if (flag) {
                        MessageBox.messageLable(3, lblMessage, "Success: Item has been Deleted.!", "", "-fx-text-fill:#2ecc71");
                    }
                    loadTableView();
                    clearFeilds();
                }
            } else {
                MessageBox.messageLable(3, lblMessage, "Warning: Please select Item to Delete", "", "-fx-text-fill:#e67e22");
            }

        } else {
            MessageBox.messageLable(3, lblMessage, "Warning: Data Not Found.!", "", "-fx-text-fill:#e67e22");
        }
    }

    @FXML
    private void btnUpdate_handleButtonAction(ActionEvent event) {
        if (txtItemName.getText().isEmpty()
                || txtCostPrice.getText().isEmpty()
                || txtRetailPrice.getText().isEmpty()) {
            MessageBox.messageLable(3, lblMessage, "Warning: Please Fill All Requird Feids.!", "", "-fx-text-fill:#e67e22");
        } else {
            if (txtWholeSalePrice.getText().isEmpty()) {
                txtWholeSalePrice.setText("0.00");
            }

            if (txtItemQty.getText().isEmpty()) {
                txtItemQty.setText("0");
            }
            if (txtReorderLevel.getText().isEmpty()) {
                txtReorderLevel.setText("0");
            }
            boolean flag = updateItem(new Item(
                    txtItemCode.getText(),
                    txtItemName.getText(),
                    Double.parseDouble(txtCostPrice.getText()),
                    Double.parseDouble(txtRetailPrice.getText()),
                    Double.parseDouble(txtWholeSalePrice.getText()),
                    Double.parseDouble(txtItemQty.getText()),
                    Integer.parseInt(txtReorderLevel.getText())
            ));
            if (flag) {
                MessageBox.messageLable(3, lblMessage, "Success: Item has been Updated.!", "", "-fx-text-fill:#2ecc71");
            }
            clearFeilds();
            loadTableView();
        }
    }

    @FXML
    private void btnSave_handleButtonAction(ActionEvent event) {
        if (txtItemName.getText().isEmpty()
                || txtCostPrice.getText().isEmpty()
                || txtRetailPrice.getText().isEmpty()) {
            MessageBox.messageLable(3, lblMessage, "Warning: Please Fill All Requird Feids.!", "", "-fx-text-fill:#e67e22");
        } else {
            if (txtWholeSalePrice.getText().isEmpty()) {
                txtWholeSalePrice.setText("0.00");
            }

            if (txtItemQty.getText().isEmpty()) {
                txtItemQty.setText("0");
            }
            if (txtReorderLevel.getText().isEmpty()) {
                txtReorderLevel.setText("0");
            }
            boolean flag = insertItem(new Item(
                    txtItemCode.getText(),
                    txtItemName.getText(),
                    Double.parseDouble(txtCostPrice.getText()),
                    Double.parseDouble(txtRetailPrice.getText()),
                    Double.parseDouble(txtWholeSalePrice.getText()),
                    Double.parseDouble(txtItemQty.getText()),
                    Integer.parseInt(txtReorderLevel.getText())
            ));
            if (flag) {
                MessageBox.messageLable(3, lblMessage, "Success: Item has been Aded.!", "", "-fx-text-fill:#2ecc71");
            }
            clearFeilds();
            loadTableView();
        }
    }

    @FXML
    private void btnClear_handleButtonAction(ActionEvent event) {
        clearFeilds();
    }

    @FXML
    private void tblItems_OnMouseClicked(MouseEvent event) {
        Item item = tblItems.getSelectionModel().getSelectedItem();
        if (item != null && event.getClickCount() == 2) {
            txtItemCode.setText(item.getItemCode());
            txtItemName.setText(item.getItemName());
            txtCostPrice.setText(String.valueOf(item.getCostPrice()));
            txtItemQty.setText(String.valueOf(item.getItemQty()));
            txtRetailPrice.setText(String.valueOf(item.getRetailPrice()));
            txtWholeSalePrice.setText(String.valueOf(item.getWholeSalePrice()));
            txtReorderLevel.setText(String.valueOf(item.getReorderLevel()));
            txtItemQty.setDisable(true);
        }
    }

    @FXML
    private void btnRandomItemCode_OnAction(ActionEvent event) {
        txtItemCode.setText(Long.toString(genRandom()));
    }

}
