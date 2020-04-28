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
import java.time.LocalDate;
import java.util.Random;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.IdeaPOS.Model.GRN;
import lk.IdeaPOS.Model.Login;
import lk.IdeaPOS.Util.DBUtil;
import lk.IdeaPOS.Util.Loader;
import lk.IdeaPOS.Util.MessageBox;

/**
 * FXML Controller class
 *
 * @author root
 */
public class GRNController implements Initializable {

    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClear;
    @FXML
    private TextField txtGRNNo;
    @FXML
    private Button btnRandomGrnNo;
    @FXML
    private Button btnRandomBatchNo;
    @FXML
    private TextField txtBatchNo;
    @FXML
    private Button btnFindItem;
    @FXML
    public TextField txtItemCode;
    @FXML
    private TextField txtSupplierID;
    @FXML
    private Button btnFindSupplier;
    @FXML
    private TableView<GRN> tblGRNote;
    @FXML
    private TableColumn<GRN, String> colGrnNo;
    @FXML
    private TableColumn<GRN, String> colItemCode;
    @FXML
    private TableColumn<GRN, String> colSupplierCode;
    @FXML
    private TableColumn<GRN, String> colBatchNo;
    @FXML
    private TableColumn<GRN, String> colDate;
    @FXML
    private TableColumn<GRN, Integer> colItemQty;
    @FXML
    private TableColumn<GRN, Double> colUnitPrice;
    @FXML
    private TableColumn<GRN, Double> colTotalCost;
    @FXML
    private TableColumn<GRN, String> colMfd;
    @FXML
    private TableColumn<GRN, String> colExpDate;
    @FXML
    private TextField txtItemQty;
    @FXML
    private TextField txtUnitPrice;
    @FXML
    private TextField txtTotalCost;
    @FXML
    private DatePicker dtpMFD;
    @FXML
    private DatePicker dtpExpDate;
    @FXML
    private DatePicker dtpGRNDate;
    @FXML
    private TextField txtSearch;
    @FXML
    private Label lblMessage;

