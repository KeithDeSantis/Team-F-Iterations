package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.EdgeEntry;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamF.pathfinding.GraphLoader;
import edu.wpi.cs3733.D21.teamF.pathfinding.Path;
import edu.wpi.cs3733.D21.teamF.pathfinding.Vertex;
import edu.wpi.cs3733.D21.teamF.states.DefaultPageState;
import edu.wpi.cs3733.D21.teamF.states.SceneContext;
import edu.wpi.cs3733.D21.teamF.utils.UIConstants;
import edu.wpi.cs3733.uicomponents.MapPanel;
import edu.wpi.cs3733.uicomponents.entities.DrawableEdge;
import edu.wpi.cs3733.uicomponents.entities.DrawableNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
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


    /**
     * These are done for displaying the start & end nodes. This should be done better (eventually)
     *
     * @author Alex Friedman (ahf)
     */
    private DrawableNode startNodeDisplay;
    private DrawableNode endNodeDisplay;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //ahf - yes this should be done better. At some point.

        List<NodeEntry> allNodeEntries = new ArrayList<>();
        try {
            allNodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries();
            List<EdgeEntry> allEdgeEntries = DatabaseAPI.getDatabaseAPI().genEdgeEntries();

            final List<NodeEntry> nodeEntries = allNodeEntries.stream().collect(Collectors.toList());

            final List<EdgeEntry> edgeEntries = allEdgeEntries.stream().filter(node -> hasNode(nodeEntries, node.getStartNode())
                    && hasNode(nodeEntries, node.getEndNode())).collect(Collectors.toList());
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

        final ContextMenu contextMenu = new ContextMenu();

        //FIXME: CHANGE TEXT TO BE MORE ACCESSABLE
        final MenuItem startPathfind = new MenuItem("Path from Here");
        final MenuItem endPathfind = new MenuItem("Path end Here");

        contextMenu.getItems().addAll(startPathfind, endPathfind);


        List<NodeEntry> finalAllNodeEntries = allNodeEntries;

        mapPanel.getMap().setOnContextMenuRequested(event -> {
            contextMenu.show(mapPanel.getMap(), event.getScreenX(), event.getScreenY());

            final double zoomLevel = mapPanel.getZoomLevel().getValue();

            startPathfind.setOnAction((ActionEvent e) -> {
                startComboBox.setValue(getClosest(finalAllNodeEntries, event.getX() * zoomLevel, event.getY() * zoomLevel).getNodeID());
            });

            endPathfind.setOnAction(e -> {
                endComboBox.setValue(getClosest(finalAllNodeEntries, event.getX() * zoomLevel, event.getY() * zoomLevel).getNodeID());
            });
        });

    }

    /**
     * Given a list of NodeEntries, returns the one closest to the current location
     *
     * @param entries The list of NodeEntries
     * @param x       the x coordinate of the mouse
     * @param y       the y cordinate
     * @return the closest nodeentry
     * @author Alex Friedman (ahf)
     */
    private final NodeEntry getClosest(List<NodeEntry> entries, double x, double y) {
        double minDist2 = Integer.MAX_VALUE;
        NodeEntry closest = null;

        for (NodeEntry nodeEntry : entries) {
            if (!nodeEntry.getFloor().equals(mapPanel.getFloor().getValue()))
                continue;

            final double currDist2 = Math.pow(x - Integer.parseInt(nodeEntry.getXcoord()), 2) + Math.pow(y - Integer.parseInt(nodeEntry.getYcoord()), 2);

            if (currDist2 < minDist2) {
                minDist2 = currDist2;
                closest = nodeEntry;
            }
        }
        return closest;
    }

    private boolean hasNode(List<NodeEntry> nodeEntries, String nodeID) {
        for (NodeEntry n : nodeEntries) {
            if (n.getNodeID().equals(nodeID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles the pushing of a button on the screen
     *
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
            DefaultPageState.getDefaultPageState().switchScene(SceneContext.getSceneContext());
            /*stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Default Page");
            stage.show();

             */
        }
    }

    /**
     * @author Alex Friedman (ahf)
     */
    @FXML
    public void handleStartBoxAction() throws SQLException {
        checkInput();
        if (this.startNodeDisplay != null)
            mapPanel.unDraw(this.startNodeDisplay.getId());


        final NodeEntry startNode = DatabaseAPI.getDatabaseAPI().getNode(startComboBox.getValue());
        if (startNode != null) {
            final DrawableNode drawableNode = startNode.getDrawable();
            drawableNode.setFill(UIConstants.NODE_COLOR);
            drawableNode.setRadius(10);

            mapPanel.draw(drawableNode);
            this.startNodeDisplay = drawableNode;
        }
    }

    /**
     * @author Alex Friedman (ahf)
     */
    @FXML
    public void handleEndBoxAction() throws SQLException {
        checkInput();
        if (this.endNodeDisplay != null)
            mapPanel.unDraw(this.endNodeDisplay.getId());


        final NodeEntry endNode = DatabaseAPI.getDatabaseAPI().getNode(endComboBox.getValue());
        if (endNode != null) {
            final DrawableNode drawableNode = endNode.getDrawable();
            drawableNode.setFill(Color.GREEN);
            drawableNode.setRadius(10);

            mapPanel.draw(drawableNode);

            this.endNodeDisplay = drawableNode;
        }
    }

    /**
     * This is used to clear the pathfinding drawn path.
     *
     * @author Alex Friedman (ahf)
     */
    private void clearPath() {
        mapPanel.clearMap();
    }

    /**
     * This is used to re-render the A* path
     *
     * @author Alex Friedman (ahf)
     */
    private boolean updatePath() {

        if (this.startNodeDisplay != null)
            mapPanel.draw(this.startNodeDisplay);
        if (this.endNodeDisplay != null)
            mapPanel.draw(this.endNodeDisplay);

        final String currentFloor = mapPanel.getFloor().getValue();

        final Color LINE_STROKE_TRANSPARENT = new Color(UIConstants.LINE_COLOR.getRed(), UIConstants.LINE_COLOR.getGreen(), UIConstants.LINE_COLOR.getBlue(), 0.4);

        final Vertex startVertex = this.graph.getVertex(startComboBox.getValue());
        final Vertex endVertex = this.graph.getVertex(endComboBox.getValue());

        if (startVertex != null && endVertex != null && !startVertex.equals(endVertex)) {
            final Path path = this.graph.getPath(startVertex, endVertex);
            if (path != null) {
                final List<Vertex> pathData = path.asList();
                for (int i = 0; i < pathData.size() - 1; i++) {
                    final Vertex start = pathData.get(i);
                    final Vertex end = pathData.get(i + 1);

                    //int startX, int startY, int endX, int endY, String ID, String startFloor, String endFloor
                    //FIXME: DO BETTER ID WHEN WE HAVE MULTIPLE PATH DIRECTIONS!!!
                    final DrawableEdge edge = new DrawableEdge((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY(), start.getID() + "_" + end.getID(), start.getFloor(), end.getFloor());
                    // final Line line = new Line(start.getX()/zoomLevel, start.getY()/zoomLevel, end.getX()/zoomLevel, end.getY()/zoomLevel);
                    edge.setStrokeWidth(UIConstants.LINE_STROKE_WIDTH);

                    final LinearGradient lineGradient = new LinearGradient(edge.getStartX(), edge.getStartY(), edge.getEndX(), edge.getEndY(), false, CycleMethod.NO_CYCLE,
                            new Stop(0, (start.getFloor().equals(currentFloor) ? Color.ORANGE : LINE_STROKE_TRANSPARENT)),
                            new Stop(1, (end.getFloor().equals(currentFloor) ? Color.ORANGE : LINE_STROKE_TRANSPARENT)));

                    edge.setStroke(lineGradient);

                    mapPanel.draw(edge);
                }
                return true;
            }
        } else {
            //FIXME: INFORM USER OF ERROR
        }

        return false; //We had an error
    }

    /**
     * Used to check if our input is valid to run the pathfinding algorithm or not
     *
     * @author Alex Friedman (ahf)
     */
    private void checkInput() {
        if (startComboBox.getValue() == null ||
                endComboBox.getValue() == null) {
            clearPath();

        } else {
            clearPath();
            updatePath();
        }
    }
}
