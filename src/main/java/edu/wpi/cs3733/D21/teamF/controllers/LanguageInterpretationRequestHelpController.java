package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LanguageInterpretationRequestHelpController {
    @FXML private JFXButton close;
    @FXML private JFXButton back;
    @FXML private JFXButton translate;




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
