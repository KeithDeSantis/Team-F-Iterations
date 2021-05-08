package edu.wpi.cs3733.uicomponents.entities;

import edu.wpi.cs3733.uicomponents.IMapDrawable;
import javafx.beans.binding.Bindings;
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

    private final Circle user;
    private final Circle direction;

    private final DoubleProperty directionAngle;

    public DrawableUser(int xCoordinate, int yCoordinate, String ID, String floor)
    {
        this.xCoordinate = new SimpleIntegerProperty(xCoordinate);
        this.yCoordinate = new SimpleIntegerProperty(yCoordinate);

        this.directionAngle = new SimpleDoubleProperty(0);

        this.floor = new SimpleStringProperty(floor);

        this.shouldDisplay = new SimpleBooleanProperty(true); //FIXME: DO BETTER!

        this.setId(ID);

        user = new Circle();
        user.setRadius(10);

        user.setFill(Color.CYAN);

        this.getChildren().add(user);


        direction = new Circle();
        direction.setRadius(4);
        direction.setFill(Color.RED);

        this.getChildren().add(direction);
    }

    public DoubleProperty directionAngleProperty() {
        return directionAngle;
    }

    @Override
    public void bindLocation(DoubleProperty zoomLevel) {
        user.centerXProperty().bind(xCoordinate.divide(zoomLevel));
        user.centerYProperty().bind(yCoordinate.divide(zoomLevel));

        direction.centerXProperty().bind(xCoordinate.divide(zoomLevel).subtract(user.radiusProperty().multiply(
                Bindings.createDoubleBinding(() -> Math.cos(directionAngle.get()), directionAngle))
        ));
        direction.centerYProperty().bind(yCoordinate.divide(zoomLevel).subtract(user.radiusProperty().multiply(
                Bindings.createDoubleBinding(() -> Math.sin(directionAngle.get()), directionAngle))
        ));
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
