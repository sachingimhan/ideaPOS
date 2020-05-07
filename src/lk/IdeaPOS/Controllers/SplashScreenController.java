/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Controllers;

import com.jfoenix.controls.JFXProgressBar;
import java.io.File;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lk.IdeaPOS.Util.MessageBox;
import lk.IdeaPOS.View.frmDbConfig;

/**
 * FXML Controller class
 *
 * @author root
 */
public class SplashScreenController implements Initializable {

    @FXML
    private JFXProgressBar pgsLoadingSystem;
    @FXML
    private Label lblInfo;
    @FXML
    private AnchorPane root;
    @FXML
    private StackPane mainroot;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void run() {

        Task task = loadingTask(100);
        pgsLoadingSystem.progressProperty().unbind();
        pgsLoadingSystem.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(e -> {
            lblInfo.setText("All Done..");
            mainroot.toBack();
            System.gc();
        });
        Thread thread = new Thread(task);
        thread.start();
    }
    private int i = 0;

    private Task loadingTask(int secound) {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        creatingConfigFile();
                    }
                });
                for (i = 0; i < secound; i++) {
                    updateProgress(i + 1, secound);
                    Thread.sleep(100);
                }
                return null;
            }
        };
    }

    private boolean checkStatus() {
        String pwd = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        File file = new File(pwd + "/Settings/Config.properties");
        if (file.exists() && file.isFile()) {
            lblInfo.setText("Loading Configuration: Config.properties Found.");
            return true;
        } else {
            lblInfo.setText("Loading Configuration: Config.properties Not Found.");
            return false;
        }
    }

    private void creatingConfigFile() {
        if (!checkStatus()) {
            try{
            lblInfo.setText("Creating Configuration File..");
            new frmDbConfig().setVisible(true);
            lblInfo.setText("Configuration File has been created..");
            }
            catch(IllegalStateException ex){
                MessageBox.showErrorMessage(ex.getMessage(), "Error");
            }
        }
    }

}
