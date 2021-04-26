package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.database.ConnectionHandler;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class MaintenenceRequestController {
    @FXML private JFXButton submit;
    @FXML private JFXComboBox<String> locationField;
    @FXML private JFXComboBox<String> typeComboBox;
    @FXML private ImageView goBack;
    @FXML private JFXTextArea descriptionField;
    @FXML private Label typeLabel;
    @FXML private Label locationLabel;
    @FXML private Label descLabel;
    @FXML private JFXButton cancel;
    @FXML private Text title;
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

    ObservableList<String> employeeList = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        // Setup fonts
        Font defaultFont = Font.loadFont("file:src/main/resources/fonts/Montserrat-SemiBold.ttf", 20);
        submit.setFont(defaultFont);
        cancel.setFont(defaultFont);
        typeLabel.setFont(defaultFont);
        locationLabel.setFont(defaultFont);
        descLabel.setFont(defaultFont);
        urgencyLabel.setFont(defaultFont);
        dateLabel.setFont(defaultFont);
        assignmentLabel.setFont(defaultFont);

        title.setFont(Font.loadFont("file:src/main/resources/fonts/Volkhov-Regular.ttf", 40));

        // Insert problem types and urgency into combo boxes
        typeComboBox.setItems(problemTypes);
        urgencyComboBox.setItems(urgencyLevels);

        // Load node long names from data base
        List<NodeEntry> nodeEntryList = new ArrayList<>();

        try{
            nodeEntryList = DatabaseAPI.getDatabaseAPI().genNodeEntries(ConnectionHandler.getConnection());
        } catch (SQLException e){
            e.printStackTrace();
        }

        // Sort by long name
        nodeEntryList.stream().sorted(Comparator.comparing(NodeEntry::getLongName)).collect(Collectors.toList()).forEach(node->{
            locations.add(node.getLongName()); // Fill observable list with node long names
        });

        // Set location combo box to use long names
        locationField.setItems(locations);

        // Load in employee list from database TODO Replace with UserEntry after merge W/ updated DB
        List<NodeEntry> employees = new ArrayList<>();
        try {
            employees = DatabaseAPI.getDatabaseAPI().genNodeEntries(ConnectionHandler.getConnection());
        } catch (SQLException e){
            e.printStackTrace();
        }

        employees.stream().sorted(Comparator.comparing(NodeEntry::getShortName)).collect(Collectors.toList()).forEach(employee ->{
            employeeList.add(employee.getShortName());
        });

        // Set list to assignment Combo Box
        assignment.setItems(employeeList);

    }

    /**
     * Takes in the values from the request page and passes them into the DB
     * @param e The FXML node that triggered the method
     * @throws IOException
     * @throws SQLException
     * @author Leo Morris
     */
    public void handleSubmit(ActionEvent e) throws IOException, SQLException {
        if(isFilled()) {
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

            String employee = "";
            try{
                employee = assignment.getValue();
            } catch (NullPointerException nullPointerException){
                // Leave assigned employee blank
            }

            DatabaseAPI.getDatabaseAPI().addServiceReq(UUID.randomUUID().toString(), name, employee,
                    "false");//, name + ": " + descriptionField.getText()); FIXME Re-add instructions to addServiceReq after merge
            Stage submittedStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/Service Requests/FormSubmittedView.fxml")); // Loading in pop up View
            Scene submitScene = new Scene(root);
            submittedStage.setScene(submitScene);
            submittedStage.setTitle("Submission Complete");
            submittedStage.initModality(Modality.APPLICATION_MODAL);
            submittedStage.initOwner(((Button) e.getSource()).getScene().getWindow());
            submittedStage.showAndWait();
        }
    }

    public void handleGoHome(MouseEvent mouseEvent) throws IOException {
        Stage stage;
        Parent root;
        stage = (Stage) goBack.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
        stage.getScene().setRoot(root);
        stage.setTitle("Default Page");
        stage.show();
    }

    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #F0C808; -fx-text-fill: #000000;");
    }

    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #03256C; -fx-text-fill: #FFFFFF;");
    }

    public void handleCancel(ActionEvent actionEvent) throws IOException {
        Stage stage;
        Parent root;
        stage = (Stage) goBack.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml"));
        stage.getScene().setRoot(root);
        stage.setTitle("Service Request Home");
        stage.show();
    }

    public boolean isFilled(){
        boolean filled = true;
        if(typeComboBox.getValue() == null){
            filled = false;
            typeComboBox.setStyle("-fx-background-color: #ff000088");
            typeComboBox.setPromptText("Specify the problem");
        }
        if(locationField.getValue() == null){
            filled = false;
            locationField.setStyle("-fx-background-color: #ff000088");
            locationField.setPromptText("Please give a location");
        }
        if(descriptionField.getText().isEmpty()){
            filled = false;
            descriptionField.setStyle("-fx-background-color: #ff000088");
            descriptionField.setPromptText("Please give a description of the problem");
        }
        if(urgencyComboBox.getValue() == null){
            filled = false;
            urgencyComboBox.setStyle("-fx-background-color: #ff000088");
            urgencyComboBox.setPromptText("Please give a location");
        }
        return filled;
    }

    public void reset(KeyEvent keyEvent) {
        locationField.setStyle("-fx-background-color: #00000000");
        descriptionField.setStyle("-fx-background-color: #00000000");
        typeComboBox.setStyle("-fx-background-color: #00000000");
        urgencyComboBox.setStyle("-fx-background-color: #00000000");
    }

    public void reset2(ActionEvent actionEvent) {
        locationField.setStyle("-fx-background-color: #00000000");
        descriptionField.setStyle("-fx-background-color: #00000000");
        typeComboBox.setStyle("-fx-background-color: #00000000");
        urgencyComboBox.setStyle("-fx-background-color: #00000000");
    }


}
