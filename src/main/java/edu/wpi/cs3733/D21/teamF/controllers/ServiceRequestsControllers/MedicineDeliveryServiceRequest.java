package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.D21.teamF.controllers.ServiceRequests;
import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class MedicineDeliveryServiceRequest extends ServiceRequests {
    @FXML
    public JFXTextField clientName;
    @FXML
    public JFXComboBox<String> clientRoom;
    @FXML
    public JFXTimePicker deliveryTime;
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


    @FXML
    public void initialize(){
        try{
            List<NodeEntry> nodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries();

            final ObservableList<String> nodeList = FXCollections.observableArrayList();
            for(NodeEntry n: nodeEntries){
                nodeList.add(n.getShortName());
            }
            this.clientRoom.setItems(nodeList);

        } catch(Exception e){

        }
    }

    /**
     * Submits a medicine delivery form
     * @param actionEvent the event signalling that the Submit button has been pressed
     * @throws IOException if the file resource is invalid
     * @author Tony Vuolo (bdane)
     */
    @FXML
    public void handleSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        boolean submitSuccessful = true;
        setNormalStyle(clientRoom, clientName, deliveryTime, medicineInformation, cardholder, cardNumber, cvc, expirationDate);
        for(int i = 0; i < 6; i++) {
            TextInputControl node = null;
            switch(i) {
                case 0:
                    node = clientName;
                    break;
                case 1:
                    node = medicineInformation;
                    break;
                case 2:
                    node = cardNumber;
                    break;
                case 3:
                    node = cvc;
                    break;
                case 4:
                    node = expirationDate;
                    break;
                case 5:
                    node = cardholder;
                    break;
                default:
                    System.out.println("Unexpected case reached.");
                    break;
            }
            if(node.getText().length() > 0) {  //currently works because nodes are only text fields
                setNormalStyle(node);
            } else {
                submitSuccessful = false;
                setTextErrorStyle(node);
            }
            if(deliveryTime.getValue() == null){
                submitSuccessful = false;
                setTextErrorStyle(deliveryTime);
            }
            if(clientRoom.getValue() == null){
                submitSuccessful = false;
                setTextErrorStyle(clientRoom);
            }
        }
        if(submitSuccessful) {
            String uuid = UUID.randomUUID().toString();
            String type = "Medicine Delivery";
            String person = "";
            String completed = "false";
            String additionalInfo = "Delivery Location: " + clientRoom.getValue() + "Medicine Info: " + medicineInformation.getText()
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



    @Override
    public void handleClear() {
        clientName.setText("");
        clientRoom.setValue(null);
        deliveryTime.setValue(null);
        medicineInformation.setText("");
        cardholder.setText("");
        cardNumber.setText("");
        cvc.setText("");
        expirationDate.setText("");
        setNormalStyle(clientRoom, clientName, medicineInformation, deliveryTime, cardholder, cvc, cardNumber, expirationDate);
    }

    public void handleHelp(ActionEvent e) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/MedicineDeliveryHelpView.fxml");
    }

    public void goBack(ActionEvent actionEvent)throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/MedicineDeliveryServiceRequestView.fxml");
    }
}
