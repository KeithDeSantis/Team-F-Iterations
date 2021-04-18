package edu.wpi.fuchsiafalcons.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.fuchsiafalcons.entities.EdgeEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for new edge or edit edge pop-up in edge editor
 * @author Karen Hou
 */
public class EditMapEdgeDialogueViewController {
    @FXML
    private JFXTextField startNode;

    @FXML
    private JFXTextField endNode;

    @FXML
    private Label edgeID;

    @FXML
    private JFXButton ok;

    private EdgeEntry edge = new EdgeEntry();
    private Stage dialogueStage;
    public boolean okClicked;

    public EditMapEdgeDialogueViewController(){}

    @FXML
    private void initialize(){
    }

    /**
     * Updated edge information with new information
     * @param e is the action event
     * @author Karen Hou
     */
    @FXML
    private void handleOKClicked(ActionEvent e){
        if(formFilled()){
            edge.setStartNode(startNode.getText());
            edge.setEndNode(endNode.getText());
            okClicked = true;
            dialogueStage.close();
        }
    }

    /**
     * Checks to see if all of the fields are filled in
     * @return true if all fields are filled in
     * @author Karen Hou
     */
    private boolean formFilled(){
        edgeID.setStyle("-fx-border-width: 0px");
        startNode.setStyle("-fx-border-width: 0px");
        endNode.setStyle("-fx-border-width: 0px");
/*
        //check each field for filled out
        if(edgeID.getText().length() <=0){
            edgeID.setStyle("-fx-border-widge: 2px");
            edgeID.setStyle("-fx-border-color: red");
        }

 */
        if(startNode.getText().length() <=0){
            startNode.setStyle("-fx-border-widge: 2px");
            startNode.setStyle("-fx-border-color: red");
        }
        if(endNode.getText().length() <=0){
            endNode.setStyle("-fx-border-widge: 2px");
            endNode.setStyle("-fx-border-color: red");
        }
        if(edgeID.getText().length() > 0 && startNode.getText().length() > 0 && endNode.getText().length() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Sets the edge values within the fields
     * @param enteredEdge
     * @author Karen Hou
     */
    public void setEdge(EdgeEntry enteredEdge){
        edge = enteredEdge;
        startNode.setText(edge.startNodeProperty().getValue());
        endNode.setText(edge.endNodeProperty().getValue());
        updateEdgeIDLabel();
    }

    /**
     * Sets the dialogue stage
     * @param theStage
     * @author Keith DeSantis
     */
    public void setDialogueStage(Stage theStage) {dialogueStage = theStage;}

    /**
     * Updates the label displaying a preview of the edge ID
     * Called on key release of either text field and when the dialogue is opened
     * @author Leo Morris
     */
    @FXML
    private void updateEdgeIDLabel(){
        edgeID.setText("Edge ID: " +startNode.getText() + "_" + endNode.getText());
    }
}
