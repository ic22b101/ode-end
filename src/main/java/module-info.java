module fhtw.odeend {
    requires javafx.controls;
    requires javafx.fxml;


    opens fhtw.odeend to javafx.fxml;
    exports fhtw.odeend;
}