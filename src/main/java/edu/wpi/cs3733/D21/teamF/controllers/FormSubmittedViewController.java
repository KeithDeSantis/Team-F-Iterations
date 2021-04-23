package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller for Form Submitted Pop Up
 */
public class FormSubmittedViewController {

    @FXML private JFXButton okButton;

    /**
     * Handles ok button by closing window.
     * @author keithdesantis
     */
    @FXML
    private void okButtonPushed() {
        ( (Stage) okButton.getScene().getWindow()).close();
    }

}
