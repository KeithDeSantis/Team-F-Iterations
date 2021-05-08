package edu.wpi.cs3733.uicomponents.entities;

import edu.wpi.cs3733.uicomponents.IMapDrawable;
import javafx.beans.property.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DrawableUser extends Group implements IMapDrawable
{
    private final IntegerProperty xCoordinate;
    private final IntegerProperty yCoordinate;
    private final StringProperty floor;

    private final SimpleBooleanProperty shouldDisplay;

    final Circle user;

    public DrawableUser(int xCoordinate, int yCoordinate, String ID, String floor)
    {
        this.xCoordinate = new SimpleIntegerProperty(xCoordinate);
        this.yCoordinate = new SimpleIntegerProperty(yCoordinate);

        this.floor = new SimpleStringProperty(floor);

        this.shouldDisplay = new SimpleBooleanProperty(true); //FIXME: DO BETTER!

        this.setId(ID);

        user = new Circle();
        user.setRadius(10);
        ///this.setRadius(10);

        //this.setFill(Color.CYAN);
        user.setFill(Color.CYAN);

        final Circle direction = new Circle();

        this.getChildren().add(user);
        //this.getCh
    }

    @Override
    public void bindLocation(DoubleProperty zoomLevel) {

        user.centerXProperty().bind(xCoordinate.divide(zoomLevel));
        user.centerYProperty().bind(yCoordinate.divide(zoomLevel));
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
