package edu.wpi.cs3733.D21.teamF.pathfinding;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EdgeEntry extends RecursiveTreeObject<EdgeEntry> {
    private NodeEntry a, b;
    private double weight;
    private StringProperty edgeID;
    private StringProperty startNode;
    private StringProperty endNode;

    /**
     * Creates a new Edge
     * @param a one endpoint Vertex
     * @param b the other endpoint Vertex
     * @author Tony Vuolo
     */
    public EdgeEntry(NodeEntry a, NodeEntry b) {
        this.a = a;
        this.b = b;
        this.weight = a.EuclideanDistance(b);
        //TODO: change heuristic function?

        //Add the edge to each vertex.
        a.addEdge(this);
        b.addEdge(this);
    }

    /**
     * Gets the Vertices this Edge spans
     * @return {this.a, this.b}
     * @author Tony Vuolo
     */
    public NodeEntry[] getVertices() {
        return new NodeEntry[]{this.a, this.b};
    }

    /**
     * Gets the weight of this Edge
     * @return this.weight
     * @author Tony Vuolo
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * Checks if a Vertex is an endpoint of this Edge
     * @param vertex the comparator Vertex
     * @return true if the Vertex is this.a or this.b, else false
     * @author Tony Vuolo
     */
    public boolean isEndpoint(NodeEntry vertex) {
        return this.a.getID().equals(vertex.getID()) || this.b.getID().equals(vertex.getID());
    }

    /**
     * Determines if two Edges are equal
     * @param edge the comparator Edge
     * @return true if this Edge and the comparator Edge share the same endpoints
     * @author Tony Vuolo
     */
    public boolean equals(EdgeEntry edge) {
        if(edge != null) {
            boolean checkEnds = (this.a.getID().equals(edge.a.getID())) && (this.b.getID().equals(edge.b.getID()));
            boolean checkEndsReverse = (this.a.getID().equals(edge.b.getID())) && (this.b.getID().equals(edge.a.getID()));
            return checkEnds || checkEndsReverse;
        }
        return false;
    }
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

    @Override
    public String toString() {
        return "EdgeEntry{" +
                "edgeID=" + edgeID +
                ", startNode=" + startNode +
                ", endNode=" + endNode +
                '}';
    }

}
