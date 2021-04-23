package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for Floral Delivery Service View
 * @author keithdesantis
 */
public class FloralDeliveryService {

    @FXML private JFXRadioButton bouquetButton;
    @FXML private JFXRadioButton vaseButton;
    @FXML private JFXRadioButton potButton;
    @FXML private JFXButton clearButton;
    @FXML private JFXButton submitButton;
    @FXML private JFXTextField deliveryField;
    @FXML private JFXTextField cardNumberField;
    @FXML private JFXTextField cardCVCField;
    @FXML private JFXTextField cardExpField;
    @FXML private JFXCheckBox roseCheckBox;
    @FXML private JFXCheckBox tulipCheckBox;
    @FXML private JFXCheckBox violetCheckBox;
    @FXML private JFXCheckBox sunflowerCheckBox;
    @FXML private JFXCheckBox orchidCheckBox;
    @FXML private JFXCheckBox daisyCheckBox;
    @FXML private Label successField;



    /**
     * Handles the push of a radio button (sets up Toggle Groups)
     * @param actionEvent button being pushed
     * @author KeithDeSantis
     */
    @FXML
    private void handleRadioButtonClicked(ActionEvent actionEvent) {
        ToggleGroup groupContainer = new ToggleGroup(); // group for container buttons
        bouquetButton.setToggleGroup(groupContainer);
        vaseButton.setToggleGroup(groupContainer);
        potButton.setToggleGroup(groupContainer);
    }

    /**
     * When back button is pressed
     * @param mouseEvent
     * @author KD
     */
    public void handleBack(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) bouquetButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml")); //FIXME Go to service request home
        stage.getScene().setRoot(root);
        stage.show();
    }

    /**
     * Return to Home when it is decided where home is
     * @param mouseEvent
     * @author KD
     */
    public void handleHome(MouseEvent mouseEvent) {}

    /**
     * Will eventually save the request to DB
     * @param actionEvent
     * @author KD
     */
    public void handleSubmit(ActionEvent actionEvent) {
        if(isFilledOut()) {
            successField.setText("Request Submitted!");
        } else { successField.setText(""); }
    }

    public boolean isFilledOut() {
        boolean isFilled = true;
        if(!(bouquetButton.isSelected() || vaseButton.isSelected() || potButton.isSelected())) {
            isFilled = false;
            bouquetButton.setStyle("-fx-border-color: #F0C808; -fx-border-width: 2px");
            vaseButton.setStyle("-fx-border-color: #F0C808; -fx-border-width: 2px");
            potButton.setStyle("-fx-border-color: #F0C808; -fx-border-width: 2px");
        }
        if(!(roseCheckBox.isSelected() || tulipCheckBox.isSelected() || violetCheckBox.isSelected() || sunflowerCheckBox.isSelected() || orchidCheckBox.isSelected() || daisyCheckBox.isSelected())) {
            isFilled = false;
            roseCheckBox.setStyle("-fx-border-color: #F0C808; -fx-border-width: 2px");
            tulipCheckBox.setStyle("-fx-border-color: #F0C808; -fx-border-width: 2px");
            violetCheckBox.setStyle("-fx-border-color: #F0C808; -fx-border-width: 2px");
            sunflowerCheckBox.setStyle("-fx-border-color: #F0C808; -fx-border-width: 2px");
            orchidCheckBox.setStyle("-fx-border-color: #F0C808; -fx-border-width: 2px");
            daisyCheckBox.setStyle("-fx-border-color: #F0C808; -fx-border-width: 2px");
        }
        if(deliveryField.getText().length() == 0) {
            isFilled = false;
            deliveryField.setStyle("-fx-border-color: #F0C808; -fx-border-width: 2px");
        }
        if(cardNumberField.getText().length() == 0) {
            isFilled = false;
            cardNumberField.setStyle("-fx-border-color: #F0C808; -fx-border-width: 2px");
        }
        if(cardCVCField.getText().length() == 0) {
            isFilled = false;
            cardCVCField.setStyle("-fx-border-color: #F0C808; -fx-border-width: 2px");
        }
        if(cardExpField.getText().length() == 0) {
            isFilled = false;
            cardExpField.setStyle("-fx-border-color: #F0C808; -fx-border-width: 2px");
        }
        return isFilled;
    }
}

