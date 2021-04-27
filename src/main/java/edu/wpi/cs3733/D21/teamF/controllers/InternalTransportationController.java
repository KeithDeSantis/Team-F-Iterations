package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class InternalTransportationController {
    @FXML private JFXButton submit;

    @FXML private JFXButton clear;

    @FXML private JFXTextField employeeName;

    @FXML private JFXTextField deliverLocation;

    @FXML private JFXTextField movingDate;

    @FXML private JFXTextField patientName;

    @FXML private JFXTextField patientRoom;

    @FXML private JFXCheckBox relativesCheckBox;

    @FXML private JFXCheckBox doctorCheckBox;

    @FXML private JFXButton cancel;

    @FXML
    public void initialize(){
        cancel.setDisableVisualFocus(true); // Clears visual focus from cancel button, cause unknown - LM
    }

    private boolean isFilledOut() {

        employeeName.setStyle("-fx-background-color: transparent"); // set all text field backgrounds to clear to reset any fields that were marked as incomplete - KD
        deliverLocation.setStyle("-fx-background-color: transparent");
        movingDate.setStyle("-fx-background-color: transparent");
        patientName.setStyle("-fx-background-color: transparent");
        patientRoom.setStyle("-fx-background-color: transparent");

        // Check if each field has been filled out, if not do not continue and highlight the text field red - KD

        if(employeeName.getText().length() <= 0) {
            employeeName.setStyle("-fx-background-color:  #ffbab8");
        }
        if(deliverLocation.getText().length() <= 0) {
            deliverLocation.setStyle("-fx-background-color:  #ffbab8");
        }
        if(movingDate.getText().length() <= 0) {
            movingDate.setStyle("-fx-background-color:  #ffbab8");
        }
        if(patientName.getText().length() <= 0) {
            patientName.setStyle("-fx-background-color:  #ffbab8");
        }
        if(patientRoom.getText().length() <= 0) {
            patientRoom.setStyle("-fx-background-color:  #ffbab8");
        }

        return employeeName.getText().length() > 0 &&
                deliverLocation.getText().length() > 0 &&
                movingDate.getText().length() > 0 &&
                patientName.getText().length() > 0 &&
                patientRoom.getText().length() > 0;
    }

    public void handleBack(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) submit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Service Requests");
        stage.show();
    }

    public void handleSubmit(ActionEvent e) throws IOException {
        if(isFilledOut()) // form is complete
        {
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
            submittedStage.initOwner(((Button) e.getSource()).getScene().getWindow());
            submittedStage.showAndWait();
        } else { //form not complete
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner((Stage) ( (Button) e.getSource()).getScene().getWindow());  // Show alert
            alert.setTitle("Form not filled.");
            alert.setHeaderText("Form incomplete");
            alert.setContentText("Please fill out at least the Location, Type of Flowers, Containers, and Payment fields.");
            alert.showAndWait();
        }
    }

    public void handleClear(ActionEvent actionEvent) {
        employeeName.setText("");
        deliverLocation.setText("");
        movingDate.setText("");
        patientName.setText("");
        patientRoom.setText("");
        relativesCheckBox.setSelected(false);
        doctorCheckBox.setSelected(false);
    }

    /**
     * Handles returning to the service menu from the cancel button
     * Requires a different argument than the image view, thus different method
     * @param actionEvent The node that calls the method
     * @author Leo Morris
     */
    public void handleBack2(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) submit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Service Requests");
        stage.show();
    }
}
