package edu.wpi.fuchsiafalcons.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LanguageInterpretationHelpController {
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
        Stage currentStage = (Stage)close.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/LanguageInterpretationServiceRequestView.fxml"));
        Scene homeScene = new Scene(root);
        currentStage.setScene(homeScene);
        currentStage.show();
    }

    /**
     * Returns to the Interpretation Service Request Form.
     * This button does the same thing as handleClose, but some people like back buttons and some people like x buttons.
     * @param actionEvent
     * @throws IOException
     */
    public void handleBack(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage)back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/LanguageInterpretationServiceRequestView.fxml"));
        Scene homeScene = new Scene(root);
        currentStage.setScene(homeScene);
        currentStage.show();
    }

    public void handleTranslate(ActionEvent actionEvent) {
    }
}