    private Login login;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Table Value
        colGrnNo.setCellValueFactory(new PropertyValueFactory<>("grnID"));
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colSupplierCode.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        colBatchNo.setCellValueFactory(new PropertyValueFactory<>("batchNo"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("grnDate"));
        colItemQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colMfd.setCellValueFactory(new PropertyValueFactory<>("mdf"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        //set Local Date
        dtpGRNDate.setValue(LocalDate.now());
        //load Table
        loadTableView();
        //Search TextChanged
        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tblGRNote.setItems(searchGRN(newValue));
            }
        });
        //Regx for Validate Text Feilds
        txtItemQty.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!txtItemQty.getText().matches("[0-9]+") && !txtItemQty.getText().isEmpty()) {
                    txtItemQty.setText(oldValue);
                    txtItemQty.positionCaret(txtItemQty.getText().length());
                }
            }
        });
        txtUnitPrice.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!txtUnitPrice.getText().matches("[0-9.]+") && !txtUnitPrice.getText().isEmpty()) {
                    txtUnitPrice.setText(oldValue);
                    txtUnitPrice.positionCaret(txtUnitPrice.getText().length());
                }
            }
        });
        txtTotalCost.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!txtTotalCost.getText().matches("[0-9.]+") && !txtTotalCost.getText().isEmpty()) {
                    txtTotalCost.setText(oldValue);
                    txtTotalCost.positionCaret(txtTotalCost.getText().length());
                }
            }
        });
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    private void loadTableView() {
        tblGRNote.setItems(loadAllData());
    }

    private long genRandom() {
        Random r = new Random(System.currentTimeMillis());
        long l = 1000000000 + r.nextInt(2000000000);
        l = l < 0 ? -l : l;
        return l;
    }

    private boolean insertGrn(GRN grn) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("INSERT INTO GRN(grnID,itemCode,supplierID,userID,batchNo,grnDate,qty,unitPrice,totalCost,mdf,expiryDate) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, grn.getGrnID());
            pst.setString(2, grn.getItemCode());
            pst.setString(3, grn.getSupplierID());
            pst.setString(4, grn.getUserID());
            pst.setString(5, grn.getBatchNo());
            pst.setString(6, grn.getGrnDate());
            pst.setInt(7, grn.getQty());
            pst.setDouble(8, grn.getUnitPrice());
            pst.setDouble(9, grn.getTotalCost());
            pst.setString(10, grn.getMdf());
            pst.setString(11, grn.getExpiryDate());
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return false;
    }

    @FXML
    private void btnRandomGrnNo_OnAction(ActionEvent event) {
        txtGRNNo.setText("GRN-" + Long.toString(genRandom()));
    }

    @FXML
    private void btnClear_OnAction(ActionEvent event) {
        clearFeild();
    }

    @FXML
    private void btnSave_OnAction(ActionEvent event) {
        if (txtGRNNo.getText().isEmpty()
                || txtItemCode.getText().isEmpty()
                || txtSupplierID.getText().isEmpty()
                || dtpGRNDate.getValue().toString().isEmpty()
                || txtBatchNo.getText().isEmpty()
                || txtItemQty.getText().isEmpty()
                || txtUnitPrice.getText().isEmpty()
                || txtTotalCost.getText().isEmpty()) {
            MessageBox.messageLable(3, lblMessage, "Warning: Please Fill The From.!", "", "-fx-text-fill:#e67e22");
        } else {
            String mfDate = dtpMFD.getValue() == null ? LocalDate.now().toString() : dtpMFD.getValue().toString();
            String expDate = dtpExpDate.getValue() == null ? LocalDate.now().toString() : dtpExpDate.getValue().toString();
            boolean flag = insertGrn(new GRN(
                    txtGRNNo.getText(),
                    txtItemCode.getText(),
                    txtSupplierID.getText(),
                    login.getUserID(),
                    txtBatchNo.getText(),
                    dtpGRNDate.getValue().toString(),
                    Integer.parseInt(txtItemQty.getText()),
                    Double.parseDouble(txtUnitPrice.getText()),
                    Double.parseDouble(txtTotalCost.getText()),
                    mfDate,
                    expDate
            ));
            if (flag) {
                MessageBox.messageLable(3, lblMessage, "Success: GRN has been Submited.!", "", "-fx-text-fill:#2ecc71");
            }
            loadTableView();
            clearFeild();
        }
    }

    private void clearFeild() {
        txtBatchNo.setText("");
        txtGRNNo.setText("");
        txtItemCode.setText("");
        txtItemQty.setText("");
        txtSupplierID.setText("");
        txtTotalCost.setText("");
        txtUnitPrice.setText("");
        dtpExpDate.setValue(null);
        dtpMFD.setValue(null);
        dtpGRNDate.setValue(LocalDate.now());
        if (txtGRNNo.isDisable()) {
            txtGRNNo.setDisable(false);
            btnRandomGrnNo.setDisable(false);
        }
        if (txtItemCode.isDisable()) {
            txtItemCode.setDisable(false);
            btnFindItem.setDisable(false);
        }
    }

    private ObservableList<GRN> loadAllData() {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection()
                    .prepareStatement("SELECT grnID,itemCode,supplierID,batchNo,grnDate,qty,unitPrice,totalCost,mdf,expiryDate FROM GRN WHERE isConformed=0");
            ResultSet rs = pst.executeQuery();
            ObservableList<GRN> list = FXCollections.observableArrayList();
            while (rs.next()) {
                list.add(new GRN(
                        rs.getString("grnID"),
                        rs.getString("itemCode"),
                        rs.getString("supplierID"),
                        rs.getString("batchNo"),
                        rs.getString("grnDate"),
                        rs.getInt("qty"),
                        rs.getDouble("unitPrice"),
                        rs.getDouble("totalCost"),
                        rs.getString("mdf"),
                        rs.getString("expiryDate")
                ));
            }
            return list;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return null;
    }

    private ObservableList<GRN> searchGRN(String filter) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection()
                    .prepareStatement("SELECT grnID,itemCode,supplierID,batchNo,grnDate,qty,unitPrice,totalCost,mdf,expiryDate FROM GRN WHERE CONCAT(grnID,itemCode,supplierID,batchNo,grnDate) LIKE '%" + filter + "%' AND isConformed=0");
            ResultSet rs = pst.executeQuery();
            ObservableList<GRN> list = FXCollections.observableArrayList();
            while (rs.next()) {
                list.add(new GRN(
                        rs.getString("grnID"),
                        rs.getString("itemCode"),
                        rs.getString("supplierID"),
                        rs.getString("batchNo"),
                        rs.getString("grnDate"),
                        rs.getInt("qty"),
                        rs.getDouble("unitPrice"),
                        rs.getDouble("totalCost"),
                        rs.getString("mdf"),
                        rs.getString("expiryDate")
                ));
            }
            return list;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return null;
    }

    public boolean updateGRN(GRN grn) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("UPDATE GRN SET supplierID=?, userID=?, batchNo=?, grnDate=?, qty=?, unitPrice=?, totalCost=?, mdf=?, expiryDate=? WHERE grnID=? AND itemCode=?");
            pst.setString(1, grn.getSupplierID());
            pst.setString(2, grn.getUserID());
            pst.setString(3, grn.getBatchNo());
            pst.setString(4, grn.getGrnDate());
            pst.setInt(5, grn.getQty());
            pst.setDouble(6, grn.getUnitPrice());
            pst.setDouble(7, grn.getTotalCost());
            pst.setString(8, grn.getMdf());
            pst.setString(9, grn.getExpiryDate());
            pst.setString(10, grn.getGrnID());
            pst.setString(11, grn.getItemCode());
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return false;
    }

    @FXML
    private void btnUpdate_OnAction(ActionEvent event) {
        if (txtGRNNo.getText().isEmpty()
                || txtItemCode.getText().isEmpty()
                || txtSupplierID.getText().isEmpty()
                || dtpGRNDate.getValue().toString().isEmpty()
                || txtBatchNo.getText().isEmpty()
                || txtItemQty.getText().isEmpty()
                || txtUnitPrice.getText().isEmpty()
                || txtTotalCost.getText().isEmpty()) {
            MessageBox.messageLable(3, lblMessage, "Warning: Please Fill The From", "", "-fx-text-fill:#e67e22");
        } else {
            String mfDate = dtpMFD.getValue() == null ? null : dtpMFD.getValue().toString();
            String expDate = dtpExpDate.getValue() == null ? null : dtpExpDate.getValue().toString();
            boolean flag = updateGRN(new GRN(
                    txtGRNNo.getText(),
                    txtItemCode.getText(),
                    txtSupplierID.getText(),
                    login.getUserID(),
                    txtBatchNo.getText(),
                    dtpGRNDate.getValue().toString(),
                    Integer.parseInt(txtItemQty.getText()),
                    Double.parseDouble(txtUnitPrice.getText()),
                    Double.parseDouble(txtTotalCost.getText()),
                    mfDate,
                    expDate
            ));
            if (flag) {
                MessageBox.messageLable(3, lblMessage, "Success: GGRN has been Updated.!", "", "-fx-text-fill:#2ecc71");
            }
            clearFeild();
            loadTableView();
        }
    }

    private boolean deleteGRN(String grnNo, String itemCode) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("DELETE FROM GRN WHERE grnID=? AND itemCode=?");
            pst.setString(1, grnNo);
            pst.setString(2, itemCode);
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.messageLable(3, lblMessage, ex.getLocalizedMessage(), "", "-fx-text-fill:#e74c3c");
        }
        return false;
    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) {
        if (tblGRNote.getItems().size() > 0) {
            if (tblGRNote.getSelectionModel().getSelectedItem() != null) {
                if (MessageBox.showConfMessage("Are you Sure?", "Delete Confiemation")) {
                    boolean flag = deleteGRN(tblGRNote.getSelectionModel().getSelectedItem().getGrnID(), tblGRNote.getSelectionModel().getSelectedItem().getItemCode());
                    if (flag) {
                        MessageBox.messageLable(3, lblMessage, "Success: GRNote has been Deleted.!", "", "-fx-text-fill:#2ecc71");
                    }
                    clearFeild();
                    loadTableView();
                }
            } else {
                MessageBox.messageLable(3, lblMessage, "Warning: Please select GRNote to Delete.!", "", "-fx-text-fill:#e67e22");
            }
        } else {
            MessageBox.messageLable(3, lblMessage, "Warning: No Data Found.!", "", "-fx-text-fill:#e67e22");
        }
    }

    @FXML
    private void btnRandomBatchNo_OnAction(ActionEvent event) {
        txtBatchNo.setText("B-" + Long.toString(genRandom()));
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
        txtSupplierID.requestFocus();
    }

    @FXML
    private void btnFindSupplier_OnAction(ActionEvent event) throws IOException {
        FXMLLoader supplierFilter = new Loader().loadeFXML("View/GRNSupplierSearch.fxml");
        Parent root = supplierFilter.load();
        GRNSupplierSearchController controller = supplierFilter.<GRNSupplierSearchController>getController();
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        stage.setAlwaysOnTop(true);
        stage.showAndWait();
        txtSupplierID.setText(controller.getSupplier().getSupplierID());
    }

    @FXML
    private void tblGRNote_OnMouseClicked(MouseEvent event) {
        if (tblGRNote.getSelectionModel().getSelectedItem() != null && event.getClickCount() == 2) {
            GRN s = tblGRNote.getSelectionModel().getSelectedItem();
            txtGRNNo.setText(s.getGrnID());
            txtItemCode.setText(s.getItemCode());
            txtSupplierID.setText(s.getSupplierID());
            txtBatchNo.setText(s.getBatchNo());
            txtItemQty.setText(Integer.toString(s.getQty()));
            txtUnitPrice.setText(Double.toString(s.getUnitPrice()));
            txtTotalCost.setText(Double.toString(s.getTotalCost()));
            dtpGRNDate.setValue(LocalDate.parse(s.getGrnDate()));
            dtpMFD.setValue(LocalDate.parse(s.getMdf()));
            dtpExpDate.setValue(LocalDate.parse(s.getExpiryDate()));
            txtGRNNo.setDisable(true);
            txtItemCode.setDisable(true);
            btnRandomGrnNo.setDisable(true);
            btnFindItem.setDisable(true);
        }
    }

}
