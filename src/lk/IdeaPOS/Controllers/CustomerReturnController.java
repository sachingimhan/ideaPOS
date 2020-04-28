/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Controllers;

import animatefx.animation.SlideInUp;
import animatefx.animation.SlideOutDown;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.IdeaPOS.Util.Loader;

/**
 * FXML Controller class
 *
 * @author root
 */
public class CustomerReturnController implements Initializable {

    @FXML
    private Button btnnBack;
    @FXML
    private AnchorPane customerReturnRoot;
    @FXML
    private ContextMenu ctmDelete;
    @FXML
    private MenuItem mnuDelete;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void btnnBack_OnAction(ActionEvent event) throws IOException {
        customerReturnRoot.toBack();
    }

    @FXML
    private void mnuDelete_OnAction(ActionEvent event) {
    }

    @FXML
    private void tblPos_OnKeyPressed(KeyEvent event) {
    }

}
