package edu.wpi.cs3733.D21.teamF.controllers;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import edu.wpi.cs3733.D21.teamF.entities.CurrentUser;

public class GiftDeliveryServiceRequestController extends ServiceRequests {
        @FXML private JFXTextField name;
        @FXML private JFXTextField ccNumber;
        @FXML private JFXTextField expirationDate;
        @FXML private JFXTextField csv;
        @FXML private JFXTextArea specialInstructions;

        public void handleSubmit(ActionEvent actionEvent) throws IOException, SQLException {
            if(formFilled()) {
                String uuid = UUID.randomUUID().toString();
                String type = "Gift Delivery";
                String additionalInformation = "Requester: " + name.getText() + "CC Number: " + ccNumber.getText() +
                        "Expiration: " + expirationDate.getText() + "CSV: " + csv.getText() + "Special instructions: " + specialInstructions.getText();
                DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, "", "false", additionalInformation);
                // Loads form submitted window and passes in current stage to return to request home
                openSuccessWindow();
            }
        }


        public void handleClear(ActionEvent actionEvent) throws IOException {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/GiftDeliveryServiceRequest.fxml");
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

}

