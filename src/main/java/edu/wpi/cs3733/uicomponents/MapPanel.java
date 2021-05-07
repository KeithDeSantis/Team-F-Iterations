package edu.wpi.cs3733.uicomponents;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import java.io.IOException;

/**
 * This class is a JavaFX Component that acts as our map viewer
 * @author Alex Friedman (ahf)
 */
public class MapPanel extends AnchorPane {

    @FXML
    public ScrollPane scroll;

    @FXML
    public ImageView map;

    @FXML
    public Pane canvas;

    @FXML
    public JFXButton zoomInButton;

    @FXML
    public JFXButton zoomOutButton;

    @FXML
    public StackPane stackPane;

    @FXML
    public JFXSlider floorSlider;

    private final DoubleProperty zoomLevel = new SimpleDoubleProperty(5.0);

    private final DoubleProperty INITIAL_WIDTH = new SimpleDoubleProperty();
    private final DoubleProperty INITIAL_HEIGHT = new SimpleDoubleProperty();


    private final StringProperty floor = new SimpleStringProperty("1");
    private final ObjectProperty<String> fp = new SimpleObjectProperty<>();


    private final BooleanProperty navigating = new SimpleBooleanProperty(false);

    //FIXME: DO BETTER!
    private final Image F1Image = new Image(getClass().getResourceAsStream("/maps/01_thefirstfloor.png"));
    private final Image F2Image = new Image(getClass().getResourceAsStream("/maps/02_thesecondfloor.png"));
    private final Image F3Image = new Image(getClass().getResourceAsStream("/maps/03_thethirdfloor.png"));
    private final Image L1Image = new Image(getClass().getResourceAsStream("/maps/00_thelowerlevel1.png"));
    private final Image L2Image = new Image(getClass().getResourceAsStream("/maps/00_thelowerlevel2.png"));
    private final Image GImage = new Image(getClass().getResourceAsStream("/maps/00_thegroundfloor.png"));

    final StringConverter<Double> doubleStringConverter = new StringConverter<Double>() {
        @Override
        public String toString(Double d) {
            final int cutValue = d.intValue();

            switch (cutValue) {
                case 0:
                    return "L2";
                case 1:
                    return "L1";
                case 2:
                    return "G";
                case 3:
                    return "1";
                case 4:
                    return "2";
                case 5:
                    return "3";
                default:
                    return "N/A";
            }
        }

        @Override
        public Double fromString(String s) {
            switch (s) {
                case "L2":
                    return 0.0;
                case "L1":
                    return 1.0;
                case "G":
                    return 2.0;
                case "1":
                    return 3.0;
                case "2":
                    return 4.0;
                case "3":
                    return 5.0;
                default:
                    return -1.0;

            }
        }
    };

    final StringConverter<Number> doublePropertyStringConverter = new StringConverter<Number>() {
        @Override
        public String toString(Number object) {
            return doubleStringConverter.toString(object.doubleValue());
        }

        @Override
        public Number fromString(String string) {
            return doubleStringConverter.fromString(string).intValue();
        }
    };

