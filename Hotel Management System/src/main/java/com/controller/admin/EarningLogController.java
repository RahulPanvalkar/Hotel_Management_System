package com.controller.admin;

import com.dao.ReservationDao;
import com.entities.Reservation;
import com.entities.Employee;
import com.utility.ConnectionProvider;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class EarningLogController implements Initializable {

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<Employee, Integer> revIdColumn;

    @FXML
    private TableColumn<Employee, Integer> custIdColumn;
    @FXML
    private TableColumn<Employee, String> roomNoColumn;
    @FXML
    private TableColumn<Employee, String> roomTypeColumn;
    @FXML
    private TableColumn<Employee, String> checkInColumn;
    @FXML
    private TableColumn<Employee, String> checkOutColumn;
    @FXML
    private TableColumn<Employee, String> priceColumn;
    @FXML
    private TableColumn<Employee, Void> custNameColumn;
    @FXML
    private TableColumn<Employee, Void> totalPriceColumn;

    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set up the cell value factories
        revIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        custIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        custNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        roomNoColumn.setCellValueFactory(new PropertyValueFactory<>("roomNo"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        checkInColumn.setCellValueFactory(new PropertyValueFactory<>("checkInTime"));
        checkOutColumn.setCellValueFactory(new PropertyValueFactory<>("checkOutTime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        loadData();
    }

    private void loadData() {
        Platform.runLater(() -> {
            Connection con = ConnectionProvider.getConnection();
            ReservationDao reservationDao = new ReservationDao(con);
            ObservableList<Reservation> reservationData = reservationDao.getAllReservations();
            for (Reservation reservation : reservationData) {
                System.out.println("ReservationId : " + reservation.getReservationId() + ",  CustomerId : " + reservation.getCustomerId() +
                        ",  checkInTime : " + reservation.getCheckInTime() + ",  checkOutTime : " + reservation.getCheckOutTime());
            }
            if (reservationData == null || reservationData.isEmpty()) {
                System.out.println("No data found!");
                return;
            }
            tableView.setMaxHeight(340);
            tableView.setItems(reservationData);
        });
    }


    public void handleSearch(ActionEvent event) {
        System.out.println("Search button clicked!");
        String searchText = searchField.getText();
        System.out.println("SearchText : " + searchText);

        if (searchText.trim().isEmpty()) {
            loadData();
            return;
        }

        Connection con = ConnectionProvider.getConnection();
        ReservationDao resDao = new ReservationDao(con);
        Reservation reservation = resDao.getReservationOnSearch(searchText);

        ObservableList<Reservation> resList = FXCollections.observableArrayList();

        if (reservation != null) {
            resList.add(reservation);
            tableView.setItems(resList);
            tableView.setMaxHeight(80);
        } else {
            tableView.setItems(resList);
            tableView.setMaxHeight(100);
        }
    }
}
