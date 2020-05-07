/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.IdeaPOS.Controllers;

import animatefx.animation.SlideInUp;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.file.FileSystems;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.IdeaPOS.Main;
import lk.IdeaPOS.Model.BusinessDetails;
import lk.IdeaPOS.Model.Item;
import lk.IdeaPOS.Model.LoginDetail;
import lk.IdeaPOS.Model.Order;
import lk.IdeaPOS.Model.OrderItem;
import lk.IdeaPOS.Util.DBUtil;
import lk.IdeaPOS.Util.Loader;
import lk.IdeaPOS.Util.MessageBox;
import lk.IdeaPOS.Util.MessageIconType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author root
 */
public class POSSystemController implements Initializable {

    @FXML
    private JFXTextField txtItemCode;
    @FXML
    private Button btnFindItem;
    @FXML
    private JFXTextField txtCustomerCode;
    @FXML
    private JFXTextField txtCustomerName;
    @FXML
    private JFXTextField txtDiscount;
    @FXML
    private TableView<OrderItem> tblPos;
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
    private Label lblOrderID;

    private String orderID;
    @FXML
    private Button btnAdd;
    @FXML
    private Label lblSubTotal;
    @FXML
    private JFXTextField txtCash;
    @FXML
    private Label lblNetAmount;
    @FXML
    private Label lblBalance;
    @FXML
    private Label lblDate;
    @FXML
    private JFXRadioButton rbCardPay;
    @FXML
    private JFXRadioButton rbCashPay;

    private double netAmount;
    private ObservableList<OrderItem> orderItems;
    private LoginDetail login;
    private double itemQty;

