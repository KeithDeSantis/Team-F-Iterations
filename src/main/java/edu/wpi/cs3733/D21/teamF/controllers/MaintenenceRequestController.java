package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
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
    @FXML private JFXComboBox locationField;
    @FXML private JFXComboBox typeComboBox;
    @FXML private ImageView goBack;
    @FXML private JFXTextArea descriptionField;
    @FXML private Label typeLabel;
    @FXML private Label locationLabel;
    @FXML private Label descLabel;
    @FXML private JFXButton cancel;
    @FXML private Text title;

    ObservableList<String> problemTypes = FXCollections.observableArrayList("Electrical", "Lighting",
            "Elevator", "Plumbing", "Safety Hazard", "Damage");

    ObservableList<String> locations = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        // Setup fonts
        Font defaultFont = Font.loadFont("file:src/main/resources/fonts/Montserrat-SemiBold.ttf", 20);
        submit.setFont(defaultFont);
        cancel.setFont(defaultFont);
        typeLabel.setFont(defaultFont);
        locationLabel.setFont(defaultFont);
        descLabel.setFont(defaultFont);

        title.setFont(Font.loadFont("file:src/main/resources/fonts/Volkhov-Regular.ttf", 40));

        // Insert problem types into combo box
        typeComboBox.setItems(problemTypes);

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
            String name = "";
            if (typeComboBox.getValue().toString().equals("Damage") || typeComboBox.getValue().toString().equals("Safety Hazard")) {
                name = typeComboBox.getValue().toString() + " at " + locationField.getValue().toString();
            } else {
                name = typeComboBox.getValue().toString() + " problem at " + locationField.getValue().toString();
            }

            DatabaseAPI.getDatabaseAPI().addServiceReq(UUID.randomUUID().toString(), name, "",
                    "false");//, descriptionField.getText()); FIXME Re-add instructions to addServiceReq after merge
            Stage submittedStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/FormSubmittedView.fxml")); // Loading in pop up View
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
        stage.setTitle("Default Page");
        stage.show();
    }

    public boolean isFilled(){
        boolean filled = true;
        try{
            typeComboBox.getValue().toString().equals("");
        } catch (NullPointerException nullPointerException){
            filled = false;
            typeComboBox.setStyle("-fx-background-color: #ff000088");
            typeComboBox.setPromptText("Specify the problem");
        }
        try{
            locationField.getValue().toString().equals("");
        } catch (NullPointerException nullPointerException){
            filled = false;
            locationField.setStyle("-fx-background-color: #ff000088");
            locationField.setPromptText("Please give a location");
        }
        if(descriptionField.getText().isEmpty()){
            filled = false;
            descriptionField.setStyle("-fx-background-color: #ff000088");
            descriptionField.setPromptText("Please give a description of the problem");
        }
        return filled;
    }

    public void reset(KeyEvent keyEvent) {
        locationField.setStyle("-fx-background-color: #00000000");
        descriptionField.setStyle("-fx-background-color: #00000000");
        typeComboBox.setStyle("-fx-background-color: #00000000");
    }

    public void reset2(ActionEvent actionEvent) {
        locationField.setStyle("-fx-background-color: #00000000");
        descriptionField.setStyle("-fx-background-color: #00000000");
        typeComboBox.setStyle("-fx-background-color: #00000000");
    }
}
