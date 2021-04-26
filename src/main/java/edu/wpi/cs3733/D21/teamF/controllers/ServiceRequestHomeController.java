package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
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

        if(buttonPushed == floralDelivery)
        {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/Service Requests/FloralDeliveryServiceRequestView.fxml"));
            stage.getScene().setRoot(root);
            // Changed method of switching scenes so the window doesn't resize everytime.
            /*
            Scene scene = new Scene(root);
            stage.setScene(scene);  //Changing the stage
             */
            stage.setTitle("Floral Delivery");
            stage.show();
        }
        else if (buttonPushed == home) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);  //Changing the stage
            stage.setTitle("Home Page");
            stage.show();
        }

        else if (buttonPushed == languageInterpretation){
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/Service Requests/LanguageInterpretationServiceRequestView.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);  //Changing the stage
            stage.setTitle("Language Interpretation");
            stage.show();
        }

        else if (buttonPushed == foodDelivery)
        {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/Service Requests/FoodDeliveryServiceRequestView.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Food Delivery");
            stage.show();

        }


    }
}
