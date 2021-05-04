package edu.wpi.cs3733.D21.teamF.controllers;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
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
        public void handleCancel(ActionEvent actionEvent) throws IOException{
//            if(/*user is admin*/) {
//                SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml");
//            }
//            else if (/*user is employee*/){
//                SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageEmployeeView.fxml");
//            }
//            else{
//                SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
//            }
        }

        public boolean formFilled() {
            return name.getText().length()>0 && ccNumber.getText().length()>0 && csv.getText().length()>0 && expirationDate.getText().length()>0 && specialInstructions.getText().length()>0;
        }

         @FXML
         public void handleClear() {
            name.setText("");
            ccNumber.setText("");
            expirationDate.setText("");
            csv.setText("");
            specialInstructions.setText("");

         }


}

