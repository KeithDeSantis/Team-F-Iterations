package edu.wpi.cs3733.uicomponents.entities;

import edu.wpi.cs3733.uicomponents.IMapDrawable;
import javafx.animation.*;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class DrawableFloorInstruction extends ImageView implements IMapDrawable
{
    private final IntegerProperty xCoordinate;
    private final IntegerProperty yCoordinate;
    private final StringProperty floor;

    private final SimpleBooleanProperty shouldDisplay;

    public DrawableFloorInstruction(String Ins, boolean isUp, int xCoordinate, int yCoordinate, String ID, String floor)
    {
        String imgLoc;

        if (Ins.contains("elevator")) {
            imgLoc = (isUp) ? "/imagesAndLogos/navIcons/takeElevatorYellow.png" : "/imagesAndLogos/navIcons/takeElevatorYellow.png";
        }
        else if(Ins.contains("stair")){
           imgLoc = (isUp) ? "/imagesAndLogos/navIcons/goUpStairsYellow.png" : "/imagesAndLogos/navIcons/goDownStairsYellow.png";
        }
        else
        {
            throw new Error("Unknown instruction type: " + Ins);
        }
        setImage(new Image(getClass().getResourceAsStream(imgLoc)));

        this.xCoordinate = new SimpleIntegerProperty(xCoordinate);
        this.yCoordinate = new SimpleIntegerProperty(yCoordinate);


        setFitWidth(20);
        setFitHeight(20);
        setId(ID);
        setOnMouseEntered(event -> {
            setFitWidth(30);
            setFitHeight(30);
        });

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(fitWidthProperty(),20, Interpolator.EASE_BOTH),
                        new KeyValue(fitHeightProperty(), 20, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(fitWidthProperty(),27, Interpolator.EASE_BOTH),
                        new KeyValue(fitHeightProperty(), 27, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(fitWidthProperty(),20, Interpolator.EASE_BOTH),
                        new KeyValue(fitHeightProperty(), 20, Interpolator.EASE_BOTH))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        setOnMouseExited(event -> {
            setFitWidth(20);
            setFitHeight(20);
        });



        toFront();


        this.floor = new SimpleStringProperty(floor);

        this.shouldDisplay = new SimpleBooleanProperty(true); //FIXME: DO BETTER!

        this.setId(ID);

    }

    @Override
    public void bindLocation(DoubleProperty zoomLevel) {
        this.xProperty().bind(xCoordinate.divide(zoomLevel).subtract(fitWidthProperty().divide(2)));
        this.yProperty().bind(yCoordinate.divide(zoomLevel).subtract(fitHeightProperty().divide(2)));
    }

    @Override
    public BooleanProperty shouldDisplay() {
        return shouldDisplay;
    }

    @Override
    public StringProperty getFloor() {
        return this.floor;
    }

    public void setFloor(String floor) {
        this.floor.set(floor);
    }
}
