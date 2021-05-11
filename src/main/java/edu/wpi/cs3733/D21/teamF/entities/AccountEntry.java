package edu.wpi.cs3733.D21.teamF.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class AccountEntry extends RecursiveTreeObject<AccountEntry> {
    String email;
    String username;
    String password;
    String userType;
    byte[] salt;
    String covidStatus;
    SimpleStringProperty usernameProperty;
    SimpleStringProperty passwordProperty;
    SimpleStringProperty userTypeProperty;
    SimpleStringProperty emailProperty;

    public AccountEntry(String email, String username, String password, String userType, String status, byte[] salt) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.covidStatus = status;
        this.salt = salt;
        usernameProperty = new SimpleStringProperty(username);
        passwordProperty = new SimpleStringProperty(password);
        userTypeProperty = new SimpleStringProperty(userType);
        emailProperty = new SimpleStringProperty(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.emailProperty.set(email);
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        usernameProperty = new SimpleStringProperty(username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        passwordProperty = new SimpleStringProperty(password);
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
        userTypeProperty = new SimpleStringProperty(userType);
    }

    public String getCovidStatus() {
        return covidStatus;
    }

    public void setCovidStatus(String covidStatus) {
        this.covidStatus = covidStatus;
    }

    public SimpleStringProperty getUsernameProperty() {
        return usernameProperty;
    }

    public SimpleStringProperty usernamePropertyProperty() {
        return usernameProperty;
    }

    public void setUsernameProperty(String usernameProperty) {
        this.usernameProperty.set(usernameProperty);
        username = usernameProperty;
    }

    public SimpleStringProperty getPasswordProperty() {
        return passwordProperty;
    }

    public void setPasswordProperty(String passwordProperty) {
        this.passwordProperty.set(passwordProperty);
        password = passwordProperty;
    }

    public SimpleStringProperty getUserTypeProperty() {
        return userTypeProperty;
    }

    public void setUserTypeProperty(String userTypeProperty) {
        this.userTypeProperty.set(userTypeProperty);
        userType = userTypeProperty;
    }

    public SimpleStringProperty passwordPropertyProperty() {
        return passwordProperty;
    }

    public SimpleStringProperty userTypePropertyProperty() {
        return userTypeProperty;
    }

    public SimpleStringProperty getEmailProperty() {
        return emailProperty;
    }

    public void setEmailProperty(String emailProperty) {
        this.emailProperty.set(emailProperty);
        email = emailProperty;
    }
}
