package com.controller.admin;

import com.dao.CustomerDao;
import com.entities.Customer;
import com.utility.ConnectionProvider;
import com.utility.HMS_Utility;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class CustomerInfoController implements Initializable {

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<Customer, Integer> custIdColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> emailColumn;
    @FXML
    private TableColumn<Customer, String> mobileColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableColumn<Customer, Void> actionColumn;

    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set up the cell value factories
        custIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        mobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNum"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        // Set up the action column (if applicable)
        setupActionColumn();
        loadData();
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button actionButton = new Button("Delete");

            {
                actionButton.setOnAction(event -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    System.out.println("Button clicked for customer: " + customer.getName());
                    showWarningAlert(customer);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionButton);
                }
            }
        });
    }

    private void showWarningAlert(Customer customer) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setPrefSize(310, 170);
        alert.setHeaderText("Are you sure?");

        // Create a custom Label with wrapped text
        Label contentLabel = new Label("All the Information of " + customer.getName() + " will be permanently deleted");
        contentLabel.setWrapText(true);
        contentLabel.setMaxHeight(Double.MAX_VALUE);

        // Create a GridPane to hold the Label
        GridPane contentPane = new GridPane();
        contentPane.add(contentLabel, 0, 0);
        alert.getDialogPane().setContent(contentPane);

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == yesButton) {
                HMS_Utility.handleDeleteRow(customer);
                loadData();
            } else if (buttonType == noButton) {
                alert.close();
            }
        });
    }

    private void loadData() {
        Platform.runLater(() -> {
            Connection con = ConnectionProvider.getConnection();
            CustomerDao customerDao = new CustomerDao(con);
            ObservableList<Customer> customerList = customerDao.getCustomerList();
            if (customerList == null || customerList.isEmpty()) {
                System.out.println("No data found!");
                return;
            }
            for (Customer cust : customerList) {
                System.out.println("CustId : " + cust.getCustomerId() + "  CustName : " + cust.getName());
            }
            tableView.setMaxHeight(340);
            tableView.setItems(customerList);
        });
    }

    public void handleSearch(ActionEvent actionEvent) {
        System.out.println("Search button clicked!");

        String searchText = searchField.getText();
        System.out.println("SearchText : " + searchText);

        if (searchText.trim().isEmpty()) {
            loadData();
            return;
        } else if (!searchText.trim().matches("\\d+")) {
            System.out.println("New search text = 0");
            searchText = "0";
        }

        Connection con = ConnectionProvider.getConnection();
        CustomerDao custDao = new CustomerDao(con);
        Customer customer = custDao.getCustomerInfoOnSearch(Integer.parseInt(searchText));

        ObservableList<Customer> custList = FXCollections.observableArrayList();

        if (customer != null) {
            custList.add(customer);
            tableView.setItems(custList);
            tableView.setMaxHeight(80);
        } else {
            tableView.setItems(custList);
            tableView.setMaxHeight(100);
        }
    }

}
