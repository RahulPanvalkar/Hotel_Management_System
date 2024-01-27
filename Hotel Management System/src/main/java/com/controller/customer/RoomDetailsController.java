package com.controller.customer;

import com.dao.RoomDao;
import com.entities.Rooms;
import com.utility.ConnectionProvider;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class RoomDetailsController implements Initializable {

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<Rooms, String> roomNoColumn;
    @FXML
    private TableColumn<Rooms, String> roomTypeColumn;
    @FXML
    private TableColumn<Rooms, String> priceColumn;
    @FXML
    private TableColumn<Rooms, String> statusColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set up the cell value factories
        roomNoColumn.setCellValueFactory(new PropertyValueFactory<>("roomNo"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadData();
    }

    private void loadData() {
        Platform.runLater(() -> {
            Connection con = ConnectionProvider.getConnection();
            RoomDao roomDao = new RoomDao(con);
            ObservableList<Rooms> roomData = roomDao.getRoomList();
            for (Rooms room : roomData) {
                System.out.println("RoomNo : " + room.getRoomNo() + ",  RoomType : " + room.getRoomType());
            }
            if (roomData == null || roomData.isEmpty()) {
                System.out.println("No data found!");
                return;
            }
            tableView.setItems(roomData);
        });
    }

}
