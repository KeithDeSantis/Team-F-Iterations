package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;


public class LaundryRequestController extends ServiceRequests {

    @FXML private JFXButton cancel;
    @FXML private JFXButton help;
    @FXML private JFXRadioButton darks;
    @FXML private JFXRadioButton lights;
    @FXML private JFXRadioButton both;
    @FXML private JFXRadioButton hot;
    @FXML private JFXRadioButton cold;
    @FXML private JFXRadioButton folded;

    @FXML public TextField employeeID;
    @FXML public TextField clientName;
    @FXML public TextField additionalInstructions;

    public TextField getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(TextField employeeID) {
        this.employeeID = employeeID;
    }

    public TextField getClientName() {
        return clientName;
    }

    public void setClientName(TextField clientName) {
        this.clientName = clientName;
    }

    public TextField getAdditionalInstructions() {
        return additionalInstructions;
    }

    public void setAdditionalInstructions(TextField additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }

    @FXML
    public void handleSubmit(ActionEvent e) throws IOException, SQLException {
        if(formFilled()) {
            // Loads form submitted window and passes in current stage to return to request home
            String uuid = UUID.randomUUID().toString();
            String type = "Laundry Request";
            String person = "";
            String completed = "false";
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, person, completed, additionalInformation());

            openSuccessWindow();
        }
    }

    private String additionalInformation(){
        ArrayList<JFXRadioButton> rButtons = new ArrayList<>();
        rButtons.add(darks);
        rButtons.add(lights);
        rButtons.add(both);
        rButtons.add(hot);
        rButtons.add(cold);
        rButtons.add(folded);
        StringBuilder additionalInfo = new StringBuilder("Laundry Instructions: ");

        for(JFXRadioButton r: rButtons){
            if(r.isSelected()){
                additionalInfo.append(", ").append(r.getText());
            }
        }

        return additionalInfo.toString();
    }

    public boolean formFilled() {
        return employeeID.getText().length()>0 && clientName.getText().length()>0;
    }

}
