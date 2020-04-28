/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.IdeaPOS.Model.DiscountItem;
import lk.IdeaPOS.Model.Item;
import lk.IdeaPOS.Util.DBUtil;
import lk.IdeaPOS.Util.Loader;
import lk.IdeaPOS.Util.MessageBox;
import lk.IdeaPOS.Util.MessageIconType;

/**
 * FXML Controller class
 *
 * @author root
 */
public class DsicountItemController implements Initializable {

    @FXML
    private JFXDatePicker dtpDiscountDate;
    @FXML
    private JFXTextField txtItemCode;
    @FXML
    private JFXTextField txtCostPrice;
    @FXML
    private JFXTextField txtDiscount;
    @FXML
    private JFXTextField txtDiscountAmount;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXTextField txtSearch;
    @FXML
    private Button btnFindItem;
    @FXML
    private TableView<DiscountItem> tblDiscount;
    @FXML
    private TableColumn<DiscountItem, Integer> colDisNo;
    @FXML
    private TableColumn<DiscountItem, String> colItemCode;
    @FXML
    private TableColumn<DiscountItem, Double> colCostPrice;
    @FXML
    private TableColumn<DiscountItem, String> colDate;
    @FXML
    private TableColumn<DiscountItem, String> colDiscount;
    @FXML
    private TableColumn<DiscountItem, Double> colDisAmount;
    @FXML
    private TableColumn<DiscountItem, String> colItemName;
    @FXML
    private Label lblMessage;
    @FXML
    private Button btnAdd;
    @FXML
    private JFXTextField txtitemName;
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Init Table
        colDisNo.setCellValueFactory(new PropertyValueFactory<>("disCode"));
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colCostPrice.setCellValueFactory(new PropertyValueFactory<>("costPrice"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("disDate"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colDisAmount.setCellValueFactory(new PropertyValueFactory<>("discountAmount"));
        //set Data Now
        dtpDiscountDate.setValue(LocalDate.now());
        //set Default value
        txtDiscount.setText("0");
        //load Discount TAble
        loadTableView();
        //Discout text change
        txtDiscount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!txtDiscount.getText().matches("\\b(0*(?:[0-9][0-9]?|100))\\b") && !txtDiscount.getText().isEmpty()) {
                    txtDiscount.setText(oldValue);
                    txtDiscount.positionCaret(txtDiscount.getText().length());
                }
                calDiscountPrice();
            }
        });
        //Search text change
        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tblDiscount.setItems(SearchdiscountItems(txtSearch.getText()));
            }
        });
    }

    public ObservableList<DiscountItem> discountItems() {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT d.disCode,d.itemCode,i.itemName,d.costPrice,d.discount,d.discountAmount,d.disDate FROM DiscountItem d,Item i WHERE d.itemCode=i.itemCode");
            ObservableList<DiscountItem> dis = FXCollections.observableArrayList();
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                dis.add(new DiscountItem(
                        rs.getInt("disCode"),
                        rs.getString("itemCode"),
                        rs.getString("itemName"),
                        rs.getDouble("costPrice"),
                        rs.getString("discount"),
                        rs.getDouble("discountAmount"),
                        rs.getString("disDate")
                ));
            }
            return dis;

        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return null;
    }

    public ObservableList<DiscountItem> SearchdiscountItems(String filter) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT d.disCode,d.itemCode,i.itemName,d.costPrice,d.discount,d.discountAmount,d.disDate FROM DiscountItem d,Item i WHERE d.itemCode=i.itemCode AND CONCAT(d.itemCode,i.itemName,d.discount) LIKE '%" + filter + "%'");
            ObservableList<DiscountItem> dis = FXCollections.observableArrayList();
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                dis.add(new DiscountItem(
                        rs.getInt("disCode"),
                        rs.getString("itemCode"),
                        rs.getString("itemName"),
                        rs.getDouble("costPrice"),
                        rs.getString("discount"),
                        rs.getDouble("discountAmount"),
                        rs.getString("disDate")
                ));
            }
            return dis;

        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return null;
    }

    private void calDiscountPrice() {
        if (!txtDiscount.getText().isEmpty()) {
            double retail = Double.parseDouble(txtCostPrice.getText());
            double discount = Double.parseDouble(txtDiscount.getText());
            double disPrice = (retail * discount) / 100;
            txtDiscountAmount.setText(Double.toString(disPrice));
        }
    }

    @FXML
    private void btnClear_OnAction(ActionEvent event) {
        clear();
    }

    private void clear() {
        txtItemCode.setText("");
        txtitemName.setText("");
        txtCostPrice.setText("");
        txtDiscountAmount.setText("");
    }

    @FXML
    private void btnSave_OnAction(ActionEvent event) {
        if (txtitemName.getText().isEmpty() || txtDiscount.getText().isEmpty() || txtDiscountAmount.getText().isEmpty()) {
            MessageBox.show(3, lblMessage, "Not a Valid Type of Data", MessageIconType.WARNING);
        } else {
            boolean insertDiscount = insertDiscount(new DiscountItem(txtItemCode.getText(), Double.parseDouble(txtCostPrice.getText()), txtDiscount.getText(), Double.parseDouble(txtDiscountAmount.getText()), dtpDiscountDate.getValue().toString()));
            if (insertDiscount) {
                MessageBox.show(3, lblMessage, "Discount has been Added.!", MessageIconType.INFORMATION);
            }
            clear();
            loadTableView();
        }
    }

    private boolean insertDiscount(DiscountItem item) {

        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("INSERT INTO DiscountItem (itemCode,costPrice,discount,discountAmount,disDate) VALUES(?,?,?,?,?)");
            pst.setString(1, item.getItemCode());
            pst.setDouble(2, item.getCostPrice());
            pst.setString(3, item.getDiscount());
            pst.setDouble(4, item.getDiscountAmount());
            pst.setString(5, item.getDisDate());
            boolean flag = pst.executeUpdate() > 0;
            if (flag) {
                boolean b = itemDiscountUpdate(item.getItemCode(), item.getDiscountAmount());
                if (b) {
                    return true;
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }

        return false;
    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) {
        if (!tblDiscount.getItems().isEmpty() || tblDiscount.getSelectionModel().getSelectedItem() != null) {
            if (MessageBox.showConfMessage("Are you Sure?", "Delete Confirmation")) {
                DiscountItem selectedItem = tblDiscount.getSelectionModel().getSelectedItem();
                boolean deleteDiscount = deleteDiscount(selectedItem.getDisCode(), selectedItem.getItemCode());
                if (deleteDiscount) {
                    MessageBox.show(3, lblMessage, "Discount has been Deleted.!", MessageIconType.INFORMATION);
                    loadTableView();
                }
            }
        }

    }

    private boolean deleteDiscount(int disID, String itemCode) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("DELETE FROM DiscountItem WHERE disCode=?");
            pst.setInt(1, disID);
            boolean flag = pst.executeUpdate() > 0;
            if (flag) {
                return itemDiscountUpdate(itemCode, 0.0);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return false;
    }

    @FXML
    private void btnFindItem_OnAction(ActionEvent event) throws IOException {
        FXMLLoader itemFilter = new Loader().loadeFXML("View/GRNItemSearch.fxml");
        Parent root = itemFilter.load();
        GRNItemSearchController controller = itemFilter.<GRNItemSearchController>getController();
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        stage.setAlwaysOnTop(true);
        stage.showAndWait();
        txtItemCode.setText(controller.getItem().getItemCode());
        btnAdd.fire();
        txtDiscount.requestFocus();
    }

    private Item selectedItemLoad(String itemCode) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT itemCode,itemName,retailPrice from Item WHERE itemCode=?");
            pst.setString(1, itemCode);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Item(rs.getString("itemCode"), rs.getString("itemName"), rs.getDouble("retailPrice"));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }

        return null;
    }

    @FXML
    private void btnAdd_OnAction(ActionEvent event) {
        if (!txtItemCode.getText().isEmpty()) {
            Item selectedItemLoad = selectedItemLoad(txtItemCode.getText());
            txtitemName.setText(selectedItemLoad.getItemName());
            txtCostPrice.setText(Double.toString(selectedItemLoad.getRetailPrice()));
        }
    }

    private boolean itemDiscountUpdate(String itemCode, double discountAmount) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("UPDATE Item SET discount=? WHERE itemCode=?");
            pst.setDouble(1, discountAmount);
            pst.setString(2, itemCode);
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return false;
    }

    private void loadTableView() {
        tblDiscount.setItems(discountItems());
    }
}
