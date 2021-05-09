package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.controllers.ServiceRequests;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class GiftDeliveryServiceRequestController extends ServiceRequests {
    @FXML private JFXTextField name;
    @FXML private JFXTextField ccNumber;
    @FXML private JFXTextField expirationDate;
    @FXML private JFXTextField csv;
    @FXML private JFXTextArea specialInstructions;
    @FXML private JFXCheckBox tbCheckBox;
    @FXML private JFXCheckBox balloonsCheckBox;
    @FXML private JFXCheckBox cardsCheckBox;
    @FXML private JFXCheckBox puzzlesCheckBox;
    @FXML private JFXCheckBox blanketCheckBox;
    @FXML private JFXCheckBox magazinesCheckBox;
    @FXML private JFXDatePicker dateField;
    @FXML private JFXTimePicker timeField;

    @FXML
    public void initialize(){
    }

    public String setSpecialInstructions(){
        ArrayList<JFXCheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(tbCheckBox);
        checkBoxes.add(balloonsCheckBox);
        checkBoxes.add(cardsCheckBox);
        checkBoxes.add(puzzlesCheckBox);
        checkBoxes.add(blanketCheckBox);
        checkBoxes.add(magazinesCheckBox);
        StringBuilder special = new StringBuilder(specialInstructions.getText());

        for(JFXCheckBox aCheckBox : checkBoxes){
            if(aCheckBox.isSelected()){
                special.append(", ").append(aCheckBox.getText());
            }
        }
        return special.toString();
    }

    public void handleSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        if(formFilled()) {
            String uuid = UUID.randomUUID().toString();
            String type = "Gift Delivery";
            String additionalInformation = "Requester: " + name.getText() + "CC Number: " + ccNumber.getText() +
                    "Expiration: " + expirationDate.getText() + "CSV: " + csv.getText() + "Special instructions: " + setSpecialInstructions() +
                    "Schedule Delivery: " + dateField.getValue() + timeField.getValue();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, "", "false", additionalInformation);
            // Loads form submitted window and passes in current stage to return to request home
            openSuccessWindow();
        }
    }

    public void handleClear(ActionEvent actionEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/GiftDeliveryServiceRequest.fxml");

    }

    public boolean formFilled() {

        boolean isFilled = true;
        setNormalStyle(name, ccNumber, csv, expirationDate);

        if(name.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(name);
        }
        if(ccNumber.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(ccNumber);
        }
        if(csv.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(csv);
        }
        if(expirationDate.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(expirationDate);
        }
        return isFilled;
    }

    @FXML
    public void handleClear() {
        name.setText("");
        ccNumber.setText("");
        expirationDate.setText("");
        csv.setText("");
        specialInstructions.setText("");

    }

    public void handleHelp(ActionEvent actionEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/GiftDeliveryHelpView.fxml");
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/GiftDeliveryServiceRequest.fxml");
    }
}

