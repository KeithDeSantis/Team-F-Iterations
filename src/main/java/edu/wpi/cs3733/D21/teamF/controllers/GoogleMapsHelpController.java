package edu.wpi.cs3733.D21.teamF.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GoogleMapsHelpController {
    @FXML private Label helpTextLabel;

    public void handleAbout() {
        helpTextLabel.setText("This page uses the google directions API to direct you to the closest guest hospital" +
                " parking lot. It will tell you step by step directions to get to the lot, as well as your ETA.");
    }

    public void handleHowTo() {
        helpTextLabel.setText("To use this page, simply enter all your information about your starting address"
        + " into the page, then click submit and view the results!");
    }

    public void handleOk() {
        ((Stage) helpTextLabel.getScene().getWindow()).close();
    }
}