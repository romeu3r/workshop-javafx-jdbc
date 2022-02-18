module workshopjavafxjdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens application to javafx.fxml;
    opens gui;
    exports application;
    exports gui;
}