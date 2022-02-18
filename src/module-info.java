module workshopjavafxjdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens application to javafx.fxml;
    opens gui;
    opens model.entities;
    exports application;
    exports gui;
    exports gui.util;
    opens gui.util;
}
