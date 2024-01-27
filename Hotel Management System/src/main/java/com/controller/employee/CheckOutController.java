package com.controller.employee;

import com.dao.ReservationDao;
import com.dao.RoomDao;
import com.entities.Reservation;
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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CheckOutController implements Initializable {


    @FXML
    ChoiceBox<String> roomNo;
    @FXML
    private Label message;
    @FXML
    private TextField searchField;

    @FXML
    private Label reservationIdLabel;
    @FXML
    private Label customerIdLabel;
    @FXML
    private Label custNameLabel;
    @FXML
    private Label checkInTimeLabel;
    @FXML
    private Label checkOutTimeLabel;
    @FXML
    private Label roomNoLabel;
    @FXML
    private Label roomTypeLabel;
    @FXML
    private Label roomPriceLabel;
    @FXML
    private Label totalDaysLabel;
    @FXML
    private Label totalPriceLabel;


    @FXML
    private TableView tableView;

    public TableColumn<Reservation, Integer> revIdColumn;
    @FXML
    private TableColumn<Reservation, Integer> custIdColumn;
    @FXML
    private TableColumn<Reservation, String> nameColumn;
    @FXML
    private TableColumn<Reservation, String> roomNoColumn;
    @FXML
    private TableColumn<Reservation, String> roomTypeColumn;
    @FXML
    private TableColumn<Reservation, String> checkInTimeColumn;
    @FXML
    private TableColumn<Reservation, String> priceColumn;
    @FXML
    private TableColumn<Reservation, Void> actionColumn;

    private Reservation res;
    private HashMap<String, String> labels = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set up the cell value factories
        revIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        custIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        roomNoColumn.setCellValueFactory(new PropertyValueFactory<>("roomNo"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        checkInTimeColumn.setCellValueFactory(new PropertyValueFactory<>("checkInTime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));

        // Set up the action column (if applicable)
        setupActionColumn();
        loadData();

        labels.put("resvnIdLabel", "Reservation Id : ");
        labels.put("custIdLabel", "Customer Id : ");
        labels.put("custNameLabel", "Name : ");
        labels.put("checkInLabel", "Check-In Time : ");
        labels.put("checkOutLabel", "Check-Out Time : ");
        labels.put("roomNoLabel", "Room No : ");
        labels.put("roomTypeLabel", "Room Type : ");
        labels.put("priceLabel", "Price/Day : ");
        labels.put("totalDaysLabel", "Total Days : ");
        labels.put("totalPriceLabel", "Total Price : ");

    }

    private void loadData() {
        Platform.runLater(() -> {
            Connection con = ConnectionProvider.getConnection();
            ReservationDao resDao = new ReservationDao(con);
            ObservableList<Reservation> reservations = resDao.getReservationData();
            if (reservations == null || reservations.isEmpty()) {
                System.out.println("No data found!");
                return;
            }
            for (Reservation reservation : reservations) {
                System.out.println("ReservationId : " + reservation.getReservationId() + "  CustomerName : " + reservation.getCustomerName());
            }
            tableView.setMaxHeight(200);
            tableView.setItems(reservations);
        });
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button actionButton = new Button("Select");

            {
                actionButton.setOnAction(event -> {
                    res = getTableView().getItems().get(getIndex());
                    System.out.println("Button clicked for Reservation : " + res.getReservationId());

                    LocalDateTime currentTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

                    String formattedCheckOutTime = currentTime.format(formatter);
                    System.out.println("Formatted Time : " + formattedCheckOutTime);

                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yy, hh:mm a");
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                    LocalDateTime checkInTime = LocalDateTime.parse(res.getCheckInTime(), inputFormatter);
                    String formattedCheckInTime = checkInTime.format(outputFormatter);
                    System.out.println("getCheckInTime : " + res.getCheckInTime() + " & CheckInTime : " + checkInTime + " & FormattedCheckInTime : " + formattedCheckInTime);


                    long totalDays = currentTime.toLocalDate().toEpochDay() - checkInTime.toLocalDate().toEpochDay();
                    System.out.println("Total Days : " + totalDays);

                    if (totalDays == 0) {
                        totalDays = 1;
                    }

                    double gstPercent = 12;
                    double initialPrice = res.getPricePerDay() * totalDays;
                    double totalPrice = initialPrice * (gstPercent / 100) + initialPrice;
                    System.out.println("Total Price : " + totalPrice);

                    res.setTotalPrice(totalPrice);
                    res.setTotalDays((int) totalDays);

                    System.out.println("value of reservation : " + res);
                    setCheckOutLabels(res.getReservationId(), String.valueOf(res.getCustomerId()), res.getCustomerName(), formattedCheckInTime,
                            formattedCheckOutTime, res.getRoomNo(), res.getRoomType(), String.valueOf(res.getPricePerDay()), String.valueOf(totalDays), totalPrice + " \u20B9");
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


    public void handleCheckOutButton(ActionEvent event) {
        Connection con = ConnectionProvider.getConnection();
        ReservationDao resDao = new ReservationDao(con);

        if (res == null) {
            message.setText("Fields cannot be empty!");
            System.out.println("res == null");
            return;
        }

        try {
            con.setAutoCommit(false);

            boolean isResUpdated = resDao.updateReservation(res.getReservationId(), res.getTotalDays(), res.getTotalPrice());
            System.out.println("isAdded : " + isResUpdated);

            RoomDao roomDao = new RoomDao(con);
            boolean isRoomStatusUpdated = roomDao.updateRoomStatus(res.getRoomNo(), "Available");

            if (isResUpdated && isRoomStatusUpdated) {
                con.commit();
                con.setAutoCommit(true);
                setCheckOutLabels("", "", "", "", "", "", "", "", "", "");
                message.setText("Successfully Checked Out!");
                loadData();
            } else {
                con.rollback();
                con.setAutoCommit(true);
                message.setText("Something went wrong, please try again");
                loadData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleCancelButton(ActionEvent event) {
        res = null;
        setCheckOutLabels("", "", "", "", "", "", "", "", "", "");
    }

    public void handleSearch(ActionEvent actionEvent) {
        String searchText = searchField.getText();
        System.out.println("SearchText : " + searchText);

        if (searchText.trim().isEmpty()) {
            loadData();
            return;
        }

        Connection con = ConnectionProvider.getConnection();
        ReservationDao resDao = new ReservationDao(con);
        Reservation reservation = resDao.getReservationDataOnSearch(searchText);

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

    private void setCheckOutLabels(String resvnId, String custId, String custName, String checkInTime, String checkOutTime, String roomNo,
                                   String roomType, String price, String totalDays, String totalPrice) {

        reservationIdLabel.setText(labels.get("resvnIdLabel") + resvnId);
        customerIdLabel.setText(labels.get("custIdLabel") + custId);
        custNameLabel.setText(labels.get("custNameLabel") + custName);
        checkInTimeLabel.setText(labels.get("checkInLabel") + checkInTime);
        checkOutTimeLabel.setText(labels.get("checkOutLabel") + checkOutTime);
        roomNoLabel.setText(labels.get("roomNoLabel") + roomNo);
        roomTypeLabel.setText(labels.get("roomTypeLabel") + roomType);
        roomPriceLabel.setText(labels.get("priceLabel") + price);
        totalDaysLabel.setText(labels.get("totalDaysLabel") + totalDays);
        totalPriceLabel.setText(labels.get("totalPriceLabel") + totalPrice);

        message.setText("Check Out");
    }
}
