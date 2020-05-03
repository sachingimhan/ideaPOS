/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import lk.IdeaPOS.*;
import lk.IdeaPOS.Util.DBUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;
import org.apache.log4j.PropertyConfigurator;

/**
 * FXML Controller class
 *
 * @author root
 */
public class ReportViewController implements Initializable {

    @FXML
    private AnchorPane holdNode;
    @FXML
    private Button btnReport;
    @FXML
    private SwingNode swingNode;
    
    private JRViewer jrv;
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
    }

    @FXML
    private void btnReport_OnAction(ActionEvent event) {
        loadRepote();
    }

    private void loadRepote() {
        try {
            InputStream jasperStream = Main.class.getResourceAsStream("Reports/rptMonthlySales.jrxml");
            JasperReport jr = JasperCompileManager.compileReport(jasperStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jr, null, DBUtil.getInstance().getConnection());
            swingNode.setContent(new JRViewer(jasperPrint));
            //JasperViewer.viewReport(jasperPrint, false);
            //JasperPrintManager.printReport(jasperPrint, true);
        } catch (ClassNotFoundException | SQLException | JRException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
