package com.controller.admin;

import com.dao.EmployeeDao;
import com.entities.Employee;
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

public class EmployeeInfoController implements Initializable {

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<Employee, Integer> idColumn;
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> emailColumn;
    @FXML
    private TableColumn<Employee, String> mobileColumn;
    @FXML
    private TableColumn<Employee, String> addressColumn;
    @FXML
    private TableColumn<Employee, String> designationColumn;
    @FXML
    private TableColumn<Employee, Void> actionColumn;

    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set up the cell value factories
        idColumn.setCellValueFactory(new PropertyValueFactory<>("empId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        mobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        designationColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));

        // Set up the action column (if applicable)
        setupActionColumn();
        loadData();
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button actionButton = new Button("Delete");

            {
                actionButton.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    System.out.println("Button clicked for employee: " + employee.getName());
                    showWarningAlert(employee);
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


    private void showWarningAlert(Employee employee){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setPrefSize(310, 170);
        alert.setHeaderText("Are you sure?");

        // Create a custom Label with wrapped text
        Label contentLabel = new Label("All the Information of " + employee.getName() + " will be permanently deleted");
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
                HMS_Utility.handleDeleteRow(employee);
                loadData();
            } else if (buttonType == noButton) {
                alert.close();
            }
        });
    }


    private void loadData() {
        Platform.runLater(() -> {
            Connection con = ConnectionProvider.getConnection();
            EmployeeDao empDao = new EmployeeDao(con);
            ObservableList<Employee> empList = empDao.getEmployeeList();
            for (Employee emp : empList) {
                System.out.println("EmpId : " + emp.getEmpId() + "  EmpName : " + emp.getName());
            }
            if (empList == null || empList.isEmpty()) {
                System.out.println("No data found!");
                return;
            }
            tableView.setMaxHeight(340);
            tableView.setItems(empList);
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
        EmployeeDao empDao = new EmployeeDao(con);
        Employee employee = empDao.getEmployeeInfoOnSearch(Integer.parseInt(searchText));

        ObservableList<Employee> custList = FXCollections.observableArrayList();

        if (employee != null) {
            custList.add(employee);
            tableView.setItems(custList);
            tableView.setMaxHeight(80);
        } else {
            tableView.setItems(custList);
            tableView.setMaxHeight(100);
        }
    }
}
