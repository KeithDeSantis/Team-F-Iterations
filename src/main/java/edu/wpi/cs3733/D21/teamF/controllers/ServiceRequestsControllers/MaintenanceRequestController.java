package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.D21.teamF.Translation.Translator;
import edu.wpi.cs3733.D21.teamF.controllers.ServiceRequests;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MaintenanceRequestController extends ServiceRequests {
    @FXML private JFXComboBox<String> locationField;
    @FXML private JFXComboBox<String> typeComboBox;
    @FXML private ImageView goBack;
    @FXML private JFXTextArea descriptionField;
    @FXML private Label typeLabel;
    @FXML private Label locationLabel;
    @FXML private Label descLabel;
    @FXML private JFXButton cancel;
    @FXML private JFXButton clearButton;
    @FXML private JFXButton submitButton;
    @FXML private Label urgencyLabel;
    @FXML private Label dateLabel;
    @FXML private JFXComboBox<String> urgencyComboBox;
    @FXML private JFXDatePicker dateOfIncident;
    @FXML private JFXComboBox<String> assignment;
    @FXML private Label assignmentLabel;

    ObservableList<String> problemTypes = FXCollections.observableArrayList("Electrical", "Lighting",
            "Elevator", "Plumbing", "Safety Hazard", "Damage", "Spill", "HAZ-MAT");

    ObservableList<String> urgencyLevels = FXCollections.observableArrayList("URGENT", "PRIORITY", "LOW PRIORITY");

    ObservableList<String> locations = FXCollections.observableArrayList();

    //ObservableList<String> employeeList = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        typeLabel.textProperty().bind(Translator.getTranslator().getTranslationBinding(typeLabel.getText()));
        locationLabel.textProperty().bind(Translator.getTranslator().getTranslationBinding(locationLabel.getText()));
        urgencyLabel.textProperty().bind(Translator.getTranslator().getTranslationBinding(urgencyLabel.getText()));
        dateLabel.textProperty().bind(Translator.getTranslator().getTranslationBinding(dateLabel.getText()));
        descLabel.textProperty().bind(Translator.getTranslator().getTranslationBinding(descLabel.getText()));

        cancel.textProperty().bind(Translator.getTranslator().getTranslationBinding(cancel.getText()));
        clearButton.textProperty().bind(Translator.getTranslator().getTranslationBinding(clearButton.getText()));
        submitButton.textProperty().bind(Translator.getTranslator().getTranslationBinding(submitButton.getText()));

        try{

            // Insert problem types and urgency into combo boxes
            typeComboBox.setItems(problemTypes);
            urgencyComboBox.setItems(urgencyLevels);

        } catch (Exception e){}

        // Load node long names from data base

        try{
            List<NodeEntry> nodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries();

            final ObservableList<String> nodeList = FXCollections.observableArrayList();
            for(NodeEntry n: nodeEntries){
                nodeList.add(n.getShortName());
            }
            nodeList.addAll(nodeEntries.stream().map(NodeEntry::getShortName).sorted().collect(Collectors.toList()));
            this.locationField.setItems(nodeList);

        } catch (Exception e){
        }


    }

    /**
     * Takes in the values from the request page and passes them into the DB
     * @param e The FXML node that triggered the method
     * @throws IOException
     * @throws SQLException
     * @author Leo Morris
     */
    public void handleSubmit(ActionEvent e) throws IOException, SQLException {
        if(formFilled()) {
            String name = urgencyComboBox.getValue() + ": ";
            if (typeComboBox.getValue().equals("Damage") || typeComboBox.getValue().equals("Safety Hazard") || typeComboBox.getValue().equals("Spill")) {
                name += typeComboBox.getValue() + " at " + locationField.getValue();
            } else {
                name += typeComboBox.getValue() + " problem at " + locationField.getValue();
            }

            try{
                name += " on " + dateOfIncident.getValue().toString();
            } catch (NullPointerException nullPointerException){
                //Do Nothing
            }

//            String employee = "";
//            try{
//                employee = assignment.getValue();
//            } catch (NullPointerException nullPointerException){
//                // Leave assigned employee blank
//            }

            String additionalInfo = "Location: " + locationField.getValue() + "Date: " + dateOfIncident.getValue() +
                    "Urgency: " + urgencyComboBox.getValue();
            DatabaseAPI.getDatabaseAPI().addServiceReq(UUID.randomUUID().toString(), name,"", "false", additionalInfo);
            openSuccessWindow();
        }
    }

    public boolean formFilled(){
        boolean filled = true;
        if(typeComboBox.getValue() == null){
            filled = false;
            setTextErrorStyle(typeComboBox);
            typeComboBox.setPromptText("Specify the problem");
        }
        if(locationField.getValue() == null){
            filled = false;
            setTextErrorStyle(locationField);
            locationField.setPromptText("Please give a location");
        }
        if(descriptionField.getText().isEmpty()){
            filled = false;
            setTextErrorStyle(descriptionField);
            descriptionField.setPromptText("Please give a description of the problem");
        }
        if(urgencyComboBox.getValue() == null){
            filled = false;
            setTextErrorStyle(urgencyComboBox);
            urgencyComboBox.setPromptText("Please give a location");
        }
        return filled;
    }

    public void reset() {
        locationField.setStyle("-fx-background-color: #00000000");
        descriptionField.setStyle("-fx-background-color: #00000000");
        typeComboBox.setStyle("-fx-background-color: #00000000");
        urgencyComboBox.setStyle("-fx-background-color: #00000000");
    }

    public void reset2() {
        locationField.setStyle("-fx-background-color: #00000000");
        descriptionField.setStyle("-fx-background-color: #00000000");
        typeComboBox.setStyle("-fx-background-color: #00000000");
        urgencyComboBox.setStyle("-fx-background-color: #00000000");
    }

    @Override
    public void handleClear(){
        locationField.setValue(null);
        typeComboBox.setValue(null);
        descriptionField.setText("");
        urgencyComboBox.setValue(null);
        dateOfIncident.setValue(null);
        //assignment.setValue(null);
        setNormalStyle(locationField, typeComboBox, descriptionField, urgencyComboBox, dateOfIncident);
        typeComboBox.setPromptText("");
        locationField.setPromptText("");
        descriptionField.setPromptText("");
        urgencyComboBox.setPromptText("");
        dateOfIncident.setPromptText("");
    }

    public void handleHelp(ActionEvent e) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/MaintenanceRequestHelpView.fxml");
    }

    public void goBack(ActionEvent actionEvent)throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/maintenanceRequest.fxml");
    }



}
