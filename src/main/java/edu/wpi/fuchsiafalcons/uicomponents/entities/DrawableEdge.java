package edu.wpi.fuchsiafalcons.uicomponents.entities;

import edu.wpi.fuchsiafalcons.uicomponents.IMapDrawable;
import edu.wpi.fuchsiafalcons.utils.UIConstants;
import javafx.beans.property.*;
import javafx.scene.shape.Line;

public class DrawableEdge extends Line implements IMapDrawable
{
    private final SimpleIntegerProperty startX;
    private final SimpleIntegerProperty startY;
    private final SimpleIntegerProperty endX;
    private final SimpleIntegerProperty endY;

    private StringProperty floor;

    private SimpleBooleanProperty shouldDisplay;


    public DrawableEdge(int startX, int startY, int endX, int endY, String ID, String floor)
    {
        this.startX = new SimpleIntegerProperty(startX);
        this.startY = new SimpleIntegerProperty(startY);

        this.endX = new SimpleIntegerProperty(endX);
        this.endY = new SimpleIntegerProperty(endY);

        this.floor = new SimpleStringProperty(floor);

        this.shouldDisplay = new SimpleBooleanProperty(true); //FIXME: DO BETTER!

        this.setStrokeWidth(UIConstants.LINE_STROKE_WIDTH);
        this.setStroke(UIConstants.LINE_COLOR);
        this.setId(ID);
    }

    @Override
    public void bindLocation(DoubleProperty zoomLevel) {
        this.startXProperty().bind(startX.divide(zoomLevel));
        this.startYProperty().bind(startY.divide(zoomLevel));

        this.endXProperty().bind(endX.divide(zoomLevel));
        this.endYProperty().bind(endY.divide(zoomLevel));
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
