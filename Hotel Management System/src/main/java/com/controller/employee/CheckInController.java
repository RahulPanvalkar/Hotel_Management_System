package com.controller.employee;

import com.dao.CustomerDao;
import com.dao.ReservationDao;
import com.dao.RoomDao;
import com.entities.Customer;
import com.entities.Reservation;
import com.entities.Rooms;
import com.utility.ConnectionProvider;
import com.utility.HMS_Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

public class CheckInController implements Initializable {

    @FXML
    ChoiceBox<String> roomNo;
    @FXML
    private Label message;
    @FXML
    private TextField searchField;

    @FXML
    private Label customerId;
    @FXML
    private Label custNameLabel;
    @FXML
    private Label custEmailLabel;
    @FXML
    private Label custMobileLabel;
    @FXML
    private Label custAddressLabel;
    @FXML
    private Label roomPriceLabel;
    @FXML
    private Label checkInTimeLabel;
    @FXML
    private Label roomTypeLabel;

    @FXML
    private TextField custNameField;
    @FXML
    private TextField custMobileField;
    @FXML
    private TextField custEmailField;
    @FXML
    private TextField custAddressField;

    @FXML
    private Button checkInButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;

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

    private Rooms room;
    private Customer customer;
    private HashMap<String, String> labels = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAvailableRoomList();

        // Set up the cell value factories
        custIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        mobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNum"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        // Set up the action column (if applicable)
        setupActionColumn();

