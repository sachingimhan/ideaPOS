/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.IdeaPOS.Model.GRN;
import lk.IdeaPOS.Util.DBUtil;
import lk.IdeaPOS.Util.MessageBox;
import lk.IdeaPOS.Util.MessageIconType;

/**
 * FXML Controller class
 *
 * @author root
 */
public class GRNConfirmationController implements Initializable {

    @FXML
    private TableView<GRN> tblGRNConfirmation;
    @FXML
    private TableColumn<GRN, String> colGrnNo;
    @FXML
    private TableColumn<GRN, String> colItemCode;
    @FXML
    private TableColumn<GRN, String> colSupplierID;
    @FXML
    private TableColumn<GRN, String> colUserID;
    @FXML
    private TableColumn<GRN, Double> colUnitPrice;
    @FXML
    private TableColumn<GRN, Integer> colQty;
    @FXML
    private TableColumn<GRN, JFXCheckBox> colSelect;
    @FXML
    private JFXCheckBox chkSelectAll;
    @FXML
    private JFXButton btnConfirm;
    @FXML
    private JFXDatePicker dtpGRNDate;
    @FXML
    private Button btnFindGrn;
    @FXML
    private Label lblMessage;
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //table Column Init
        colGrnNo.setCellValueFactory(new PropertyValueFactory<>("grnID"));
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colSupplierID.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        colUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colSelect.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
        //CheckBox
        chkSelectAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (chkSelectAll.isSelected()) {
                    selectAll(true);
                } else {
                    selectAll(false);
                }

            }
        });

    }

    private void selectAll(boolean checked) {
        for (int i = 0; i < tblGRNConfirmation.getItems().size(); i++) {
            if (!tblGRNConfirmation.getItems().get(i).getCheckBox().isSelected()) {
                tblGRNConfirmation.getItems().get(i).getCheckBox().setSelected(checked);
                tblGRNConfirmation.getItems().get(i).setIsConformed(checked);
            } else {
                tblGRNConfirmation.getItems().get(i).getCheckBox().setSelected(checked);
                tblGRNConfirmation.getItems().get(i).setIsConformed(checked);
            }
        }
        tblGRNConfirmation.refresh();
    }

    private ObservableList<GRN> loadUnConfirmedGrn(String grnDate) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT grnID,itemCode,supplierID,userID,unitPrice,qty FROM GRN WHERE grnDate=? AND isConformed=false");
            pst.setDate(1, Date.valueOf(grnDate));
            ResultSet rs = pst.executeQuery();
            ObservableList<GRN> list = FXCollections.observableArrayList();
            while (rs.next()) {
                list.add(new GRN(
                        rs.getString("grnID"),
                        rs.getString("itemCode"),
                        rs.getString("supplierID"),
                        rs.getString("userID"),
                        rs.getInt("qty"),
                        rs.getDouble("unitPrice")
                ));
            }
            return list;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return null;
    }

    @FXML
    private void btnConfirm_OnAction(ActionEvent event) {
        if (!tblGRNConfirmation.getItems().isEmpty()) {
            CheckSelectedCellValues();
            if (MessageBox.showConfMessage("Are you Sure?", "Confirmation")) {
                boolean confirmGRN = false;
                for (GRN grn : tblGRNConfirmation.getItems()) {
                    if (grn.isConformed()) {
                        confirmGRN = confirmGRN(grn);
                        if (!confirmGRN) {
                            MessageBox.show(3, lblMessage, "Query Faild.!", MessageIconType.ERROR);
                            return;
                        }
                    }
                }
                if (confirmGRN) {
                    MessageBox.show(3, lblMessage, "GRN has been Confirmed.!", MessageIconType.INFORMATION);
                    tblGRNConfirmation.setItems(loadUnConfirmedGrn(dtpGRNDate.getValue().format(DateTimeFormatter.ISO_DATE)));
                }
            }
        }
    }

    private void CheckSelectedCellValues() {
        if (!tblGRNConfirmation.getItems().isEmpty()) {
            for (int i = 0; i < tblGRNConfirmation.getItems().size(); i++) {
                if (tblGRNConfirmation.getItems().get(i).getCheckBox().isSelected()) {
                    tblGRNConfirmation.getItems().get(i).setIsConformed(true);
                } else {
                    tblGRNConfirmation.getItems().get(i).setIsConformed(false);
                }
            }
            tblGRNConfirmation.refresh();
        }
    }

    private boolean confirmGRN(GRN grn) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("UPDATE GRN SET isConformed=? WHERE grnID=? AND itemCode=?");
            pst.setBoolean(1, grn.isConformed());
            pst.setString(2, grn.getGrnID());
            pst.setString(3, grn.getItemCode());
            boolean flag = pst.executeUpdate() > 0;
            if (flag) {
                boolean updateItemQty = updateItemQty(grn.getItemCode(), grn.getQty(), grn.getUnitPrice());
                if (!updateItemQty) {
                    return false;
                }
            }
            return flag;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return false;
    }

    private boolean updateItemQty(String itemCode, int Qty, double retail) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("UPDATE Item SET itemQty=itemQty+?, costPrice=? WHERE itemCode=?");
            pst.setInt(1, Qty);
            pst.setDouble(2, retail);
            pst.setString(3, itemCode);
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return false;
    }

    @FXML
    private void btnFindGrn_OnAction(ActionEvent event) {
        if (dtpGRNDate.getValue() != null) {
            tblGRNConfirmation.setItems(loadUnConfirmedGrn(dtpGRNDate.getValue().format(DateTimeFormatter.ISO_DATE)));
        }
    }

}
