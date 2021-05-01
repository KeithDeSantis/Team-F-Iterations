package edu.wpi.cs3733.D21.teamF.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.uicomponents.entities.DrawableNode;
import javafx.beans.property.SimpleStringProperty;

/**
 * Class that represents a node, uses SimpleStringProperties so that it can be used in a TableView
 * @author KD
 */
public class NodeEntry extends RecursiveTreeObject<NodeEntry> {

    private String nodeID;
    private String xCoordinate;
    private String yCoordinate;
    private String floor;
    private String building;
    private String nodeType;
    private String longName;
    private String shortName;
    private final SimpleStringProperty nodeIDProperty;
    private final SimpleStringProperty shortNameProperty;



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
    public String getFloor() {
        return floor;
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
