package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;

import java.util.ArrayList;

public class GoogleMapsViewController {
    @FXML
    private JFXTextField streetAddress;
    @FXML
    private JFXTextField city;
    @FXML
    private JFXComboBox<String> state;
    @FXML
    private JFXTextField zipCode;
    @FXML
    private JFXComboBox<String> destination;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXButton clearButton;
    @FXML
    private JFXButton helpButton;
    @FXML
    private  JFXButton submitButton;

    public JFXTextField getStreetAddress() {
        return streetAddress;
    }

    public JFXTextField getCity() {
        return city;
    }

    public JFXComboBox<String> getState() {
        return state;
    }

    public JFXTextField getZipCode() {
        return zipCode;
    }

    public JFXComboBox<String> getDestination() {
        return destination;
    }

    public void setStreetAddress(JFXTextField streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setCity(JFXTextField city) {
        this.city = city;
    }

    public void setState(JFXComboBox<String> state) {
        this.state = state;
    }

    public void setZipCode(JFXTextField zipCode) {
        this.zipCode = zipCode;
    }

    public void setDestination(JFXComboBox<String> destination) {
        this.destination = destination;
    }

    @FXML
    public void initialize(){

    }
}
