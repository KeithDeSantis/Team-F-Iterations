package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputControl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class MedicineDeliveryServiceRequest extends ServiceRequests {
    @FXML
    public JFXTextField clientName;
    @FXML
    public JFXTextField clientRoom;
    @FXML
    public JFXTextArea medicineInformation;
    @FXML
    public JFXTextField cardNumber;
    @FXML
    public JFXTextField cvc;
    @FXML
    public JFXTextField expirationDate;
    @FXML
    public JFXTextField cardholder;

    /**
     * Submits a medicine delivery form
     * @param actionEvent the event signalling that the Submit button has been pressed
     * @throws IOException if the file resource is invalid
     * @author Tony Vuolo (bdane)
     */
    @FXML
    public void handleSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        boolean submitSuccessful = true;
        for(int i = 0; i < 7; i++) {
            TextInputControl node = null;
            switch(i) {
                case 0:
                    node = clientName;
                    break;
                case 1:
                    node = clientRoom;
                    break;
                case 2:
                    node = medicineInformation;
                    break;
                case 3:
                    node = cardNumber;
                    break;
                case 4:
                    node = cvc;
                    break;
                case 5:
                    node = expirationDate;
                    break;
                case 6:
                    node = cardholder;
                    break;
                default:
                    System.out.println("Unexpected case reached.");
                    break;
            }
            if(node.getText().length() > 0) {
                node.setStyle("-fx-border-color: transparent");
                node.setStyle("-fx-background-color: transparent");
            } else {
                submitSuccessful = false;
                node.setStyle("-fx-border-color: #FF0000");
                node.setStyle("-fx-background-color: #FF000088");
            }
        }
        if(submitSuccessful) {
            String uuid = UUID.randomUUID().toString();
            String type = "Medicine Delivery";
            String person = "";
            String completed = "false";
            String additionalInfo = "Delivery Location: " + clientRoom.getText() + "Medicine Info: " + medicineInformation.getText()
                    + "Card Number: " + cardNumber.getText() + "Card Holder: " + cardholder.getText() + "CVC: " + cvc.getText()
                    + "Expiration Date: " + expirationDate.getText();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, person, completed, additionalInfo);

            // Loads form submitted window and passes in current stage to return to request home
            openSuccessWindow();
        }
    }

    /*  REMOVED: Caused duplicate window instead of closing request page- LM
    /**
     * Closes the application
     * @param actionEvent the ActionEvent signalling that the page is to be closed
     * @param resource the file resource (with path)
     * @param title the title of the new page
     * @throws IOException if the file resource is invalid
     * @author Tony Vuolo (bdane)

    private void close(ActionEvent actionEvent, String resource, String title) throws IOException {
        Stage submittedStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(resource));
        Scene submitScene = new Scene(root);
        submittedStage.setScene(submitScene);
        submittedStage.setTitle(title);
        submittedStage.initModality(Modality.APPLICATION_MODAL);
        submittedStage.initOwner(((Button) actionEvent.getSource()).getScene().getWindow());
        submittedStage.showAndWait();
    }
    */


}
