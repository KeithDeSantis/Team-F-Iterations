package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;
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

        boolean isFilled = true;
        setNormalStyle(deliverLocation, movingDate, patientName, patientRoom);

        if(deliverLocation.getText().length() <= 0) {
            isFilled = false;
            setTextErrorStyle(deliverLocation);
        }
        if(movingDate.getText().length() <= 0) {
            isFilled = false;
            setTextErrorStyle(movingDate);
        }
        if(patientName.getText().length() <= 0) {
            isFilled = false;
            setTextErrorStyle(patientName);
        }
        if(patientRoom.getText().length() <= 0) {
            isFilled = false;
            setTextErrorStyle(patientRoom);
        }

        return isFilled;
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
            alert.initOwner(( (Button) e.getSource()).getScene().getWindow());  // Show alert
            alert.setTitle("Form not filled.");
            alert.setHeaderText("Form incomplete");
            alert.setContentText("Please fill out at least the Location, Type of Flowers, Containers, and Payment fields."); //??? TODO: fix this
            alert.showAndWait();
        }
    }

    public void handleClear() {
        deliverLocation.setText("");
        movingDate.setText("");
        patientName.setText("");
        patientRoom.setText("");
        relativesCheckBox.setSelected(false);
        doctorCheckBox.setSelected(false);
        setNormalStyle(deliverLocation, movingDate, patientName, patientRoom, relativesCheckBox, doctorCheckBox);
    }

}
