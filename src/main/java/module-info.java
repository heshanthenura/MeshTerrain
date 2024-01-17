module com.heshanthenura.meshterrain {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens com.heshanthenura.meshterrain to javafx.fxml;
    exports com.heshanthenura.meshterrain;
}