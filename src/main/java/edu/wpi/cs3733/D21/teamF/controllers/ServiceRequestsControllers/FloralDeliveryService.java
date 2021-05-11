package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.controllers.ServiceRequests;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Controller for Floral Delivery Service View
 * @author keithdesantis
 */
public class FloralDeliveryService extends ServiceRequests {

    @FXML private JFXRadioButton bouquetButton;
    @FXML private JFXRadioButton vaseButton;
    @FXML private JFXRadioButton potButton;
    @FXML private JFXComboBox<String> deliveryField;
    @FXML private JFXDatePicker dateField;
    @FXML private JFXTextField emailField;
    @FXML private JFXTextField cardNumberField;
    @FXML private JFXTextField cardCVCField;
    @FXML private JFXTextField cardExpField;
    @FXML private JFXCheckBox roseCheckBox;
    @FXML private JFXCheckBox tulipCheckBox;
    @FXML private JFXCheckBox violetCheckBox;
    @FXML private JFXCheckBox sunflowerCheckBox;
    @FXML private JFXCheckBox orchidCheckBox;
    @FXML private JFXCheckBox daisyCheckBox;



    @FXML
    public void initialize() {
        try{
            List<NodeEntry> nodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries();

            final ObservableList<String> nodeList = FXCollections.observableArrayList();
            for(NodeEntry n: nodeEntries){
                nodeList.add(n.getShortName());
            }
            this.deliveryField.setItems(nodeList);

        } catch(Exception e){

        }
    }

    /**
     * Handles the push of a radio button (sets up Toggle Groups)
     * @author KeithDeSantis
     */
    @FXML
    private void handleRadioButtonClicked() {
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
            String additionalInfo = "Date: " + dateField.getValue() + "Deliver to: " + deliveryField.getValue() +
            "CC Number: " + cardNumberField.getText() + "CC CVC: " + cardCVCField.getText() + "CC Exp. Date: " + cardExpField.getText()
                    + " Email;" + emailField.getText();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, "", "false", additionalInfo);
            // Loads form submitted window and passes in current stage to return to request home
            openSuccessWindow();
        }
    }

    public boolean formFilled() {
        boolean isFilled = true;

        setNormalStyle(bouquetButton, vaseButton, potButton, roseCheckBox, tulipCheckBox, violetCheckBox, sunflowerCheckBox,
                orchidCheckBox, daisyCheckBox, deliveryField, emailField, cardNumberField, cardCVCField, cardExpField, dateField);

        if(!(bouquetButton.isSelected() || vaseButton.isSelected() || potButton.isSelected())) {
            isFilled = false;
            setButtonErrorStyle(bouquetButton, vaseButton, potButton);
        }
        if(!(roseCheckBox.isSelected() || tulipCheckBox.isSelected() || violetCheckBox.isSelected() || sunflowerCheckBox.isSelected() || orchidCheckBox.isSelected() || daisyCheckBox.isSelected())) {
            isFilled = false;
            setButtonErrorStyle(roseCheckBox, tulipCheckBox, violetCheckBox, sunflowerCheckBox, orchidCheckBox,daisyCheckBox);
        }
        if(deliveryField.getValue() == null) {
            isFilled = false;
            setTextErrorStyle(deliveryField);
        }
        if(emailField.getText().length() == 0) {
            isFilled = false;
            setTextErrorStyle(emailField);
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
        deliveryField.setValue(null);
        emailField.setText("");
        cardNumberField.setText("");
        cardExpField.setText("");
        cardCVCField.setText("");
        dateField.setValue(null);

        setNormalStyle(bouquetButton, vaseButton, potButton, roseCheckBox, tulipCheckBox, violetCheckBox, sunflowerCheckBox,
                orchidCheckBox, daisyCheckBox, deliveryField, emailField, cardNumberField, cardCVCField, cardExpField, dateField);
    }


    public void handleHelp(ActionEvent actionEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/FloralDeliveryHelpView.fxml");
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/FloralDeliveryServiceRequestView.fxml");
    }
}

