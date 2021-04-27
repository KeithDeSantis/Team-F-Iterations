package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MedicineDeliveryServiceRequest {
    @FXML
    public JFXTextField employeeName;
    @FXML
    public JFXTextField clientName;
    @FXML
    public JFXTextField clientRoom;
    @FXML
    public JFXTextArea medicineInformation;
    @FXML
    public JFXTextField cardNumber;
    @FXML
    public JFXTextField cvc;
    @FXML
    public JFXTextField expirationDate;
    @FXML
    public JFXTextField cardholder;

    /**
     * Submits a medicine delivery form
     * @param actionEvent the event signalling that the Submit button has been pressed
     * @throws IOException if the file resource is invalid
     * @author Tony Vuolo (bdane)
     */
    @FXML
    private void submit(ActionEvent actionEvent) throws IOException {
        boolean submitSuccessful = true;
        for(int i = 0; i < 8; i++) {
            TextInputControl node = null;
            switch(i) {
                case 0:
                    node = employeeName;
                    break;
                case 1:
                    node = clientName;
                    break;
                case 2:
                    node = clientRoom;
                    break;
                case 3:
                    node = medicineInformation;
                    break;
                case 4:
                    node = cardNumber;
                    break;
                case 5:
                    node = cvc;
                    break;
                case 6:
                    node = expirationDate;
                    break;
                case 7:
                    node = cardholder;
                    break;
                default:
                    System.out.println("Unexpected case reached.");
                    break;
            }
            if(node.getText().length() > 0) {
                node.setStyle("-fx-border-color: transparent");
                node.setStyle("-fx-background-color: transparent");
            } else {
                submitSuccessful = false;
                node.setStyle("-fx-border-color: #FF0000");
                node.setStyle("-fx-background-color: #FF000088");
            }
        }
        if(submitSuccessful) {
            // Loads form submitted window and passes in current stage to return to request home
            FXMLLoader submitedPageLoader = new FXMLLoader();
            submitedPageLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/Service Requests/FormSubmittedView.fxml"));
            Stage submittedStage = new Stage();
            Parent root = submitedPageLoader.load();
            FormSubmittedViewController formSubmittedViewController = submitedPageLoader.getController();
            formSubmittedViewController.changeStage((Stage) employeeName.getScene().getWindow());
            Scene submitScene = new Scene(root);
            submittedStage.setScene(submitScene);
            submittedStage.setTitle("Submission Complete");
            submittedStage.initModality(Modality.APPLICATION_MODAL);
            submittedStage.showAndWait();
        }
    }

    /**
     * Cancels this service request
     * @param actionEvent the event signalling that the Cancel button has been pressed
     * @throws IOException if the new file resource is invalid
     * @author Tony Vuolo (bdane)
     */
    @FXML // Replaced close method with this (See comment on close) - LM
    private void cancel(ActionEvent actionEvent) throws IOException {
        Stage stage;
        Parent root;
        stage = (Stage) employeeName.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml"));
        stage.getScene().setRoot(root);
        stage.setTitle("Service Request Home");
        stage.show();
    }

    /*  REMOVED: Caused duplicate window instead of closing request page- LM
    /**
     * Closes the application
     * @param actionEvent the ActionEvent signalling that the page is to be closed
     * @param resource the file resource (with path)
     * @param title the title of the new page
     * @throws IOException if the file resource is invalid
     * @author Tony Vuolo (bdane)

    private void close(ActionEvent actionEvent, String resource, String title) throws IOException {
        Stage submittedStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(resource));
        Scene submitScene = new Scene(root);
        submittedStage.setScene(submitScene);
        submittedStage.setTitle(title);
        submittedStage.initModality(Modality.APPLICATION_MODAL);
        submittedStage.initOwner(((Button) actionEvent.getSource()).getScene().getWindow());
        submittedStage.showAndWait();
    }
    */

    /**
     * Changes the style of a Button when moused over
     * @param mouseEvent the event signalling that the mouse is over the JFXButton
     * @author Tony Vuolo (bdane)
     */
    @FXML
    private void mouseOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #F0C808; -fx-text-fill: #000000;");
    }

    /**
     * Reverts the style of a Button back to its original settings
     * @param mouseEvent the event signalling that the mouse is no longer over the JFXButton
     * @author Tony Vuolo (bdane)
     */
    @FXML
    private void mouseOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #03256C; -fx-text-fill: #FFFFFF;");
    }


}
