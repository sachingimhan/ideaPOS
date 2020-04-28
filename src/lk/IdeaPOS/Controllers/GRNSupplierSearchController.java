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
import lk.IdeaPOS.Model.Supplier;
import lk.IdeaPOS.Util.DBUtil;
import lk.IdeaPOS.Util.MessageBox;

/**
 * FXML Controller class
 *
 * @author root
 */
public class GRNSupplierSearchController implements Initializable {

    @FXML
    private TextField txtFilter;
    @FXML
    private TableView<Supplier> tblFindSupplier;
    @FXML
    private Button btnFind;
    private Button btnExit;
    @FXML
    private TableColumn<Supplier, String> colSupplierCode;
    @FXML
    private TableColumn<Supplier, String> colSupplierName;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colSupplierCode.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tblFindSupplier.setItems(loadSupplier());
        txtFilter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tblFindSupplier.setItems(searchFilterItem(newValue));
            }

        });
    }

    private ObservableList<Supplier> loadSupplier() {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT * FROM Supplier");
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
            MessageBox.showErrorMessage(ex.toString(), "Error");
        }
        return null;
    }

    private ObservableList<Supplier> searchFilterItem(String filter) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT * FROM Supplier WHERE CONCAT(supplierID,name) LIKE '%" + filter + "%'");
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
            MessageBox.showErrorMessage(ex.toString(), "Error");
        }
        return null;
    }

    @FXML
    private void btnFind_OnAction(ActionEvent event) {
        Stage window = (Stage) btnFind.getScene().getWindow();
        if (tblFindSupplier.getSelectionModel().getSelectedItem() != null) {
            getSupplier();
            window.close();
            return;
        }
        if (tblFindSupplier.getItems().isEmpty()) {
            window.close();
        }
    }
    private Supplier selectedItem;

    public Supplier getSupplier() {
        selectedItem = tblFindSupplier.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            selectedItem = new Supplier();
        }
        return selectedItem;
    }

}
