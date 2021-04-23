package edu.wpi.fuchsiafalcons.uicomponents.entities;

import edu.wpi.fuchsiafalcons.uicomponents.IMapDrawable;
import javafx.beans.property.*;
import javafx.scene.shape.Circle;

public class DrawableNode extends Circle implements IMapDrawable
{
    private final SimpleIntegerProperty xCoordinate;
    private final SimpleIntegerProperty yCoordinate;
    private StringProperty floor;

    private SimpleBooleanProperty shouldDisplay;

    public DrawableNode(int xCoordinate, int yCoordinate, String floor)
    {
        this.xCoordinate = new SimpleIntegerProperty(xCoordinate);
        this.yCoordinate = new SimpleIntegerProperty(yCoordinate);

        this.floor = new SimpleStringProperty(floor);

        this.shouldDisplay = new SimpleBooleanProperty(true); //FIXME: DO BETTER!
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
}
