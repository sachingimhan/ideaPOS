/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Controllers;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.IdeaPOS.Model.CustomerReturn;
import lk.IdeaPOS.Model.Login;
import lk.IdeaPOS.Model.OrderItem;
import lk.IdeaPOS.Util.DBUtil;
import lk.IdeaPOS.Util.Loader;
import lk.IdeaPOS.Util.MessageBox;
import lk.IdeaPOS.Util.MessageIconType;

/**
 * FXML Controller class
 *
 * @author root
 */
public class CustomerReturnController implements Initializable {

    @FXML
    private AnchorPane customerReturnRoot;
    @FXML
    private ContextMenu ctmDelete;
    @FXML
    private MenuItem mnuDelete;
    @FXML
    private JFXTextField txtCustomerName;
    @FXML
    private JFXTextField txtItemCode;
    @FXML
    private TableView<OrderItem> tblReturn;
    @FXML
    private TableColumn<OrderItem, String> colItemCode;
    @FXML
    private TableColumn<OrderItem, String> colItemName;
    @FXML
    private TableColumn<OrderItem, Double> colUnitPrice;
    @FXML
    private TableColumn<OrderItem, String> colQty;
    @FXML
    private TableColumn<OrderItem, Double> colDiscount;
    @FXML
    private TableColumn<OrderItem, Double> colSubTotal;
    @FXML
    private Label lblMessage;
    @FXML
    private Label lblBalance;
    @FXML
    private Button btnReturn;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnItemCode;
    @FXML
    private Label lblBillAmount;
    @FXML
    private Label lblReturnAmount;

    private static ObservableList<OrderItem> tmp;
    @FXML
    private JFXTextField txtInvoiceNo;

    private Login login;
    private OrderItem orderItem;
    @FXML
    private Button btnClear;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init table
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        //
        initEditableoumn();
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    private ObservableList<OrderItem> loadInvoice(String orderID) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT * FROM OrderItem WHERE orderID=?");
            pst.setString(1, orderID);
            ResultSet rs = pst.executeQuery();
            ObservableList<OrderItem> list = FXCollections.observableArrayList();
            while (rs.next()) {
                list.add(new OrderItem(rs.getString("orderID"), rs.getString("itemCode"), rs.getString("description"), rs.getDouble("unitPrice"), rs.getString("qty"), rs.getDouble("discount"), rs.getDouble("subTotal")));
            }
            return list;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(0, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return null;
    }

