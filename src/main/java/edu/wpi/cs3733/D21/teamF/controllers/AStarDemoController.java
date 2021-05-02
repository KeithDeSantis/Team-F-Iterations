package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.EdgeEntry;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.pathfinding.*;
import edu.wpi.cs3733.D21.teamF.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamF.pathfinding.GraphLoader;
import edu.wpi.cs3733.D21.teamF.pathfinding.Path;
import edu.wpi.cs3733.D21.teamF.pathfinding.Vertex;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import edu.wpi.cs3733.D21.teamF.utils.UIConstants;
import edu.wpi.cs3733.uicomponents.MapPanel;
import edu.wpi.cs3733.uicomponents.entities.DrawableEdge;
import edu.wpi.cs3733.uicomponents.entities.DrawableNode;
import edu.wpi.cs3733.uicomponents.entities.DrawableUser;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AStarDemoController implements Initializable {

    @FXML
    private ImageView goBack;

    @FXML
    private JFXComboBox<String> startComboBox;

    @FXML
    private JFXComboBox<String> endComboBox;

    @FXML
    private MapPanel mapPanel;

    @FXML
    private JFXButton Go;

    @FXML
    private JFXButton End;

    @FXML
    private JFXButton Prev;

    @FXML
    private JFXButton Next;

    @FXML
    private Label Instruction;

    @FXML
    private Label ETA;

    @FXML
    public JFXButton viewInstructionsBtn;

    //FIXME: DO BETTER
    private Graph graph;

    private final int MAX_RECENTLY_USED = 5;

    private static final double PIXEL_TO_METER_RATIO = 10;

    private DoublyLinkedHashSet<Vertex> recentlyUsed, favorites;

    /**
     * These are done for displaying the start & end nodes. This should be done better (eventually)
     *
     * @author Alex Friedman (ahf)
     */
    private DrawableNode startNodeDisplay;
    private DrawableNode endNodeDisplay;
    private DrawableUser userNodeDisplay;

    // Global variables for the stepper
    private final ObservableList<Vertex> pathVertex = FXCollections.observableArrayList();

    private List<NodeEntry> allNodeEntries = new ArrayList<>();

    private final BooleanProperty isCurrentlyNavigating = new SimpleBooleanProperty(false);
    private final ObservableList<Integer> stopsList = FXCollections.observableArrayList();
    private final ObservableList<String> instructionsList = FXCollections.observableArrayList();
    private final ObservableList<String> etaList = FXCollections.observableArrayList();
    private final IntegerProperty currentStep = new SimpleIntegerProperty(0);


    private DrawableNode direction;

    private String currentDirection;

    final ObservableList<String> nodeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //ahf - yes this should be done better. At some point.

        try {
            allNodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries();
            List<EdgeEntry> allEdgeEntries = DatabaseAPI.getDatabaseAPI().genEdgeEntries();
            this.graph = GraphLoader.load(allNodeEntries, allEdgeEntries);
        } catch (Exception e) {
            this.graph = new Graph();
            e.printStackTrace();
        }


        try {
            final String algorithmFromAPI = DatabaseAPI.getDatabaseAPI().getCurrentAlgorithm();

            if(algorithmFromAPI == null)
                DatabaseAPI.getDatabaseAPI().addSystemPreferences("MASTER", "A Star"); //We default to A* if noting explicitly set
            else
                graph.setPathfindingAlgorithm(algorithmFromAPI);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }


        List<String> shortNameList = new ArrayList<>();
        for(Vertex vertex : this.graph.getVertices()){
            NodeEntry node = findNodeEntry(vertex.getID());
            if (node == null) { continue; } // Error checking
            if(!node.getNodeType().equals("HALL")){
                if(!shortNameList.contains(node.getShortName())){
                    shortNameList.add(node.getShortName());
                }else{
                    shortNameList.add(node.getShortName() +
                            node.getNodeID().substring(node.getNodeID().length() - 5));
                }
            }
        }
        nodeList.addAll(shortNameList.stream().sorted().collect(Collectors.toList()));

        startComboBox.setItems(nodeList);
        endComboBox.setItems(nodeList);

        isCurrentlyNavigating.set(false);

        final ContextMenu contextMenu = new ContextMenu();

        //FIXME: CHANGE TEXT TO BE MORE ACCESSIBLE
        final MenuItem startPathMenu = new MenuItem("Path from Here");
        final MenuItem endPathMenu = new MenuItem("Path end Here");

        final MenuItem whatsHereMenu = new MenuItem("What's here?");

        contextMenu.getItems().addAll(startPathMenu, endPathMenu, new SeparatorMenuItem(), whatsHereMenu);

        mapPanel.getMap().setOnContextMenuRequested(event -> {
            if(isCurrentlyNavigating.get()){
                return;
            }

            final double zoomLevel = mapPanel.getZoomLevel().getValue();
            final NodeEntry currEntry = getClosest(event.getX() * zoomLevel, event.getY() * zoomLevel);

            if(currEntry == null)
                return;

            mapPanel.centerNode(mapPanel.getNode(currEntry.getNodeID()));

            contextMenu.show(mapPanel.getMap(), event.getScreenX(), event.getScreenY());

            startPathMenu.setOnAction(e -> startComboBox.setValue(idToShortName(currEntry.getNodeID())));

            endPathMenu.setOnAction(e -> endComboBox.setValue(idToShortName(currEntry.getNodeID())));

            //FIXME: Make these ones require that thing is visible
            whatsHereMenu.setOnAction(e -> {

                final JFXDialog dialog = new JFXDialog();
                final JFXDialogLayout layout = new JFXDialogLayout();


                layout.setHeading(new Text(currEntry.getLongName()));

                //FIXME: DO BREAKS W/ CSS
                layout.setBody(new Text("Lorem ipsum this is a generic content body that will be filled out by some system\n" +
                        "administrator (presumably). It will contain information about the node, floors, etc. I suppose. It\n" +
                        "may also be prone to contain information about running to the second arrangement (it's only the\n" +
                        "natural thing!). As per Doctor Wu, it may also contain directions to Magnolia Boulevard and the\n" +
                        "avenue by Radio City."));

                final JFXButton closeBtn = new JFXButton("Close");
                closeBtn.setOnAction(a -> dialog.close());

                final JFXButton directionsTo = new JFXButton("Direction To");
                directionsTo.setOnAction(a -> {startComboBox.setValue(idToShortName(currEntry.getNodeID())); dialog.close();});

                final JFXButton directionsFrom = new JFXButton("Directions From");
                directionsFrom.setOnAction(a ->  {endComboBox.setValue(idToShortName(currEntry.getNodeID())); dialog.close();});

                final JFXButton toggleFavorite = new JFXButton("FIXME: Add Favorite");

                layout.setActions(toggleFavorite, directionsTo, directionsFrom, closeBtn);

                dialog.setContent(layout);
                mapPanel.showDialog(dialog);
            });
        });


        startComboBox.disableProperty().bind(isCurrentlyNavigating);
        endComboBox.disableProperty().bind(isCurrentlyNavigating);

        Go.setDisable(true);
        End.setDisable(true);
        Prev.setDisable(true);
        Next.setDisable(true);
        pathVertex.clear();
        Instruction.setVisible(false);
        ETA.setVisible(false);

        viewInstructionsBtn.visibleProperty().bind(ETA.visibleProperty());

        direction = null;

        loadRecentlyUsedVertices();
        loadFavorites();

        /*
         * initializes user node
         */

        final DrawableUser drawableUser = new DrawableUser(0, 0, "userNode", "");

        final ObjectBinding<Vertex> vertexProperty = Bindings.when(Bindings.isEmpty(stopsList))
                .then(new Vertex("N/A", -1, -1, "N/A"))
                .otherwise(Bindings.valueAt(pathVertex, Bindings.integerValueAt(stopsList, currentStep)));

        drawableUser.shouldDisplay().bind(isCurrentlyNavigating);

        drawableUser.getFloor().bind(Bindings.createStringBinding(() -> vertexProperty.get().getFloor(), vertexProperty));

        drawableUser.xCoordinateProperty().bind(Bindings.createDoubleBinding(() -> vertexProperty.get().getX(), vertexProperty));

        drawableUser.yCoordinateProperty().bind(Bindings.createDoubleBinding(() -> vertexProperty.get().getY(), vertexProperty));
        this.userNodeDisplay = drawableUser;

        mapPanel.draw(this.userNodeDisplay);


        for(NodeEntry e : allNodeEntries)
          getDrawableNode(e.getNodeID());
    }
    private void loadFavorites() {
        this.favorites = new DoublyLinkedHashSet<>();
        //TODO: load recentlyUsed
    }

    private String shortNameToID(String shortName){
        for(NodeEntry node : allNodeEntries) {
            if (node.getShortName().equals(shortName)) {
                return node.getNodeID();
            }
            if (shortName.length() > 5 && node.getShortName().equals(shortName.substring(0, shortName.length() - 5)) &&
                    shortName.substring(shortName.length() - 5).equals(node.getNodeID().substring(node.getNodeID().length() - 5))) {
                return node.getNodeID();
            }
        }
        return null;
    }

    private String idToShortName(String ID){
        NodeEntry node = findNodeEntry(ID);
        if(node == null){
            return null;
        }
        if(nodeList.contains(node.getShortName())){
            return node.getShortName();
        }else{
            return node.getShortName() + node.getNodeID().substring(node.getNodeID().length() - 5);
        }
    }

    /**
     * Given a list of NodeEntries, returns the one closest to the current location
     *
     * @param x the x coordinate of the mouse
     * @param y the y coordinate
     * @return the closest NodeEntry
     * @author Alex Friedman (ahf)
     */
    private NodeEntry getClosest(double x, double y)
    {
        double minDist2 = Integer.MAX_VALUE;
        NodeEntry closest = null;

        for(String sn : nodeList)
        {
            NodeEntry nodeEntry = findNodeEntry(shortNameToID(sn));
            if(nodeEntry == null){
                return null;
            }
            if(!nodeEntry.getFloor().equals(mapPanel.getFloor().getValue()))
                continue;

            final double currDist2 = Math.pow(x - Integer.parseInt(nodeEntry.getXCoordinate()), 2) + Math.pow(y - Integer.parseInt(nodeEntry.getYCoordinate()), 2);

            if(currDist2 < minDist2)
            {
                minDist2 = currDist2;
                closest = nodeEntry;
            }
        }
        return closest;
    }


    /**
     * Handles the pushing of a button on the screen
     *
     * @param actionEvent the button's push
     * @throws IOException in case of scene switch, if the next fxml scene file cannot be found
     * @author ZheCheng Song
     */
    @FXML
    public void handleButtonPushed(ActionEvent actionEvent) throws IOException {

        ImageView buttonPushed = (ImageView) actionEvent.getSource();  //Getting current stage

        if (buttonPushed == goBack) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
        }
    }

    /**
     *
     * @author Alex Friedman (ahf)
     */
    @FXML
    public void handleStartBoxAction() {
        checkInput();
       // if(this.startNodeDisplay != null)
        //    mapPanel.unDraw(this.startNodeDisplay.getId());
        //FIXME: USE BINDINGS
        this.startNodeDisplay = mapPanel.getNode(shortNameToID(startComboBox.getValue()));

        mapPanel.switchMap(findNodeEntry(startNodeDisplay.getId()).getFloor());
        mapPanel.centerNode(startNodeDisplay);
        loadRecentlyUsedVertices();
    }
    /**
     * Helper function used to draw the startNode with given ID, snatched from handleStartBoxAction()
     * @param nodeID the ID of the Node
     * @author Alex Friedman (ahf) / ZheCheng Song
     */
    private DrawableNode getDrawableNode(String nodeID) {
        final NodeEntry node = findNodeEntry(nodeID);

        if(node != null)
        {
            final DrawableNode drawableNode = node.getDrawable();
            //drawableNode.setRadius(UIConstants.NODE_RADIUS);

            drawableNode.setOnContextMenuRequested(mapPanel.getMap().getOnContextMenuRequested());

            final BooleanBinding isStartNode = startComboBox.valueProperty().isEqualTo(idToShortName(drawableNode.getId()));
            final BooleanBinding isEndNode = endComboBox.valueProperty().isEqualTo(idToShortName(drawableNode.getId()));
            final BooleanBinding isStartOrEndNode = isStartNode.or(isEndNode);

            drawableNode.radiusProperty().bind(Bindings.when(isStartOrEndNode).then(10).otherwise(5));

          //  drawableNode.fillProperty().set(new Color(0, 0, 0, 0));
            drawableNode.setStrokeWidth(2.0);

            drawableNode.fillProperty().bind(Bindings.when(isStartOrEndNode).then(getNodeTypeColor(drawableNode.getNodeType())).otherwise(new Color(0, 0, 0, 0)));

            drawableNode.strokeProperty().bind(
                    Bindings.when(isStartNode).then(Color.ORANGE).otherwise(
                            Bindings.when(isEndNode).then(Color.GREEN).otherwise(getNodeTypeColor(drawableNode.getNodeType()))
            ));

            drawableNode.opacityProperty().bind(Bindings.when(isCurrentlyNavigating.not().or(isStartOrEndNode)).then(1.0).otherwise(0.2));


            Tooltip tt = new JFXTooltip();
           drawableNode.opacityProperty().addListener((observable, oldValue, newValue) -> {// (isCurrentlyNavigating.not().or(isStartOrEndNode)).addListener((observable, oldValue, newValue) -> {
                if(newValue.doubleValue() == 1.0)
                    Tooltip.install(drawableNode, tt);
                else
                    Tooltip.uninstall(drawableNode, tt);
            });

            tt.setText(node.getShortName() +
                        "\nBuilding: " + node.getBuilding() +
                        "\nFloor: " + node.getFloor());

            tt.setStyle("-fx-font: normal bold 15 Langdon; "
                    + "-fx-base: #AE3522; "
                    + "-fx-text-fill: orange;");
            Tooltip.install(drawableNode, tt);


            mapPanel.draw(drawableNode); //FIXME: MOVE OUT?
            return drawableNode;
        }
        return null;
    }

    private Color getNodeTypeColor(String type)
    {
        switch (type){
            case "HALL":
            case "WALK":
                return new Color(0, 0, 0, 0);
            case "CONF":
                return Color.GREEN;
            case "DEPT":
                return Color.BROWN;
            case "ELEV":
                return Color.YELLOW;
            case "INFO":
                return Color.LIGHTBLUE;
            case "REST":
            case "BATH":
                return Color.BLUE;
            case "LABS":
                return Color.LIGHTGREEN;
            case "STAI":
                return Color.RED;
            case "SERV":
                return Color.GRAY;
            case "PARK":
                return Color.BLACK;
            case "EXIT":
                return Color.GOLD;
            case "RETL":
                return Color.PINK;
            default:
                System.out.println(type);
                return Color.RED.brighter();
        }
    }

    /**
     * Loads recently used vertices from the database into the controller
     * @author Tony Vuolo (bdane)
     */
    private void loadRecentlyUsedVertices() {
        this.recentlyUsed = new DoublyLinkedHashSet<>();
        //TODO: load recently used from database
    }

    /**
     *
     * @author Alex Friedman (ahf)
     */
    @FXML
    public void handleEndBoxAction() {
        checkInput();
//        if(this.endNodeDisplay != null)
//            mapPanel.unDraw(this.endNodeDisplay.getId());
        //FIXME: USE BINDINGS?
        this.endNodeDisplay = mapPanel.getNode(shortNameToID(endComboBox.getValue()));//getDrawableNode(endComboBox.getValue(), Color.GREEN, 10);
        mapPanel.switchMap(findNodeEntry(endNodeDisplay.getId()).getFloor());
        mapPanel.centerNode(endNodeDisplay);
        loadRecentlyUsedVertices();
    }



    /**
     * This is used to re-render the A* path
     *
     * @author Alex Friedman (ahf)
     */
    private boolean updatePath()
    {
        final Vertex startVertex = this.graph.getVertex(shortNameToID(startComboBox.getValue()));
        final Vertex endVertex = this.graph.getVertex(shortNameToID(endComboBox.getValue()));

        updateRecentlyUsed(endVertex);

        if(startVertex != null && endVertex != null && !startVertex.equals(endVertex))
        {
            final Path path = this.graph.getPath(startVertex, endVertex);
            pathVertex.clear();
            if(path != null)
            {
                pathVertex.addAll(path.asList());

                parseRoute();

                final Color LINE_STROKE_TRANSPARENT = new Color(UIConstants.LINE_COLOR.getRed(), UIConstants.LINE_COLOR.getGreen(), UIConstants.LINE_COLOR.getBlue(), 0.4);

                for (int i = 0; i < pathVertex.size() - 1; i++)
                {
                    final Vertex start = pathVertex.get(i);
                    final Vertex end = pathVertex.get(i + 1);

                    //int startX, int startY, int endX, int endY, String ID, String startFloor, String endFloor
                    //FIXME: DO BETTER ID WHEN WE HAVE MULTIPLE PATH DIRECTIONS!!!
                    final DrawableEdge edge = new DrawableEdge((int)start.getX(), (int)start.getY(), (int)end.getX(), (int)end.getY(), start.getID() + "_" + end.getID(), start.getFloor(), end.getFloor(), new NodeEntry(), new NodeEntry());
                    edge.setStrokeWidth(UIConstants.LINE_STROKE_WIDTH);

                    edge.strokeProperty().bind(
                            Bindings.when(Bindings.isEmpty(stopsList)).then(Color.RED).otherwise(
                                    Bindings.when(Bindings.integerValueAt(stopsList, currentStep).greaterThan(i)).then(LINE_STROKE_TRANSPARENT).otherwise(Color.ORANGE)
                            )
                    );

                    int localStop;
                    for(localStop = 0; localStop < stopsList.size() - 1; localStop++)
                    {
                        if(stopsList.get(localStop + 1) > i)
                            break;
                    }

                    Tooltip tt = new JFXTooltip();
                    tt.textProperty().set(instructionsList.get(localStop) + "\nETA: " + etaList.get(localStop));

                    tt.setStyle("-fx-font: normal bold 15 Langdon; "
                            + "-fx-base: #AE3522; "
                            + "-fx-text-fill: orange;");
                    Tooltip.install(edge, tt);

                    mapPanel.draw(edge);
                }
                return true;
            }
        }
        else
        {
            //FIXME: INFORM USER OF ERROR
        }

        return false; //We had an error
    }

    /**
     * Updates the recently used DLHS with the newest destination Vertex
     * @param endVertex the new destination to be considered a recently used Vertex
     * @author Tony Vuolo (bdane)
     */
    private void updateRecentlyUsed(Vertex endVertex) {
        if(this.recentlyUsed.size() == MAX_RECENTLY_USED) {
            this.recentlyUsed.add(this.recentlyUsed.removeIndex(0));
        } else if(this.recentlyUsed.containsKey(endVertex)) {
            this.recentlyUsed.remove(endVertex);
            this.recentlyUsed.add(endVertex);
        }
    }

    /**
     * Used to check if our input is valid to run the pathfinding algorithm or not
     *
     * @author Alex Friedman (ahf)
     */
    private void checkInput() {
        if (startComboBox.getValue() == null || endComboBox.getValue() == null){
          mapPanel.getCanvas().getChildren().removeIf(x -> x instanceof DrawableEdge);
        }else{
            mapPanel.getCanvas().getChildren().removeIf(x -> x instanceof DrawableEdge);
            updatePath();
            ETA.textProperty().unbind();
            ETA.setText("ETA"); //FIXME: DO BETTER EVENTUALLY
            Go.setDisable(false);
        }
    }

    /**
     * Helper function used to find the corresponding NodeEntry with nodeID
     * @param nodeID the ID used to get the corresponding NodeEntry
     * @return a NodeEntry with given ID
     * @author ZheCheng Song
     */
    private NodeEntry findNodeEntry(String nodeID){
        for(NodeEntry n : allNodeEntries){
            if (n.getNodeID().equals(nodeID)) {
                return n;
            }
        }
        return null;
    }

    /**
     * Function to parse through the path list and extract potential stops(turn, change floor) and instructions
     * @author ZheCheng Song
     */
    private void parseRoute(){
        stopsList.clear();
        instructionsList.clear();
        etaList.clear();
        if(this.pathVertex.isEmpty()) return;

        // Assert "Up" is forward for start
        double prevAngle = Math.toDegrees(Math.atan2(-1.0,0.0)) + 180;
        double currAngle;
        String prevDirect = "Look forward";
        String currDirect;
        double distance;
        boolean lastSE = false;

        stopsList.add(0);

        for(int i = 0; i < pathVertex.size() -1; i++){
            Vertex curV = pathVertex.get(i);
            Vertex nexV = pathVertex.get(i + 1);

            NodeEntry curN = findNodeEntry(curV.getID());
            if (curN == null) return;

            // Stair or Elevator found
            if ((curN.getNodeType().equals("STAI") || curN.getNodeType().equals("ELEV"))
            && curV.getID().substring(1, 5).equals(nexV.getID().substring(1, 5))){
                // Not first node, finish line search
                if(i!=0){
                    stopsList.add(i);
                    distance = calculateDistance(pathVertex, stopsList.get(stopsList.size()-2), stopsList.get(stopsList.size()-1));
                    instructionsList.add(prevDirect + " and walk " + Math.round(distance) + " m");
                }
                i = searchSE(i);
                if(i == pathVertex.size() - 1) {
                    lastSE = true;
                    break;
                }
                curV = pathVertex.get(i);
                nexV = pathVertex.get(i + 1);
                // do better
                currAngle = Math.toDegrees(Math.atan2(nexV.getY() - curV.getY(), nexV.getX() - curV.getX())) + 180;
                prevDirect = calculateDirection(prevAngle, currAngle);
                prevAngle = currAngle;
                stopsList.add(i);
            }

            currAngle = Math.toDegrees(Math.atan2(nexV.getY() - curV.getY(), nexV.getX() - curV.getX())) + 180;
            currDirect = calculateDirection(prevAngle, currAngle);
            prevAngle = currAngle;
            if(i != 0) {
                if (!currDirect.equals("Look forward")) {
                    stopsList.add(i);
                    distance = calculateDistance(pathVertex, stopsList.get(stopsList.size() - 2), stopsList.get(stopsList.size() - 1));
                    instructionsList.add(prevDirect + " and walk " + Math.round(distance) + " m");
                    prevDirect = currDirect;
                }
            }else{
                prevDirect = currDirect;
            }
        }
        stopsList.add(pathVertex.size() - 1);
        if(!lastSE) {
            distance = calculateDistance(pathVertex, stopsList.get(stopsList.size() - 2), stopsList.get(stopsList.size() - 1));
            instructionsList.add(prevDirect + " and walk " + Math.round(distance) + " m");
        }
        instructionsList.add("Arrive at destination!");

        // Calculate ETA
        for (Integer stop : stopsList) {
            etaList.add(calculateETA(stop, pathVertex.size() - 1));
        }
    }

    private String calculateETA(int startIndex, int endIndex){
        int distance = (int)Math.round(calculateDistance(pathVertex, startIndex, endIndex));
        // Assume walks in 1.2m/s
        int totalSecond = (int)Math.round(distance / 1.2);
        int hour = totalSecond / 3600;
        if(hour > 0) totalSecond -= 3600 * hour;
        int min = totalSecond / 60;
        if(min > 0) totalSecond -= 60 * min;
        String hZero = "";
        String mZero = "";
        String sZero = "";
        if(hour / 10 < 1) hZero = "0";
        if(min / 10 < 1) mZero = "0";
        if(totalSecond / 10 < 1) sZero = "0";
        return ("ETA : " + hZero + hour + ":" + mZero + min + ":" + sZero + totalSecond);
    }

    private int searchSE(int startIndex){
        NodeEntry curN;
        Vertex preV = pathVertex.get(startIndex);
        Vertex curV;
        for(int i = startIndex + 1; i < pathVertex.size(); i++){
            curV = pathVertex.get(i);
            if(!curV.getID().substring(1, 5).equals(preV.getID().substring(1, 5)) || i == pathVertex.size() - 1){
                curN = findNodeEntry(curV.getID());
                if (curN == null) return -1;
                String type = curN.getNodeType();
                if (type.equals("STAI"))
                    type = "Stair";
                else
                    type = "Elevator";
                instructionsList.add("Take " + type + " to Floor " + preV.getFloor());
                if(i == pathVertex.size() - 1)
                    return pathVertex.size() - 1;
                else
                    return i - 1;
            }
            preV = curV;
        }
        // Error
        return -1;
    }

    private String calculateDirection(double prevAngle, double curAngle){
        double angle = (curAngle + (360 - prevAngle)) % 360;

        // small angle (45) alternation ignored
        if (angle <= 45 || angle >= 315) {
            return "Look forward";
        } else {
            if (Math.abs(Math.abs(curAngle - prevAngle) - 180) <= 30) {
                return "Turn around";
            } else if (angle < 180) {
                return "Turn right";
            } else {
                return "Turn left";
            }
        }
    }

    private void drawDirection(){
        if(direction != null)
            mapPanel.unDraw(this.direction.getId());
        Vertex curV = pathVertex.get(stopsList.get(currentStep.get()));
        switch (currentDirection) {
            case "UP":
                direction = new DrawableNode((int) Math.round(curV.getX()), (int) Math.round(curV.getY() - 50.0),
                        "direction", curV.getFloor(), "", "", "", "");
                break;
            case "LEFT":
                direction = new DrawableNode((int) Math.round(curV.getX() - 50.0), (int) Math.round(curV.getY()),
                        "direction", curV.getFloor(), "", "", "", "");
                break;
            case "RIGHT":
                direction = new DrawableNode((int) Math.round(curV.getX() + 50.0), (int) Math.round(curV.getY()),
                        "direction", curV.getFloor(), "", "", "", "");
                break;
            case "DOWN":
                direction = new DrawableNode((int) Math.round(curV.getX()), (int) Math.round(curV.getY() + 50.0),
                        "direction", curV.getFloor(), "", "", "", "");
                break;
        }
        direction.setFill(Color.RED);
        direction.setRadius(4);

        mapPanel.draw(direction);
    }

    private void changeDirection(String inst){
        String[] instruction = inst.split(" ");
        if(!instruction[0].equals("Take") && !instruction[0].equals("Look")){
            switch (instruction[1]) {
                case "around":
                    switchDirectionDown();
                    break;
                case "left":
                    switchDirectionLeft();
                    break;
                case "right":
                    switchDirectionRight();
                    break;
            }
        }
    }

    private void switchDirectionDown() {
        switch (currentDirection) {
            case "UP":
                currentDirection = "DOWN";
                break;
            case "LEFT":
                currentDirection = "RIGHT";
                break;
            case "RIGHT":
                currentDirection = "LEFT";
                break;
            case "DOWN":
                currentDirection = "UP";
                break;
        }
    }

    private void switchDirectionRight() {
        switch (currentDirection) {
            case "UP":
                currentDirection = "RIGHT";
                break;
            case "LEFT":
                currentDirection = "UP";
                break;
            case "RIGHT":
                currentDirection = "DOWN";
                break;
            case "DOWN":
                currentDirection = "LEFT";
                break;
        }
    }

    private void switchDirectionLeft() {
        switch (currentDirection) {
            case "UP":
                currentDirection = "LEFT";
                break;
            case "LEFT":
                currentDirection = "DOWN";
                break;
            case "RIGHT":
                currentDirection = "UP";
                break;
            case "DOWN":
                currentDirection = "RIGHT";
                break;
        }
    }

    private void changeDirectionRevert(String inst){
        String[] instruction = inst.split(" ");
        if(!instruction[0].equals("Take") && !instruction[0].equals("Look")){
            switch (instruction[1]) {
                case "around":
                    switchDirectionDown();
                    break;
                case "left":
                    switchDirectionRight();
                    break;
                case "right":
                    switchDirectionLeft();
                    break;
            }
        }
    }

    /**
     * Function to react to 'Start Navigation' button being pressed and start the route stepper
     * @author ZheCheng Song
     */
    public void startNavigation() {
        Go.setDisable(true);
        Next.setDisable(false);
        End.setDisable(false);
        Instruction.setVisible(true);
        ETA.setVisible(true);

        currentStep.set(0);

        isCurrentlyNavigating.set(true);

        parseRoute();
        mapPanel.switchMap(pathVertex.get(0).getFloor());

        if(userNodeDisplay != null)
            mapPanel.unDraw(userNodeDisplay.getId());
        mapPanel.draw(this.userNodeDisplay);

        this.startNodeDisplay = mapPanel.getNode(pathVertex.get(0).getID());
        this.endNodeDisplay = mapPanel.getNode(pathVertex.get(pathVertex.size()-1).getID());
        mapPanel.centerNode(userNodeDisplay);

        Instruction.textProperty().bind(Bindings.when(Bindings.isEmpty(instructionsList)).then("").otherwise(Bindings.stringValueAt(instructionsList, currentStep)));
        ETA.textProperty().bind(Bindings.stringValueAt(etaList, currentStep));

        currentDirection = "UP";
        drawDirection();
    }

    /**
     * Function to react to 'Prev' button being pressed and go to the previous point with stepper
     * @author ZheCheng Song
     */
    public void goToPrevNode() {
        currentStep.set(currentStep.get() - 1);

        if(currentStep.get() == 0){
            Prev.setDisable(true);
        }
        else {
            Prev.setDisable(false);
            Next.setDisable(false);
        }

        if(!pathVertex.get(stopsList.get(currentStep.get())).getFloor().equals(mapPanel.getFloor().getValue())){
            mapPanel.switchMap(pathVertex.get(stopsList.get(currentStep.get())).getFloor());
        }

        mapPanel.centerNode(userNodeDisplay);

        changeDirectionRevert(instructionsList.get(currentStep.get()));
        drawDirection();
    }

    /**
     * Function to react to 'Next' button being pressed and go to the next point with stepper
     * @author ZheCheng Song
     */
    public void goToNextNode() {
        changeDirection(instructionsList.get(currentStep.get()));

        currentStep.set(currentStep.get() + 1);
        if(currentStep.get() == Math.min(stopsList.size() - 1, instructionsList.size() - 1)){
            Next.setDisable(true);
        }
        else {
            Prev.setDisable(false);
            Next.setDisable(false);
        }
        if(!pathVertex.get(stopsList.get(currentStep.get())).getFloor().equals(mapPanel.getFloor().getValue())){
            mapPanel.switchMap(pathVertex.get(stopsList.get(currentStep.get())).getFloor());
        }

        mapPanel.centerNode(userNodeDisplay);

        drawDirection();

        if(direction != null && currentStep.get() == Math.min(stopsList.size() - 1, instructionsList.size() - 1))
                mapPanel.unDraw(this.direction.getId());

    }

    /**
     * Function to react to 'End Navigation' button being pressed and stop the stepper
     * @author ZheCheng Song
     */
    public void endNavigation() {
        Go.setDisable(false);
        Prev.setDisable(true);
        Next.setDisable(true);
        End.setDisable(true);
        Instruction.setVisible(false);
        ETA.setVisible(false);
        currentStep.set(0);
        isCurrentlyNavigating.set(false);

        if(direction != null)
            mapPanel.unDraw(this.direction.getId());

        mapPanel.switchMap(pathVertex.get(0).getFloor());
        mapPanel.centerNode(startNodeDisplay);
    }

    /**
     * Helper function to calculate total distance from start index to end index of a list of vertex
     * @param path List of Vertex to be used
     * @param start Start index represent the starting node in list
     * @param end End index represent the ending node in list
     * @return sumDist the total distance from start Vertex to end Vertex
     */
    private double calculateDistance(List<Vertex> path, int start, int end) {
        double sumDist = 0.0;
        for (int i = start; i < end; i++) {
            sumDist += path.get(i).EuclideanDistance(path.get(i + 1));
        }
        return sumDist / PIXEL_TO_METER_RATIO;
    }

    /**
     * Returns user to main page after clicking on the B&W Logo
     * Replaces handleButtonPushed
     * @throws IOException
     * @author Leo Morris
     */
    public void handleGoBack() throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
    }

    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #F0C808; -fx-text-fill: #000000;");
    }

    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #03256C; -fx-text-fill: #FFFFFF;");
    }

    /**
     * On clicked, displays the whole list of instructions
     * @author Alex Friedman (ahf)
     */
    public void handleViewInstructions() {
        final JFXDialog dialog = new JFXDialog();
        final JFXDialogLayout layout = new JFXDialogLayout();

        //TODO: Italics for previously finished instructions?
        //TODO: better align ETA text.
        layout.setHeading(new Text("Directions from: " + startComboBox.getValue() + " to " +  endComboBox.getValue()));

        String directions = "";
        for(int i = 0; i < stopsList.size(); i++)
        {
            final String instruction = instructionsList.get(i);
            final String eta = etaList.get(i);

            if(i < stopsList.size() - 1)
                directions += instruction + "\t\t(" + eta + ")\n";
            else
                directions += instruction;
        }

        //FIXME: DO BREAKS W/ CSS
        layout.setBody(new Text(directions));

        final JFXButton closeBtn = new JFXButton("Close");
        closeBtn.setOnAction(a -> dialog.close());

        final JFXButton printBtn = new JFXButton("Print");


        layout.setActions(printBtn, closeBtn);

        dialog.setContent(layout);
        mapPanel.showDialog(dialog);
    }
}
