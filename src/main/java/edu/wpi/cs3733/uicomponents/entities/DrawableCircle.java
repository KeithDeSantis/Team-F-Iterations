package edu.wpi.cs3733.uicomponents.entities;

import edu.wpi.cs3733.uicomponents.IMapDrawable;
import javafx.beans.property.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Please do not use this w/o talking to ahf
 */
public class DrawableCircle extends Circle implements IMapDrawable
{
    private final IntegerProperty xCoordinate;
    private final IntegerProperty yCoordinate;
    private final StringProperty floor;

    private final SimpleBooleanProperty shouldDisplay = new SimpleBooleanProperty(false); //FIXME: DO BETTER!;

    public DrawableCircle(int xCoordinate, int yCoordinate, String ID, String floor)
    {
        this.xCoordinate = new SimpleIntegerProperty(xCoordinate);
        this.yCoordinate = new SimpleIntegerProperty(yCoordinate);

        this.floor = new SimpleStringProperty(floor);

        this.setId(ID);

        this.setRadius(10);

        //DON'T SWITCH THIS TO DARKGREY. DARKGREY IS LIGHTER THAN GREY
        this.setFill(Color.GRAY.darker());// just for the meme : .brighter().grayscale());
    }

    @Override
    public void bindLocation(DoubleProperty zoomLevel) {
        this.centerXProperty().bind(xCoordinate);//.divide(zoomLevel));
        this.centerYProperty().bind(yCoordinate);//.divide(zoomLevel));
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

    public void setFloor(String floor) {
        this.floor.set(floor);
    }
}
