module com.hms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.hms to javafx.fxml;
    opens com.controller.admin to javafx.fxml;
    opens com.controller.employee to javafx.fxml;
    opens com.controller.customer to javafx.fxml;
    opens com.entities to javafx.base;


    exports com.hms;
    exports com.controller.admin;
    exports com.controller.employee;
    exports com.controller.customer;

}