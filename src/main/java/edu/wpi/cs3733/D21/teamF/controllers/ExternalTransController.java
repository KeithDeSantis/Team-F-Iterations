package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class ExternalTransController extends ServiceRequests{
    @FXML private JFXTextField employeeName;
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
        return employeeName.getText().length()>0 && methodTrans.getText().length()>0 && special.getText().length()>0;
    }
}
