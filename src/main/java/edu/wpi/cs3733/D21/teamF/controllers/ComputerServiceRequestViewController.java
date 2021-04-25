package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class ComputerServiceRequestViewController {

    @FXML
    private JFXTextField computerNameText;

    @FXML
    private JFXTextField computerLocationText;

    @FXML
    private JFXTextField requestorTextText;

    @FXML
    private JFXComboBox urgencyComboBox;

    @FXML
    private JFXTextArea descriptionText;


    @FXML
    public void handleGoHome(MouseEvent mouseEvent) {
    }

    @FXML
    public void handleSubmit(ActionEvent actionEvent) {
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
    }
}
