package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Controller for Form Submitted Pop Up
 */
public class FormSubmittedViewController extends AbsController {
    Stage previousStage = new Stage();
    @FXML private JFXButton okButton;

    /**
     * Handles ok button by closing window.
     * @author keithdesantis
     */
    @FXML
    private void okButtonPushed() throws IOException {
        ( (Stage) okButton.getScene().getWindow()).close();
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml");
    }

    public void changeStage(Stage previousStage){
        this.previousStage = previousStage;
    }
}
