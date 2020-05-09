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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lk.IdeaPOS.Model.DatabaseSetting;
import lk.IdeaPOS.Model.LoginDetail;
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
    private JFXButton btnSetting;
    @FXML
    private Label lblUserStatus;

    private LoginDetail login;
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

    public void setLogin(LoginDetail login) {
        this.login = login;
        lblUserStatus.setText("User : " + login.getUserName() + "  Role : " + login.getRole());
        if (!this.login.getRole().equals("Administrator")) {
            btnMainConfirmation.setDisable(true);
            btnMainDiscount.setDisable(true);
            btnSetting.setDisable(true);
        }
    }

    public LoginDetail getLogin() {
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
        SupplierController controller = loadeFXML.<SupplierController>getController();
        controller.setLogin(login);
        new SlideInUp(supplier).play();
        rootMain.getChildren().setAll(supplier);

    }
    private AnchorPane item;

    @FXML
    private void btnMainItem_OnAction(ActionEvent event) throws IOException {
        FXMLLoader loadeFXML = new Loader().loadeFXML("View/Item.fxml");
        item = loadeFXML.load();
        ItemController controller = loadeFXML.<ItemController>getController();
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
        if (MessageBox.showConfMessage("Do you want to Exit?", "Confirmation")) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        backupDb();
                    } catch (IOException | InterruptedException ex) {
                        MessageBox.showErrorMessage(ex.getLocalizedMessage(), "Error");
                    }
                }
            });
            Platform.exit();
        }

    }

    private void backupDb() throws IOException, InterruptedException {
        DatabaseSetting rp = readProperties();
        String path = rp.getBackupPath();
        File file = new File(path);
        file.mkdir();
        String p = path + "db_" + LocalDateTime.now().toLocalDate().toString() + LocalDateTime.now().getHour() + "-" + LocalDateTime.now().getMinute() + "-" + LocalDateTime.now().getSecond() + ".sql";
        String cmd = rp.getMysqlDir() + "mysqldump --no-create-info -u " + rp.getUser() + " -p" + rp.getPasswd() + " -B " + rp.getDbName() + " -r " + p;
        Runtime runtime = Runtime.getRuntime();
        Process exec = runtime.exec(cmd);
        exec.waitFor();
    }

    private DatabaseSetting readProperties() {
        //get currnt path
        String pwd = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        try {
            //
            InputStream inputStream = new FileInputStream(pwd + "/Settings/Config.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            return new DatabaseSetting(
                    properties.getProperty("db.Host"),
                    properties.getProperty("db.User"),
                    properties.getProperty("db.Pass"),
                    properties.getProperty("db.DbName"),
                    properties.getProperty("db.backupPath"),
                    properties.getProperty("db.mysqlDir")
            );
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    AnchorPane setting;

    @FXML
    private void btnSetting_OnAction(ActionEvent event) throws IOException {
        FXMLLoader loadeFXML = new Loader().loadeFXML("View/Settings.fxml");
        setting = loadeFXML.load();
        new SlideInRight(setting).play();
        rootMain.getChildren().setAll(setting);
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
        FXMLLoader loadeFXML = new Loader().loadeFXML("View/ReportView.fxml");
        report = loadeFXML.load();
        ReportViewController controller = loadeFXML.<ReportViewController>getController();
        controller.setLogin(login);
        new SlideInUp(report).play();
        rootMain.getChildren().setAll(report);
    }
}
