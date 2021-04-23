package edu.wpi.cs3733.D21.teamF.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for Floral Delivery Service View
 * @author keithdesantis
 */
public class FloralDeliveryService {

    @FXML private Button xButton;
    @FXML private RadioButton rButtonVase;
    @FXML private RadioButton rButtonBouquet;
    @FXML private RadioButton rButtonCard;
    @FXML private RadioButton rButtonPayPal;
    @FXML private Button helpButton;
    @FXML private Button helpXButton;
    @FXML private Button cancelButton;
    @FXML private Button submitButton;
    @FXML private TextField deliveryField;
    @FXML private TextField cardNumField;
    @FXML private TextField cardCVCField;
    @FXML private TextField cardYearField;
    @FXML private CheckBox dandelionCheckBox;
    @FXML private CheckBox rosesCheckBox;
    @FXML private CheckBox sunflowersCheckBox;

    /**
     * Handles the push of a button on the screen
     * @param e the button push
     * @throws IOException in case of scene switch, if the next fxml scene file cannot be found
     * @author KeithDeSantis
     */
    @FXML
    private void handleButtonPushed(ActionEvent e) throws IOException {

        Button buttonPushed = (Button) e.getSource();

        if (buttonPushed == xButton || buttonPushed == cancelButton) { // is x button
            Stage stage = (Stage) xButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeView.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Service Requests");
            stage.show();
        }

        else if (buttonPushed == helpButton) { // is help button
            Stage popUpStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/FloralHelpView.fxml"));
            Scene popUpScene = new Scene(root);
            popUpStage.setScene(popUpScene);
            popUpStage.setTitle("Floral Request Help Menu");
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            popUpStage.initOwner(buttonPushed.getScene().getWindow());
            popUpStage.showAndWait();
        }
        else if (buttonPushed == helpXButton) { // is the x button on the help menu
            Stage popUpStage = (Stage) helpXButton.getScene().getWindow();
            popUpStage.close();
        }
    }

    /**
     * Handles the pushing of a submit button.
     * If successful notifies user through pop up.
     * If form not filled out shows user alert.
     * @param e
     * @author KeithDeSantis
     */
    @FXML
    private void handleSubmitButtonPushed(ActionEvent e) throws IOException {

        if(formFilledOut()) // form is complete
        {
            Stage submittedStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/FormSubmittedView.fxml")); // Loading in pop up View
            Scene submitScene = new Scene(root);
            submittedStage.setScene(submitScene);
            submittedStage.setTitle("Submission Complete");
            submittedStage.initModality(Modality.APPLICATION_MODAL);
            submittedStage.initOwner(((Button) e.getSource()).getScene().getWindow());
            submittedStage.showAndWait();

        } else { //form not complete
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner((Stage) ( (Button) e.getSource()).getScene().getWindow());  // Show alert
            alert.setTitle("Form not filled.");
            alert.setHeaderText("Form incomplete");
            alert.setContentText("Please fill out at least the Location, Type of Flowers, Containers, and Payment fields.");
            alert.showAndWait();
        }


    }

    /**
     * Helper that returns true if the form is acceptably filled out
     * @return true if form is filled out
     * @author KeithDeSantis
     */
    private boolean formFilledOut() {
        boolean deliveryLocation = deliveryField.getText().length() > 0;
        boolean card = cardCVCField.getText().length() > 0 && cardNumField.getText().length() > 0 && cardYearField.getText().length() > 0;
        boolean containerChosen = rButtonBouquet.isSelected() || rButtonVase.isSelected();
        boolean paymentChosen = rButtonCard.isSelected() || rButtonPayPal.isSelected();
        boolean flowersRequested = dandelionCheckBox.isSelected() || rosesCheckBox.isSelected() || sunflowersCheckBox.isSelected();

        return deliveryLocation && card && containerChosen && paymentChosen && flowersRequested;
    }

    /**
     * Handles the push of a radio button (sets up Toggle Groups)
     * @param actionEvent button being pushed
     * @author KeithDeSantis
     */
    @FXML
    private void handleRadioButtonPushed(ActionEvent actionEvent) {

        ToggleGroup groupContainer = new ToggleGroup(); // group for container buttons
        rButtonBouquet.setToggleGroup(groupContainer);
        rButtonVase.setToggleGroup(groupContainer);
        ToggleGroup groupPayment = new ToggleGroup(); // group for payment buttons
        rButtonCard.setToggleGroup(groupPayment);
        rButtonPayPal.setToggleGroup(groupPayment);

    }
}
