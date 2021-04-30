package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.pathfinding.*;
import edu.wpi.cs3733.D21.teamF.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamF.pathfinding.GraphLoader;
import edu.wpi.cs3733.D21.teamF.pathfinding.Path;
import edu.wpi.cs3733.D21.teamF.pathfinding.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import edu.wpi.cs3733.D21.teamF.utils.UIConstants;
import edu.wpi.cs3733.uicomponents.MapPanel;
import edu.wpi.cs3733.uicomponents.entities.DrawableEdge;
import edu.wpi.cs3733.uicomponents.entities.DrawableNode;
import edu.wpi.cs3733.uicomponents.entities.DrawableUser;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AStarDemoController implements Initializable {

    @FXML
    private Button goBack;

    @FXML
    private ComboBox<String> startComboBox;

    @FXML
    private ComboBox<String> endComboBox;

    @FXML
    private MapPanel mapPanel;

    @FXML
    private Button Go;

    @FXML
    private Button End;

    @FXML
    private Button Prev;

    @FXML
    private Button Next;

    @FXML
    private Label Instruction;

    @FXML
    private Label ETA;


    //FIXME: DO BETTER
    private Graph graph;

    private final int MAX_RECENTLY_USED = 5;

    private static final double PIXEL_TO_METER_RATIO = 10;

    private DoublyLinkedHashSet<NodeEntry> recentlyUsed, favorites;

    /**
     * These are done for displaying the start & end nodes. This should be done better (eventually)
     *
     * @author Alex Friedman (ahf)
     */
    private DrawableNode startNodeDisplay;
    private DrawableNode endNodeDisplay;
    private DrawableUser userNodeDisplay;

    // Global variables for the stepper
    private final ObservableList<NodeEntry> pathVertex = FXCollections.observableArrayList();

    private List<NodeEntry> allNodeEntries = new ArrayList<>();

    private final SimpleBooleanProperty pathFinding = new SimpleBooleanProperty(false);
    private final ObservableList<Integer> stops = FXCollections.observableArrayList();
    private final ObservableList<String> instructions = FXCollections.observableArrayList();
    private final ObservableList<String> eta = FXCollections.observableArrayList();
    private final SimpleIntegerProperty curStep = new SimpleIntegerProperty(0);


    private DrawableNode direction;

    private String currentDirection;

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


        final ObservableList<String> nodeList = FXCollections.observableArrayList();
        nodeList.addAll(this.graph.getVertices().stream().map(NodeEntry::getNodeID).sorted().collect(Collectors.toList()));


        startComboBox.setItems(nodeList);
        endComboBox.setItems(nodeList);

        pathFinding.set(false);

        final ContextMenu contextMenu = new ContextMenu();

        //FIXME: CHANGE TEXT TO BE MORE ACCESSIBLE
        final MenuItem startPathMenu = new MenuItem("Path from Here");
        final MenuItem endPathMenu = new MenuItem("Path end Here");

        contextMenu.getItems().addAll(startPathMenu, endPathMenu);

        mapPanel.getMap().setOnContextMenuRequested(event -> {
            if(pathFinding.get()){
                return;
            }
            contextMenu.show(mapPanel.getMap(), event.getScreenX(), event.getScreenY());

            final double zoomLevel = mapPanel.getZoomLevel().getValue();

            startPathMenu.setOnAction((ActionEvent e) -> startComboBox.setValue(getClosest(allNodeEntries, event.getX() * zoomLevel, event.getY() * zoomLevel).getNodeID()));

            endPathMenu.setOnAction(e -> endComboBox.setValue(getClosest(allNodeEntries, event.getX() * zoomLevel, event.getY() * zoomLevel).getNodeID()));
        });


        startComboBox.disableProperty().bind(pathFinding);
        endComboBox.disableProperty().bind(pathFinding);

        Go.setDisable(true);
        End.setDisable(true);
        Prev.setDisable(true);
        Next.setDisable(true);
        pathVertex.clear();
        Instruction.setVisible(false);

        direction = null;

        loadRecentlyUsedVertices();
        loadFavorites();

        /*
         * initializes user node
         */

        final DrawableUser drawableUser = new DrawableUser(0, 0, "userNode", "");

        final ObjectBinding<NodeEntry> vertexProperty = Bindings.when(Bindings.isEmpty(stops))
                .then(new NodeEntry("N/A", -1, -1, "N/A"))
                .otherwise(Bindings.valueAt(pathVertex, Bindings.integerValueAt(stops, curStep)));

        drawableUser.shouldDisplay().bind(pathFinding);

        drawableUser.getFloor().bind(Bindings.createStringBinding(() -> vertexProperty.get().getFloor(), vertexProperty));

        drawableUser.xCoordinateProperty().bind(Bindings.createDoubleBinding(() -> vertexProperty.get().getX(), vertexProperty));

        drawableUser.yCoordinateProperty().bind(Bindings.createDoubleBinding(() -> vertexProperty.get().getY(), vertexProperty));
        this.userNodeDisplay = drawableUser;

        mapPanel.draw(this.userNodeDisplay);
    }
    private void loadFavorites() {
        this.favorites = new DoublyLinkedHashSet<>();
        //TODO: load recentlyUsed
    }
    /**
     * Given a list of NodeEntries, returns the one closest to the current location
     *
     * @param entries The list of NodeEntries
     * @param x the x coordinate of the mouse
     * @param y the y coordinate
     * @return the closest NodeEntry
     * @author Alex Friedman (ahf)
     */
    private NodeEntry getClosest(List<NodeEntry> entries, double x, double y)
    {
        double minDist2 = Integer.MAX_VALUE;
        NodeEntry closest = null;

        for(NodeEntry nodeEntry : entries)
        {
            if(!nodeEntry.getFloor().equals(mapPanel.getFloor().getValue()))
                continue;

            final double currDist2 = Math.pow(x - Integer.parseInt(nodeEntry.getXcoord()), 2) + Math.pow(y - Integer.parseInt(nodeEntry.getYcoord()), 2);

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

        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage

        if (buttonPushed == goBack) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
        }
    }

    /**
     *
     * @author Alex Friedman (ahf)
     */
    @FXML
    public void handleStartBoxAction() throws SQLException {
        checkInput();
        if(this.startNodeDisplay != null)
            mapPanel.unDraw(this.startNodeDisplay.getId());
        this.startNodeDisplay = getDrawableNode(startComboBox.getValue(), UIConstants.NODE_COLOR, 10);
        mapPanel.switchMap(findNodeEntry(startNodeDisplay.getId()).getFloor());
        mapPanel.centerNode(startNodeDisplay);
        loadRecentlyUsedVertices();
    }
    /**
     * Helper function used to draw the startNode with given ID, snatched from handleStartBoxAction()
     * @param nodeID the ID of the Node
     * @author Alex Friedman (ahf) / ZheCheng Song
     */
    private DrawableNode getDrawableNode(String nodeID, Color color, double radius) throws SQLException{
        final NodeEntry startNode = DatabaseAPI.getDatabaseAPI().getNode(nodeID);

        if(startNode != null)
        {
            final DrawableNode drawableNode = startNode.getDrawable();
            drawableNode.setFill(color);//UIConstants.NODE_COLOR);
            drawableNode.setRadius(radius);//10);

            Tooltip tt = new Tooltip();
            tt.setText(startNode.getShortName() +
                        "\nBuilding: " + startNode.getBuilding() +
                        "\nFloor: " + startNode.getFloor());

            tt.setStyle("-fx-font: normal bold 15 Langdon; "
                    + "-fx-base: #AE3522; "
                    + "-fx-text-fill: orange;");
            Tooltip.install(drawableNode, tt);


            mapPanel.draw(drawableNode); //FIXME: MOVE OUT?
            return drawableNode;
        }
        return null;
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
    public void handleEndBoxAction() throws SQLException {
        checkInput();
        if(this.endNodeDisplay != null)
            mapPanel.unDraw(this.endNodeDisplay.getId());
        this.endNodeDisplay = getDrawableNode(endComboBox.getValue(), Color.GREEN, 10);
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
        if(this.startNodeDisplay != null)
            mapPanel.draw(this.startNodeDisplay);
        if(this.endNodeDisplay != null)
            mapPanel.draw(this.endNodeDisplay);

        final NodeEntry startVertex = this.graph.getVertex(startComboBox.getValue());
        final NodeEntry endVertex = this.graph.getVertex(endComboBox.getValue());

        updateRecentlyUsed(endVertex);

        if(startVertex != null && endVertex != null && !startVertex.equals(endVertex))
        {
            final Path path = this.graph.getPath(startVertex, endVertex);
            pathVertex.clear();
            if(path != null)
            {
                pathVertex.addAll(path.asList());

                final Color LINE_STROKE_TRANSPARENT = new Color(UIConstants.LINE_COLOR.getRed(), UIConstants.LINE_COLOR.getGreen(), UIConstants.LINE_COLOR.getBlue(), 0.4);

                for (int i = 0; i < pathVertex.size() - 1; i++)
                {
                    final NodeEntry start = pathVertex.get(i);
                    final NodeEntry end = pathVertex.get(i + 1);

                    //int startX, int startY, int endX, int endY, String ID, String startFloor, String endFloor
                    //FIXME: DO BETTER ID WHEN WE HAVE MULTIPLE PATH DIRECTIONS!!!
                    final DrawableEdge edge = new DrawableEdge((int)start.getX(), (int)start.getY(), (int)end.getX(), (int)end.getY(), start.getNodeID() + "_" + end.getNodeID(), start.getFloor(), end.getFloor(), new NodeEntry(), new NodeEntry());
                    edge.setStrokeWidth(UIConstants.LINE_STROKE_WIDTH);

                    edge.strokeProperty().bind(
                            Bindings.when(Bindings.isEmpty(stops)).then(Color.RED).otherwise(
                                    Bindings.when(Bindings.integerValueAt(stops, curStep).greaterThan(i)).then(LINE_STROKE_TRANSPARENT).otherwise(Color.ORANGE)
                            )
                    );

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
    private void updateRecentlyUsed(NodeEntry endVertex) {
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
            mapPanel.clearMap();
        }else{
            mapPanel.clearMap();
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
        stops.clear();
        instructions.clear();
        eta.clear();
        if(this.pathVertex.isEmpty()) return;

        // Assert "Up" is forward for start
        double prevAngle = Math.toDegrees(Math.atan2(-1.0,0.0)) + 180;
        double currAngle;
        String prevDirect = "Look forward";
        String currDirect;
        double distance;
        boolean lastSE = false;

        stops.add(0);

        for(int i = 0; i < pathVertex.size() -1; i++){
            NodeEntry curV = pathVertex.get(i);
            NodeEntry nexV = pathVertex.get(i + 1);

            NodeEntry curN = findNodeEntry(curV.getNodeID());
            if (curN == null) return;

            // Stair or Elevator found
            if ((curN.getNodeType().equals("STAI") || curN.getNodeType().equals("ELEV"))
            && curV.getNodeID().substring(0, 5).equals(nexV.getNodeID().substring(0, 5))){
                // Not first node, finish line search
                if(i!=0){
                    stops.add(i);
                    distance = calculateDistance(pathVertex, stops.get(stops.size()-2), stops.get(stops.size()-1));
                    instructions.add(prevDirect + " and walk " + Math.round(distance) + " m");
                }
                i = searchSE(i);
                if(i == pathVertex.size() - 1) {
                    lastSE = true;
                    break;
                }
                curV = pathVertex.get(i);
                nexV = pathVertex.get(i + 1);
                // do better
                prevAngle = Math.toDegrees(Math.atan2(nexV.getY() - curV.getY(), nexV.getX() - curV.getX())) + 180;
                stops.add(i);
            }

            currAngle = Math.toDegrees(Math.atan2(nexV.getY() - curV.getY(), nexV.getX() - curV.getX())) + 180;
            currDirect = calculateDirection(prevAngle, currAngle);
            prevAngle = currAngle;
            if(!currDirect.equals("Look forward") && i != 0){
                stops.add(i);
                distance = calculateDistance(pathVertex, stops.get(stops.size()-2), stops.get(stops.size()-1));
                instructions.add(prevDirect + " and walk " + Math.round(distance) + " m");
                prevDirect = currDirect;
            }
        }
        stops.add(pathVertex.size() - 1);
        if(!lastSE) {
            distance = calculateDistance(pathVertex, stops.get(stops.size() - 2), stops.get(stops.size() - 1));
            instructions.add(prevDirect + " and walk " + Math.round(distance) + " m");
        }
        instructions.add("Arrive at destination!");

        if(instructions.size()==0) return;
        // Fixing Directions. Hard code, do better!
        boolean lookAtNext = false;
        for(int i = 0; i < instructions.size() - 1; i++){
            String ins = instructions.get(i);
            int step = stops.get(i);
            if(step == pathVertex.size() - 1 || step < 1)
                continue;
            if(lookAtNext) {
                NodeEntry curV = pathVertex.get(step);
                NodeEntry nexV = pathVertex.get(step + 1);
                currAngle = Math.toDegrees(Math.atan2(nexV.getY() - curV.getY(), nexV.getX() - curV.getX())) + 180;
                currDirect = calculateDirection(prevAngle, currAngle);
                String[] firstInst = instructions.get(i).split(" ", 3);
                instructions.set(i, currDirect + " " + firstInst[2]);
                lookAtNext = false;
            }
            if (ins.split(" ")[0].equals("Take")) {
                NodeEntry preV = pathVertex.get(step - 1);
                NodeEntry curV = pathVertex.get(step);
                prevAngle = Math.toDegrees(Math.atan2(curV.getY() - preV.getY(), curV.getX() - preV.getX())) + 180;
                lookAtNext = true;
            }
        }
        if (!instructions.get(0).split(" ")[0].equals("Take")){
            NodeEntry curV = pathVertex.get(0);
            NodeEntry nexV = pathVertex.get(1);
            prevAngle = Math.toDegrees(Math.atan2(-1.0, 0.0)) + 180;
            currAngle = Math.toDegrees(Math.atan2(nexV.getY() - curV.getY(), nexV.getX() - curV.getX())) + 180;
            currDirect = calculateDirection(prevAngle, currAngle);
            String[] firstInst = instructions.get(0).split(" ", 3);
            instructions.set(0, currDirect + " " + firstInst[2]);
        }else{
            NodeEntry curV = pathVertex.get(1);
            NodeEntry nexV = pathVertex.get(2);
            prevAngle = Math.toDegrees(Math.atan2(-1.0, 0.0)) + 180;
            currAngle = Math.toDegrees(Math.atan2(nexV.getY() - curV.getY(), nexV.getX() - curV.getX())) + 180;
            currDirect = calculateDirection(prevAngle, currAngle);
            String[] firstInst = instructions.get(1).split(" ", 3);
            instructions.set(1, currDirect + " " + firstInst[2]);
        }

        // Calculate ETA
        for (Integer stop : stops) {
            eta.add(calculateETA(stop, pathVertex.size() - 1));
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
        NodeEntry preV = pathVertex.get(startIndex);
        NodeEntry curV;
        for(int i = startIndex + 1; i < pathVertex.size(); i++){
            curV = pathVertex.get(i);
            if(!curV.getNodeID().substring(0, 5).equals(preV.getNodeID().substring(0, 5)) || i == pathVertex.size() - 1){
                curN = findNodeEntry(curV.getNodeID());
                if (curN == null) return -1;
                String type = curN.getNodeType();
                if (type.equals("STAI"))
                    type = "Stair";
                else
                    type = "Elevator";
                instructions.add("Take " + type + " to Floor " + preV.getFloor());
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
            if (Math.abs(Math.abs(curAngle - prevAngle) - 180) <= 15) {
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
        NodeEntry curV = pathVertex.get(stops.get(curStep.get()));
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
     * @throws SQLException thrown if getDrawableNode has an issue
     * @author ZheCheng Song
     */
    public void startNavigation() throws SQLException {
        Go.setDisable(true);
        Next.setDisable(false);
        End.setDisable(false);
        Instruction.setVisible(true);

        curStep.set(0);

        pathFinding.set(true);

        parseRoute();
        mapPanel.switchMap(pathVertex.get(0).getFloor());

        mapPanel.draw(this.userNodeDisplay);
        this.startNodeDisplay = getDrawableNode(pathVertex.get(0).getNodeID(), UIConstants.NODE_COLOR, 10);
        this.endNodeDisplay = getDrawableNode(pathVertex.get(pathVertex.size()-1).getNodeID(), Color.GREEN, 10);
        mapPanel.centerNode(userNodeDisplay);

        Instruction.textProperty().bind(Bindings.when(Bindings.isEmpty(instructions)).then("").otherwise(Bindings.stringValueAt(instructions, curStep)));
        ETA.textProperty().bind(Bindings.stringValueAt(eta, curStep));

        currentDirection = "UP";
        drawDirection();
    }

    /**
     * Function to react to 'Prev' button being pressed and go to the previous point with stepper
     * @author ZheCheng Song
     */
    public void goToPrevNode() {
        curStep.set(curStep.get() - 1);

        if(curStep.get() == 0){
            Prev.setDisable(true);
        }
        else {
            Prev.setDisable(false);
            Next.setDisable(false);
        }

        if(!pathVertex.get(curStep.get()).getFloor().equals(mapPanel.getFloor().getValue())){
            mapPanel.switchMap(pathVertex.get(stops.get(curStep.get())).getFloor());
        }

        mapPanel.centerNode(userNodeDisplay);

        changeDirectionRevert(instructions.get(curStep.get()));
        drawDirection();
    }

    /**
     * Function to react to 'Next' button being pressed and go to the next point with stepper
     * @author ZheCheng Song
     */
    public void goToNextNode() {
        changeDirection(instructions.get(curStep.get()));

        curStep.set(curStep.get() + 1);
        if(curStep.get() == Math.min(stops.size() - 1, instructions.size() - 1)){
            Next.setDisable(true);
        }
        else {
            Prev.setDisable(false);
            Next.setDisable(false);
        }
        if(!pathVertex.get(stops.get(curStep.get())).getFloor().equals(mapPanel.getFloor().getValue())){
            mapPanel.switchMap(pathVertex.get(stops.get(curStep.get())).getFloor());
        }

        mapPanel.centerNode(userNodeDisplay);

        drawDirection();

        if(direction != null && curStep.get() == Math.min(stops.size() - 1, instructions.size() - 1))
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
        curStep.set(0);
        pathFinding.set(false);

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
    private double calculateDistance(List<NodeEntry> path, int start, int end) {
        double sumDist = 0.0;
        for (int i = start; i < end; i++) {
            sumDist += path.get(i).EuclideanDistance(path.get(i + 1));
        }
        return sumDist / PIXEL_TO_METER_RATIO;
    }
}