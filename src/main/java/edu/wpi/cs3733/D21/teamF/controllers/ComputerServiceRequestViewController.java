package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
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
    public void handleGoHome(MouseEvent mouseEvent) throws IOException {
        Stage stage;
        Parent root;
        stage = (Stage) computerNameText.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml"));
        stage.getScene().setRoot(root);
        stage.setTitle("Default Page");
        stage.show();
    }

    private boolean validate()
    {
        boolean accept = true;

        if(computerNameText.getText().isEmpty())
        {

        }
    }

    @FXML
    public void handleSubmit(ActionEvent actionEvent) {
        System.out.println(computerNameText.getText());
        System.out.println(computerLocationText.getText());
        System.out.println(requesterTextText.getText());
        System.out.println(descriptionText.getText());
        System.out.println(urgencyComboBox.getValue());
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
    }
}
