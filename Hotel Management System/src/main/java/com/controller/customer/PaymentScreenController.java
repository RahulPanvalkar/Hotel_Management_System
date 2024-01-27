package com.controller.customer;

import com.dao.ReservationDao;
import com.dao.RoomDao;
import com.entities.Reservation;
import com.utility.ConnectionProvider;
import com.utility.HMS_Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PaymentScreenController implements Initializable {
    public TextField cardNumber;
    public DatePicker expiryDate;
    public TextField cvcNumber;
    public Label payAmountLabel;
    @FXML
    private Label errorMsg;

    private Reservation reservation;

    private final String priceLabel = "Total Amount : ";
    private boolean isPaymentSuccessful;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Inside initialize of PaymentScreenController.."+reservation);
        if (reservation != null)
            System.out.println("Total Price : "+reservation.getTotalPrice());
    }

    public boolean getIsPaymentSuccessful() {
        return isPaymentSuccessful;
    }

    public void handleKeyTyped(KeyEvent keyEvent) {
        HMS_Utility.limitTextFieldLength(cardNumber,16);
        HMS_Utility.limitTextFieldLength(cvcNumber,3);
    }

    public void handlePayButton(ActionEvent event) {
        System.out.println("Inside handlePayButton");

        String cardNo = cardNumber.getText().trim();
        String cvcNo = cvcNumber.getText().trim();
        LocalDate expiry = expiryDate.getValue();
        System.out.println("cardNo : "+cardNo+" & cvcNo : "+cvcNo+" & expiry : "+expiry);

        if (cardNo.trim().isEmpty() || cvcNo.trim().isEmpty() ){
            errorMsg.setText("Field cannot be empty!");
            errorMsg.setVisible(true);
            return;
        } else if (expiry == null) {
            errorMsg.setText("Invalid Expiry Date!");
            errorMsg.setVisible(true);
            return;
        }
        if (validateData()){
            return;
        }

        Connection con = ConnectionProvider.getConnection();
        try {
            con.setAutoCommit(false);
            if (reservation != null) {
                ReservationDao resDao = new ReservationDao(con);

                boolean isAdded = resDao.addResvnByCustomer(reservation);
                RoomDao roomDao = new RoomDao(con);

                boolean isUpdated = roomDao.updateRoomStatus(reservation.getRoomNo(), "Booked");
                System.out.println("isResvnAdded : " + isAdded + " & statusUpdated : " + isUpdated);

                if (isAdded && isUpdated) {
                    con.commit();
                    con.setAutoCommit(true);
                    isPaymentSuccessful = true;
                    System.out.println("isPaymentSuccessful : "+isPaymentSuccessful);
                    Stage currentStage = (Stage) cardNumber.getScene().getWindow();
                    currentStage.close();
                } else {
                    con.rollback();
                    errorMsg.setText("Something went wrong, please try again");
                    errorMsg.setVisible(true);
                }
            } else {
                errorMsg.setText("Fields can not be empty!");
                errorMsg.setVisible(true);
            }
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public void receiveCheckInData(Reservation reservation) {
        this.reservation = reservation;
        if (reservation != null) {
            payAmountLabel.setText(priceLabel + reservation.getTotalPrice()+" \u20B9");
        }
    }

    private boolean validateData(){
        System.out.println("inside validateData...");
        boolean isInValid = false;
        String cardNo = cardNumber.getText().trim();
        String cvcNo = cvcNumber.getText().trim();
        System.out.println("cardNo : "+cardNo+" & cvcNo : "+cvcNo);

        if (!cardNo.matches("\\d+") && !cardNo.matches("\\d+")){
            errorMsg.setText("Only numbers are allowed!");
            errorMsg.setVisible(true);
            return isInValid = true;
        }else if (cardNo.length() != 16){
            errorMsg.setText("Invalid card number!");
            errorMsg.setVisible(true);
            return isInValid = true;
        } else if (cvcNo.length() != 3) {
            errorMsg.setText("Invalid CVC number!");
            errorMsg.setVisible(true);
            return isInValid = true;
        }

        LocalDate date = expiryDate.getValue();
        LocalDate currentDate = LocalDate.now();
        System.out.println("date : "+date+" & currentDate : "+currentDate);
        if (currentDate.isAfter(date)){
            errorMsg.setText("Card expired! please check expiry date");
            errorMsg.setVisible(true);
            isInValid = true;
        }
        return isInValid;
    }



}
