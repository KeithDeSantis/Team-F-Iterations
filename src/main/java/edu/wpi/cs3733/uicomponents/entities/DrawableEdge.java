package edu.wpi.cs3733.uicomponents.entities;

import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.uicomponents.IMapDrawable;
import edu.wpi.cs3733.D21.teamF.utils.UIConstants;
import javafx.beans.property.*;
import javafx.scene.shape.Line;

public class DrawableEdge extends Line implements IMapDrawable
{
    private final SimpleIntegerProperty startX;
    private final SimpleIntegerProperty startY;
    private final SimpleIntegerProperty endX;
    private final SimpleIntegerProperty endY;

    private final StringProperty startFloor;
    private final StringProperty endFloor;

    private final SimpleBooleanProperty shouldDisplay;

    private final NodeEntry startNode;
    private final NodeEntry endNode;


    public DrawableEdge(int startX, int startY, int endX, int endY, String ID, String startFloor, String endFloor, NodeEntry startNode, NodeEntry endNode)
    {
        this.startX = new SimpleIntegerProperty(startX);
        this.startY = new SimpleIntegerProperty(startY);

        this.endX = new SimpleIntegerProperty(endX);
        this.endY = new SimpleIntegerProperty(endY);

        this.startFloor = new SimpleStringProperty(startFloor);
        this.endFloor = new SimpleStringProperty(endFloor);

        this.shouldDisplay = new SimpleBooleanProperty(true); //FIXME: DO BETTER!

        this.setStrokeWidth(UIConstants.LINE_STROKE_WIDTH);
        this.setStroke(UIConstants.LINE_COLOR);
        this.setId(ID);

        this.startNode = startNode;
        this.endNode = endNode;
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
        return this.startFloor; //FIXME: DO BETTER!!!!
    }
    public StringProperty getEndFloor() {
        return this.endFloor; //FIXME: DO BETTER!!!!
    }



    public SimpleIntegerProperty getMapStartX() { return this.startX; }
    public SimpleIntegerProperty getMapStartY() { return this.startY; }

    public SimpleIntegerProperty getMapEndX() { return this.endX; }
    public SimpleIntegerProperty getMapEndY() { return this.endY; }

    public void setShouldDisplay(boolean shouldDisplay) {
        this.shouldDisplay.set(shouldDisplay);
    }

    public NodeEntry getStartNode() {
        return startNode;
    }

    public NodeEntry getEndNode() {
        return endNode;
    }
}
