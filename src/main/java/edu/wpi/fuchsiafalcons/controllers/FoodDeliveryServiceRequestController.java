package edu.wpi.fuchsiafalcons.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for Food Delivery Service View
 * @author karenhou
 */
public class FoodDeliveryServiceRequestController {

    @FXML private Button xButton;
    @FXML private Button cancelButton;
    @FXML private TextField deliveryLocationField;
    @FXML private TextField deliveryTimeField;
    @FXML private TextField allergyField;
    @FXML private TextField specialInstructionsField;
    @FXML private RadioButton rButtonFood1;
    @FXML private RadioButton rButtonFood2;
    @FXML private RadioButton rButtonFood3;
    @FXML private RadioButton rButtonFood4;
    @FXML private RadioButton rButtonDrink1;
    @FXML private RadioButton rButtonDrink2;
    @FXML private RadioButton rButtonDrink3;
    @FXML private RadioButton rButtonDrink4;
    @FXML private CheckBox cbSide1;
    @FXML private CheckBox cbSide2;
    @FXML private CheckBox cbSide3;
    @FXML private CheckBox cbSide4;


    @FXML
    private void handleButtonPushed(ActionEvent e) throws IOException{
        Button buttonPushed = (Button) e.getSource();

        if (buttonPushed == xButton || buttonPushed == cancelButton) { // is x button
            Stage stage = (Stage) xButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/ServiceRequestHomeView.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Service Requests");
            stage.show();
        }
    }



}
