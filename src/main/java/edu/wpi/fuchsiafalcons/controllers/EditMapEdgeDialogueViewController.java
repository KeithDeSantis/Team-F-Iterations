package edu.wpi.fuchsiafalcons.controllers;

import edu.wpi.fuchsiafalcons.database.ConnectionHandler;
import edu.wpi.fuchsiafalcons.database.DatabaseAPI;
import edu.wpi.fuchsiafalcons.entities.EdgeEntry;
import edu.wpi.fuchsiafalcons.entities.NodeEntry;
import edu.wpi.fuchsiafalcons.pathfinding.Graph;
import edu.wpi.fuchsiafalcons.pathfinding.GraphLoader;
import edu.wpi.fuchsiafalcons.pathfinding.Vertex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for new edge or edit edge pop-up in edge editor
 * @author Karen Hou
 */
public class EditMapEdgeDialogueViewController {
    @FXML private TextField edgeID;
    @FXML private ComboBox<String> startNode;
    @FXML private ComboBox<String> endNode;

    private EdgeEntry edge = new EdgeEntry();
    private Stage dialogueStage;
    public boolean okClicked;

    public EditMapEdgeDialogueViewController(){}
    @FXML
    private void initialize(){
        Graph graph = new Graph();
        try {
            List<NodeEntry> nodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries(ConnectionHandler.getConnection());
            List<EdgeEntry> edgeEntries = DatabaseAPI.getDatabaseAPI().genEdgeEntries(ConnectionHandler.getConnection());

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
     * @param e is the action event
     * @author Karen Hou
     */
    @FXML
    private void handleOKClicked(ActionEvent e){
        if(formFilled()){
            String newID = edgeID.getText(), newStart = startNode.getValue(), newEnd = endNode.getValue();

            if((newID.contains(edge.getStartNode()) && !newID.contains(newStart))
                    || (newID.contains(edge.getEndNode()) && !newID.contains(newEnd))) {
                String updatedID = newStart + "_" + newEnd;
                ChoiceDialog<String> cd = new ChoiceDialog<>("YES", "NO");
                cd.setTitle("Improper edge name");
                cd.setContentText("The new ID of this edge may not properly reflect its location.\nWould you like " +
                        "to change the ID from " + newID + " to " + updatedID + "?");
                cd.setHeaderText("Edge ID Error");
                Optional<String> result = cd.showAndWait();
                if(result.isPresent() && result.get().equals("YES")) {
                    newID = updatedID;
                }
            }
            edge.setEdgeID(newID);
            edge.setStartNode(newStart);
            edge.setEndNode(newEnd);
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

        //check each field for filled out
        if(edgeID.getText().length() <=0){
            edgeID.setStyle("-fx-border-widge: 2px");
            edgeID.setStyle("-fx-border-color: red");
        }
        if(startNode.getValue().length() <=0){
            startNode.setStyle("-fx-border-widge: 2px");
            startNode.setStyle("-fx-border-color: red");
        }
        if(endNode.getValue().length() <=0){
            endNode.setStyle("-fx-border-widge: 2px");
            endNode.setStyle("-fx-border-color: red");
        }
        return edgeID.getText().length() > 0 && startNode.getValue().length() > 0 && endNode.getValue().length() > 0;
    }

    /**
     * Sets the edge values within the fields
     * @param enteredEdge
     * @author Karen Hou
     */
    public void setEdge(EdgeEntry enteredEdge){
        edge = enteredEdge;
        edgeID.setText(edge.edgeIDProperty().getValue());
        startNode.setValue(edge.startNodeProperty().getValue());
        endNode.setValue(edge.endNodeProperty().getValue());
    }

    /**
     * Sets the dialogue stage
     * @param theStage
     * @author Keith DeSantis
     */
    public void setDialogueStage(Stage theStage) {dialogueStage = theStage;}
}
