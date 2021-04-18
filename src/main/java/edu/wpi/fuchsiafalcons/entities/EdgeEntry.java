package edu.wpi.fuchsiafalcons.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Entity Class to store basic info for edges
 * @author Leo Morris
 */
public class EdgeEntry extends RecursiveTreeObject<EdgeEntry> {

    private final StringProperty edgeID;
    private final StringProperty startNode;
    private final StringProperty endNode;

    /**
     * Edge entity constructor, edgeID is created by combining the start and end node IDs
     * @param startingNode starting node ID
     * @param endingNode ending node ID
     * @author Leo Morris
     */
    public EdgeEntry(String edgeID, String startingNode, String endingNode){
        this.startNode = new SimpleStringProperty(startingNode);
        this.endNode = new SimpleStringProperty(endingNode);
        this.edgeID = new SimpleStringProperty(edgeID);
    }

    /**
     * Empty edge entity constructor
     * @author Karen Hou
     */
    public EdgeEntry(){
        startNode = new SimpleStringProperty("");
        endNode = new SimpleStringProperty("");
        edgeID = new SimpleStringProperty("");
    }


    public StringProperty edgeIDProperty() {return edgeID;}
    public StringProperty startNodeProperty() {return startNode;}
    public StringProperty endNodeProperty() {return endNode;}


    /**
     * getter for edgeID
     *
     */
    public String getEdgeID() {
        return edgeID.get();
    }

    /**
     * getter for startNode
     *
     */
    public String getStartNode() {
        return startNode.get();
    }


    /**
     * getter for endNode
     * @return
     */
    public String getEndNode() {
        return endNode.get();
    }

    /**
     * Setter to input a new edge ID, will update the starting and end nodes for consistency
     * @param newEdgeID New edge ID formatted as [NODEID]_[NODEID]
     * @author Leo Morris
     */
    public void setEdgeID(String newEdgeID){
        edgeID.setValue(newEdgeID);
        startNode.setValue(newEdgeID.split("_")[0]);
        endNode.setValue(newEdgeID.split("_")[1]);
    }

    /**
     * Setter to update the start node ID, will update the edge ID
     * @param newStartNode The ID for the new starting node
     * @author Leo Morris
     */
    public void setStartNode(String newStartNode) {
        startNode.setValue(newStartNode);
        edgeID.setValue(newStartNode + "_" + endNode.getValue());
    }

    /**
     * Setter to update the end node ID, will update the edge ID
     * @param newEndNode The ID for the new end node
     * @author Leo Morris
     */
    public void setEndNode(String newEndNode) {
        endNode.setValue(newEndNode);
        edgeID.setValue(startNode.getValue() + "_" + newEndNode);
    }
}
