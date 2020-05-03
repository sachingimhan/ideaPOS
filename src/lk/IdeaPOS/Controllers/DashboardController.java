/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Controllers;

import animatefx.animation.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.IdeaPOS.Model.Login;
import lk.IdeaPOS.Util.Loader;
import lk.IdeaPOS.Util.MessageBox;

/**
 * FXML Controller class
 *
 * @author root
 */
public class DashboardController implements Initializable {

    @FXML
    private JFXButton btnMainCustomer;
    @FXML
    private JFXButton btnMainSuppliers;
    @FXML
    public StackPane rootMain;
    @FXML
    private JFXButton btnMainItem;
    @FXML
    private JFXButton btnMainGRN;
    @FXML
    private JFXButton exitMainButton;
    @FXML
    private StackPane mainStackPane;
    @FXML
    private JFXButton btnMainMinimize;
    @FXML
    private JFXButton btnSetting;
    @FXML
    private Label lblUserStatus;

    private Login login;
    @FXML
    private JFXButton btnMainPOS;
    @FXML
    private JFXButton btnMainDiscount;
    @FXML
    private JFXButton btnMainConfirmation;
    @FXML
    private Label lblDateTime;
    @FXML
    private JFXButton btnMainDashboard;
    @FXML
    private JFXButton btnMainReport;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DateTime();
        homeLoad();
    }

    private void DateTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            lblDateTime.setText(date.format(DateTimeFormatter.ISO_DATE) + " " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void setLogin(Login login) {
        this.login = login;
        lblUserStatus.setText("User : " + login.getUserName() + "  Role : " + login.getRole());
        if (!this.login.getRole().equals("Administrator")) {
            btnMainConfirmation.setDisable(true);
            btnMainDiscount.setDisable(true);
            btnSetting.setDisable(true);
        }
    }

    public Login getLogin() {
        return login;
    }

    private AnchorPane customer;

    @FXML
    private void btnMainCustomer_onAction(ActionEvent event) throws IOException {
        FXMLLoader loadeFXML = new Loader().loadeFXML("View/Customer.fxml");
        customer = loadeFXML.load();
        CustomerController controller = loadeFXML.<CustomerController>getController();
        controller.setLogin(login);
        new SlideInRight(customer).play();
        rootMain.getChildren().setAll(customer);

    }
    private AnchorPane supplier;

    @FXML
    private void btnMainSuppliers_OnActin(ActionEvent event) throws IOException {
        FXMLLoader loadeFXML = new Loader().loadeFXML("View/Supplier.fxml");
        supplier = loadeFXML.load();
        SupplierController controller=loadeFXML.<SupplierController>getController();
        controller.setLogin(login);
        new SlideInUp(supplier).play();
        rootMain.getChildren().setAll(supplier);

    }
    private AnchorPane item;

    @FXML
    private void btnMainItem_OnAction(ActionEvent event) throws IOException {
        FXMLLoader loadeFXML = new Loader().loadeFXML("View/Item.fxml");
        item=loadeFXML.load();
        ItemController controller=loadeFXML.<ItemController>getController();
        controller.setLogin(login);
        new SlideInRight(item).play();
        rootMain.getChildren().setAll(item);

    }
    private AnchorPane grn;

    @FXML
    private void btnMainGRN_OnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new Loader().loadeFXML("View/GRNote.fxml");
        grn = loader.load();
        new SlideInRight(grn).play();
        rootMain.getChildren().setAll(grn);
        GRNController controller = loader.<GRNController>getController();
        controller.setLogin(login);

    }

    @FXML
    private void exitMainButton_OnAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void btnMainMinimize_OnAction(ActionEvent event) {
        Stage window = (Stage) btnMainMinimize.getScene().getWindow();
        window.setIconified(true);
    }

    @FXML
    private void btnSetting_OnAction(ActionEvent event) {

    }
    private StackPane poss;

    @FXML
    private void btnMainPOS_OnAction(ActionEvent event) throws IOException {
        FXMLLoader pos = new Loader().loadeFXML("View/POSSystem.fxml");
        poss = pos.load();
        new SlideInUp(poss).play();
        rootMain.getChildren().setAll(poss);
        POSSystemController controller = pos.<POSSystemController>getController();
        controller.setLogin(login);

    }
    private AnchorPane discount;

    @FXML
    private void btnMainDiscount_OnAction(ActionEvent event) throws IOException {
        discount = new Loader().loadPage("View/DsicountItem.fxml");
        new SlideInUp(discount).play();
        rootMain.getChildren().setAll(discount);

    }
    private AnchorPane grnConfirm;

    @FXML
    private void btnMainConfirmation_OnAction(ActionEvent event) throws IOException {
        grnConfirm = new Loader().loadPage("View/GRNConfirmation.fxml");
        new SlideInUp(grnConfirm).play();
        rootMain.getChildren().setAll(grnConfirm);
    }

    @FXML
    private void btnMainDashboard_OnAction(ActionEvent event) {
        homeLoad();
    }
    private AnchorPane home;

    private void homeLoad() {
        try {
            home = new Loader().loadPage("View/Home.fxml");
            new SlideInUp(home).play();
            rootMain.getChildren().setAll(home);
        } catch (IOException ex) {
            MessageBox.showErrorMessage(ex.getMessage(), "Error");
        }
    }
    private AnchorPane report;

    @FXML
    private void btnMainReport_OnAction(ActionEvent event) throws IOException {
        report = new Loader().loadPage("View/ReportView.fxml");
        new SlideInUp(report).play();
        rootMain.getChildren().setAll(report);
    }
}