    public MapPanel() {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/uicomponents/MapPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    @FXML
    public void initialize(){
        zoomInButton.setOnAction(this::handleZoom);
        zoomOutButton.setOnAction(this::handleZoom);

        // Set button fonts - LM
        scroll.hbarPolicyProperty().bind(Bindings.when(navigating)
                .then(ScrollPane.ScrollBarPolicy.NEVER).otherwise(ScrollPane.ScrollBarPolicy.AS_NEEDED));
        scroll.vbarPolicyProperty().bind(Bindings.when(navigating)
                .then(ScrollPane.ScrollBarPolicy.NEVER).otherwise(ScrollPane.ScrollBarPolicy.AS_NEEDED));
        floorSlider.disableProperty().bind(Bindings.when(navigating).then(true).otherwise(false));

        map.setPreserveRatio(true);


        INITIAL_WIDTH.setValue(F1Image.getWidth());
        INITIAL_HEIGHT.setValue(F1Image.getHeight());


        stackPane.prefWidthProperty().bind(this.widthProperty());
        stackPane.prefHeightProperty().bind(this.heightProperty());


        canvas.prefWidthProperty().bind(INITIAL_WIDTH.divide(zoomLevel));
        canvas.prefHeightProperty().bind(INITIAL_HEIGHT.divide(zoomLevel));

        map.fitWidthProperty().bind(INITIAL_WIDTH.divide(zoomLevel));
        map.fitHeightProperty().bind(INITIAL_HEIGHT.divide(zoomLevel));
        map.setImage(F1Image); // Copied from A* Vis - KD

        floorSlider.setLabelFormatter(doubleStringConverter);

       // final StringBinding binding =  Bindings.createStringBinding(() -> floorSlider.getLabelFormatter().toString(floorSlider.valueProperty().get()), floorSlider.valueProperty());


        Bindings.bindBidirectional(this.floor, floorSlider.valueProperty(), doublePropertyStringConverter);

        this.floorSlider.valueProperty().addListener(e -> switchMap(this.doubleStringConverter.toString(this.floorSlider.valueProperty().get())));

        fp.bind(this.floor);
    }


    /**
     * Handle switching floor map and redraw the nodes in new floor
     * @author ZheCheng
     */
    public void switchMap(String floor){

        if(floor.equals(this.floor.get()))
            return;

        final IMapDrawable regionSelector = getNode("regionSelector");
        if(regionSelector != null) //FIXME: DO BETTER
            regionSelector.shouldDisplay().set(false);

        this.floor.setValue(floor);
        switch(floor){
            case "1":
                map.setImage(F1Image); break;
            case "2":
                map.setImage(F2Image); break;
            case "3":
                map.setImage(F3Image); break;
            case "L1":
                map.setImage(L1Image); break;
            case "L2":
                map.setImage(L2Image); break;
            case "G":
                map.setImage(GImage); break;
            default:
                map.setImage(F1Image); System.out.println("No Such Floor!"); break; //FIXME : Error Handling
        }
        //drawNodeOnFloor();
    }



    public ImageView getMap() {
        return map;
    }


    public DoubleProperty getZoomLevel() {
        return zoomLevel;
    }

    public ObjectProperty<String> getFloor() {
        return this.fp;
    }

    public Pane getCanvas() {
        return canvas;
    }


    /**
     * Basic implementation of Zooming the map by changing the zoom level and reloading
     * @param actionEvent the press of zoom in or zoom out
     * @author KD
     */
    public void handleZoom(ActionEvent actionEvent) { //TODO Fix Centering so centering node works when zoom level is changed
        JFXButton btn = (JFXButton) actionEvent.getSource();
        if(btn == zoomInButton) {
            if(zoomLevel.get() > 1) {
                zoomLevel.setValue(zoomLevel.get()  - 1);
            }
        } else if (btn == zoomOutButton) {
            if(zoomLevel.get() < 8) {
                zoomLevel.setValue(zoomLevel.get() + 1);
            }
        }

        Image image = map.getImage();
        map.setImage(image);
    }

    /**
     * Center the given node in the map
     * @param node The node to be centered
     * @author ZheCheng
     */
    public void centerNode(Node node){

        double h = scroll.getContent().getBoundsInLocal().getHeight();
        double y = (node.getBoundsInParent().getMaxY() +
                node.getBoundsInParent().getMinY()) / 2.0;
        double v = scroll.getViewportBounds().getHeight();
        scroll.setVvalue(scroll.getVmax() * ((y - 0.5 * v) / (h - v)));

        double w = scroll.getContent().getBoundsInLocal().getWidth();
        double x = (node.getBoundsInParent().getMaxX() +
                node.getBoundsInParent().getMinX()) / 2.0;
        double hw = scroll.getViewportBounds().getWidth();
        scroll.setHvalue(scroll.getHmax() * -((x - 0.5 * hw) / (hw - w)));
    }

    /**
     * Used to add an element to the map so that it can be drawn.
     * @param element The element to be drawn
     * @param <Element> An object type that extends Node and implements IMapDrawable
     * @return The element to be drawn
     * @author Alex Friedman (ahf)
     */
    public <Element extends Node & IMapDrawable> Element draw(Element element)
    {
        element.bindLocation(zoomLevel);

        element.visibleProperty().bind(element.shouldDisplay().and(this.floor.isEqualTo(element.getFloor())));

        this.canvas.getChildren().add(element);
        return element;
    }

    /**
     * Given an ID, removes the given node from the map.
     * @param ID The id of the node to be removed.
     * @author Alex Friedman (ahf)
     */
    public void unDraw(String ID)
    {
        canvas.getChildren().removeIf(x -> x.getId().equals(ID));
    }


    /**
     * Given an ID, finds and returns that node.
     * @param ID The id of the node.
     * @param <Element> An object type that extends Node and implements IMapDrawable
     * @return The element with the corresponding ID.
     * @author Alex Friedman (ahf)
     */
    public <Element extends Node & IMapDrawable> Element getNode(String ID)
    {
        for (Node x : canvas.getChildren())
        {
            if (x.getId().equals(ID)) {
                return (Element) x;
            }
        }
        return null;
    }

    /**
     * Removes all IMapDrawables from the map
     * @author Alex Friedman (ahf)
     */
    public void clearMap()
    {
        canvas.getChildren().removeIf(x -> x instanceof IMapDrawable);
    }


    public void showDialog(JFXDialog dialog)
    {
        dialog.show(stackPane);
    }

    EventHandler<ScrollEvent> consume = event -> event.consume();

    public void disableInteract(){
        navigating.setValue(true);
        scroll.addEventFilter(ScrollEvent.ANY, consume);
    }

    public void enableInteract() {
        navigating.setValue(false);
        scroll.removeEventFilter(ScrollEvent.ANY, consume);
    }
}