    private void initEditableoumn() {
        //Init Qty Column For Editable
        colQty.setCellFactory(TextFieldTableCell.forTableColumn());
        colQty.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<OrderItem, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<OrderItem, String> event) {
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setQty(event.getNewValue());
                double q = Double.parseDouble(tmp.stream().filter(e -> e.getItemCode().equals(event.getTableView().getItems().get(event.getTablePosition().getRow()).getItemCode())).findAny().get().getQty());
                double n = Double.parseDouble(event.getNewValue());
                if (n > q) {
                    event.getTableView().getItems().get(event.getTablePosition().getRow()).setQty(Double.toString(q));
                }
                //update Table Column
                double subtotal = 0;
                double unitprice = 0;
                double discount = 0;
                double itemQty = 0;
                if (event.getTableView().getItems().get(event.getTablePosition().getRow()).getDiscount() != 0.0) {
                    //update table SubTotal When item has Discount                   
                    unitprice = event.getTableView().getItems().get(event.getTablePosition().getRow()).getUnitPrice();
                    discount = event.getTableView().getItems().get(event.getTablePosition().getRow()).getDiscount();
                    itemQty = Double.parseDouble(event.getTableView().getItems().get(event.getTablePosition().getRow()).getQty());
                    subtotal = (unitprice - discount) * itemQty;
                    event.getTableView().getItems().get(event.getTablePosition().getRow()).setSubTotal(subtotal);
                } else {
                    //update table Subtotal When item dose not have a discount
                    itemQty = Double.parseDouble(event.getTableView().getItems().get(event.getTablePosition().getRow()).getQty());
                    unitprice = event.getTableView().getItems().get(event.getTablePosition().getRow()).getUnitPrice();
                    subtotal = itemQty * unitprice;
                    event.getTableView().getItems().get(event.getTablePosition().getRow()).setSubTotal(subtotal);
                }
                tblReturn.refresh();
                calSubTotal();
                txtItemCode.requestFocus();
            }
        });
    }

    private void loadReturnItem(String itemCode) {
        try {
            orderItem = tmp.stream().filter(e -> e.getItemCode().equals(itemCode)).findAny().get();
            boolean found = false;
            if (orderItem != null) {
                for (int i = 0; i < tblReturn.getItems().size(); i++) {
                    if (tblReturn.getItems().get(i).getItemCode().equals(orderItem.getItemCode())) {
                        double ItemQty = Double.parseDouble(tblReturn.getItems().get(i).getQty()) + 1.0;
                        double subTotal = 0;
                        if (orderItem.getDiscount() != 0.0) {
                            subTotal = (tblReturn.getItems().get(i).getUnitPrice() - orderItem.getDiscount()) * ItemQty;
                        } else {
                            subTotal = ItemQty * tblReturn.getItems().get(i).getUnitPrice();
                        }
                        if (Double.parseDouble(tblReturn.getItems().get(i).getQty()) < Double.parseDouble(orderItem.getQty())) {
                            tblReturn.getItems().set(i, new OrderItem(txtInvoiceNo.getText(), orderItem.getItemCode(), orderItem.getDescription(), orderItem.getUnitPrice(), Double.toString(ItemQty), orderItem.getDiscount(), subTotal));
                        }
                        found = true;
                    }
                }
                if (!found) {
                    double subTotal = 0;
                    if (orderItem.getDiscount() != 0.0) {
                        subTotal = (orderItem.getUnitPrice() - orderItem.getDiscount()) * 1;
                    } else {
                        subTotal = orderItem.getUnitPrice() * 1;
                    }
                    tblReturn.getItems().add(new OrderItem(txtInvoiceNo.getText(), orderItem.getItemCode(), orderItem.getDescription(), orderItem.getUnitPrice(), "1.0", orderItem.getDiscount(), subTotal));
                }

            } else {
                MessageBox.show(3, lblMessage, "Item Not Found.!", MessageIconType.WARNING);
            }
        } catch (Exception ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        } finally {
            calSubTotal();
            txtItemCode.setText("");
            txtItemCode.requestFocus();
        }
    }

    private void loadLastBill(String invNo) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT custID,netAmount FROM `Order` WHERE orderID=?");
            pst.setString(1, invNo);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                txtCustomerName.setText(rs.getString("custID"));
                lblBillAmount.setText(rs.getString("netAmount"));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
    }

    private void calSubTotal() {
        double sub = 0;
        for (int i = 0; i < tblReturn.getItems().size(); i++) {
            sub += tblReturn.getItems().get(i).getSubTotal();
        }
        lblReturnAmount.setText(new BigDecimal(sub).setScale(2, RoundingMode.HALF_UP).toString());

        if (!lblBillAmount.getText().isEmpty()) {
            double balance = Double.parseDouble(lblBillAmount.getText()) - sub;
            lblBalance.setText(new BigDecimal(balance).setScale(2, RoundingMode.HALF_UP).toString());
        }
    }

    private void deleteTableItem() {
        if (!tblReturn.getItems().isEmpty() || tblReturn.getSelectionModel().getSelectedItem() != null) {
            int selectedIndex = tblReturn.getSelectionModel().getSelectedIndex();
            tblReturn.getItems().remove(selectedIndex);
            calSubTotal();
        }
    }

    private boolean insertReturnItem(CustomerReturn cr) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("INSERT INTO CustomerReturn (orderID,itemCode,userID,retDate,returnQty,unitPrice,totalAmount) VALUES(?,?,?,?,?,?,?)");
            pst.setString(1, cr.getOrderID());
            pst.setString(2, cr.getItemCode());
            pst.setString(3, cr.getUserID());
            pst.setString(4, cr.getRetDate());
            pst.setDouble(5, cr.getReturnQty());
            pst.setDouble(6, cr.getUnitPrice());
            pst.setDouble(7, cr.getTotalAmount());
            boolean flag = pst.executeUpdate() > 0;
            if (flag) {
                boolean updateItemQty = updateItemQty(cr.getItemCode(), cr.getReturnQty());
                if (!updateItemQty) {
                    return false;
                }
            }
            return flag;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return false;
    }

    private boolean updateItemQty(String itemCode, double returnQty) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("UPDATE Item SET itemQty=itemQty+? WHERE itemCode=?");
            pst.setDouble(1, returnQty);
            pst.setString(2, itemCode);
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return false;
    }

    private void clear() {
        txtCustomerName.setText("");
        txtInvoiceNo.setText("");
        txtItemCode.setText("");
        lblBalance.setText("0.0");
        lblBillAmount.setText("0.0");
        lblReturnAmount.setText("0.0");
        tblReturn.getItems().clear();
    }

    @FXML
    private void btnBack_OnAction(ActionEvent event) throws IOException {
        customerReturnRoot.toBack();
    }

    @FXML
    private void mnuDelete_OnAction(ActionEvent event) {
        deleteTableItem();
    }

    @FXML
    private void tblPos_OnKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE) {
            deleteTableItem();
        }
    }

    @FXML
    private void txtItemCode_OnAction(ActionEvent event) {
        loadReturnItem(txtItemCode.getText());
    }

    @FXML
    private void btnReturn_OnAction(ActionEvent event) {
        ObservableList<CustomerReturn> list = FXCollections.observableArrayList();
        if (tblReturn.getItems().isEmpty() || txtInvoiceNo.getText().isEmpty()) {
            MessageBox.show(3, lblMessage, "There is No Items to Return.", MessageIconType.WARNING);
        } else if (MessageBox.showConfMessage("Are you Sure? Do you want to Return.", "Confirmation")) {
            for (int i = 0; i < tblReturn.getItems().size(); i++) {
                OrderItem get = tblReturn.getItems().get(i);
                list.add(new CustomerReturn(
                        txtInvoiceNo.getText(),
                        get.getItemCode(),
                        login.getUserID(),
                        LocalDate.now().toString(),
                        Double.parseDouble(get.getQty()),
                        get.getUnitPrice(),
                        get.getSubTotal()
                ));
            }
            boolean returnItem = returnItem(list);
            if (returnItem) {
                MessageBox.show(3, lblMessage, "Items has been Return to Store.!", MessageIconType.INFORMATION);
                clear();
            }
        }

    }

    private boolean returnItem(ObservableList<CustomerReturn> list) {
        for (CustomerReturn cr : list) {
            boolean flag = insertReturnItem(cr);
            if (!flag) {
                return false;
            }
        }
        return true;
    }

    @FXML
    private void btnItemCode_OnAction(ActionEvent event) {
        try {
            FXMLLoader itemFilter = new Loader().loadeFXML("View/GRNItemSearch.fxml");
            Parent root = itemFilter.load();
            GRNItemSearchController controller = itemFilter.<GRNItemSearchController>getController();
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
            txtItemCode.setText(controller.getItem().getItemCode());
        } catch (IOException ex) {
            Logger.getLogger(CustomerReturnController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void txtInvoiceNo_OnAction(ActionEvent event) {
        try {
            ObservableList<OrderItem> loadInvoice = loadInvoice(txtInvoiceNo.getText());
            loadLastBill(txtInvoiceNo.getText());
            if (loadInvoice != null) {
                tmp = loadInvoice;
                txtItemCode.requestFocus();
            } else {
                MessageBox.show(3, lblMessage, "Invoice Not Found.!", MessageIconType.ERROR);
            }
        } catch (Exception ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }

    }

    @FXML
    private void btnClear_OnAction(ActionEvent event) {
        clear();
    }

}