    @FXML
    private Button btnReturn;
    @FXML
    private Button btnHoldTran;
    @FXML
    private Button btnPayment;
    @FXML
    private ContextMenu ctmDelete;
    @FXML
    private MenuItem mnuDelete;
    @FXML
    private StackPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Init Table Column
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory<>("subTotal"));

        //
        txtDiscount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtDiscount.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (!txtDiscount.getText().isEmpty()) {
                    if (!txtDiscount.getText().matches("\\b(0*(?:[0-9][0-9]?|100))\\b")) {
                        txtDiscount.setText(oldValue);
                        txtDiscount.positionCaret(txtDiscount.getText().length());
                    }
                    netAmount();
                }
            }
        });
        txtCash.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtCash.setText(newValue.replaceAll("[^\\d]", ""));
                }
                callBalance();
            }
        });
        //
        lblDate.setText(LocalDate.now().toString());

        //Add Payment Method to Toggle Group
        ToggleGroup group = new ToggleGroup();
        rbCashPay.setToggleGroup(group);
        rbCardPay.setToggleGroup(group);

        //
        txtCustomerName.setDisable(true);

        //Set Default Values
        setDefautValues();
        initEditableoumn();
    }

    private void initEditableoumn() {
        //Init Qty Column For Editable
        colQty.setCellFactory(TextFieldTableCell.forTableColumn());
        colQty.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<OrderItem, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<OrderItem, String> event) {
                try {
                    event.getTableView().getItems().get(event.getTablePosition().getRow()).setQty(event.getNewValue());
                    if (Double.parseDouble(event.getNewValue()) > itemQty) {
                        event.getTableView().getItems().get(event.getTablePosition().getRow()).setQty(Double.toString(itemQty));
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
                    tblPos.refresh();
                    calSubTotal();
                    txtItemCode.requestFocus();
                } catch (NumberFormatException | NullPointerException ex) {
                    MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
                }

            }
        });
    }

    private void setDefautValues() {
        txtCustomerCode.setText("CUST-0001");
        txtCustomerName.setText(getCustomer("CUST-0001"));
        //set Discount
        txtDiscount.setText("0");
        //set Paymet Method
        rbCashPay.fire();
        getInvoiceNo();
    }

    public void setLogin(LoginDetail login) {
        this.login = login;
    }

    private void getInvoiceNo() {
        String last = getLastOrderID();
        if (last != null) {
            String[] split = last.split("-");
            int num = Integer.parseInt(split[1]);
            num++;
            if (num < 10) {
                orderID = "INV-000" + num;
            } else if (num < 100) {
                orderID = "INV-00" + num;
            } else if (num < 1000) {
                orderID = "INV-0" + num;
            } else {
                orderID = "INV-" + num;
            }
        } else {
            orderID = "INV-0001";
        }
        lblOrderID.setText(orderID);
    }

    @FXML
    private void btnFindItem_OnAction(ActionEvent event) throws IOException {
        FXMLLoader itemFilter = new Loader().loadeFXML("View/GRNItemSearch.fxml");
        Parent root = itemFilter.load();
        GRNItemSearchController controller = itemFilter.<GRNItemSearchController>getController();
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        stage.setAlwaysOnTop(true);
        stage.showAndWait();
        txtItemCode.setText(controller.getItem().getItemCode());
        btnAdd.fire();
    }

    @FXML
    private void txtItemCode_OnAction(ActionEvent event) {
        btnAdd.fire();
    }

    private void addItemtoTable() {
        Item item = selectedItemLoad(txtItemCode.getText());
        boolean found = false;
        if (item != null) {
            if (item.getItemQty() > 0) {
                itemQty = item.getItemQty();
                for (int i = 0; i < tblPos.getItems().size(); i++) {
                    if (tblPos.getItems().get(i).getItemCode().equals(txtItemCode.getText())) {
                        double ItemQty = Double.parseDouble(tblPos.getItems().get(i).getQty()) + 1.0;
                        double subTotal = 0;
                        if (item.getDiscount() != 0.0) {
                            subTotal = (tblPos.getItems().get(i).getUnitPrice() - item.getDiscount()) * ItemQty;
                        } else {
                            subTotal = ItemQty * tblPos.getItems().get(i).getUnitPrice();
                        }
                        if (Double.parseDouble(tblPos.getItems().get(i).getQty()) < itemQty) {
                            tblPos.getItems().set(i, new OrderItem(orderID, item.getItemCode(), item.getItemName(), item.getRetailPrice(), Double.toString(ItemQty), item.getDiscount(), subTotal));
                        }
                        found = true;
                    }
                }
                if (!found) {
                    double subTotal = 0;
                    if (item.getDiscount() != 0.0) {
                        subTotal = (item.getRetailPrice() - item.getDiscount()) * 1;
                    } else {
                        subTotal = item.getRetailPrice() * 1;
                    }
                    tblPos.getItems().add(new OrderItem(orderID, item.getItemCode(), item.getItemName(), item.getRetailPrice(), "1.0", item.getDiscount(), subTotal));
                }

            } else {
                MessageBox.show(3, lblMessage, "Stock is Empty.! Please Refill Stock", MessageIconType.WARNING);
            }
            txtItemCode.setText("");
            calSubTotal();
            txtItemCode.requestFocus();
        }
    }

    private void calSubTotal() {
        double sub = 0;
        for (int i = 0; i < tblPos.getItems().size(); i++) {
            sub += tblPos.getItems().get(i).getSubTotal();
        }
        lblSubTotal.setText(new BigDecimal(sub).setScale(2, RoundingMode.HALF_UP).toString());
        netAmount();
    }

    private void netAmount() {
        if (txtDiscount.getText().isEmpty()) {
            netAmount = Double.valueOf(lblSubTotal.getText());
            lblNetAmount.setText("Rs." + new BigDecimal(netAmount).setScale(2, RoundingMode.HALF_UP));
        } else {
            double tmp = Double.valueOf(lblSubTotal.getText());
            netAmount = tmp - (tmp * Double.valueOf(txtDiscount.getText()) / 100);
            lblNetAmount.setText("Rs." + new BigDecimal(netAmount).setScale(2, RoundingMode.HALF_UP).toString());
        }
    }

    private void callBalance() {
        if (!txtCash.getText().isEmpty()) {
            double net = Double.valueOf(txtCash.getText()) - netAmount;
            lblBalance.setText(new BigDecimal(net).setScale(2, RoundingMode.HALF_UP).toString());
        }
    }

    private Item selectedItemLoad(String itemCode) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT itemCode,itemName,retailPrice,discount,itemQty from Item WHERE itemCode=?");
            pst.setString(1, itemCode);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Item(rs.getString("itemCode"), rs.getString("itemName"), rs.getDouble("retailPrice"), rs.getDouble("discount"), rs.getInt("itemQty"));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }

        return null;
    }

    private String getLastOrderID() {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT orderID FROM `Order` ORDER BY orderID DESC LIMIT 1");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return null;
    }

    private String getCustomer(String custID) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("SELECT custName FROM Customer WHERE custID=?");
            pst.setString(1, custID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("custName");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return null;
    }

    private boolean insertOrder(Order order) throws ClassNotFoundException, SQLException {
        try {
            DBUtil.getInstance().getConnection().setAutoCommit(false);
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("INSERT INTO `Order` VALUES(?,?,?,?,?,?,?,?,?)");
            pst.setString(1, order.getOrderID());
            pst.setString(2, order.getCustID());
            pst.setString(3, order.getUserID());
            pst.setString(4, order.getOrderDate());
            pst.setString(5, order.getPaymentMethod());
            pst.setDouble(6, order.getGrossAmount());
            pst.setDouble(7, order.getNetAmount());
            pst.setDouble(8, order.getCash());
            pst.setDouble(9, order.getBalance());
            boolean flag = pst.executeUpdate() > 0;
            if (flag) {
                for (OrderItem oi : order.getOrderItems()) {
                    boolean insertOrderItem = insertOrderItem(oi);
                    if (!insertOrderItem) {
                        DBUtil.getInstance().getConnection().rollback();
                        return false;
                    }
                }
            }
            DBUtil.getInstance().getConnection().commit();
            return flag;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        } finally {
            DBUtil.getInstance().getConnection().setAutoCommit(true);
        }
        return false;
    }

    private boolean insertOrderItem(OrderItem item) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("INSERT INTO OrderItem VALUES(?,?,?,?,?,?,?)");
            pst.setString(1, item.getOrderID());
            pst.setString(2, item.getItemCode());
            pst.setString(3, item.getDescription());
            pst.setDouble(4, item.getUnitPrice());
            pst.setDouble(5, Double.parseDouble(item.getQty()));
            pst.setDouble(6, item.getDiscount());
            pst.setDouble(7, item.getSubTotal());
            boolean flag = pst.executeUpdate() > 0;
            if (flag) {
                boolean updateStock = updateStock(item.getItemCode(), item.getQty());
                if (!updateStock) {
                    return false;
                }
            }
            return flag;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return false;
    }

    private boolean updateStock(String ItemCode, String qty) {
        try {
            PreparedStatement pst = DBUtil.getInstance().getConnection().prepareStatement("UPDATE Item SET itemQty=itemQty-? WHERE itemCode=?");
            pst.setDouble(1, Double.parseDouble(qty));
            pst.setString(2, ItemCode);
            return pst.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return false;
    }

    private void clear() {
        setDefautValues();
        lblSubTotal.setText("0.00");
        lblNetAmount.setText("0.00");
        txtCash.setText("");
        lblBalance.setText("0.00");
        tblPos.getItems().clear();
    }

    @FXML
    private void btnAdd_OnAction(ActionEvent event) {
        addItemtoTable();
    }

    @FXML
    private void txtDiscount_OnAction(ActionEvent event) {
        txtCash.requestFocus();
    }

    @FXML
    private void txtCash_OnActin(ActionEvent event) {
        btnPayment.fire();
    }

    @FXML
    private void txtCustomerCode_OnAction(ActionEvent event) {
        txtCustomerName.setText(getCustomer(txtCustomerCode.getText()));
    }
    AnchorPane custReturn;

    @FXML
    private void btnReturn_OnAction(ActionEvent event) throws IOException {
        if (root.getChildren().contains(custReturn)) {
            new SlideInUp(custReturn).play();
            custReturn.toFront();
        } else {
            FXMLLoader loadeFXML = new Loader().loadeFXML("View/CustomerReturn.fxml");
            custReturn = loadeFXML.load();
            CustomerReturnController controller = loadeFXML.<CustomerReturnController>getController();
            controller.setLogin(login);
            new SlideInUp(custReturn).play();
            root.getChildren().add(custReturn);
        }
    }

    @FXML
    private void btnHoldTran_OnAction(ActionEvent event) {

    }

    @FXML
    private void btnPayment_OnAction(ActionEvent event) {
        orderItems = FXCollections.observableArrayList();
        String payment = null;
        if (txtCustomerCode.getText().isEmpty() || txtCustomerName.getText().isEmpty() || tblPos.getItems().isEmpty() || txtCash.getText().isEmpty()) {
            MessageBox.show(3, lblMessage, "Order Canot Complete.!", MessageIconType.WARNING);
            //clear();
        } else {
            if (rbCashPay.isSelected()) {
                payment = rbCashPay.getText();
            } else {
                payment = rbCardPay.getText();
            }
            if (MessageBox.showConfMessage("Are you sure do you want to complete transaction", "Confirmation")) {
                for (int i = 0; i < tblPos.getItems().size(); i++) {
                    OrderItem get = tblPos.getItems().get(i);
                    orderItems.add(new OrderItem(
                            lblOrderID.getText(),
                            get.getItemCode(),
                            get.getDescription(),
                            get.getUnitPrice(),
                            get.getQty(),
                            get.getDiscount(),
                            get.getSubTotal()
                    ));
                }
            }
            // Insert Order
            boolean insertOrder;
            try {
                insertOrder = insertOrder(new Order(
                        lblOrderID.getText(),
                        txtCustomerCode.getText(),
                        login.getUserID(),
                        lblDate.getText(),
                        payment,
                        Double.valueOf(lblSubTotal.getText()),
                        netAmount,
                        Double.valueOf(txtCash.getText()),
                        Double.valueOf(lblBalance.getText()),
                        orderItems
                ));
                if (insertOrder) {
                    printBill(lblOrderID.getText());
                    MessageBox.show(3, lblMessage, "Payment Success.!", MessageIconType.INFORMATION);
                    clear();
                }
            } catch (ClassNotFoundException | SQLException ex) {
                MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
            }
        }
    }

    private void deleteTableItem() {
        if (!tblPos.getItems().isEmpty() || tblPos.getSelectionModel().getSelectedItem() != null) {
            int selectedIndex = tblPos.getSelectionModel().getSelectedIndex();
            tblPos.getItems().remove(selectedIndex);
            calSubTotal();
        }
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

    private void printBill(String OrderID) {
        try {
            BusinessDetails rp = readProperties();
            InputStream jasperStream = Main.class.getResourceAsStream("Reports/rptBill.jrxml");
            JasperReport jr = JasperCompileManager.compileReport(jasperStream);
            Map<String, Object> parm = new HashMap<>();
            parm.put("orderID", OrderID);
            parm.put("businessName", rp.getName());
            parm.put("address", rp.getAddress());
            parm.put("tel", rp.getContact());
            parm.put("regNo", rp.getRegNo());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jr, parm, DBUtil.getInstance().getConnection());
            //JasperViewer.viewReport(jasperPrint, false);
            JasperPrintManager.printReport(jasperPrint, false);
        } catch (ClassNotFoundException | SQLException | JRException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private BusinessDetails readProperties() {
        //get currnt path
        String pwd = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        try {
            File file = new File(pwd + "/Settings/BusinessData.properties");
            if (file.exists()) {
                InputStream inputStream = new FileInputStream(pwd + "/Settings/BusinessData.properties");
                Properties properties = new Properties();
                properties.load(inputStream);
                return new BusinessDetails(
                        properties.getProperty("biz.Name"),
                        properties.getProperty("biz.Address"),
                        properties.getProperty("biz.Contact"),
                        properties.getProperty("biz.RegNo")
                );
            }
            else{
                MessageBox.showErrorMessage("BusinessData.properties Not Found.!", "Error");
            }

        } catch (IOException ex) {
            MessageBox.show(3, lblMessage, ex.getLocalizedMessage(), MessageIconType.ERROR);
        }
        return null;
    }
}
