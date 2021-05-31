module com.angon {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.angon to javafx.fxml;
    exports com.angon;
}