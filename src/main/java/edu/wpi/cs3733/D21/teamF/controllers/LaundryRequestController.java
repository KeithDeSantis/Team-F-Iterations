package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;


public class LaundryRequestController {

    @FXML private JFXButton submit;
    @FXML private JFXButton cancel;
    @FXML private JFXButton help;
    @FXML private JFXRadioButton darks;
    @FXML private JFXRadioButton lights;
    @FXML private JFXRadioButton both;
    @FXML private JFXRadioButton hot;
    @FXML private JFXRadioButton cold;
    @FXML private JFXRadioButton folded;

    @FXML public TextField employeeID;
    @FXML public TextField clientName;
    @FXML public TextField additionalInstructions;

    public TextField getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(TextField employeeID) {
        this.employeeID = employeeID;
    }

    public TextField getClientName() {
        return clientName;
    }

    public void setClientName(TextField clientName) {
        this.clientName = clientName;
    }

    public TextField getAdditionalInstructions() {
        return additionalInstructions;
    }

    public void setAdditionalInstructions(TextField additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }

    @FXML
    public void submitReq(ActionEvent actionEvent) {
    }

    @FXML
    public void cancelReq(ActionEvent actionEvent) {
    }
}
