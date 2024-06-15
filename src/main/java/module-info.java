module dev.naimsulejmani.grupi3javafxregistration {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.microsoft.sqlserver.jdbc;
    requires static lombok;
    requires java.sql;

    opens dev.naimsulejmani.grupi3javafxregistration.models;
    opens dev.naimsulejmani.grupi3javafxregistration to javafx.fxml;
    exports dev.naimsulejmani.grupi3javafxregistration;
}