package com.controller.employee;

import com.dao.RoomDao;
import com.entities.Rooms;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class RoomCheckController implements Initializable {

    @FXML
    private TextField roomNo;
    @FXML
    private TextField price;
    @FXML
    private ChoiceBox<String> status;
    @FXML
    private ChoiceBox<String> roomType;
    @FXML
    private Label message;

    @FXML
    private TextField searchField;

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<Rooms, Integer> roomNoCol;
    @FXML
    private TableColumn<Rooms, String> roomTypeCol;
    @FXML
    private TableColumn<Rooms, String> priceCol;
    @FXML
    private TableColumn<Rooms, String> statusCol;
    @FXML
    private TableColumn<Rooms, Void> actionCol;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up the cell value factories
        roomNoCol.setCellValueFactory(new PropertyValueFactory<>("roomNo"));
        roomTypeCol.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Set up the action column (if applicable)
        setupActionColumn();
        loadData();
    }

    private void setupActionColumn() {
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button actionButton = new Button("Delete");

            {
                actionButton.setOnAction(event -> {
                    Rooms rooms = getTableView().getItems().get(getIndex());
                    System.out.println("Button clicked for Room No: " + rooms.getRoomNo());
                    showWarningAlert(rooms);
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

    private void showWarningAlert(Rooms rooms) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setPrefSize(310, 170);
        alert.setHeaderText("Are you sure?");

        // Create a custom Label with wrapped text
        Label contentLabel = new Label("All the Information of room " + rooms.getRoomNo() + " will be permanently deleted");
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
                HMS_Utility.handleDeleteRow(rooms);
                loadData();
            } else if (buttonType == noButton) {
                alert.close();
            }
        });
    }

    private void loadData() {
        Platform.runLater(() -> {
            Connection con = ConnectionProvider.getConnection();
            RoomDao roomsDao = new RoomDao(con);
            ObservableList<Rooms> roomList = roomsDao.getRoomList();
            if (roomList == null || roomList.isEmpty()) {
                System.out.println("No data found!");
                return;
            }
            for (Rooms room : roomList) {
                System.out.println("RoomNo : " + room.getRoomNo() + "  RoomType : " + room.getRoomType());
            }
            tableView.setMaxHeight(250);
            tableView.setItems(roomList);
        });
    }

    public void handleKeyTyped(KeyEvent event) {
        HMS_Utility.limitTextFieldLength(roomNo, 3);
        HMS_Utility.limitTextFieldLength(price, 6);
        HMS_Utility.limitTextFieldLength(searchField, 3);
    }

    public void handleAddButton(ActionEvent event) {

        String roomNoFieldValue = roomNo.getText().trim();
        String priceFieldValue = price.getText().trim();

        if (roomNoFieldValue.isEmpty() || priceFieldValue.isEmpty()){
            message.setText("Fields cannot be empty");
            return;
        }

        if (!price.getText().matches("\\d+")){
            message.setText("Only number is allowed");
            return;
        }

        double priceDouble = Double.parseDouble(price.getText());

        Connection con = ConnectionProvider.getConnection();
        RoomDao roomDao = new RoomDao(con);

        Rooms room = new Rooms(roomNo.getText(), roomType.getValue(), priceDouble, status.getValue());
        boolean isAdded = roomDao.addRoom(room);

        if (isAdded){
            roomNo.setText("");
            price.setText("");
            message.setText("Room Successfully Added");
            loadData();
        }

    }

    @FXML
    public void handleSearch(ActionEvent event){
        System.out.println("clicked on search..");

        String searchText = searchField.getText();
        if (searchText.trim().isEmpty()){
            loadData();
            return;
        }

        Connection con = ConnectionProvider.getConnection();
        RoomDao roomDao = new RoomDao(con);
        Rooms room = roomDao.getRoom(searchText);

        ObservableList<Rooms> roomList = FXCollections.observableArrayList();
        if (room != null) {
            roomList.add(room);
            System.out.println("RoomList : "+roomList);
            tableView.setMaxHeight(80);
            tableView.setItems(roomList);
        }else{
            tableView.setMaxHeight(100);
            tableView.setItems(roomList);
        }
    }

}
