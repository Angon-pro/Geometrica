module pro.angon {
    requires javafx.controls;
    requires javafx.fxml;

    opens pro.angon to javafx.fxml;
    exports pro.angon;
}