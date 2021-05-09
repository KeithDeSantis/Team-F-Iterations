package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.Translation.Translator;
import edu.wpi.cs3733.D21.teamF.controllers.ServiceRequests;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;

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
    @FXML private Label deliveryLocLbl;
    @FXML private Label nameLbl;
    @FXML private Label deliveryDateLbl;
    @FXML private Label flowerTypeLbl;
    @FXML private Label containerTypeLbl;
    @FXML private Label paymentLbl;
    @FXML private JFXButton cancelButton;
    @FXML private JFXButton clearButton;
    @FXML private JFXButton submitButton;

    @FXML private Label successField;
    @FXML private ImageView logoHome;


    @FXML
    public void initialize() {
//        deliveryLocLbl.textProperty().bind(Translator.getTranslator().getTranslationBinding(deliveryLocLbl.getText()));
//        nameLbl.textProperty().bind(Translator.getTranslator().getTranslationBinding(nameLbl.getText()));
//        deliveryDateLbl.textProperty().bind(Translator.getTranslator().getTranslationBinding(deliveryDateLbl.getText()));
//        paymentLbl.textProperty().bind(Translator.getTranslator().getTranslationBinding(paymentLbl.getText()));
//        flowerTypeLbl.textProperty().bind(Translator.getTranslator().getTranslationBinding(flowerTypeLbl.getText()));
//        containerTypeLbl.textProperty().bind(Translator.getTranslator().getTranslationBinding(containerTypeLbl.getText()));
//        roseCheckBox.textProperty().bind(Translator.getTranslator().getTranslationBinding(roseCheckBox.getText()));
//        tulipCheckBox.textProperty().bind(Translator.getTranslator().getTranslationBinding(tulipCheckBox.getText()));
//        violetCheckBox.textProperty().bind(Translator.getTranslator().getTranslationBinding(violetCheckBox.getText()));
//        sunflowerCheckBox.textProperty().bind(Translator.getTranslator().getTranslationBinding(sunflowerCheckBox.getText()));
//        orchidCheckBox.textProperty().bind(Translator.getTranslator().getTranslationBinding(orchidCheckBox.getText()));
//        daisyCheckBox.textProperty().bind(Translator.getTranslator().getTranslationBinding(daisyCheckBox.getText()));
//        bouquetButton.textProperty().bind(Translator.getTranslator().getTranslationBinding(bouquetButton.getText()));
//        vaseButton.textProperty().bind(Translator.getTranslator().getTranslationBinding(vaseButton.getText()));
//        potButton.textProperty().bind(Translator.getTranslator().getTranslationBinding(potButton.getText()));
//        cardNumberField.textProperty().bind(Translator.getTranslator().getTranslationBinding(cardNumberField.getPromptText()));
//        cancelButton.textProperty().bind(Translator.getTranslator().getTranslationBinding(cancelButton.getText()));
//        clearButton.textProperty().bind(Translator.getTranslator().getTranslationBinding(clearButton.getText()));
//        submitButton.textProperty().bind(Translator.getTranslator().getTranslationBinding(submitButton.getText()));

//        Image img = new Image(getClass().getResourceAsStream("/imagesAndLogos/BandWLogo.png"));
//        logoHome.setImage(img);

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
            setButtonErrorStyle(roseCheckBox, tulipCheckBox, violetCheckBox, sunflowerCheckBox, orchidCheckBox,daisyCheckBox);
        }
        if(deliveryField.getValue() == null) {
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
        deliveryField.setValue(null);
        nameField.setText("");
        cardNumberField.setText("");
        cardExpField.setText("");
        cardCVCField.setText("");
        dateField.setValue(null);

        setNormalStyle(bouquetButton, vaseButton, potButton, roseCheckBox, tulipCheckBox, violetCheckBox, sunflowerCheckBox,
                orchidCheckBox, daisyCheckBox, deliveryField, nameField, cardNumberField, cardCVCField, cardExpField, dateField);
    }


    public void handleHelp(ActionEvent actionEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/FloralDeliveryHelpView.fxml");
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/FloralDeliveryServiceRequestView.fxml");
    }
}

