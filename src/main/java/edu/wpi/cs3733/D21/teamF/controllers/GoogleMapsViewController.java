package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.pathfinding.GoogleAPI;
import javafx.fxml.FXML;

import java.io.IOException;
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
    @FXML
    private JFXTextArea outputBox;

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
        String[] stateAbbreviations = {"AK","AL","AR","AZ","CA","CO","CT","DE","FL","GA","HI","IA","ID","IL","IN","KS",
                "KY","LA","MA","MD","ME","MI","MN","MO","MS","MT","NC","ND","NE","NH","NJ","NM","NV","NY","OH","OK","OR",
                "PA","RI","SC","SD","TN","TX","UT","VA","VT","WA","WI","WV","WY"};
        for (String s : stateAbbreviations){
            state.getItems().add(s);
        }
    }

    @FXML
    public void handleSubmit() throws IOException {
        String address = streetAddress.getText() + " " + city.getText() + " " + state.getValue() + ", " + zipCode.getText();
        String closestLot = GoogleAPI.getGoogleAPI().parseClosestParkingLot(address);
        String[] directions = GoogleAPI.getGoogleAPI().queryAPI(address, closestLot);
        outputBox.setText(directions[0]);
    }
}
