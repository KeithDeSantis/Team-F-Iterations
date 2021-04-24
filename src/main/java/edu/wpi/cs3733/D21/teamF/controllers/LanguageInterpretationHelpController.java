package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LanguageInterpretationHelpController {
    @FXML private JFXButton close;
    @FXML private JFXButton back;
    @FXML private JFXButton translate;

    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #F0C808; -fx-text-fill: #000000;");
    }

    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #03256C; -fx-text-fill: #FFFFFF;");
    }
    /**
     * Closes the help window and returns to the Interpretation Service Request Form
     * @param actionEvent
     * @throws IOException
     * @author Jay Yen
     */
    public void handleClose(ActionEvent actionEvent) throws IOException {
        ( (Stage) close.getScene().getWindow()).close();
    }

    /**
     * Returns to the Interpretation Service Request Form.
     * This button does the same thing as handleClose, but some people like back buttons and some people like x buttons.
     * @param actionEvent
     * @throws IOException
     */
    public void handleBack(ActionEvent actionEvent) throws IOException {
        ( (Stage) close.getScene().getWindow()).close();
    }

    public void handleTranslate(ActionEvent actionEvent) {
    }
}
