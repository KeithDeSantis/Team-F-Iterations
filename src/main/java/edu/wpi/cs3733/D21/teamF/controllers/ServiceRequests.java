package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.entities.CurrentUser;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public abstract class ServiceRequests {

    @FXML
    private JFXButton submitButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXButton helpButton;
    @FXML
    private JFXButton clearButton;

    public void handleSubmit(ActionEvent e) throws IOException, SQLException {}

    public void handleCancel() throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml");
    }

    public void handleHelp(ActionEvent e) throws IOException {}

    public void handleHome(ActionEvent e) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml");
    }

    public boolean formFilled() {
        return false;
    }

    /**
     * Applies the 'normal' style to the given components
     * @param components The nodes to apply the style to
     * @author Alex Friedman (ahf)
     */
    public void setNormalStyle(Node...components)
    {
        setStyle("-fx-background-color: transparent", components);
        setStyle("-fx-text-fill: #000000", components);
    }

    /**
     * Used to set the given components to use the error/invalid input style
     * @param components The components to apply the style to
     */
    public void setButtonErrorStyle(Node...components) //for rbuttons and cboxes
    {
        setStyle("-fx-text-fill: #e8321e", components);
    }

    public void setTextErrorStyle(Node...components) //for textfields and date/time fields
    {
        setStyle("-fx-background-color: #ffbab8", components);
    }

    /**
     * Used to set the components in the given list to have the given style.
     * @param style The string style to apply
     * @param components The components to apply the style to
     * @author Alex Friedman (ahf)
     */
    public void setStyle(String style, Node...components)
    {
        for(Node n : components)
            n.setStyle(style);
    }

    public void openSuccessWindow() throws IOException {
        FXMLLoader submittedPageLoader = new FXMLLoader();
        submittedPageLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/FormSubmittedView.fxml"));
        Stage submittedStage = new Stage();
        Parent root = submittedPageLoader.load();
        FormSubmittedViewController formSubmittedViewController = submittedPageLoader.getController();
        formSubmittedViewController.changeStage((Stage) submitButton.getScene().getWindow());
        Scene submitScene = new Scene(root);
        submittedStage.setScene(submitScene);
        submittedStage.setTitle("Submission Complete");
        submittedStage.initModality(Modality.APPLICATION_MODAL);
        submittedStage.showAndWait();
    }

    public void handleClear(){ }

}
