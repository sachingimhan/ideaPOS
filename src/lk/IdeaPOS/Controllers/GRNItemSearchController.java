/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.IdeaPOS.Model.Item;
import lk.IdeaPOS.Util.DBUtil;
import lk.IdeaPOS.Util.MessageBox;

/**
 * FXML Controller class
 *
 * @author root
 */
public class GRNItemSearchController implements Initializable {

    @FXML
    private TextField txtFilter;
    @FXML
    private TableView<Item> tblFindItem;
    @FXML
    private TableColumn<Item, String> colItemCode;
    @FXML
    private TableColumn<Item, String> colItemName;
    @FXML
    private TableColumn<Item, Integer> colItemQty;
    @FXML
    private TableColumn<Item, Double> colDiscount;
    @FXML
    private Button btnFind;
    @FXML
    private Button btnExit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Init Table Column
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colItemQty.setCellValueFactory(new PropertyValueFactory<>("itemQty"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        //
        tblFindItem.setItems(loadItem());
        txtFilter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tblFindItem.setItems(searchFilterItem(newValue));
            }

        });
    }

    private ObservableList<Item> loadItem() {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT * FROM Item");
            ResultSet rs = pst.executeQuery();
            ObservableList<Item> list = FXCollections.observableArrayList();
            while (rs.next()) {
                list.add(new Item(
                        rs.getString("itemCode"),
                        rs.getString("itemName"),
                        rs.getDouble("costPrice"),
                        rs.getDouble("retailPrice"),
                        rs.getDouble("wholeSalePrice"),
                        rs.getInt("itemQty"),
                        rs.getDouble("discount"),
                        rs.getInt("reorderLevel")
                ));
            }
            return list;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.showErrorMessage(ex.toString(), "Error");
        }
        return null;
    }

    private ObservableList<Item> searchFilterItem(String filter) {
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
                        rs.getInt("itemQty"),
                        rs.getDouble("discount"),
                        rs.getInt("reorderLevel")
                ));
            }
            return list;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.showErrorMessage(ex.toString(), "Error");
        }
        return null;
    }

    @FXML
    private void btnFind_OnAction(ActionEvent event) throws IOException {
        Stage window = (Stage) btnFind.getScene().getWindow();
        if (tblFindItem.getSelectionModel().getSelectedItem() != null) {
            //getItem();
            window.close();
            return;
        }
        if (tblFindItem.getItems().isEmpty()) {
            window.close();
        }

    }
    private Item selectedItem;

    public Item getItem() {
        selectedItem = tblFindItem.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            selectedItem = new Item();
        }
        return selectedItem;
    }

    @FXML
    private void btnExit_OnAction(ActionEvent event) {
        Stage window = (Stage) btnFind.getScene().getWindow();
        selectedItem = new Item();
        window.close();
    }
}
