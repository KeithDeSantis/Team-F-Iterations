package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class ExternalTransController extends ServiceRequests implements IController{
    @FXML private JFXTextField patientName;
    @FXML private JFXTextField loc;
    @FXML private JFXTextField methodTrans;
    @FXML private JFXTextField special;


    @FXML
    public void handleSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        if(formFilled()) {
            String uuid = UUID.randomUUID().toString();
            String type = "External Transit";
            String assignedPerson = "";
            String additionalInfo = "Location: " + loc.getText() + "Transit method: " + methodTrans.getText()
                    + "Special info:" + special.getText();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, assignedPerson, "false", additionalInfo);
            // Loads form submitted window and passes in current stage to return to request home
            openSuccessWindow();
        }
    }

    @Override
    public boolean formFilled() {
        boolean isFilled = true;

        setNormalStyle(patientName, methodTrans, special, loc);
        if(patientName.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(patientName);
        }
        if(methodTrans.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(methodTrans);
        }
        if(loc.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(loc);
        }
        return isFilled;
    }

    @Override
    public void handleClear() {
        patientName.setText("");
        loc.setText("");
        methodTrans.setText("");
        special.setText("");
        special.setStyle("-fx-text-fill: #000000");
        loc.setStyle("-fx-text-fill: #000000");
        methodTrans.setStyle("-fx-text-fill: #000000");
    }
}
