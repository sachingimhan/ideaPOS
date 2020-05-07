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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import lk.IdeaPOS.Util.DBUtil;
import lk.IdeaPOS.Util.MessageBox;

/**
 * FXML Controller class
 *
 * @author root
 */
public class HomeController implements Initializable {

    @FXML
    private LineChart<String, Double> chtSales;
    @FXML
    private NumberAxis ySales;
    @FXML
    private CategoryAxis xMonth;
    @FXML
    private Label lblOrderCount;
    @FXML
    private Label lblReturnCount;
    @FXML
    private Label lblReorderlvl;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chtSales.getData().setAll(monthlySales());
        chtSales.setLegendVisible(false);
        toDayOrders();
        toDayReturns();
        reorderItems();
    }

    private XYChart.Series monthlySales() {
        try {
            String query = "select CONCAT(YEAR(o.orderDate),'-',MONTHNAME(o.orderDate)) AS YearMonth,sum(o.netAmount)-coalesce(sum(cr.totalAmount),0) AS Salse from `Order` o left join CustomerReturn cr on o.orderID = cr.orderID group by DATE_FORMAT(o.orderDate, '%Y-%m')";
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement(query);
            XYChart.Series series = new XYChart.Series();
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("YearMonth"), rs.getDouble("Salse")));
            }
            return series;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.showErrorMessage(ex.getMessage(), "Error");
        }
        return null;
    }

    private void toDayOrders() {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT COUNT(orderID) as orderCount FROM `Order` WHERE orderDate=DATE(now())");
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                lblOrderCount.setText(String.valueOf(rs.getInt("orderCount")));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.showErrorMessage(ex.getMessage(), "Error");
        }
    }
    
    private void toDayReturns(){
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT COUNT(retID) as returnCount FROM CustomerReturn WHERE retDate=DATE(now())");
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                lblReturnCount.setText(String.valueOf(rs.getInt("returnCount")));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.showErrorMessage(ex.getMessage(), "Error");
        }
    }
    
    private void reorderItems(){
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT COUNT(itemCode) AS reordrItem FROM Item WHERE itemQty<=reorderLevel");
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                lblReorderlvl.setText(String.valueOf(rs.getInt("reordrItem")));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.showErrorMessage(ex.getMessage(), "Error");
        }
    }
}
