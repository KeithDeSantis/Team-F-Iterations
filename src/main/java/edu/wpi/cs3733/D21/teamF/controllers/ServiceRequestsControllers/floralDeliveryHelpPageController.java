package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

public class floralDeliveryHelpPageController {
    @FXML private JFXButton back;


    public void goBack(ActionEvent actionEvent) throws IOException {
        ( (Stage) back.getScene().getWindow()).close();
    }
}
