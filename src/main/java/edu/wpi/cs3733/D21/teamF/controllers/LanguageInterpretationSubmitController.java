package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LanguageInterpretationSubmitController {
    @FXML private JFXButton close;
    @FXML private JFXButton home;

    /**
     *  Closes the Submit message and returns to the Interpretation Service Request Form
     * @param actionEvent
     * @throws IOException
     * @author Jay Yen
     */
    public void handleClose(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage)close.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/LanguageInterpretationServiceRequestView.fxml"));
        Scene homeScene = new Scene(root);
        currentStage.setScene(homeScene);
        currentStage.show();
    }

    /**
     * Closes the submit message and returns to the service request home page
     * @param actionEvent
     * @throws IOException
     * @author Jay Yen
     */
    public void handleHome(ActionEvent actionEvent) throws IOException{
        Stage currentStage = (Stage)home.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeView.fxml"));
        Scene homeScene = new Scene(root);
        currentStage.setScene(homeScene);
        currentStage.show();
    }
}
