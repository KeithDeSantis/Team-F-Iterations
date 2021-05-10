package edu.wpi.cs3733.uicomponents.entities;


import edu.wpi.cs3733.uicomponents.IMapDrawable;
import edu.wpi.cs3733.D21.teamF.utils.UIConstants;
import javafx.beans.property.*;
import javafx.scene.shape.*;

public class DrawableNode extends Path implements IMapDrawable
{
    private final IntegerProperty xCoordinate;
    private final IntegerProperty yCoordinate;
    private final StringProperty floor;
    private final StringProperty building;
    private final StringProperty nodeType;
    private final StringProperty longName;
    private final StringProperty shortName;

    private final SimpleBooleanProperty shouldDisplay;

    private final MoveTo move = new MoveTo(10, -20);

    public DrawableNode(int xCoordinate, int yCoordinate, String ID, String floor, String building, String nodeType, String longName, String shortName)
    {
        super();
       // move.setAbsolute(false);
        ArcTo arc = new ArcTo(1, 1, 0, -20, 0, false, false);
        arc.setAbsolute(false);
        CubicCurveTo l1 = new CubicCurveTo(0, 7, 3, 6, 10, 20);
        l1.setAbsolute(false);
        //(double controlX1, double controlY1, double controlX2, double controlY2, double x, double y)
        MoveTo m2 = new MoveTo(10, -20);
        m2.setAbsolute(false);
        CubicCurveTo l2 = new CubicCurveTo(0, 7, -4, 6, -10, 20);
        l2.setAbsolute(false);
        getElements().addAll(move, arc, l1, m2, l2);
        this.xCoordinate = new SimpleIntegerProperty(xCoordinate);
        this.yCoordinate = new SimpleIntegerProperty(yCoordinate);

        this.floor = new SimpleStringProperty(floor);
        this.building = new SimpleStringProperty(building);
        this.nodeType = new SimpleStringProperty(nodeType);
        this.longName = new SimpleStringProperty(longName);
        this.shortName = new SimpleStringProperty(shortName);

        this.shouldDisplay = new SimpleBooleanProperty(true); //FIXME: DO BETTER!

        this.setId(ID);


        this.setFill(UIConstants.NODE_COLOR);
        this.setOpacity(0.5);

    }

    @Override
    public void bindLocation(DoubleProperty zoomLevel) {
        this.move.xProperty().bind(xCoordinate.divide(zoomLevel).add(10));
        this.move.yProperty().bind(yCoordinate.divide(zoomLevel).subtract(20));
    }

    @Override
    public BooleanProperty shouldDisplay() {
        return shouldDisplay;
    }

    @Override
    public StringProperty getFloor() {
        return this.floor;
    }

    public IntegerProperty xCoordinateProperty() {
        return xCoordinate;
    }

    public IntegerProperty yCoordinateProperty() {
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
