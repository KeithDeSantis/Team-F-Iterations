package edu.wpi.cs3733.D21.teamF.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class AccountEntry extends RecursiveTreeObject<AccountEntry> {
    String username;
    String password;
    String userType;
    SimpleStringProperty usernameProperty;
    SimpleStringProperty passwordProperty;
    SimpleStringProperty userTypeProperty;

    public AccountEntry(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        usernameProperty = new SimpleStringProperty(username);
        passwordProperty = new SimpleStringProperty(password);
        userTypeProperty = new SimpleStringProperty(userType);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public SimpleStringProperty getUsernameProperty() {
        return usernameProperty;
    }

    public SimpleStringProperty usernamePropertyProperty() {
        return usernameProperty;
    }

    public void setUsernameProperty(String usernameProperty) {
        this.usernameProperty.set(usernameProperty);
    }

    public SimpleStringProperty getPasswordProperty() {
        return passwordProperty;
    }

    public void setPasswordProperty(String passwordProperty) {
        this.passwordProperty.set(passwordProperty);
    }

    public SimpleStringProperty getUserTypeProperty() {
        return userTypeProperty;
    }

    public void setUserTypeProperty(String userTypeProperty) {
        this.userTypeProperty.set(userTypeProperty);
    }
}
