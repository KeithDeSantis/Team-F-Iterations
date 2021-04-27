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
        Font buttonDefault = Font.loadFont("file:src/main/resources/fonts/Montserrat-SemiBold.ttf", 12);
        formatErrorMessage.setFont(buttonDefault);
        Font titleFont = Font.loadFont("file:src/main/resources/fonts/Volkhov-Regular.ttf", 14);
        formatErrorTitle.setFont(titleFont);
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

    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #F0C808; -fx-text-fill: #000000;");
    }

    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #03256C; -fx-text-fill: #FFFFFF;");
    }
}
