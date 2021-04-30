package edu.wpi.cs3733.uicomponents;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.StringConverter;

import java.io.IOException;

/**
 * This class is a JavaFX Component that acts as our map viewer
 * @author Alex Friedman (ahf)
 */
public class MapPanel extends AnchorPane {

    @FXML private ScrollPane scroll;
    @FXML private ImageView map;
    @FXML private Pane canvas;

    @FXML private JFXButton zoomInButton;

    @FXML private JFXButton zoomOutButton;

    @FXML
    private JFXSlider floorSlider;

    private DoubleProperty zoomLevel = new SimpleDoubleProperty(5.0);

    private final DoubleProperty INITIAL_WIDTH = new SimpleDoubleProperty();
    private final DoubleProperty INITIAL_HEIGHT = new SimpleDoubleProperty();


    private final StringProperty floor = new SimpleStringProperty("1");
    private final ObjectProperty<String> fp = new SimpleObjectProperty<>();

    private Image F1Image,F2Image,F3Image,L1Image,L2Image,GImage = null;

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
        // Set button fonts - LM
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        map.setPreserveRatio(true);
        F1Image = new Image(getClass().getResourceAsStream("/maps/01_thefirstfloor.png"));


        INITIAL_WIDTH.setValue(F1Image.getWidth());
        INITIAL_HEIGHT.setValue(F1Image.getHeight());

        canvas.prefWidthProperty().bind(INITIAL_WIDTH.divide(zoomLevel));
        canvas.prefHeightProperty().bind(INITIAL_HEIGHT.divide(zoomLevel));

        map.fitWidthProperty().bind(INITIAL_WIDTH.divide(zoomLevel));
        map.fitHeightProperty().bind(INITIAL_HEIGHT.divide(zoomLevel));
        map.setImage(F1Image); // Copied from A* Vis - KD


        // Set up floor comboBox and draw nodes on that floor
        final ObservableList<String> floorName = FXCollections.observableArrayList();
        floorName.addAll("1","2","3","L1","L2","G");




        floorSlider.setLabelFormatter(doubleStringConverter);

        final StringBinding binding =  Bindings.createStringBinding(() -> floorSlider.getLabelFormatter().toString(floorSlider.valueProperty().get()), floorSlider.valueProperty());


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

        this.floor.setValue(floor);
        switch(floor){
            case "1": if (F1Image == null)F1Image = new Image("/maps/01_thefirstfloor.png");
                map.setImage(F1Image); break;
            case "2": if (F2Image == null)F2Image = new Image("/maps/02_thesecondfloor.png");
                map.setImage(F2Image); break;
            case "3": if (F3Image == null)F3Image = new Image("/maps/03_thethirdfloor.png");
                map.setImage(F3Image); break;
            case "L1": if (L1Image == null)L1Image = new Image("/maps/00_thelowerlevel1.png");
                map.setImage(L1Image); break;
            case "L2": if (L2Image == null)L2Image = new Image("/maps/00_thelowerlevel2.png");
                map.setImage(L2Image); break;
            case "G": if (GImage == null)GImage = new Image("/maps/00_thegroundfloor.png");
                map.setImage(GImage); break;
            default: if (F1Image == null)F1Image = new Image("/maps/01_thefirstfloor.png");
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
     * Center the given node in scrollpane
     * @param c The node to be centered
     * @author ZheCheng
     */
    public void centerNode(Circle c){

        double h = scroll.getContent().getBoundsInLocal().getHeight();
        double y = (c.getBoundsInParent().getMaxY() +
                c.getBoundsInParent().getMinY()) / 2.0;
        double v = scroll.getViewportBounds().getHeight();
        scroll.setVvalue(scroll.getVmax() * ((y - 0.5 * v) / (h - v)));

        double w = scroll.getContent().getBoundsInLocal().getWidth();
        double x = (c.getBoundsInParent().getMaxX() +
                c.getBoundsInParent().getMinX()) / 2.0;
        double hw = scroll.getViewportBounds().getWidth();
        scroll.setHvalue(scroll.getHmax() * -((x - 0.5 * hw) / (hw - w)));

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
     * Center the given line in scrollpane
     * @param l The line to be centered
     * @author ZheCheng
     */
    public void centerNode(Line l){

        double h = scroll.getContent().getBoundsInLocal().getHeight();
        double y = (l.getBoundsInParent().getMaxY() +
                l.getBoundsInParent().getMinY()) / 2.0;
        double v = scroll.getViewportBounds().getHeight();
        scroll.setVvalue(scroll.getVmax() * ((y - 0.5 * v) / (h - v)));

        double w = scroll.getContent().getBoundsInLocal().getWidth();
        double x = (l.getBoundsInParent().getMaxX() +
                l.getBoundsInParent().getMinX()) / 2.0;
        double hw = scroll.getViewportBounds().getWidth();
        scroll.setHvalue(scroll.getHmax() * -((x - 0.5 * hw) / (hw - w)));
    }

    /**
     * Draw a single line to represent the edge
     * @author ZheCheng
     */
//    public Line drawLine(double startX, double startY, double endX, double endY, String edgeID){
//        Line l = new Line();
//        l.startXProperty().bind((new SimpleDoubleProperty(startX)).divide(zoomLevel));
//        l.startYProperty().bind((new SimpleDoubleProperty(startY)).divide(zoomLevel));
//
//        l.endXProperty().bind((new SimpleDoubleProperty(endX)).divide(zoomLevel));
//        l.endYProperty().bind((new SimpleDoubleProperty(endY)).divide(zoomLevel));
//
//        l.setStrokeWidth(UIConstants.LINE_STROKE_WIDTH);
//        l.setStroke(UIConstants.LINE_COLOR);
//        l.setId(edgeID);
//        this.canvas.getChildren().add(l);
//
//        return l;
//    }

    public <Element extends Node & IMapDrawable> Element draw(Element element)
    {
        element.bindLocation(zoomLevel);


        element.visibleProperty().bind(element.shouldDisplay().and(this.floor.isEqualTo(element.getFloor())));

        this.canvas.getChildren().add(element);
        return element;
    }

    public void unDraw(String ID)
    {
        canvas.getChildren().removeIf(x -> x.getId().equals(ID));
    }

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

    public void clearMap()
    {
        canvas.getChildren().removeIf(x -> x instanceof IMapDrawable);
    }

    // Added by LM



}
