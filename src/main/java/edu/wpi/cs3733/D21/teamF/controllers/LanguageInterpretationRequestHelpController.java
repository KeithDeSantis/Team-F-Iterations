package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class LanguageInterpretationRequestHelpController {
    @FXML private JFXButton close;
    @FXML private JFXButton back;
    @FXML private JFXButton translate;




    /**
     * Closes the help window and returns to the Interpretation Service Request Form
     * @author Jay Yen
     */
    public void handleClose() {
        ( (Stage) close.getScene().getWindow()).close();
    }

    /**
     * Returns to the Interpretation Service Request Form.
     * This button does the same thing as handleClose, but some people like back buttons and some people like x buttons.
     */
    public void handleBack() {
        ( (Stage) close.getScene().getWindow()).close();
    }

    public void handleTranslate() {
    }

}
