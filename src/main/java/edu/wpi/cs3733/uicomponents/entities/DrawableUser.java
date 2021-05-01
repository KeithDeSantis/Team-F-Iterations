package edu.wpi.cs3733.uicomponents.entities;


import edu.wpi.cs3733.D21.teamF.utils.UIConstants;
import edu.wpi.cs3733.uicomponents.IMapDrawable;
import javafx.beans.property.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DrawableUser extends Circle implements IMapDrawable
{
    private final SimpleIntegerProperty xCoordinate;
    private final SimpleIntegerProperty yCoordinate;
    private StringProperty floor;

    private SimpleBooleanProperty shouldDisplay;

    public DrawableUser(int xCoordinate, int yCoordinate, String ID, String floor)
    {
        this.xCoordinate = new SimpleIntegerProperty(xCoordinate);
        this.yCoordinate = new SimpleIntegerProperty(yCoordinate);

        this.floor = new SimpleStringProperty(floor);

        this.shouldDisplay = new SimpleBooleanProperty(true); //FIXME: DO BETTER!

        this.setId(ID);

        this.setRadius(10);

        this.setFill(Color.CYAN);
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
}
