/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Util;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lk.IdeaPOS.Main;

/**
 *
 * @author root
 */
public class Loader {

    private AnchorPane root;
    private FXMLLoader fXMLLoader;
    private StackPane pane;

    public AnchorPane loadPage(String fileName) throws IOException {
        URL resource = Main.class.getResource(fileName);
        root = FXMLLoader.load(resource);
        return root;
    }

    public FXMLLoader loadeFXML(String s) throws IOException {
        URL resource = Main.class.getResource(s);
        fXMLLoader = new FXMLLoader(resource);
        return fXMLLoader;
    }

    public StackPane stackPaneLoader(String s) throws IOException {
        URL resource = Main.class.getResource(s);
        pane = FXMLLoader.load(resource);
        return pane;
    }
}
