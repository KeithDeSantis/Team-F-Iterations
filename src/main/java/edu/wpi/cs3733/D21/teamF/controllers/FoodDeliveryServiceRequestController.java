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
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller for Food Delivery Service View
 * @author karenhou
 */
public class FoodDeliveryServiceRequestController {

    @FXML private JFXButton xButton;
    @FXML private JFXButton cancelButton;
    @FXML private JFXButton helpButton;
    @FXML private Button helpXButton;
    @FXML private JFXButton submitButton;
    @FXML private JFXComboBox<String> deliveryLocationField;
    @FXML private JFXTimePicker deliveryTimeField;
    @FXML private JFXTextField allergyField;
    @FXML private JFXTextField specialInstructionsField;
    @FXML private JFXRadioButton rButtonFood1;
    @FXML private JFXRadioButton rButtonFood2;
    @FXML private JFXRadioButton rButtonFood3;
    @FXML private JFXRadioButton rButtonFood4;
    @FXML private JFXRadioButton rButtonDrink1;
    @FXML private JFXRadioButton rButtonDrink2;
    @FXML private JFXRadioButton rButtonDrink3;
    @FXML private JFXRadioButton rButtonDrink4;
    @FXML private JFXCheckBox cbSide1;
    @FXML private JFXCheckBox cbSide2;
    @FXML private JFXCheckBox cbSide3;
    @FXML private JFXCheckBox cbSide4;


    @FXML
    private void initialize(){
        try{
            List<NodeEntry> nodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries();

            final ObservableList<String> nodeList = FXCollections.observableArrayList();
            for(NodeEntry n: nodeEntries){
                nodeList.add(n.getShortName());
            }
            //nodeList.addAll(nodeEntries.stream().map(NodeEntry::getShortName).sorted().collect(Collectors.toList()));
            this.deliveryLocationField.setItems(nodeList);

        } catch(Exception e){

        }
    }
    /**
     * handles submit being pressed
     * @param e is the button being pushed
     * @throws IOException
     * @author KH
     */
    @FXML
    private void handleSubmitPushed(ActionEvent e) throws IOException, SQLException {
        if(formFilledOut()){
            String uuid = UUID.randomUUID().toString();
            String type = "Food Delivery";
            String person = "";
            String completed = "false";
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, person, type, completed);
            Stage submittedStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/FormSubmittedView.fxml"));
            Scene submitScene = new Scene(root);
            submittedStage.setScene(submitScene);
            submittedStage.setTitle("Submission Complete");
            submittedStage.initModality(Modality.APPLICATION_MODAL);
            submittedStage.initOwner(((Button) e.getSource()).getScene().getWindow());
            submittedStage.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner((Stage) ((Button) e.getSource()).getScene().getWindow());
            alert.setTitle("Form not filled.");
            alert.setHeaderText("Form incomplete");
            alert.setContentText("Please fill out the location, type of food, drink, and side.");
            alert.showAndWait();
        }
    }


    /**
     * handles cancel and help button being pushed
     * @param e is the button being pressed
     * @throws IOException
     * @author KH
     */
    @FXML
    private void handleButtonPushed(ActionEvent e) throws IOException{
        Button buttonPushed = (Button) e.getSource();

        if (buttonPushed == cancelButton) { // is cancel button
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeView.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Service Requests");
            stage.show();
        } else if (buttonPushed == helpButton){
            Stage helpPopUpStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/FoodDeliveryHelpView.fxml"));
            Scene helpPopUpScene = new Scene(root);
            helpPopUpStage.setScene(helpPopUpScene);
            helpPopUpStage.setTitle("Food Delivery Request Help Menu");
            helpPopUpStage.initModality(Modality.APPLICATION_MODAL);
            helpPopUpStage.initOwner(buttonPushed.getScene().getWindow());
            helpPopUpStage.showAndWait();
        } else if (buttonPushed == helpXButton){
            Stage popUpStage = (Stage) helpXButton.getScene().getWindow();
            popUpStage.close();
        }
    }

    /**
     * Helper that checks if form is acceptably filled out
     * @return true if form is filled out
     * @author KH
     */
    private boolean formFilledOut() {
        boolean deliveryLocation = (deliveryLocationField.getValue() != null);
        //boolean deliveryTime = deliveryTimeField.getText().length() > 0;
        //boolean allergy = allergyField.getText().length() > 0;
        boolean foodChosen = rButtonFood1.isSelected() || rButtonFood2.isSelected() || rButtonFood3.isSelected() || rButtonFood4.isSelected();
        boolean drinkChosen = rButtonDrink1.isSelected() || rButtonDrink2.isSelected() || rButtonDrink3.isSelected() || rButtonDrink4.isSelected();
        boolean sideChosen = cbSide1.isSelected() || cbSide2.isSelected() || cbSide3.isSelected() || cbSide4.isSelected();

        return deliveryLocation && foodChosen && drinkChosen && sideChosen;
    }

    /**
     * Handles radial button groups
     * @param e is the button being pushed
     * @author KH
     */
    @FXML
    private void handleRadialButtonPushed(ActionEvent e){
        ToggleGroup foodGroup = new ToggleGroup(); //group for foods
        rButtonFood1.setToggleGroup(foodGroup);
        rButtonFood2.setToggleGroup(foodGroup);
        rButtonFood3.setToggleGroup(foodGroup);
        rButtonFood4.setToggleGroup(foodGroup);

        ToggleGroup drinkGroup = new ToggleGroup(); //group for drinks
        rButtonDrink1.setToggleGroup(drinkGroup);
        rButtonDrink2.setToggleGroup(drinkGroup);
        rButtonDrink3.setToggleGroup(drinkGroup);
        rButtonDrink4.setToggleGroup(drinkGroup);

    }




}
