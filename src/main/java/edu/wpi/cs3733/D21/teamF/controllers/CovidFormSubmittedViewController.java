package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Controller for Form Submitted Pop Up
 */
public class CovidFormSubmittedViewController {
    Stage previuosStage = new Stage();
    @FXML private JFXButton okButton;

    /**
     * Handles ok button by closing window.
     * @author keithdesantis
     */
    @FXML
    private void okButtonPushed() throws IOException {
        ( (Stage) okButton.getScene().getWindow()).close();
        /*
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
        previuosStage.getScene().setRoot(root);
        previuosStage.setTitle("Service Request Home");
        previuosStage.show();
         */
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
    }





    public void changeStage(Stage setPreviuosStage){
        previuosStage = setPreviuosStage;
    }
}

