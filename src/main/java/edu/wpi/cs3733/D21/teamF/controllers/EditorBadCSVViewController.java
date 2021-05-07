package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class EditorBadCSVViewController extends AbsController {
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
