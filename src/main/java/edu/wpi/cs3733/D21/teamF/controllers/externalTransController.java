package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class externalTransController {
    @FXML private JFXTextField employeeName;
    @FXML private JFXTextField loc;
    @FXML private JFXTextField methodTrans;
    @FXML private JFXTextField special;
    @FXML private JFXButton submit;
    @FXML private JFXButton Cancel;

    public void goHome(MouseEvent mouseEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
    }

    @FXML
    public void submitpushed(ActionEvent actionEvent) throws IOException {
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
        submittedStage.initOwner(((Button) actionEvent.getSource()).getScene().getWindow());
        submittedStage.showAndWait();
    }


    @FXML
    public void cancel(ActionEvent actionEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml");
    }




}
