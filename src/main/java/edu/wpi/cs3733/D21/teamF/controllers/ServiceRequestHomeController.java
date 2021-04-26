package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.states.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for Service Request Home Page
 */
public class ServiceRequestHomeController {

    @FXML private JFXButton floralDelivery;
    @FXML private JFXButton foodDelivery;
    @FXML private JFXButton languageInterpretation;
    @FXML private JFXButton home;

    /**
     * Handles the pushing of a button on the screen
     * @param actionEvent the button's push
     * @throws IOException in case of scene switch, if the next fxml scene file cannot be found
     * @author keithdesantis
     */
    @FXML
    private void handleButtonPushed(ActionEvent actionEvent) throws IOException {

        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage
        Stage stage;
        Parent root;

        if(buttonPushed == floralDelivery) {
            FloralDeliveryState.getFloralDeliveryState().switchScene(SceneContext.getSceneContext());
        }
        else if (buttonPushed == home) {
            DefaultPageState.getDefaultPageState().switchScene(SceneContext.getSceneContext());
        }
        else if (buttonPushed == languageInterpretation){
            LanguageInterpretationState.getLanguageInterpretationState().switchScene(SceneContext.getSceneContext());
        }
        else if (buttonPushed == foodDelivery)
        {
            FoodDeliveryState.getFoodDeliveryState().switchScene(SceneContext.getSceneContext());
        }


    }
}
