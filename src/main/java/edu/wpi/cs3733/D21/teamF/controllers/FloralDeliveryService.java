package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Controller for Floral Delivery Service View
 * @author keithdesantis
 */
public class FloralDeliveryService extends ServiceRequests{

    @FXML private JFXRadioButton bouquetButton;
    @FXML private JFXRadioButton vaseButton;
    @FXML private JFXRadioButton potButton;
    @FXML private JFXButton clearButton;
    @FXML private JFXTextField deliveryField;
    @FXML private JFXDatePicker dateField;
    @FXML private JFXTextField nameField;
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
    @FXML private ImageView logoHome;


    @FXML
    public void initialize() {
        Image img = new Image(getClass().getResourceAsStream("/imagesAndLogos/BandWLogo.png"));
        logoHome.setImage(img);
    }

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
     * Will eventually save the request to DB
     * @param actionEvent
     * @author KD
     */
    public void handleSubmit(ActionEvent actionEvent) throws SQLException, IOException {
        if(formFilled()) {
            String type = "Flower Delivery";
            String uuid = UUID.randomUUID().toString();
            String additionalInfo = "Date: " + dateField.getValue() + "Deliver to: " + deliveryField.getText() +
            "CC Number: " + cardNumberField.getText() + "CC CVC: " + cardCVCField.getText() + "CC Exp. Date: " + cardExpField.getText();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, "", "false", additionalInfo);
            // Loads form submitted window and passes in current stage to return to request home
            openSuccessWindow();
        }
    }

    public boolean formFilled() {
        boolean isFilled = true;

        setNormalStyle(bouquetButton, vaseButton, potButton, roseCheckBox, tulipCheckBox, violetCheckBox, sunflowerCheckBox,
                orchidCheckBox, daisyCheckBox, deliveryField, nameField, cardNumberField, cardCVCField, cardExpField, dateField);

        if(!(bouquetButton.isSelected() || vaseButton.isSelected() || potButton.isSelected())) {
            isFilled = false;
            setButtonErrorStyle(bouquetButton, vaseButton, potButton);
        }
        if(!(roseCheckBox.isSelected() || tulipCheckBox.isSelected() || violetCheckBox.isSelected() || sunflowerCheckBox.isSelected() || orchidCheckBox.isSelected() || daisyCheckBox.isSelected())) {
            isFilled = false;
            setButtonErrorStyle(roseCheckBox, tulipCheckBox, violetCheckBox, sunflowerCheckBox, orchidCheckBox);
        }
        if(deliveryField.getText().length() == 0) {
            isFilled = false;
            setTextErrorStyle(deliveryField);
        }
        if(nameField.getText().length() == 0) {
            isFilled = false;
            setTextErrorStyle(nameField);
        }
        if(cardNumberField.getText().length() == 0) {
            isFilled = false;
            setTextErrorStyle(cardNumberField);
        }
        if(cardCVCField.getText().length() == 0) {
            isFilled = false;
            setTextErrorStyle(cardCVCField);
        }
        if(cardExpField.getText().length() == 0) {
            isFilled = false;
            setTextErrorStyle(cardExpField);
        }
        if(dateField.getValue() == null) {
            isFilled = false;
            setTextErrorStyle(dateField);
        }
        return isFilled;
    }

    public void handleClear() {
        bouquetButton.setSelected(false);
        vaseButton.setSelected(false);
        potButton.setSelected(false);
        roseCheckBox.setSelected(false);
        tulipCheckBox.setSelected(false);
        violetCheckBox.setSelected(false);
        sunflowerCheckBox.setSelected(false);
        orchidCheckBox.setSelected(false);
        daisyCheckBox.setSelected(false);
        deliveryField.setText("");
        nameField.setText("");
        cardNumberField.setText("");
        cardExpField.setText("");
        cardCVCField.setText("");
        dateField.setValue(null);

        setNormalStyle(bouquetButton, vaseButton, potButton, roseCheckBox, tulipCheckBox, violetCheckBox, sunflowerCheckBox,
                orchidCheckBox, daisyCheckBox, deliveryField, nameField, cardNumberField, cardCVCField, cardExpField, dateField);
    }
}

