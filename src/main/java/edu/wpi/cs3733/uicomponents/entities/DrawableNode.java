package edu.wpi.cs3733.uicomponents.entities;


import edu.wpi.cs3733.uicomponents.IMapDrawable;
import edu.wpi.cs3733.D21.teamF.utils.UIConstants;
import javafx.beans.property.*;
import javafx.scene.shape.Circle;

public class DrawableNode extends Circle implements IMapDrawable
{
    private final SimpleIntegerProperty xCoordinate;
    private final SimpleIntegerProperty yCoordinate;
    private final StringProperty floor;
    private final StringProperty building;
    private final StringProperty nodeType;
    private final StringProperty longName;
    private final StringProperty shortName;

    private final SimpleBooleanProperty shouldDisplay;


    public DrawableNode(int xCoordinate, int yCoordinate, String ID, String floor, String building, String nodeType, String longName, String shortName)
    {
        this.xCoordinate = new SimpleIntegerProperty(xCoordinate);
        this.yCoordinate = new SimpleIntegerProperty(yCoordinate);

        this.floor = new SimpleStringProperty(floor);
        this.building = new SimpleStringProperty(building);
        this.nodeType = new SimpleStringProperty(nodeType);
        this.longName = new SimpleStringProperty(longName);
        this.shortName = new SimpleStringProperty(shortName);

        this.shouldDisplay = new SimpleBooleanProperty(true); //FIXME: DO BETTER!

        this.setId(ID);

        this.setRadius(UIConstants.NODE_RADIUS);

        this.setFill(UIConstants.NODE_COLOR);
    }

    @Override
    public void bindLocation(DoubleProperty zoomLevel) {
        this.centerXProperty().bind(xCoordinate.divide(zoomLevel));
        this.centerYProperty().bind(yCoordinate.divide(zoomLevel));
    }

    @Override
    public BooleanProperty shouldDisplay() {
        return shouldDisplay;
    }

    @Override
    public StringProperty getFloor() {
        return this.floor;
    }

    public SimpleIntegerProperty xCoordinateProperty() {
        return xCoordinate;
    }

    public SimpleIntegerProperty yCoordinateProperty() {
        return yCoordinate;
    }

    public void setShouldDisplay(boolean shouldDisplay) {
        this.shouldDisplay.set(shouldDisplay);
    }

    public void setFloor(String floor) {
        this.floor.set(floor);
    }


    public String getBuilding() {
        return building.get();
    }

    public String getNodeType() {
        return nodeType.get();
    }

    public String getLongName() {
        return longName.get();
    }

    public String getShortName() {
        return shortName.get();
    }

}
