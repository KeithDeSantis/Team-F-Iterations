package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.EdgeEntry;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamF.pathfinding.GraphLoader;
import edu.wpi.cs3733.D21.teamF.pathfinding.Vertex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for new edge or edit edge pop-up in edge editor
 * @author Karen Hou
 */
public class EditMapEdgeDialogueViewController {

    @FXML private JFXComboBox<String> startNode;
    @FXML private JFXComboBox<String> endNode;

    @FXML
    private Label edgeID;

    @FXML
    private JFXButton ok;

    private EdgeEntry edge = new EdgeEntry();
    private Stage dialogueStage;
    public boolean okClicked;
    private ObservableList<EdgeEntry> edgeList;
    private String currentIDIfEditing; //FIXME stupid way to fix making nodes with duplicate ID's, find better way

    public EditMapEdgeDialogueViewController(){}
    @FXML
    private void initialize(){
        // Load in fonts
        Graph graph = new Graph();
        try {
            List<NodeEntry> nodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries();
            List<EdgeEntry> edgeEntries = DatabaseAPI.getDatabaseAPI().genEdgeEntries();

            graph = GraphLoader.load(nodeEntries, edgeEntries);
        } catch (Exception e) {
            e.printStackTrace();
            //return;
        }
        final ObservableList<String> nodeList = FXCollections.observableArrayList();
        nodeList.addAll(graph.getVertices().stream().map(Vertex::getID)
                .sorted().collect(Collectors.toList()));
        this.startNode.setItems(nodeList);
        this.endNode.setItems(nodeList);
    }

    /**
     * Updated edge information with new information
     * @author Karen Hou
     */
    @FXML
    private void handleOKClicked(){
        if(formFilled()){
            edge.setStartNode(startNode.getValue());
            edge.setEndNode(endNode.getValue());

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

        if(startNode.getValue().length() <=0){
            startNode.setStyle("-fx-border-widge: 2px");
            startNode.setStyle("-fx-border-color: red");
            startNode.setStyle("-fx-background-color: #ff000088");
        }
        if(endNode.getValue().length() <=0){
            endNode.setStyle("-fx-border-widge: 2px");
            endNode.setStyle("-fx-border-color: red");
            endNode.setStyle("-fx-background-color: #ff000088");
        }
        return isUniqueNodeID(edgeID.getText()) &&
                edgeID.getText().length() > 0 &&
                startNode.getValue().length() > 0 &&
                endNode.getValue().length() > 0;
    }

    /**
     * Sets the edge values within the fields
     * @param enteredEdge
     * @author Karen Hou
     */
    public void setEdge(EdgeEntry enteredEdge){
        edge = enteredEdge;
        startNode.setValue(edge.startNodeProperty().getValue());
        endNode.setValue(edge.endNodeProperty().getValue());
        updateEdgeIDLabel();
    }

    /**
     * Sets the dialogue stage
     * @param theStage
     * @author Keith DeSantis
     */
    public void setDialogueStage(Stage theStage) {dialogueStage = theStage;}
    public void setEdgeList(ObservableList<EdgeEntry> edgeList) { this.edgeList = edgeList; }

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
        for(EdgeEntry n : edgeList) {
            if(n.getEdgeID().equals(nodeID) && !(n.getEdgeID().equals(currentIDIfEditing)))
                return false;
        }
        return true;
    }
 /**
     * Updates the label displaying a preview of the edge ID
     * Called on key release of either text field and when the dialogue is opened
     * @author Leo Morris
     */
    @FXML
    private void updateEdgeIDLabel(){
        startNode.setStyle("-fx-border-widge: 0px");
        startNode.setStyle("-fx-background-color: #00000000");
        endNode.setStyle("-fx-border-widge: 0px");
        endNode.setStyle("-fx-background-color: #00000000");
        edgeID.setText("Edge ID: " +startNode.getValue() + "_" + endNode.getValue());
    }

}
