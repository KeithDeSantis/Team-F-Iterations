package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class EditorBadCSVViewController {
    @FXML private Label formatErrorTitle;
    @FXML private Label formatErrorMessage;

    @FXML
    public void initialize() {

    }

    /**
     * Used to close the format CSV warning window
     * @param actionEvent
     */
    public void handleOK(ActionEvent actionEvent) {
        JFXButton btn = (JFXButton) actionEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }




}
