package edu.wpi.fuchsiafalcons.entities;

import com.jfoenix.controls.JFXTreeTableView;
import edu.wpi.fuchsiafalcons.utils.UIConstants;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 * Entity that represents the state of the Node Map and handles changes to it as well as the graphical component
 * @author KD
 */
public class NodeMap {

    private JFXTreeTableView<NodeEntry> nodeTreeTable;
    private String floor;
    private Circle selectedCircle;
    private Image F1Image,F2Image,F3Image,L1Image,L2Image,GImage = null;
    private Pane canvas;
    private ComboBox<String> floorComboBox;
    private ObservableList<NodeEntry> nodeList;
    private ScrollPane scroll;
    private double zoomLevel;
    private ImageView map;

    public NodeMap(JFXTreeTableView<NodeEntry> nodeTreeTable, String floor, Circle selectedCircle, Pane canvas, ComboBox<String> floorComboBox, ObservableList<NodeEntry> nodeList, ScrollPane scroll, double zoomLevel, ImageView map) {
        this.nodeTreeTable = nodeTreeTable;
        this.floor = floor;
        this.selectedCircle = selectedCircle;
        this.canvas = canvas;
        this.floorComboBox = floorComboBox;
        this.nodeList = nodeList;
        this.scroll = scroll;
        this.zoomLevel = zoomLevel;
        this.map = map;
    }

    /**
     * Deletes the selected circle
     * @author KD
     */
    public void deleteCircle() {
        canvas.getChildren().remove(selectedCircle);
        selectedCircle = null;
        drawNodeOnFloor(); // added to handle deletion without selection - KD
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

    /**
     * Draw a single circle to represent the node
     * @author ZheCheng
     */
    public void drawCircle(double x, double y, String nodeID){
        Circle c = new Circle(x, y, UIConstants.NODE_RADIUS);
        c.setFill(UIConstants.NODE_COLOR);
        c.setId(nodeID);
        c.setOnMouseEntered(e->{if(!c.equals(selectedCircle))c.setFill(UIConstants.NODE_COLOR_HIGHLIGHT);});
        c.setOnMouseExited(e->{if(!c.equals(selectedCircle))c.setFill(UIConstants.NODE_COLOR);});
        c.setOnMouseClicked(e->{
            if(selectedCircle != null)
                selectedCircle.setFill(UIConstants.NODE_COLOR);
            selectedCircle = c;
            c.setFill(UIConstants.NODE_COLOR_SELECTED);
            nodeTreeTable.getSelectionModel().clearAndSelect(findNode(nodeID));
            nodeTreeTable.requestFocus();nodeTreeTable.scrollTo(findNode(nodeID));});

        this.canvas.getChildren().add(c);
    }

    /**
     * Find the index of a given node with nodeID in nodeList
     * @author ZheCheng
     */
    public int findNode(String nodeID){
        int index = 0;
        for(NodeEntry n: nodeList){
            if(n.getNodeID() == nodeID){
                break;
            }
            index++;
        }
        return index;
    }

    /**
     * Select node based on selection in Table, focus on the node
     * @author ZheCheng
     */
    public void selectNode() {
        if(nodeTreeTable.getSelectionModel().getSelectedIndex() < 0){
            // FIXME Error Handling
            return;
        }
        // FIXME: ADD TRY_CATCH
        NodeEntry node = nodeList.get(nodeTreeTable.getSelectionModel().getSelectedIndex());

        if(node == null){
            //FIXME Null Warning
            return;
        }

        // Check if need to switch map
        if(node.getFloor().equals(floor)){
            drawNodeOnFloor();
            if(selectedCircle != null)
                selectedCircle.setFill(UIConstants.NODE_COLOR);
        }else{
            floor = node.getFloor();
            switchMap();
        }
        Circle c = (Circle) canvas.lookup("#"+node.getNodeID());
        if(c == null){
            //FIXME Null Warning
            return;
        }
        selectedCircle = c;
        c.setFill(UIConstants.NODE_COLOR_SELECTED);
        centerNode(c);
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
     * Handle switching floor map and redraw the nodes in new floor
     * @author ZheCheng
     */
    public void switchMap(){
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

    public void setFloor(String floor) { this.floor = floor; }
    public void setNodeTreeTable(JFXTreeTableView<NodeEntry> nodeTreeTable) {
        this.nodeTreeTable = nodeTreeTable;
    }
    public void setSelectedCircle(Circle selectedCircle) {
        this.selectedCircle = selectedCircle;
    }
    public void setF1Image(Image f1Image) {
        F1Image = f1Image;
    }
    public void setF2Image(Image f2Image) {
        F2Image = f2Image;
    }
    public void setF3Image(Image f3Image) {
        F3Image = f3Image;
    }
    public void setL1Image(Image l1Image) {
        L1Image = l1Image;
    }
    public void setL2Image(Image l2Image) {
        L2Image = l2Image;
    }
    public void setGImage(Image GImage) {
        this.GImage = GImage;
    }
    public void setCanvas(Pane canvas) {
        this.canvas = canvas;
    }
    public void setFloorComboBox(ComboBox<String> floorComboBox) {
        this.floorComboBox = floorComboBox;
    }
    public void setNodeList(ObservableList<NodeEntry> nodeList) {
        this.nodeList = nodeList;
    }
    public void setScroll(ScrollPane scroll) {
        this.scroll = scroll;
    }
    public void setZoomLevel(double zoomLevel) {
        this.zoomLevel = zoomLevel;
    }
    public void setMap(ImageView map) {
        this.map = map;
    }
    public Pane getCanvas() {
        return canvas;
    }
    public JFXTreeTableView<NodeEntry> getNodeTreeTable() {
        return nodeTreeTable;
    }
    public String getFloor() {
        return floor;
    }
    public Circle getSelectedCircle() {
        return selectedCircle;
    }
    public Image getF1Image() {
        return F1Image;
    }
    public Image getF2Image() {
        return F2Image;
    }
    public Image getF3Image() {
        return F3Image;
    }
    public Image getL1Image() {
        return L1Image;
    }
    public Image getL2Image() {
        return L2Image;
    }
    public Image getGImage() {
        return GImage;
    }
    public ComboBox<String> getFloorComboBox() {
        return floorComboBox;
    }
    public ObservableList<NodeEntry> getNodeList() {
        return nodeList;
    }
    public ScrollPane getScroll() {
        return scroll;
    }
    public double getZoomLevel() {
        return zoomLevel;
    }
    public ImageView getMap() {
        return map;
    }
    public void decrementZoom() {zoomLevel--;}
    public void incrementZoom() {zoomLevel++;}
}
