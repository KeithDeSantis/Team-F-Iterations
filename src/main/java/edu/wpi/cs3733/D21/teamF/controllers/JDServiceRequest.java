package edu.wpi.cs3733.D21.teamF.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;


public class JDServiceRequest {
    @FXML private Button submitButton;
    @FXML private TextField employeeName;
    @FXML private TextField location;
    @FXML private TextField Trans;
    @FXML private TextField speacal;

    private boolean allFilled(){
        boolean employee = employeeName.getText().length() >0;
        boolean loc = location.getText().length() >0;
        boolean Transportation = Trans.getText().length() >0;
        return employee && loc && Transportation;
    }

    @FXML
    private void buttonPushed(ActionEvent e) throws IOException
    {
        if(allFilled()) {
            Stage finished = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/FormSubmittedView.fxml"));
            Scene show = new Scene(root);
            finished.setScene(show);
            finished.setTitle("Service Request");
            finished.initModality(Modality.APPLICATION_MODAL);
            finished.initOwner(((Button) e.getSource()).getScene().getWindow());
            finished.showAndWait();
        }
    }

    @FXML
    private void handleButtonPushed(javafx.event.ActionEvent e) throws IOException {
/*
        Button buttonPushed = (Button) e.getSource();

        if (buttonPushed == xButton || buttonPushed == cancelButton) { // is x button
            Stage stage = (Stage) xButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeView.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Service Requests");
            stage.show();
        }
 */
    }


}
