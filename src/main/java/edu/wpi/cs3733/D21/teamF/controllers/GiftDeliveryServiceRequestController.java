package edu.wpi.cs3733.D21.teamF.controllers;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class GiftDeliveryServiceRequestController {
        @FXML private Button cancel;
        @FXML private Button submit;
        @FXML private JFXTextField name;
        @FXML private JFXTextField ccNumber;
        @FXML private JFXTextField expirationDate;
        @FXML private JFXTextField csv;
        @FXML private JFXTextArea specialInstructions;

        public void handleClose(ActionEvent actionEvent) throws IOException {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml");
        }

        public void handleSubmit(ActionEvent actionEvent) throws IOException, SQLException {
            String uuid = UUID.randomUUID().toString();
            String type = "Gift Delivery";
            String additionalInformation = "Requester: " + name.getText() + "CC Number: " + ccNumber.getText() +
                    "Expiration: " + expirationDate.getText() + "CSV: " + csv.getText() + "Special instructions: " + specialInstructions.getText();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, "", "false", additionalInformation);
            // Loads form submitted window and passes in current stage to return to request home
            FXMLLoader submitedPageLoader = new FXMLLoader();
            submitedPageLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/FormSubmittedView.fxml"));
            Stage submittedStage = new Stage();
            Parent root = submitedPageLoader.load();
            FormSubmittedViewController formSubmittedViewController = submitedPageLoader.getController();
            formSubmittedViewController.changeStage((Stage) submit.getScene().getWindow());
            Scene submitScene = new Scene(root);
            submittedStage.setScene(submitScene);
            submittedStage.setTitle("Submission Complete");
            submittedStage.initModality(Modality.APPLICATION_MODAL);
            submittedStage.initOwner(((Button) actionEvent.getSource()).getScene().getWindow());
            submittedStage.showAndWait();
        }


    public void handleBack(MouseEvent mouseEvent) throws IOException {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
    }
}

