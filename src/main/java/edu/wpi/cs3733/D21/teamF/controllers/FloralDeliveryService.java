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
public class FloralDeliveryService {

    @FXML private JFXRadioButton bouquetButton;
    @FXML private JFXRadioButton vaseButton;
    @FXML private JFXRadioButton potButton;
    @FXML private JFXButton clearButton;
    @FXML private JFXButton submitButton;
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
     * When back button is pressed
     * @author KD
     */
    public void handleBack() throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml");
    }

    /**
     * Return to Home when it is decided where home is
     * @param mouseEvent
     * @author KD
     */
    public void handleHome(MouseEvent mouseEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
    }

    /**
     * Will eventually save the request to DB
     * @param actionEvent
     * @author KD
     */
    public void handleSubmit(ActionEvent actionEvent) throws SQLException, IOException {
        if(isFilledOut()) {
            String type = "Flower Delivery";
            String uuid = UUID.randomUUID().toString();
            String additionalInfo = "Date: " + dateField.getValue() + "Deliver to: " + deliveryField.getText() +
            "CC Number: " + cardNumberField.getText() + "CC CVC: " + cardCVCField.getText() + "CC Exp. Date: " + cardExpField.getText();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, "", "false", additionalInfo);
            //successField.setText("Request Submitted!");
            // Loads form submitted window and passes in current stage to return to request home
            FXMLLoader submitedPageLoader = new FXMLLoader();
            submitedPageLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/FormSubmittedView.fxml"));
            Stage submittedStage = new Stage();
            Parent root = submitedPageLoader.load();
            FormSubmittedViewController formSubmittedViewController = submitedPageLoader.getController();
            formSubmittedViewController.changeStage((Stage) submitButton.getScene().getWindow());
            Scene submitScene = new Scene(root);
            submittedStage.setScene(submitScene);
            submittedStage.setTitle("Submission Complete");
            submittedStage.initModality(Modality.APPLICATION_MODAL);
            submittedStage.showAndWait();
        } else { successField.setText(""); }
    }

    public boolean isFilledOut() {
        boolean isFilled = true;
        if(!(bouquetButton.isSelected() || vaseButton.isSelected() || potButton.isSelected())) {
            isFilled = false;
            bouquetButton.setStyle("-fx-text-fill: #e8321e");
            vaseButton.setStyle("-fx-text-fill: #e8321e");
            potButton.setStyle("-fx-text-fill: #e8321e");
        }
        else {
            bouquetButton.setStyle("-fx-text-fill: #000000");
            vaseButton.setStyle("-fx-text-fill: #000000");
            potButton.setStyle("-fx-text-fill: #000000");
        }
        if(!(roseCheckBox.isSelected() || tulipCheckBox.isSelected() || violetCheckBox.isSelected() || sunflowerCheckBox.isSelected() || orchidCheckBox.isSelected() || daisyCheckBox.isSelected())) {
            isFilled = false;
            roseCheckBox.setStyle("-fx-text-fill: #e8321e");
            tulipCheckBox.setStyle("-fx-text-fill: #e8321e");
            violetCheckBox.setStyle("-fx-text-fill: #e8321e");
            sunflowerCheckBox.setStyle("-fx-text-fill: #e8321e");
            orchidCheckBox.setStyle("-fx-text-fill: #e8321e");
            daisyCheckBox.setStyle("-fx-text-fill: #e8321e");
        }
        else {
            roseCheckBox.setStyle("-fx-text-fill: #000000");
            tulipCheckBox.setStyle("-fx-text-fill: #000000");
            violetCheckBox.setStyle("-fx-text-fill: #000000");
            sunflowerCheckBox.setStyle("-fx-text-fill: #000000");
            orchidCheckBox.setStyle("-fx-text-fill: #000000");
            daisyCheckBox.setStyle("-fx-text-fill: #000000");
        }
        if(deliveryField.getText().length() == 0) {
            isFilled = false;
            deliveryField.setStyle("-fx-background-color: #ffbab8;");
        }
        else {
            deliveryField.setStyle("-fx-background-color: transparent;");
        }
        if(nameField.getText().length() == 0) {
            isFilled = false;
            nameField.setStyle("-fx-background-color: #ffbab8;");
        }
        else {
            nameField.setStyle("-fx-background-color: transparent;");
        }
        if(cardNumberField.getText().length() == 0) {
            isFilled = false;
            cardNumberField.setStyle("-fx-background-color: #ffbab8;");
        }
        else {
            cardNumberField.setStyle("-fx-background-color: transparent;");
        }
        if(cardCVCField.getText().length() == 0) {
            isFilled = false;
            cardCVCField.setStyle("-fx-background-color: #ffbab8;");
        }
        else {
            cardCVCField.setStyle("-fx-background-color: transparent;");
        }
        if(cardExpField.getText().length() == 0) {
            isFilled = false;
            cardExpField.setStyle("-fx-background-color: #ffbab8;");
        }
        else {
            cardExpField.setStyle("-fx-background-color: transparent;");
        }
        if(dateField.getValue() == null) {
            isFilled = false;
            dateField.setStyle("-fx-background-color: #ffbab8;");
        }
        else {
            dateField.setStyle("-fx-background-color: transparent;");
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
        bouquetButton.setStyle("-fx-text-fill: #000000");
        vaseButton.setStyle("-fx-text-fill: #000000");
        potButton.setStyle("-fx-text-fill: #000000");
        roseCheckBox.setStyle("-fx-text-fill: #000000");
        tulipCheckBox.setStyle("-fx-text-fill: #000000");
        violetCheckBox.setStyle("-fx-text-fill: #000000");
        sunflowerCheckBox.setStyle("-fx-text-fill: #000000");
        orchidCheckBox.setStyle("-fx-text-fill: #000000");
        daisyCheckBox.setStyle("-fx-text-fill: #000000");
        deliveryField.setStyle("-fx-background-color: transparent;");
        nameField.setStyle("-fx-background-color: transparent;");
        cardNumberField.setStyle("-fx-background-color: transparent;");
        cardCVCField.setStyle("-fx-background-color: transparent;");
        cardExpField.setStyle("-fx-background-color: transparent;");
        dateField.setStyle("-fx-background-color: transparent;");
    }
}

