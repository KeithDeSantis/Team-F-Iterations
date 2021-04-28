package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the pop-up dialog to edit or create a new node in the node editor
 * @author Keith DeSantis
 */
public class EditMapNodeDialogViewController {

    @FXML private TextField nodeIDField;
    @FXML private TextField xCoordField;
    @FXML private TextField yCoordField;
    @FXML private TextField floorField;
    @FXML private TextField buildingField;
    @FXML private TextField nodeTypeField;
    @FXML private TextField longNameField;
    @FXML private TextField shortNameField;
    @FXML private Label nodeIDLabel;
    @FXML private Label xCoordLabel;
    @FXML private Label yCoordLabel;
    @FXML private Label floorLabel;
    @FXML private Label buildingLabel;
    @FXML private Label nodeTypeLabel;
    @FXML private Label longNameLabel;
    @FXML private Label shortNameLabel;
    @FXML private Label title;

    private NodeEntry node = new NodeEntry(); // This will be the node that we pass in to edit - KD
    private Stage dialogStage; // This will be so we have access to our stage - KD
    public boolean okClicked; // Used to tell if edit was went through with - KD
    private ObservableList<NodeEntry> nodeList;
    private String currentIDIfEditing; //FIXME stupid way to fix making nodes with duplicate ID's, find better way


    public EditMapNodeDialogViewController() {}

    /**
     * On opening the dialog we want the node's info in the correct place (given we are editing an existing node)
     * @author KD
     */
    @FXML
    private void initialize() {

    }


    /**
     * Updates node information with newly inputted info
     * @param e
     * @author KD
     */
    @FXML
    private void handleOkClicked(ActionEvent e) {
        if(isFilledOut()) // ensure the form is filled out - KD
        {
            node.setNodeID(nodeIDField.getText()); // set all the fields of our node object to the new values given by the user - KD
            node.setNodeIDProperty(nodeIDField.getText());
            node.setXcoord(xCoordField.getText());
            node.setYcoord(yCoordField.getText());
            node.setFloor(floorField.getText());
            node.setBuilding(buildingField.getText());
            node.setNodeType(nodeTypeField.getText());
            node.setLongName(longNameField.getText());
            node.setShortNameProperty(shortNameField.getText());
            node.setShortName(shortNameField.getText());
            okClicked = true;
            dialogStage.close(); // close the edit dialog
        }
    }

    /**
     * Helper to make sure all fields are filled in
     * @return true if all fields are filled in
     * @author KD
     */
    private boolean isFilledOut() {

        nodeIDField.setStyle("-fx-background-color: transparent"); // set all text field backgrounds to clear to reset any fields that were marked as incomplete - KD
        xCoordField.setStyle("-fx-background-color: transparent");
        yCoordField.setStyle("-fx-background-color: transparent");
        floorField.setStyle("-fx-background-color: transparent");
        buildingField.setStyle("-fx-background-color: transparent");
        longNameField.setStyle("-fx-background-color: transparent");
        shortNameField.setStyle("-fx-background-color: transparent");
        nodeTypeField.setStyle("-fx-background-color: transparent");
        boolean xCoordValid = true;
        boolean yCoordValid = true;

        // Check if each field has been filled out, if not do not continue and highlight the text field red - KD

        if(nodeIDField.getText().length() <= 0 || !isUniqueNodeID(nodeIDField.getText())) {
            nodeIDField.setStyle("-fx-background-color:  #ff000088");
        }
        if(!isProperFloor(floorField.getText())) {
            floorField.setStyle("-fx-background-color:  #ff000088");
        }
        if(buildingField.getText().length() <= 0) {
            buildingField.setStyle("-fx-background-color:  #ff000088");
        }
        if(nodeTypeField.getText().length() <= 0) {
            nodeTypeField.setStyle("-fx-background-color:  #ff000088");
        }
        if(longNameField.getText().length() <= 0) {
            longNameField.setStyle("-fx-background-color:  #ff000088");
        }
        if(shortNameField.getText().length() <= 0) {
            shortNameField.setStyle("-fx-background-color:  #ff000088");
        }
        try {
            xCoordValid = Integer.parseInt(xCoordField.getText()) >= 0 && Integer.parseInt(xCoordField.getText()) <= 5000; } // make sure coordinates are valid integers and not negative
        catch(NumberFormatException e) {
            xCoordValid = false;
        }
        try {
            yCoordValid = Integer.parseInt(yCoordField.getText()) >= 0 && Integer.parseInt(yCoordField.getText()) <= 3400;
        } catch (NumberFormatException e){
            yCoordValid = false;
        }

        if(!xCoordValid) {xCoordField.setStyle("-fx-background-color:  #ff000088");}
        if(!yCoordValid) {yCoordField.setStyle("-fx-background-color: #ff000088");}

        return nodeIDField.getText().length() > 0 &&
                isUniqueNodeID(nodeIDField.getText()) &&
                xCoordValid &&
                yCoordValid &&
                isProperFloor(floorField.getText()) &&
                buildingField.getText().length() > 0 &&
                nodeTypeField.getText().length() > 0 &&
                longNameField.getText().length() > 0 &&
                shortNameField.getText().length() > 0;
    }

    /**
     * Setter for node
     * @param theNode
     * @author KD
     */
    public void setTheNode(NodeEntry theNode) {
        node = theNode;
        nodeIDField.setText(node.getNodeID());
        xCoordField.setText(node.getXcoord());
        yCoordField.setText(node.getYcoord());
        floorField.setText(node.getFloor());
        buildingField.setText(node.getBuilding());
        nodeTypeField.setText(node.getNodeType());
        longNameField.setText(node.getLongName());
        shortNameField.setText(node.getShortName());}

    /**
     * Setter for dialog stage
     * @param theStage
     * @author KD
     */
    public void setDialogStage (Stage theStage) { dialogStage = theStage; }

    public void setNodeList(ObservableList<NodeEntry> nodeList) { this.nodeList = nodeList; }

    /**
     * Checks that the inputted floor is a valid floor
     * @param floor the inputted floor
     * @author KD
     */
    public boolean isProperFloor (String floor) {
        boolean is1 = floor.equals("1");
        boolean is2 = floor.equals("2");
        boolean is3 = floor.equals("3");
        boolean isL1 = floor.equals("L1");
        boolean isL2 = floor.equals("L2");
        boolean isG = floor.equals("G");
        return is1 || is2 || is3 || isL1 || isL2 || isG;
    }

    public void setCurrentIDIfEditing(String currentIDIfEditing) {
        this.currentIDIfEditing = currentIDIfEditing;
    }

    /**
     * Helper for isFilledOut() that ensures the nodeID given by the users isn't a duplicate to one
     * that already exists.
     * @param nodeID the nodeID given by the user
     * @return true if the ID is unique
     * @author KD
     */
    public boolean isUniqueNodeID(String nodeID) {
        for(NodeEntry n : nodeList) {
            if(n.getNodeID().equals(nodeID) && !(n.getNodeID().equals(currentIDIfEditing)))
                return false;
        }
        return true;
    }

    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #F0C808; -fx-text-fill: #000000;");
    }

    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #03256C; -fx-text-fill: #FFFFFF;");
    }
}
