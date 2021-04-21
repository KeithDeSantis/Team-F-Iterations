package edu.wpi.fuchsiafalcons.uicomponents;

import com.jfoenix.controls.JFXButton;
import edu.wpi.fuchsiafalcons.entities.NodeEntry;
import edu.wpi.fuchsiafalcons.utils.UIConstants;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.io.IOException;

/**
 * This class is a JavaFX Component that acts as our map viewer
 * @author Alex Friedman (ahf)
 */
public class MapPanel extends AnchorPane {

    @FXML private ScrollPane scroll;
    @FXML private ImageView map;
    @FXML private Pane canvas;

    @FXML private ComboBox<String> floorComboBox;

    @FXML private JFXButton zoomInButton;

    @FXML private JFXButton zoomOutButton;

    private ObservableList<NodeEntry> nodeList = FXCollections.observableArrayList();

    double zoomLevel = 5.0;
    private String floor = "1";

    private Image F1Image,F2Image,F3Image,L1Image,L2Image,GImage = null;

    private Circle selectedCircle = null;

    public MapPanel() {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/uicomponents/MapPanel.fxml"));
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
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        map.setPreserveRatio(true);
        F1Image = new Image(getClass().getResourceAsStream("/maps/01_thefirstfloor.png"));


        final double width = F1Image.getWidth()/zoomLevel;
        final double height = F1Image.getHeight()/zoomLevel;

        canvas.setPrefSize(width,height);

        map.setFitWidth(width);
        map.setFitHeight(height);
        map.setImage(F1Image); // Copied from A* Vis - KD


        // Set up floor comboBox and draw nodes on that floor
        final ObservableList<String> floorName = FXCollections.observableArrayList();
        floorName.addAll("1","2","3","L1","L2","G");
        floorComboBox.setItems(floorName);
        floorComboBox.setValue(floor);
        drawNodeOnFloor();
    }


    /**
     * Handle switching floor using combobox
     * @param actionEvent
     * @author ZheCheng
     */
    @FXML
    public void handleFloorBoxAction(ActionEvent actionEvent) {
        switchMap(floorComboBox.getValue().toString());
    }

    /**
     * Handle switching floor map and redraw the nodes in new floor
     * @author ZheCheng
     */
    public void switchMap(String floor){
        this.floor = floor;
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
        floorComboBox.setValue(floor);
        drawNodeOnFloor();
    }

    /**
     * Clear the canvas and draw nodes that are on current floor
     * @author ZheCheng
     */
    public void drawNodeOnFloor(){
        canvas.getChildren().removeIf(x -> {
            return x instanceof Circle;
        });
        selectedCircle = null;
        for(NodeEntry n : nodeList){
            if(n.getFloor().equals(floor)) {
                drawCircle(Double.parseDouble(n.getXcoord())/zoomLevel, Double.parseDouble(n.getYcoord())/zoomLevel, n.getNodeID());
            }
        }
    }

    public ImageView getMap() {
        return map;
    }

    public Circle getSelectedCircle() {
        return selectedCircle;
    }

    public void setSelectedCircle(Circle selectedCircle) {
        this.selectedCircle = selectedCircle;
    }

    public double getZoomLevel() {
        return zoomLevel;
    }

    public ObjectProperty<String> getFloor() {
        return floorComboBox.valueProperty();
    }

    public Pane getCanvas() {
        return canvas;
    }

    public ObservableList<NodeEntry> getNodeList() {
        return nodeList;
    }

    /**
     * Draw a single circle to represent the node
     * @author ZheCheng
     */
    private void drawCircle(double x, double y, String nodeID){
        Circle c = new Circle(x, y, UIConstants.NODE_RADIUS);
        c.setFill(UIConstants.NODE_COLOR);
        c.setId(nodeID);
        c.setOnMouseEntered(e->{if(!c.equals(selectedCircle))c.setFill(UIConstants.NODE_COLOR_HIGHLIGHT);});
        c.setOnMouseExited(e->{if(!c.equals(selectedCircle))c.setFill(UIConstants.NODE_COLOR);});
        /*
        c.setOnMouseClicked(e->{
            if(selectedCircle != null)
                selectedCircle.setFill(UIConstants.NODE_COLOR);
            selectedCircle = c;
            c.setFill(UIConstants.NODE_COLOR_SELECTED);
            nodeTreeTable.getSelectionModel().clearAndSelect(findNode(nodeID));
            nodeTreeTable.requestFocus();
            nodeTreeTable.scrollTo(findNode(nodeID));});


         */
        this.canvas.getChildren().add(c);
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
            if(zoomLevel > 1) {
                zoomLevel--;
            }
        } else if (btn == zoomOutButton) {
            if(zoomLevel < 8) {
                zoomLevel++;
            }
        }
        drawNodeOnFloor();
        Image image = map.getImage();
        double width = image.getWidth()/zoomLevel;
        double height = image.getHeight()/zoomLevel;
        canvas.setPrefSize(width,height);
        map.setFitWidth(width);
        map.setFitHeight(height);
        map.setImage(image);
    }

}
