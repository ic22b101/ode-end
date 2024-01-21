module fhtw.odeend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;


    opens fhtw to javafx.fxml;
    exports fhtw;
}