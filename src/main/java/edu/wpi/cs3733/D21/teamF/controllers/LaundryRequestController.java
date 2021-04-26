package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;


public class LaundryRequestController {

    @FXML private Button submit;
    @FXML private Button cancel;
    @FXML private Button help;
    @FXML private RadioButton darks;
    @FXML private RadioButton lights;
    @FXML private RadioButton both;
    @FXML private RadioButton hot;
    @FXML private RadioButton cold;
    @FXML private RadioButton folded;
    public TextField employeeID;
    public TextField clientName;
    public TextField additionalInstructions;

    @FXML
    public void submitReq(ActionEvent actionEvent) {
    }

    @FXML
    public void cancelReq(ActionEvent actionEvent) {
    }
}
