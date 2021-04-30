package edu.wpi.cs3733.D21.teamF.pathfinding;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.uicomponents.entities.DrawableNode;
import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;
import java.util.List;

public class NodeEntry  extends RecursiveTreeObject<NodeEntry> {
    private List<EdgeEntry> edges;
    private double x, y;
    private String floor;
    private String nodeID;
    private String xCoordinate;
    private String yCoordinate;
    private String building;
    private String nodeType;
    private String longName;
    private String shortName;
    private SimpleStringProperty nodeIDProperty;
    private SimpleStringProperty shortNameProperty;

    /**
     * Creates a new Vertex
     * @param ID the given ID String for the node
     * @author Tony Vuolo
     */
    public NodeEntry(String ID, int x, int y, String floor) {
        //this.ID = ID;
        this.nodeID = ID; //TODO HERE
        this.x = x;
        this.y = y;
        this.edges = new LinkedList<>();

        this.floor = floor;
    }

    // KD - Constructor for testing purposes
    public NodeEntry(String nodeID, String name) {
        this(nodeID,"", "", "" ,"", "",  "", name);
    }

    // KD - Empty Constructor
    public NodeEntry() {
        this("", "", "", "", "", "", "", "");
    }

    //ahf - constructor for all fields.
    public NodeEntry(String nodeID, String xCoordinate, String yCoordinate, String floor, String building, String nodeType, String longName, String shortName) {
        this.nodeID = nodeID;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
        nodeIDProperty = new SimpleStringProperty(this.nodeID);
        shortNameProperty = new SimpleStringProperty(this.shortName);
    }


    public String getFloor() {
        return floor;
    }

    /**
     * Adds an Edge to this Vertex
     * @param edge the additive Edge
     * @author Tony Vuolo
     */
    public void addEdge(EdgeEntry edge) {
        this.edges.add(edge);
    }

    /**
     * Gets the List of Edges incident to this Vertex
     * @return this.edges
     * @author Tony Vuolo
     */
    public List<EdgeEntry> getEdges() {
        return this.edges;
    }

    /**
     * Returns the coordinates of this Vertex
     * @return {this.x, this.y}
     * @author Tony Vuolo
     */
    public double[] getCoordinates() {
        return new double[]{this.x, this.y};
    }

    /**
     * Finds the other Vertex incident to a specific Edge
     * @param edge the target Edge
     * @return the opposite Vertex incident to this Edge
     * @author Tony Vuolo
     */
    public NodeEntry getNeighbor(EdgeEntry edge) {
        NodeEntry[] endpoints = edge.getVertices();
        return endpoints[equals(endpoints[1]) ? 0 : 1];
    }

    /**
     * Finds the Euclidean Distance between two Vertices.
     * @param vertex the comparator Vertex
     * @return the sum of the squared distances between the x- and y-coordinates of the two Vertices
     * @author Tony Vuolo
     */
    public double EuclideanDistance(NodeEntry vertex) {
        double[] coordinates = vertex.getCoordinates();
        return Math.sqrt(Math.pow(coordinates[0] - this.x, 2) + Math.pow(coordinates[1] - this.y, 2));
    }

    /**
     * Determines whether two vertices are equal
     * @param vertex the comparator Vertex
     * @return true if the Vertices share the same ID, else false
     */
    public boolean equals(NodeEntry vertex) {
        return vertex != null && this.nodeID.equals(vertex.getNodeID());
    }

    /**
     * Finds the heuristic estimate of the distance from this Vertex to another Vertex
     * @param vertex the endpoint goal Vertex
     * @return the heuristic weight of the "distance" from this Vertex to the endpoint Vertex
     * @author Tony Vuolo
     */
    public double heuristic(NodeEntry vertex) {
        return EuclideanDistance(vertex); //just the Euclidean distance for now
    }

    /**
     * Converts this Vertex to a printable format
     * @return this.ID
     */
    public String toString() {
        return this.nodeID;
    }


    /**
     * Used to get the x-coordinate of the vertex
     * @return the x-coordinate of this vertex.
     * @author Alex Friedman (ahf)
     */
    public double getX() {
        return x;
    }

    /**
     * Used to get the y-coordinate of the vertex
     * @return the y-coordinate of this vertex.
     * @author Alex Friedman (ahf)
     */
    public double getY() {
        return y;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
        this.nodeIDProperty.set(nodeID);
    }
    public void setXCoordinate(String xCoordinate) {
        this.xCoordinate = xCoordinate;
    }
    public void setYCoordinate(String yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
    public void setFloor(String floor) {
        this.floor = floor;
    }
    public void setBuilding(String building) {
        this.building = building;
    }
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }
    public void setLongName(String longName) {
        this.longName = longName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
        this.shortNameProperty.set(shortName);
    }
    public void setShortNameProperty(String shortNameProperty) {
        this.shortNameProperty.set(shortNameProperty);
        this.shortName = shortNameProperty;
    }
    public void setNodeIDProperty(String nodeIDProperty) {
        this.nodeIDProperty.set(nodeIDProperty);
        this.nodeID = nodeIDProperty;
    }

    public SimpleStringProperty getNodeIDProperty() {
        return nodeIDProperty;
    }
    public SimpleStringProperty getShortNameProperty() {
        return shortNameProperty;
    }

    public String getNodeID() {
        return nodeID;
    }
    public String getXCoordinate() {
        return xCoordinate;
    }

    public String getYCoordinate() {
        return yCoordinate;
    }
    public String getBuilding() {
        return building;
    }
    public String getLongName() {
        return longName;
    }
    public String getNodeType() {
        return nodeType;
    }
    public String getShortName() {
        return shortName;
    }


    public DrawableNode getDrawable() {
        return new DrawableNode(
                Integer.parseInt(xCoordinate),
                Integer.parseInt(yCoordinate),
                nodeID,
                floor,
                building,
                nodeType,
                longName,
                shortName
        );
    }
}
