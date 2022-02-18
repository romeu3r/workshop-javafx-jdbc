module workshopjavafxjdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens application to javafx.fxml;
    opens gui;
    opens model.entities;
    exports application;
    exports gui;
}