        labels.put("custIdLabel","Customer Id : ");
        labels.put("custNameLabel","Name : ");
        labels.put("custEmailLabel","Email : ");
        labels.put("custMobileLabel","Mobile : ");
        labels.put("custAddressLabel","Address : ");
        labels.put("roomTypeString","Room Type : ");
        labels.put("roomPriceString","Price : ");
        labels.put("checkInTimeString","Date : ");

    }

    private void setAvailableRoomList(){
        Connection con = ConnectionProvider.getConnection();
        RoomDao roomDao = new RoomDao(con);
        ArrayList<String> roomList = roomDao.getAvailableRoomList();
        System.out.println("RoomList : "+roomList);
        roomNo.setValue("Select");
        roomNo.getItems().setAll(roomList);
    }

    public void handleKeyTyped(KeyEvent event) {
        HMS_Utility.limitTextFieldLength(custNameField, 30);
        HMS_Utility.limitTextFieldLength(custEmailField, 50);
        HMS_Utility.limitTextFieldLength(custMobileField, 10);
        HMS_Utility.limitTextFieldLength(custAddressField, 50);
    }

    public void handleSearch(ActionEvent actionEvent) {
        String searchText = searchField.getText();
        System.out.println("SearchText : "+searchText);
        if (searchText.trim().isEmpty()){

            return;
        }
        String credentialType = "";
        // Determine the credential type based on the input format (Mobile number or email)
        if (searchText.matches("\\d+")) {
            credentialType = "mobNumber";
        } else {
            credentialType = "email";
        }

        Connection con = ConnectionProvider.getConnection();
        CustomerDao custDao = new CustomerDao(con);
        customer = custDao.getCustomer(credentialType, searchText);
        System.out.println("Customer info : "+customer);

        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        if (customer != null) {
            customerList.add(customer);
            tableView.setItems(customerList);
        }else{
            tableView.setItems(customerList);
        }

    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button actionButton = new Button("Select");

            {
                actionButton.setOnAction(event -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    System.out.println("Button clicked for customer: " + customer.getName());

                    setCheckInLabels(String.valueOf(customer.getCustomerId()),customer.getName(),customer.getEmail(),
                            customer.getMobileNum(), customer.getAddress(),"","","" );
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

    public void handleChoiceBoxSelection(ActionEvent event) {
        Connection con = ConnectionProvider.getConnection();
        RoomDao roomDao = new RoomDao(con);
        room = roomDao.getRoom(roomNo.getValue());

        if (room != null){
            roomTypeLabel.setText(labels.get("roomTypeString") + room.getRoomType());
            roomPriceLabel.setText(labels.get("roomPriceString") + room.getPrice());
            LocalDateTime localDateTime = LocalDateTime.now();
            System.out.println("currentDateTime : "+localDateTime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            checkInTimeLabel.setText(labels.get("checkInTimeString") + localDateTime.format(formatter));

        }
    }

    public void handleCheckInButton(ActionEvent event) {
        Connection con = ConnectionProvider.getConnection();
        ReservationDao resDao = new ReservationDao(con);
        Reservation reservation = null;
        if (customer != null && room != null) {
            reservation = new Reservation();
            reservation.setReservationId(createReservationNo());
            reservation.setCustomerId(customer.getCustomerId());
            reservation.setCustomerName(customer.getName());
            reservation.setRoomNo(room.getRoomNo());
            reservation.setRoomType(room.getRoomType());
            reservation.setPricePerDay(room.getPrice());
        }
        if (reservation != null) {
            boolean isAdded = resDao.addReservation(reservation);
            RoomDao roomDao = new RoomDao(con);
            boolean isUpdated = roomDao.updateRoomStatus(reservation.getRoomNo(),"Booked");
            System.out.println("isResvnAdded : " + isAdded + " & statusUpdated : "+isUpdated);
            if (isAdded && isUpdated) {
                setCheckInLabels("","","", "","","","","");
                setAvailableRoomList();
                searchField.setText("");
                ObservableList<Reservation> resList = FXCollections.observableArrayList();
                tableView.setItems(resList);
                message.setText("Successfully Checked In!");
            } else {
                message.setText("Something went wrong, please try again");
            }
        } else {
            message.setText("Fields can not be empty!");
        }
    }

    public void handleAddButton(ActionEvent event) {
        custNameField.setVisible(true);
        custMobileField.setVisible(true);
        custEmailField.setVisible(true);
        custAddressField.setVisible(true);

        checkInButton.setVisible(false);
        addButton.setVisible(false);
        saveButton.setVisible(true);
        cancelButton.setVisible(true);

        setCheckInLabels("","","", "","","","","");
    }

    public void handleSaveButton(ActionEvent event) {

        String name = custNameField.getText();
        String email = custEmailField.getText();
        String mobile = custMobileField.getText();
        String address = custAddressField.getText();

        if (name.trim().isEmpty() || email.trim().isEmpty() || mobile.trim().isEmpty() || address.trim().isEmpty()){
            message.setText("Fields can not be empty!");
            return;
        }

        Customer customer = new Customer(name,email,mobile,
        "",address);

        Connection con = ConnectionProvider.getConnection();
        CustomerDao customerDao = new CustomerDao(con);
        boolean isAdded  = customerDao.addCustomer(customer);

        if (isAdded){
            System.out.println("Customer info added.. ");
            custNameField.setVisible(false);
            custMobileField.setVisible(false);
            custEmailField.setVisible(false);
            custAddressField.setVisible(false);

            checkInButton.setVisible(true);
            addButton.setVisible(true);
            saveButton.setVisible(false);
            cancelButton.setVisible(false);

            Customer updatedCustomer = customerDao.getCustomer("mobNumber", customer.getMobileNum());
            System.out.println("Customer info : "+updatedCustomer);

            setCheckInLabels(String.valueOf(updatedCustomer.getCustomerId()),updatedCustomer.getName(),updatedCustomer.getEmail(),
                    updatedCustomer.getMobileNum(), updatedCustomer.getAddress(),"","","" );
        }
    }

    public void handleCancelButton(ActionEvent event) {
        custNameField.setVisible(false);
        custMobileField.setVisible(false);
        custEmailField.setVisible(false);
        custAddressField.setVisible(false);

        checkInButton.setVisible(true);
        addButton.setVisible(true);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);

        message.setText("Check In");
    }

    private String createReservationNo(){
        Random random = new Random();
        int randomNumber = random.nextInt(9000000)+1000000;
        System.out.println("RandomNumber : "+randomNumber);
        String reservationNo = "HMS"+randomNumber;
        System.out.println("ReservationNo : "+reservationNo);

        Connection con = ConnectionProvider.getConnection();
        ReservationDao resDao = new ReservationDao(con);
        boolean isPresent = resDao.checkResNo(reservationNo);

        if (isPresent){
            System.out.println("Reservation No Already Exist!");
            createReservationNo();
        }
        return reservationNo;
    }

    private void setCheckInLabels(String custId, String custName,String custEmail,String custMob,String custAddress,
    String roomType, String price, String checkInTime){
        customerId.setText(labels.get("custIdLabel") + custId);
        custNameLabel.setText(labels.get("custNameLabel") + custName);
        custEmailLabel.setText(labels.get("custEmailLabel") + custEmail);
        custMobileLabel.setText(labels.get("custMobileLabel") + custMob);
        custAddressLabel.setText(labels.get("custAddressLabel") + custAddress);
        roomTypeLabel.setText(labels.get("roomTypeString") + roomType);
        roomPriceLabel.setText(labels.get("roomPriceString") + price);
        checkInTimeLabel.setText(labels.get("checkInTimeString" + checkInTime));

        message.setText("Check In");
    }
}
