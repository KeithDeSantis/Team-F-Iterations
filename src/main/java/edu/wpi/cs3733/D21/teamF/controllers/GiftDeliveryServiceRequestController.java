package edu.wpi.cs3733.D21.teamF.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class GiftDeliveryServiceRequestController {
        @FXML private Button cancel;
        @FXML private Button submit;

        public void handleClose(ActionEvent actionEvent) throws IOException {
            Stage currentStage = (Stage)cancel.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeView.fxml"));
            Scene homeScene = new Scene(root);
            currentStage.setScene(homeScene);
            currentStage.show();
        }

        public void handleSubmit(ActionEvent actionEvent) throws IOException, SQLException {
            Stage currentStage = (Stage)submit.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/FormSubmittedView.fxml"));
            Scene homeScene = new Scene(root);
            currentStage.setScene(homeScene);
            currentStage.show();
        }


}

