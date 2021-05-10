package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.pathfinding.GoogleAPI;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class GoogleMapsViewController extends AbsController {
    @FXML
    private JFXTextField streetAddress;
    @FXML
    private JFXTextField city;
    @FXML
    private JFXComboBox<String> state;
    @FXML
    private JFXTextField zipCode;
    @FXML
    private Label destinationAddress;
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
    @FXML
    private Label etaLabel;

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

    @FXML
    private void initialize(){
        String[] stateAbbreviations = {"AK","AL","AR","AZ","CA","CO","CT","DE","FL","GA","HI","IA","ID","IL","IN","KS",
                "KY","LA","MA","MD","ME","MI","MN","MO","MS","MT","NC","ND","NE","NH","NJ","NM","NV","NY","OH","OK","OR",
                "PA","RI","SC","SD","TN","TX","UT","VA","VT","WA","WI","WV","WY"};
        state.getItems().addAll(stateAbbreviations);
    }

    /*
    @FXML
    public void handleSubmit() throws IOException {
        String address = streetAddress.getText() + " " + city.getText() + " " + state.getValue() + ", " + zipCode.getText();
        String closestLot = GoogleAPI.getGoogleAPI().parseClosestParkingLot(address);
        String[] directions = GoogleAPI.getGoogleAPI().queryAPI(address, closestLot);
        outputBox.setText(directions[0]);
    }
     */

    @FXML
    private void handleButtonPushed(ActionEvent actionEvent) throws IOException {
        JFXButton buttonPushed = (JFXButton) actionEvent.getSource();
        
        if (buttonPushed == submitButton){
            String address = streetAddress.getText() + " " + city.getText() + " " + state.getValue() + ", " + zipCode.getText();
            String closestLot = GoogleAPI.getGoogleAPI().parseClosestParkingLot(address);
            String[] directions = GoogleAPI.getGoogleAPI().queryAPI(address, closestLot);
            etaLabel.setText(directions[1]);
            destinationAddress.setText(closestLot);
            outputBox.setText(directions[0]);
        }
        else if (buttonPushed == cancelButton){
            SceneContext.getSceneContext().loadDefault();
        }
        else if (buttonPushed == helpButton){
            FXMLLoader dialogLoader = new FXMLLoader();
            dialogLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/GoogleMapsHelpView.fxml")); // load in Edit Dialog - KD
            Stage dialogStage = new Stage();
            Parent root = dialogLoader.load();
            AccountManagerHelpController dialogController = dialogLoader.getController();
            dialogStage.initModality(Modality.WINDOW_MODAL); // make window a pop up - KD
            dialogStage.initOwner(destinationAddress.getScene().getWindow());
            dialogStage.setScene(new Scene(root)); // set scene - KD
            dialogStage.showAndWait(); // open pop up - KD
        }
        else if (buttonPushed == clearButton){
            JFXTextField[] values = {streetAddress, city, zipCode};

            for (JFXTextField j : values){
                j.setText("");
            }
        }
    }
}
