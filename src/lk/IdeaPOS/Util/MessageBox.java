/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

/**
 *
 * @author root
 */
public class MessageBox {

    public static void showInfoMessage(String message, String title) {
        Alert altInfo = new Alert(AlertType.INFORMATION, "", ButtonType.OK);
        altInfo.setTitle(title);
        altInfo.setHeaderText("");
        altInfo.setContentText(message);
        altInfo.show();
    }

    public static void showErrorMessage(String message, String title) {
        Alert altInfo = new Alert(AlertType.ERROR, "", ButtonType.OK);
        altInfo.setTitle(title);
        altInfo.setHeaderText("");
        altInfo.setContentText(message);
        altInfo.show();
    }

    public static void showWarningMessage(String message, String title) {
        Alert altInfo = new Alert(AlertType.WARNING, "", ButtonType.OK);
        altInfo.setTitle(title);
        altInfo.setHeaderText("");
        altInfo.setContentText(message);
        altInfo.show();
    }

    public static boolean showConfMessage(String message, String title) {
        Alert altInfo = new Alert(AlertType.CONFIRMATION);
        altInfo.setTitle(title);
        altInfo.setHeaderText("");
        altInfo.setContentText(message);
        Optional<ButtonType> result = altInfo.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public static void messageLable(int diration, Label lable, String startMessage, String endMessage, String style) {
        lable.setText(startMessage);
        lable.setStyle(style);
        PauseTransition transition = new PauseTransition(Duration.seconds(diration));
        transition.setOnFinished(evt -> lable.setText(endMessage));
        transition.play();
    }

    public static void show(int duration, Label label, String message, MessageIconType typeOf) {
        PauseTransition transition = new PauseTransition(Duration.seconds(duration));
        label.setText(message);
        switch (typeOf) {
            case INFORMATION:
                label.setStyle("-fx-text-fill:#2ecc71");
                break;
            case ERROR:
                label.setStyle("-fx-text-fill:#e74c3c");
                break;
            case WARNING:
                label.setStyle("-fx-text-fill:#e67e22");
                break;
            default:
                break;
        }
        transition.setOnFinished(evt -> label.setText(""));
        transition.play();
    }

//    public static void show(StackPane parentNode, String message, String heading, MessageIconType iconType) {
//        JFXDialogLayout layout = new JFXDialogLayout();
//        JFXDialog dialog = new JFXDialog(parentNode, layout, JFXDialog.DialogTransition.CENTER);
//        FontAwesomeIcon icon = null;
//        //
//        switch (iconType) {
//            case INFORMATION:
//                layout.setStyle("-fx-background-color: #27ae60;");
//                icon = FontAwesomeIcon.CHECK_CIRCLE;
//                break;
//            case ERROR:
//                layout.setStyle("-fx-background-color: #c0392b;");
//                icon = FontAwesomeIcon.TIMES_CIRCLE;
//                break;
//            case WARNING:
//                layout.setStyle("-fx-background-color: #d35400;");
//                icon = FontAwesomeIcon.EXCLAMATION_TRIANGLE;
//                break;
//            case QUESTION:
//                layout.setStyle("-fx-background-color: #2980b9;");
//                icon = FontAwesomeIcon.QUESTION_CIRCLE;
//                break;
//            default:
//                throw new AssertionError();
//        }
//
//        JFXButton button = new JFXButton("OK");
//        button.getStyleClass().add("msg-btn");
//        button.setOnAction((ActionEvent e) -> {
//            dialog.close();
//        });
//        layout.setActions(button);
//
//        Label head = new Label(heading);
//        head.getStyleClass().add("messagebox-lable");
//        Label body = new Label(message);
//        body.getStyleClass().add("messagebox-lable");
//        FontAwesomeIconView view = new FontAwesomeIconView(icon);
//        view.setSize("30");
//        view.setFill(Paint.valueOf("#ffffff"));
//        HBox box = new HBox(10, view, body);
//        box.setAlignment(Pos.CENTER);
//        layout.setHeading(head);
//        layout.setBody(box);
//        dialog.show();
//    }
}
