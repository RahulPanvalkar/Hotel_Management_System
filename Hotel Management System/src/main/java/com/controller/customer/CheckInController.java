package com.controller.customer;

import com.dao.ReservationDao;
import com.dao.RoomDao;
import com.entities.Customer;
import com.entities.Reservation;
import com.entities.Rooms;
import com.hms.Main;
import com.hms.SessionManager;
import com.utility.ConnectionProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

public class CheckInController implements Initializable {

    @FXML
    private ChoiceBox<String> roomNo;
    @FXML
    private Label message;
    @FXML
    private Label customerId;
    @FXML
    private Label custNameLabel;
    @FXML
    private Label custEmailLabel;
    @FXML
    private Label custMobileLabel;
    @FXML
    private Label roomPriceLabel;
    @FXML
    private Label roomTypeLabel;
    @FXML
    private DatePicker checkInDate;
    @FXML
    private DatePicker checkOutDate;

    private Rooms room;
    private Customer customer;
    private HashMap<String, String> labels = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (roomNo != null) {
            labels.put("custIdLabel", "Customer Id : ");
            labels.put("custNameLabel", "Name : ");
            labels.put("custEmailLabel", "Email : ");
            labels.put("custMobileLabel", "Mobile : ");
            labels.put("roomTypeString", "Room Type : ");
            labels.put("roomPriceString", "Price/Day : ");

            SessionManager session = SessionManager.getInstance();
            customer = (Customer) session.getCurrentUser();
            System.out.println("Check in started for customer: \n" + customer);

            setAvailableRoomList();

            setCheckInLabels(String.valueOf(customer.getCustomerId()), customer.getName(), customer.getEmail(),
                    customer.getMobileNum(),  "", "");
        }

    }

    private void setAvailableRoomList() {
        Connection con = ConnectionProvider.getConnection();
        RoomDao roomDao = new RoomDao(con);
        ArrayList<String> roomList = roomDao.getAvailableRoomList();
        System.out.println("RoomList : " + roomList);
        roomNo.setValue("Select");
        roomNo.getItems().setAll(roomList);
    }

    public void handleChoiceBoxSelection(ActionEvent event) {
        Connection con = ConnectionProvider.getConnection();
        RoomDao roomDao = new RoomDao(con);
        room = roomDao.getRoom(roomNo.getValue());

        if (room != null) {
            roomTypeLabel.setText(labels.get("roomTypeString") + room.getRoomType());
            roomPriceLabel.setText(labels.get("roomPriceString") + room.getPrice());
        }
    }

    public void handleCheckInButton(ActionEvent event) throws IOException {
        System.out.println("inside handleCheckInButton..");
        if (!datesValidation()){
            return;
        }

        LocalDate checkInLocalDate = checkInDate.getValue();
        LocalDate checkOutLocalDate = checkOutDate.getValue();

        if (checkInLocalDate == null || checkOutLocalDate == null || roomNo.getValue().trim().equalsIgnoreCase("Select")){
            message.setText("Field cannot be empty!");
            return;
        }

        long totalDays = checkOutLocalDate.toEpochDay() - checkInLocalDate.toEpochDay();
        System.out.println("Total Days : " + totalDays);

        if (totalDays == 0) {
            totalDays = 1;
        }

        Reservation reservation = null;
        if (customer != null && room != null) {

            reservation = new Reservation();
            reservation.setReservationId(createReservationNo());
            reservation.setCustomerId(customer.getCustomerId());
            reservation.setCustomerName(customer.getName());
            reservation.setRoomNo(room.getRoomNo());
            reservation.setRoomType(room.getRoomType());
            reservation.setPricePerDay(room.getPrice());

            double gstPercent = 12;
            double initialPrice = reservation.getPricePerDay() * totalDays;
            double totalPrice = initialPrice * (gstPercent / 100) + initialPrice;
            System.out.println("Total Price : " + totalPrice);

            reservation.setTotalPrice(totalPrice);
            reservation.setTotalDays((int) totalDays);
            System.out.println("value of reservation : " + reservation);

            LocalDateTime checkInDateTime = LocalDateTime.of(checkInLocalDate, LocalTime.of(0,0));
            LocalDateTime checkOutDateTime = LocalDateTime.of(checkOutLocalDate, LocalTime.of(0,0));
            System.out.println("checkInDateTime : "+checkInDateTime+" & checkOutDateTime : "+checkOutDateTime);
            reservation.setCheckInTime(checkInDateTime);
            reservation.setCheckOutTime(checkOutDateTime);

            //load Payment page
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Customer/PaymentScreen.fxml"));
            Parent root = loader.load();
            // pass checkInData to Payment page
            PaymentScreenController paymentScreenController = loader.getController();
            paymentScreenController.receiveCheckInData(reservation);

            Stage paymentStage = new Stage();
            paymentStage.initModality(Modality.APPLICATION_MODAL);
            paymentStage.setTitle("Payment Details");
            paymentStage.setScene(new Scene(root));
            paymentStage.setResizable(false);
            paymentStage.showAndWait();

            // Retrieve the value from PaymentScreenController
            boolean isPaymentSuccessful = paymentScreenController.getIsPaymentSuccessful();
            System.out.println("Value from paymentStage => isPaymentSuccessful : " + isPaymentSuccessful);

            System.out.println("Back in handleCheckInButton method..");
            if (isPaymentSuccessful) {
                setCheckInLabels(String.valueOf(customer.getCustomerId()), customer.getName(), customer.getEmail(),
                        customer.getMobileNum(), "", "");
                message.setText("Check In Successful!");
                message.setStyle("-fx-text-fill : white");
            } else {
                message.setText("Payment Unsuccessful! please try again");
                setCheckInLabels(String.valueOf(customer.getCustomerId()), customer.getName(), customer.getEmail(),
                        customer.getMobileNum(),  "", "");

            }
        }

    }

    private String createReservationNo() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000000) + 1000000;
        System.out.println("RandomNumber : " + randomNumber);
        String reservationNo = "HMS" + randomNumber;
        System.out.println("ReservationNo : " + reservationNo);

        Connection con = ConnectionProvider.getConnection();
        ReservationDao resDao = new ReservationDao(con);
        boolean isPresent = resDao.checkResNo(reservationNo);

        if (isPresent) {
            System.out.println("Reservation No Already Exist!");
            createReservationNo();
        }
        return reservationNo;
    }

    private void setCheckInLabels(String custId, String custName, String custEmail, String custMob,
                                  String roomType, String price) {

        customerId.setText(labels.get("custIdLabel") + custId);
        custNameLabel.setText(labels.get("custNameLabel") + custName);
        custEmailLabel.setText(labels.get("custEmailLabel") + custEmail);
        custMobileLabel.setText(labels.get("custMobileLabel") + custMob);
        roomTypeLabel.setText(labels.get("roomTypeString") + roomType);
        roomPriceLabel.setText(labels.get("roomPriceString") + price);
        roomNo.setValue("Select");

        message.setText("Check In");
    }

    private boolean datesValidation(){
        System.out.println("inside datesValidation...");
        boolean isValid = false;

        LocalDate checkIn = checkInDate.getValue();
        LocalDate checkOut = checkOutDate.getValue();
        System.out.println("checkIn : "+checkIn+" & checkOut : "+checkOut);

        if (checkIn == null || checkOut == null){
            message.setStyle("-fx-text-fill : #ce3c2f;  -fx-font-weight: bold;");
            message.setText("Please enter valid date!");
            return isValid;
        }

        boolean isCheckInBeforeCurrent = checkIn.isBefore(LocalDate.now());
        boolean isCheckOutBeforeCurrent = checkOut.isBefore(LocalDate.now());
        boolean isCheckInAfterCheckOut = checkIn.isAfter(checkOut);
        System.out.println("isCheckInBeforeCurrent : "+isCheckInBeforeCurrent+" & \nisCheckOutBeforeCurrent : "+
                isCheckOutBeforeCurrent+" & \nisCheckInAfterCheckOut : "+isCheckInAfterCheckOut);

        if(isCheckInBeforeCurrent){
            message.setText("Invalid Check In date!");
            return false;
        } else if (isCheckOutBeforeCurrent){
            message.setText("Invalid Check Out date!");
            return false;
        } else if (isCheckInAfterCheckOut){
            message.setText("Check-In date cannot be greater than Check-Out date");
            return false;
        }else {
            isValid = true;
        }

        return isValid;
    }

}
