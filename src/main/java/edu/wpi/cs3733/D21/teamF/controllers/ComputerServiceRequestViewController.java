package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ComputerServiceRequestViewController {

    @FXML
    private JFXTextField computerNameText;

    @FXML
    private JFXTextField computerLocationText;

    @FXML
    private JFXTextField requesterTextText;

    @FXML
    private JFXComboBox<String> urgencyComboBox;

    @FXML
    private JFXTextArea descriptionText;


    private static final String LOW_URGENCY = "Low (fix when possible)";
    private static final String MEDIUM_URGENCY = "Medium (fix soon)";
    private static final String HIGH_URGENCY = "High (fix ASAP)";

    @FXML
    public void initialize(){
        // Set up floor comboBox and draw nodes on that floor
        final ObservableList<String> urgencies = FXCollections.observableArrayList();
        urgencies.addAll(LOW_URGENCY, MEDIUM_URGENCY, HIGH_URGENCY);
        urgencyComboBox.setItems(urgencies);
    }

    @FXML
    public void handleGoHome() throws IOException { goHome(); }



    @FXML
    public void handleSubmit() throws IOException {
        if(validate())
        {
            //Do something
            Stage submittedStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/FormSubmittedView.fxml")); // Loading in pop up View
            Scene submitScene = new Scene(root);
            submittedStage.setScene(submitScene);
            submittedStage.setTitle("Submission Complete");
            submittedStage.initModality(Modality.APPLICATION_MODAL);
            submittedStage.initOwner((computerNameText).getScene().getWindow());
            submittedStage.showAndWait();
        }
    }



    @FXML
    public void handleCancel() throws IOException { goHome(); }


    /**
     * Checks if our form has been filled out correctly and sets the components to use the proper style.
     * @return true if the form has been filled out validly; returns false otherwise.
     */
    private boolean validate()
    {
        boolean accept = true;

        //Clear old styles
        setNormalStyle(computerNameText, computerLocationText, requesterTextText, urgencyComboBox,descriptionText);


        if(computerNameText.getText().trim().isEmpty())
        {
            setErrorStyle(computerNameText);
            accept = false;
        }

        if(computerLocationText.getText().trim().isEmpty())
        {
            setErrorStyle(computerLocationText);
            accept = false;
        }

        if(requesterTextText.getText().trim().isEmpty())
        {
            setErrorStyle(requesterTextText);
            accept = false;
        }

        if(urgencyComboBox.getValue() == null)
        {
            setErrorStyle(urgencyComboBox);
            accept = false;
        }

        if(descriptionText.getText().trim().isEmpty())
        {
            setErrorStyle(descriptionText);
            accept = false;
        }

        return accept;
    }

    /**
     * Applies the 'normal' style to the given components
     * @param components The nodes to apply the style to
     * @author Alex Friedman (ahf)
     */
    private void setNormalStyle(Node...components)
    {
        setStyle("-fx-border-width: 0px", components);
    }

    /**
     * Used to set the given components to use the error/invalid input style
     * @param components The components to apply the style to
     * @author Alex Friedman (ahf)
     */
    private void setErrorStyle(Node...components)
    {
        setStyle("-fx-border-width: 2px", components);
        setStyle("-fx-border-color: red", components);
    }

    /**
     * Used to set the components in the given list to have the given style.
     * @param style The string style to apply
     * @param components The components to apply the style to
     * @author Alex Friedman (ahf)
     */
    private void setStyle(String style, Node...components)
    {
        for(Node n : components)
            n.setStyle(style);
    }


    /**
     * Used to return to the home page.
     *
     */
    private void goHome() throws IOException {
        //FIXME: AT SOME POINT ADD WARNING IF FORM FILLED OUT!
        Stage stage;
        Parent root;
        stage = (Stage) computerNameText.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml"));
        stage.getScene().setRoot(root);
        stage.setTitle("Default Page");
        stage.show();
    }

    @FXML
    public void handleClear() {

        setNormalStyle(computerNameText, computerLocationText, requesterTextText, urgencyComboBox,descriptionText);
        //FIXME: ADD WARNING
        computerNameText.setText("");
        computerLocationText.setText("");
        requesterTextText.setText("");

        urgencyComboBox.setValue(null);


         descriptionText.setText("");
    }
}