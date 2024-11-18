module com.example.helloworldjfxtemplate {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.helloworldjfxtemplate.model to javafx.base;

    opens com.example.helloworldjfxtemplate to javafx.fxml;
    exports com.example.helloworldjfxtemplate;

    exports com.example.helloworldjfxtemplate.controller;
    opens com.example.helloworldjfxtemplate.controller to javafx.fxml;

}