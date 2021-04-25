package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.entities.EdgeEntry;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.database.ConnectionHandler;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamF.pathfinding.GraphLoader;
import edu.wpi.cs3733.D21.teamF.pathfinding.Path;
import edu.wpi.cs3733.D21.teamF.pathfinding.Vertex;
import edu.wpi.cs3733.D21.teamF.utils.UIConstants;
import edu.wpi.cs3733.uicomponents.IMapDrawable;
import edu.wpi.cs3733.uicomponents.MapPanel;
import edu.wpi.cs3733.uicomponents.entities.DrawableEdge;
import edu.wpi.cs3733.uicomponents.entities.DrawableNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

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

    //FIXME: DO BETTER
    private Graph graph;

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

    /**
     * These are done for displaying the start & end nodes. This should be done better (eventually)
     * @author Alex Friedman (ahf)
     */
    private DrawableNode startNodeDisplay;
    private DrawableNode endNodeDisplay;


    // Global variables for the stepper
    private List<Vertex> pathVertex;

    List<NodeEntry> allNodeEntries = new ArrayList<>();
    List<EdgeEntry> allEdgeEntries = new ArrayList<>();

    boolean pathFinding;
    List<Integer> stops;
    List<String> instructions;
    int curStep;
    String curFloor;

    DrawableNode direction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //ahf - yes this should be done better. At some point.

        try {
            allNodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries(ConnectionHandler.getConnection());
            allEdgeEntries = DatabaseAPI.getDatabaseAPI().genEdgeEntries(ConnectionHandler.getConnection());

            final List<NodeEntry> nodeEntries = allNodeEntries.stream().collect(Collectors.toList());

            final List<EdgeEntry> edgeEntries = allEdgeEntries.stream().filter( node -> hasNode(nodeEntries, node.getStartNode())
                    && hasNode(nodeEntries, node.getEndNode()) ).collect(Collectors.toList());
            this.graph = GraphLoader.load(nodeEntries, edgeEntries);
        } catch (Exception e) {
            this.graph = new Graph();
            e.printStackTrace();
            //return;
        }

        final ObservableList<String> nodeList = FXCollections.observableArrayList();
        nodeList.addAll(this.graph.getVertices().stream().map(Vertex::getID)
                .sorted().collect(Collectors.toList()));

        startComboBox.setItems(nodeList);
        endComboBox.setItems(nodeList);

        pathFinding = false;

        final ContextMenu contextMenu = new ContextMenu();

        //FIXME: CHANGE TEXT TO BE MORE ACCESSABLE
        final MenuItem startPathfind = new MenuItem("Path from Here");
        final MenuItem endPathfind = new MenuItem("Path end Here");

        contextMenu.getItems().addAll(startPathfind, endPathfind);

        List<NodeEntry> finalAllNodeEntries = allNodeEntries;

        mapPanel.getMap().setOnContextMenuRequested(event -> {
            if(pathFinding){
                return;
            }
            contextMenu.show(mapPanel.getMap(), event.getScreenX(), event.getScreenY());

            final double zoomLevel = mapPanel.getZoomLevel().getValue();

            startPathfind.setOnAction((ActionEvent e) -> {
                startComboBox.setValue(getClosest(finalAllNodeEntries, event.getX()* zoomLevel , event.getY() * zoomLevel).getNodeID());
            });

            endPathfind.setOnAction(e -> {
                endComboBox.setValue(getClosest(finalAllNodeEntries, event.getX() * zoomLevel, event.getY() * zoomLevel).getNodeID());
            });
        });

        Go.setDisable(true);
        End.setDisable(true);
        Prev.setDisable(true);
        Next.setDisable(true);
        pathVertex = null;
        Instruction.setVisible(false);

        direction = null;
    }

    /**
     * Given a list of NodeEntries, returns the one closest to the current location
     * @param entries The list of NodeEntries
     * @param x the x coordinate of the mouse
     * @param y the y cordinate
     * @return the closest nodeentry
     * @author Alex Friedman (ahf)
     */
    private final NodeEntry getClosest(List<NodeEntry> entries, double x, double y)
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
     * Search a list of node and see if exist node with given ID
     * @param nodeEntries the list to be searched
     * @param nodeID the ID to be used in search
     * @return true if node exist in list, false otherwise
     * @author ZheCheng Song
     */
    private boolean hasNode(List<NodeEntry> nodeEntries, String nodeID){
        for(NodeEntry n : nodeEntries){
            if (n.getNodeID().equals(nodeID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles the pushing of a button on the screen
     * @param actionEvent the button's push
     * @throws IOException in case of scene switch, if the next fxml scene file cannot be found
     * @author ZheCheng Song
     */
    @FXML
    private void handleButtonPushed(ActionEvent actionEvent) throws IOException {

        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage
        Stage stage;
        Parent root;

        if (buttonPushed == goBack) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Default Page");
            stage.show();
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
        drawStartNode(startComboBox.getValue());
    }

    /**
     * Helper function used to draw the startNode with given ID, snatched from handleStartBoxAction()
     * @param nodeID the ID of the Node
     * @author Alex Friedman (ahf) / ZheCheng Song
     */
    private void drawStartNode(String nodeID) throws SQLException{
        final NodeEntry startNode = DatabaseAPI.getDatabaseAPI().getNode(ConnectionHandler.getConnection(),nodeID);
        if(startNode != null)
        {
            final DrawableNode drawableNode = startNode.getDrawable();
            drawableNode.setFill(UIConstants.NODE_COLOR);
            drawableNode.setRadius(10);

            mapPanel.draw(drawableNode);
            this.startNodeDisplay = drawableNode;
        }else {
            System.out.println("Can't find node!");
        }
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
        drawEndNode(endComboBox.getValue());
    }

    /**
     * Helper function used to draw the endNode with given ID, snatched from handleEndBoxAction()
     * @param nodeID the ID of the Node
     * @author Alex Friedman (ahf) / ZheCheng Song
     */
    private void drawEndNode(String nodeID) throws SQLException{
        final NodeEntry endNode = DatabaseAPI.getDatabaseAPI().getNode(ConnectionHandler.getConnection(),nodeID);
        if(endNode != null)
        {
            final DrawableNode drawableNode = endNode.getDrawable();
            drawableNode.setFill(Color.GREEN);
            drawableNode.setRadius(10);

            mapPanel.draw(drawableNode);

            this.endNodeDisplay = drawableNode;
        }
    }

    /**
     * This is used to clear the pathfinding drawn path.
     * @author Alex Friedman (ahf)
     */
    private void clearPath()
    {
        mapPanel.clearMap();
    }

    /**
     * This is used to re-render the A* path
     * @author Alex Friedman (ahf)
     */
    private boolean updatePath()
    {

        if(this.startNodeDisplay != null)
            mapPanel.draw(this.startNodeDisplay);
        if(this.endNodeDisplay != null)
            mapPanel.draw(this.endNodeDisplay);


        final Vertex startVertex = this.graph.getVertex(startComboBox.getValue());
        final Vertex endVertex = this.graph.getVertex(endComboBox.getValue());

        if(startVertex != null && endVertex != null && !startVertex.equals(endVertex))
        {
            final Path path = this.graph.getPath(startVertex, endVertex);
            pathVertex = null;
            if(path != null)
            {
                pathVertex = path.asList();
                drawPathFromIndex(0);
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
     * Helper function to draw the path starting from given index, input 0 as index to draw the whole path
     * snatched from updatePath()
     * @param index starting index
     * @author Alex Friedman (ahf) / ZheCheng Song
     */
    private void drawPathFromIndex(int index){
        final String currentFloor = mapPanel.getFloor().getValue();

        final Color LINE_STROKE_TRANSPARENT = new Color(UIConstants.LINE_COLOR.getRed(), UIConstants.LINE_COLOR.getGreen(), UIConstants.LINE_COLOR.getBlue(), 0.4);


        for (int i = index; i < pathVertex.size() - 1; i++)
        {
            final Vertex start = pathVertex.get(i);
            final Vertex end = pathVertex.get(i + 1);

            //int startX, int startY, int endX, int endY, String ID, String startFloor, String endFloor
            //FIXME: DO BETTER ID WHEN WE HAVE MULTIPLE PATH DIRECTIONS!!!
            final DrawableEdge edge = new DrawableEdge((int)start.getX(), (int)start.getY(), (int)end.getX(), (int)end.getY(), start.getID() + "_" + end.getID(), start.getFloor(), end.getFloor());
            // final Line line = new Line(start.getX()/zoomLevel, start.getY()/zoomLevel, end.getX()/zoomLevel, end.getY()/zoomLevel);
            edge.setStrokeWidth(UIConstants.LINE_STROKE_WIDTH);

            final LinearGradient lineGradient = new LinearGradient(edge.getStartX(), edge.getStartY(), edge.getEndX(), edge.getEndY(), false, CycleMethod.NO_CYCLE,
                    new Stop(0, (start.getFloor().equals(currentFloor) ? Color.ORANGE : LINE_STROKE_TRANSPARENT)),
                    new Stop(1, (end.getFloor().equals(currentFloor) ? Color.ORANGE : LINE_STROKE_TRANSPARENT)));

            edge.setStroke(lineGradient);

            mapPanel.draw(edge);
        }
    }

    /**
     * Used to check if our input is valid to run the pathfinding algorithm or not
     * @author Alex Friedman (ahf)
     */
    private void checkInput() {
        if (startComboBox.getValue() == null ||
                endComboBox.getValue() == null){
            clearPath();
        }else{
            clearPath();
            updatePath();
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
        stops = new ArrayList<>();
        instructions = new ArrayList<>();
        if(this.pathVertex == null) return;

        String floor = "";
        String type = "";
        String SEid = "";

        // Assert "Up" is forward for start
        double prevAngle = Math.toDegrees(Math.atan2(-1.0,0.0)) + 180;

        // Stair or Elevator search
        double distance = 0.0;
        boolean SEsearch = false;
        String prevDiret = "";

        for(int i = 0; i < pathVertex.size() - 1; i++) {
            Vertex curV = pathVertex.get(i);
            Vertex nexV = pathVertex.get(i + 1);

            NodeEntry curN = findNodeEntry(curV.getID());
            if (curN == null) return;

            // current node is stair or elevator
            if (curN.getNodeType().equals("STAI") || curN.getNodeType().equals("ELEV")) {
                // If have sequence of ELEV/STAI in a row
                if (curV.getID().substring(0,curV.getID().length()-1).equals(nexV.getID().substring(0,nexV.getID().length()-1))){
                    stops.add(i);
                    distance = calculateDistance(pathVertex, stops.get(stops.size()-2), stops.get(stops.size()-1));
                    instructions.add(prevDiret + " and walk " + Math.round(distance) + " m");

                    for (int j = i + 1 ; j < pathVertex.size() - 1; j++){
                        curV = pathVertex.get(j);
                        nexV = pathVertex.get(j + 1);

                        if (!curV.getID().substring(0,curV.getID().length()-1).equals(nexV.getID().substring(0,nexV.getID().length()-1))){
                            // Jumped to different floor by Stair or Elevator
                            curN = findNodeEntry(curV.getID());
                            if (curN == null) return;

                            if (curN.getNodeType().equals("STAI"))
                                type = "Stair";
                            else
                                type = "Elevator";

                            i = j + 1;
                            stops.add(i);
                            instructions.add("Take " + type + " to Floor " + nexV.getFloor());

                            curV = pathVertex.get(i);
                            nexV = pathVertex.get(i + 1);
                            prevAngle = Math.toDegrees(Math.atan2(nexV.getY() - curV.getY(), nexV.getX() - curV.getX())) + 180;
                            break;
                        }
                    }
                }
            }

            if( i == pathVertex.size() - 1)
                break;

            // Search Straight line on same floor
            double curAngle = Math.toDegrees(Math.atan2(nexV.getY() - curV.getY(), nexV.getX() - curV.getX())) + 180;
            double angle = curAngle + (360 - prevAngle);
            if (angle >= 360)
                angle -= 360;
             // small angle (45) alternation ignored
            if (angle <= 45 || angle >= 315) {
                if(i == 0){
                    prevDiret = "Look Forward";
                    stops.add(i);
                }
                // Skip
            } else {
                // Finished calculating distance after last turn
                stops.add(i);
                if(!prevDiret.equals("")){
                    distance = calculateDistance(pathVertex, stops.get(stops.size()-2), stops.get(stops.size()-1));
                    instructions.add(prevDiret + " and walk " + Math.round(distance) + " m");
                }

                if (Math.abs(Math.abs(curAngle - prevAngle) - 180) <= 30) {
                    prevDiret = "Turn around";
                } else if (angle < 180) {
                    prevDiret = "Turn right";
                } else {
                    prevDiret = "Turn left";
                }
                prevAngle = curAngle;
            }
        }
        stops.add(pathVertex.size() - 1);
        distance = calculateDistance(pathVertex, stops.get(stops.size()-2), stops.get(stops.size()-1));
        if(!prevDiret.equals("")) instructions.add(prevDiret + " and walk " + Math.round(distance) + " m");
        instructions.add("Reach Destination!");

        System.out.println(pathVertex);
        System.out.println(instructions);
        System.out.println(stops);
    }

    String curD;
    private void drawDirection() throws SQLException {
        if(direction != null)
            mapPanel.unDraw(this.direction.getId());
        Vertex curV = pathVertex.get(stops.get(curStep));
        //DrawableNode node = new DrawableNode(Integer.parseInt(curV.getX()), curV.getY(), curV.getID(), curV.getFloor());
        //if(curN == null)
        //    return;
        //direction = curN.getDrawable();
        direction.setId("direction");
        if(curD.equals("UP")){
            double Y = direction.getCenterY() - 10.0;
            direction.setCenterY(Y);
        }else if(curD.equals("LEFT")){
            direction.setCenterX(direction.getCenterX() - 10.0);
        }else if(curD.equals("RIGHT")){
            direction.setCenterX(direction.getCenterX() + 10.0);
        }else if(curD.equals("DOWN")){
            direction.setCenterY(direction.getCenterY() + 10.0);
        }
        direction.setFill(Color.RED);
        direction.setRadius(5);

        mapPanel.draw(direction);
    }

    private void changeDirection(String inst){
        String instruction[] = inst.split(" ");
        System.out.println(instruction);
        if(!instruction[0].equals("Take") && !instruction[0].equals("Look")){
            if(instruction[1].equals("around")){
                switch (curD) {
                    case "UP" : curD = "DOWN"; break;
                    case "LEFT" : curD = "RIGHT"; break;
                    case "RIGHT" : curD = "LEFT"; break;
                    case "DOWN" : curD = "UP"; break;
                }
            }else if(instruction[1].equals("left")){
                switch (curD) {
                    case "UP" : curD = "LEFT"; break;
                    case "LEFT" : curD = "DOWN"; break;
                    case "RIGHT" : curD = "UP"; break;
                    case "DOWN" : curD = "RIGHT"; break;
                }
            }else if(instruction[1].equals("right")){
                switch (curD) {
                    case "UP" : curD = "RIGHT"; break;
                    case "LEFT" : curD = "UP"; break;
                    case "RIGHT" : curD = "DOWN"; break;
                    case "DOWN" : curD = "LEFT"; break;
                }
            }
        }
    }

    /**
     * Function to react to 'Start Navigation' button being pressed and start the route stepper
     * @param actionEvent
     * @throws SQLException
     * @author ZheCheng Song
     */
    public void startNavigation(ActionEvent actionEvent) throws SQLException {
        startComboBox.setDisable(true);
        endComboBox.setDisable(true);
        Go.setDisable(true);
        Next.setDisable(false);
        End.setDisable(false);
        Instruction.setVisible(true);
        curStep = 0;
        pathFinding = true;

        parseRoute();
        mapPanel.switchMap(pathVertex.get(0).getFloor());
        clearPath();
        drawStartNode(pathVertex.get(0).getID());
        drawEndNode(pathVertex.get(pathVertex.size()-1).getID());
        drawPathFromIndex(curStep);
        Instruction.setText(instructions.get(curStep));
        mapPanel.centerNode(startNodeDisplay);
        curFloor = pathVertex.get(0).getFloor();

        //curD = "UP";
        //drawDirection();
    }

    /**
     * Function to react to 'Prev' button being pressed and go to the previous point with stepper
     * @param actionEvent
     * @throws SQLException
     * @author ZheCheng Song
     */
    public void goToPrevNode(ActionEvent actionEvent) throws SQLException {
        clearPath();
        curStep--;
        if(curStep == 0){
            Prev.setDisable(true);
        }
        else {
            Prev.setDisable(false);
            Next.setDisable(false);
        }
        if(!pathVertex.get(curStep).getFloor().equals(curFloor)){
            mapPanel.switchMap(pathVertex.get(stops.get(curStep)).getFloor());
        }
        curFloor = pathVertex.get(stops.get(curStep)).getFloor();
        drawStartNode(pathVertex.get(stops.get(curStep)).getID());
        drawEndNode(pathVertex.get(pathVertex.size() - 1).getID());
        drawPathFromIndex(stops.get(curStep));
        Instruction.setText(instructions.get(curStep));
        mapPanel.centerNode(startNodeDisplay);
    }

    /**
     * Function to react to 'Next' button being pressed and go to the next point with stepper
     * @param actionEvent
     * @throws SQLException
     * @author ZheCheng Song
     */
    public void goToNextNode(ActionEvent actionEvent) throws SQLException {
        clearPath();
        curStep++;
        if(curStep == Math.min(stops.size() - 1, instructions.size() - 1)){
            Next.setDisable(true);
        }
        else {
            drawEndNode(pathVertex.get(pathVertex.size()-1).getID());
            Prev.setDisable(false);
            Next.setDisable(false);
        }
        if(!pathVertex.get(stops.get(curStep)).getFloor().equals(curFloor)){
            mapPanel.switchMap(pathVertex.get(stops.get(curStep)).getFloor());
        }
        curFloor = pathVertex.get(stops.get(curStep)).getFloor();
        drawStartNode(pathVertex.get(stops.get(curStep)).getID());
        drawPathFromIndex(stops.get(curStep));
        Instruction.setText(instructions.get(curStep));
        mapPanel.centerNode(startNodeDisplay);
    }

    /**
     * Function to react to 'End Navigation' button being pressed and stop the stepper
     * @param actionEvent
     * @throws SQLException
     * @author ZheCheng Song
     */
    public void endNavigation(ActionEvent actionEvent) throws SQLException {
        startComboBox.setDisable(false);
        endComboBox.setDisable(false);
        Go.setDisable(false);
        Prev.setDisable(true);
        Next.setDisable(true);
        End.setDisable(true);
        Instruction.setVisible(false);
        curStep = 0;
        pathFinding = false;

        mapPanel.switchMap(pathVertex.get(0).getFloor());
        clearPath();
        drawStartNode(pathVertex.get(0).getID());
        drawEndNode(pathVertex.get(pathVertex.size()-1).getID());
        drawPathFromIndex(curStep);
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
        return sumDist;
    }
}
