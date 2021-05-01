package edu.wpi.cs3733.uicomponents.entities;

import edu.wpi.cs3733.uicomponents.IMapDrawable;
import javafx.beans.property.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DrawableRectSelection extends Rectangle implements IMapDrawable
{
    private final IntegerProperty x0Coordinate;
    private final IntegerProperty y0Coordinate;
    private final IntegerProperty x1Coordinate;
    private final IntegerProperty y1Coordinate;

    private final StringProperty floor;

    private final SimpleBooleanProperty shouldDisplay = new SimpleBooleanProperty(true); //FIXME: DO BETTER!

    public DrawableRectSelection(int xCoordinate, int yCoordinate, String floor)
    {
        this.x0Coordinate = new SimpleIntegerProperty(xCoordinate);
        this.y0Coordinate = new SimpleIntegerProperty(yCoordinate);
        this.x1Coordinate = new SimpleIntegerProperty(xCoordinate);
        this.y1Coordinate = new SimpleIntegerProperty(yCoordinate);

        this.floor = new SimpleStringProperty(floor);

        this.setId("regionSelector");

        //transparent fill
        this.setFill(new Color(0, 0, 0, 0));

        //dotted blue outline
        this.getStrokeDashArray().addAll(2.0);
        this.setStroke(Color.BLUE.brighter().brighter());
    }



    public IntegerProperty x0CoordinateProperty() {
        return x0Coordinate;
    }

    public IntegerProperty y0CoordinateProperty() {
        return y0Coordinate;
    }

    public void setFloor(String floor) {
        this.floor.set(floor);
    }



    @Override
    public void bindLocation(DoubleProperty zoomLevel) {
        this.xProperty().bind(x0Coordinate.divide(zoomLevel));
        this.yProperty().bind(y0Coordinate.divide(zoomLevel));

        this.widthProperty().bind((x1Coordinate.subtract(x0Coordinate)).divide(zoomLevel));
        this.heightProperty().bind((y1Coordinate.subtract(y0Coordinate)).divide(zoomLevel));
    }

    @Override
    public BooleanProperty shouldDisplay() {
        return shouldDisplay;
    }

    @Override
    public StringProperty getFloor() {
        return this.floor;
    }
}
