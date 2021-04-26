package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.database.ConnectionHandler;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SanitationRequestController {

    @FXML private JFXButton submit;
    @FXML private JFXButton cancel;
    @FXML private JFXTextArea description;
    @FXML private JFXComboBox<String> loc;
    @FXML private JFXComboBox<String> employeeAssigned;


    private void initialize(){
        try {
            List<NodeEntry> nodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries(ConnectionHandler.getConnection());
        //    List<UserEntry> UserEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries(ConnectionHandler.getConnection());

            final ObservableList<String> nodeList = FXCollections.observableArrayList();
            nodeList.addAll(nodeEntries.stream().map(NodeEntry::getShortName)
                    .sorted().collect(Collectors.toList()));
            this.loc.setItems(nodeList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleSubmit(ActionEvent actionEvent) throws IOException{
        Stage submittedStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/FormSubmittedView.fxml")); // Loading in pop up View
        Scene submitScene = new Scene(root);
        submittedStage.setScene(submitScene);
        submittedStage.setTitle("Submission Complete");
        submittedStage.initModality(Modality.APPLICATION_MODAL);
        submittedStage.initOwner(((Button)actionEvent.getSource()).getScene().getWindow());
        submittedStage.showAndWait();
    }

    public void handleCancel(ActionEvent actionEvent) throws IOException {
        Stage stage;
        Parent root;
        stage = (Stage) cancel.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml"));
        stage.getScene().setRoot(root);
        stage.setTitle("Service Request Home");
        stage.show();
    }

}