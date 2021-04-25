package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class MaintenenceRequestController {
    @FXML private JFXButton submit;
    @FXML private JFXTextField employeeName;
    @FXML private JFXComboBox type;

    public void handleSubmit(ActionEvent actionEvent) {
        System.out.println(employeeName.getText());
    }

    public void handleGoBack(MouseEvent mouseEvent) {
    }
}
