module com.main.chessenginejavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.main to javafx.fxml;
    exports com.main;
}