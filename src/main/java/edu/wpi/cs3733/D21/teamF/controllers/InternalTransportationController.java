package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class InternalTransportationController extends ServiceRequests {

    @FXML private JFXButton clear;

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

    public boolean formFilled() {

        // set all text field backgrounds to clear to reset any fields that were marked as incomplete - KD
        deliverLocation.setStyle("-fx-background-color: transparent");
        movingDate.setStyle("-fx-background-color: transparent");
        patientName.setStyle("-fx-background-color: transparent");
        patientRoom.setStyle("-fx-background-color: transparent");

        // Check if each field has been filled out, if not do not continue and highlight the text field red - KD

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

        return deliverLocation.getText().length() > 0 &&
                movingDate.getText().length() > 0 &&
                patientName.getText().length() > 0 &&
                patientRoom.getText().length() > 0;
    }

    public void handleSubmit(ActionEvent e) throws IOException, SQLException {
        if(formFilled()) // form is complete
        {
            String uuid = UUID.randomUUID().toString();
            String type = "Internal Transport";
            String person = "";
            String completed = "false";
            String additionalInfo = "Delivery Location: " + deliverLocation.getText() + "Delivery Date: " + movingDate.getText()
                    + "Patient Room: " + patientRoom.getText();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, person, completed, additionalInfo);

            // Loads form submitted window and passes in current stage to return to request home
            openSuccessWindow();
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
        deliverLocation.setText("");
        movingDate.setText("");
        patientName.setText("");
        patientRoom.setText("");
        relativesCheckBox.setSelected(false);
        doctorCheckBox.setSelected(false);
    }

}
