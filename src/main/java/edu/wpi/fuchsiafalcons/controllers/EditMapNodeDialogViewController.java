package edu.wpi.fuchsiafalcons.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.fuchsiafalcons.entities.NodeEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
    @FXML private JFXButton backButton;
    private NodeEntry node = new NodeEntry(); // This will be the node that we pass in to edit - KD
    private Stage dialogStage; // This will be so we have access to our stage - KD
    public boolean okClicked; // Used to tell if edit was went through with - KD


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

        if(nodeIDField.getText().length() <= 0) {
            nodeIDField.setStyle("-fx-background-color:  #ffbab8");
        }
        if(!isProperFloor(floorField.getText())) {
            floorField.setStyle("-fx-background-color:  #ffbab8");
        }
        if(buildingField.getText().length() <= 0) {
            buildingField.setStyle("-fx-background-color:  #ffbab8");
        }
        if(nodeTypeField.getText().length() <= 0) {
            nodeTypeField.setStyle("-fx-background-color:  #ffbab8");
        }
        if(longNameField.getText().length() <= 0) {
            longNameField.setStyle("-fx-background-color:  #ffbab8");
        }
        if(shortNameField.getText().length() <= 0) {
            shortNameField.setStyle("-fx-background-color:  #ffbab8");
        }
        try {
            Integer.parseInt(xCoordField.getText()); }
        catch(NumberFormatException e) {
            xCoordField.setStyle("-fx-background-color:  #ffbab8");
            xCoordValid = false;
        }
        try {
            Integer.parseInt(yCoordField.getText());
        } catch (NumberFormatException e){
            yCoordField.setStyle("-fx-background-color: #ffbab8");
            yCoordValid = false;
        }

        return nodeIDField.getText().length() > 0 &&
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


    public void handleBackClicked() {
         Stage helpStage = (Stage) backButton.getScene().getWindow();
         helpStage.close();
    }

    public void openHelpMenu() throws IOException {
        FXMLLoader dialogLoader = new FXMLLoader();
        dialogLoader.setLocation(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/NodeEditHelpView.fxml")); // load in Edit Dialog - KD
        Stage helpStage = new Stage();
        Parent root = dialogLoader.load();
        EditMapNodeDialogViewController dialogController = dialogLoader.getController(); // get edit dialog's controller - KD
        helpStage.setTitle("Edit Node");
        helpStage.initModality(Modality.WINDOW_MODAL); // make window a pop up - KD
        helpStage.initOwner(dialogStage);
        helpStage.setScene(new Scene(root)); // set scene - KD

        helpStage.showAndWait(); // open pop up - KD
    }
}
