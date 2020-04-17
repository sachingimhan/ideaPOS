/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.AnchorPane;
import lk.IdeaPOS.*;
import lk.IdeaPOS.Model.LoginDetail;
import lk.IdeaPOS.Util.DBUtil;
import lk.IdeaPOS.Util.MessageBox;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;

/**
 * FXML Controller class
 *
 * @author root
 */
public class ReportViewController implements Initializable {

    @FXML
    private AnchorPane holdNode;
    private SwingNode swingNode;

    private JRViewer jrv;
    @FXML
    private JFXComboBox<String> cmbType;
    @FXML
    private JFXDatePicker dtpFrom;
    @FXML
    private JFXDatePicker dtpTo;
    @FXML
    private JFXButton btnReport;

    private LoginDetail login;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        swingNode = new SwingNode();
        swingNode.setContent(new JRViewer(null));
        AnchorPane.setBottomAnchor(swingNode, 0.0);
        AnchorPane.setLeftAnchor(swingNode, 0.0);
        AnchorPane.setRightAnchor(swingNode, 0.0);
        AnchorPane.setTopAnchor(swingNode, 0.0);
        holdNode.getChildren().add(swingNode);
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll("Order Detail", "Sales Report", "GRN Report", "User Sales");
        cmbType.setItems(list);
        cmbType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (cmbType.getSelectionModel().getSelectedIndex() == 3) {
                    dtpFrom.setDisable(true);
                    dtpTo.setDisable(true);
                } else {
                    dtpFrom.setDisable(false);
                    dtpTo.setDisable(false);
                }
            }
        });
    }

    public void setLogin(LoginDetail login) {
        this.login = login;
    }

    private void loadRepote(Map<String, Object> parm, String report) {
        try {
            InputStream jasperStream = Main.class.getResourceAsStream("Reports/" + report + ".jrxml");
            JasperReport jr = JasperCompileManager.compileReport(jasperStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jr, parm, DBUtil.getInstance().getConnection());
            swingNode.setContent(new JRViewer(jasperPrint));
            //JasperViewer.viewReport(jasperPrint, false);
            //JasperPrintManager.printReport(jasperPrint, true);
        } catch (ClassNotFoundException | SQLException | JRException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void btnReport_OnAction(ActionEvent event) {
        if (cmbType.getSelectionModel().getSelectedItem() != null) {

            if (cmbType.getSelectionModel().getSelectedIndex() == 3) {
                Map<String, Object> map = new HashMap<>();
                map.put("userID", login.getUserID());
                loadRepote(map, "rptUserSalseReport");
                return;
            }

            if (dtpFrom.getValue() != null && dtpTo.getValue() != null) {
                if (cmbType.getSelectionModel().getSelectedIndex() == 0) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("fromDate", dtpFrom.getValue().toString());
                    map.put("toDate", dtpTo.getValue().toString());
                    loadRepote(map, "rptorderDetail");
                } else if (cmbType.getSelectionModel().getSelectedIndex() == 1) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("fromDate", dtpFrom.getValue().toString());
                    map.put("toDate", dtpTo.getValue().toString());
                    loadRepote(map, "rptItemSalesReport");
                } else if (cmbType.getSelectionModel().getSelectedIndex() == 2) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("fromDate", dtpFrom.getValue().toString());
                    map.put("toDate", dtpTo.getValue().toString());
                    loadRepote(map, "rptGRNReport");
                }
            } else {
                MessageBox.showWarningMessage("Please Select From Date Or To Date.!", "Warning");
            }

        } else {
            MessageBox.showWarningMessage("Please Select Report Type.!", "Warning");
        }
    }

}
