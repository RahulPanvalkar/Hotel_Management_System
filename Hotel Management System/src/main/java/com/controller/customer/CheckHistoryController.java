package com.controller.customer;

import com.dao.ReservationDao;
import com.entities.Customer;
import com.entities.Reservation;
import com.hms.SessionManager;
import com.utility.ConnectionProvider;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class CheckHistoryController implements Initializable {

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<Customer, Integer> revIdColumn;

    @FXML
    private TableColumn<Customer, Integer> custIdColumn;
    @FXML
    private TableColumn<Customer, String> roomNoColumn;
    @FXML
    private TableColumn<Customer, String> roomTypeColumn;
    @FXML
    private TableColumn<Customer, String> checkInColumn;
    @FXML
    private TableColumn<Customer, String> checkOutColumn;
    @FXML
    private TableColumn<Customer, String> priceColumn;
    @FXML
    private TableColumn<Customer, Void> custNameColumn;
    @FXML
    private TableColumn<Customer, Void> totalPriceColumn;

    @FXML
    private TextField searchField;

    private Customer customer;

    public void handleSearch(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SessionManager session = SessionManager.getInstance();
        customer = (Customer) session.getCurrentUser();

        // Set up the cell value factories
        revIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
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
            ObservableList<Reservation> reservationData = reservationDao.getResvnDataForCustomer(customer.getCustomerId());
            for (Reservation reservation : reservationData) {
                System.out.println("ReservationId : " + reservation.getReservationId() +
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

}
