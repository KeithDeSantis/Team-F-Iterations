package edu.wpi.fuchsiafalcons.controllers;

import edu.wpi.fuchsiafalcons.database.ConnectionHandler;
import edu.wpi.fuchsiafalcons.database.DatabaseAPI;
import edu.wpi.fuchsiafalcons.entities.EdgeEntry;
import edu.wpi.fuchsiafalcons.entities.NodeEntry;
import edu.wpi.fuchsiafalcons.pathfinding.Graph;
import edu.wpi.fuchsiafalcons.pathfinding.GraphLoader;
import edu.wpi.fuchsiafalcons.pathfinding.Path;
import edu.wpi.fuchsiafalcons.pathfinding.Vertex;
import edu.wpi.fuchsiafalcons.utils.UIConstants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AStarDemoController implements Initializable {

    @FXML
    private Button goBack;
    @FXML
    private ImageView map;
    @FXML
    private ScrollPane scroll;
    @FXML
    private Pane canvas;

    @FXML
    private ComboBox<String> startComboBox;

    @FXML
    private ComboBox<String> endComboBox;

    @FXML
    private Button pathfindButton;

    //FIXME: DO BETTER
    private Graph graph;
    
    private double zoomLevel = 4.0;

    /**
     * These are done for displaying the start & end nodes. This should be done better (eventually)
     * @author Alex Friedman (ahf)
     */
    private Circle startNodeDisplay;
    private Circle endNodeDisplay;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        map.setPreserveRatio(true);

        final Image image = new Image(getClass().getResourceAsStream("/maps/01_thefirstfloor.png"));

        final double width = image.getWidth()/zoomLevel;
        final double height = image.getHeight()/zoomLevel;

        canvas.setPrefSize(width,height);
        map.setFitWidth(width);
        map.setFitHeight(height);


        map.setImage(image);
        //ahf - yes this should be done better. At some point.

        try {
            List<NodeEntry> allNodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries(ConnectionHandler.getConnection());
            List<EdgeEntry> allEdgeEntries = DatabaseAPI.getDatabaseAPI().genEdgeEntries(ConnectionHandler.getConnection());

            final List<NodeEntry> nodeEntries = allNodeEntries.stream().filter(node -> node.getFloor().equals("1")
            && !node.getBuilding().equals("Shapiro") && !node.getBuilding().equals("BTM")).collect(Collectors.toList());

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
        pathfindButton.setDisable(true);
    }

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
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/DefaultPageView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Default Page");
            stage.show();
        }
    }

    /**
     * Handles the pathfind btn.
     * @author Alex Friedman (ahf), Tony Vuolo
     */
    @FXML
    public void handlePathfindButtonClicked() {
        clearPath();
        updatePath();
    }

    /**
     *
     * @author Alex Friedman (ahf)
     */
    @FXML
    public void handleStartBoxAction() {
        checkInput();
        if(this.startNodeDisplay == null)
        {
            this.startNodeDisplay = new Circle();
            this.startNodeDisplay.setFill(UIConstants.NODE_COLOR);
            this.startNodeDisplay.setRadius(UIConstants.NODE_RADIUS);

            this.canvas.getChildren().add(this.startNodeDisplay);
        }

        final Vertex startVertex = this.graph.getVertex(startComboBox.getValue());
        if(startVertex != null) {
            this.startNodeDisplay.setCenterX(startVertex.getX() / this.zoomLevel);
            this.startNodeDisplay.setCenterY(startVertex.getY() / this.zoomLevel);
        }

    }

    /**
     *
     * @author Alex Friedman (ahf)
     */
    @FXML
    public void handleEndBoxAction() {
        checkInput();
        if(this.endNodeDisplay == null)
        {
            this.endNodeDisplay = new Circle();
            this.endNodeDisplay.setFill(Color.GREEN); //FIXME: Choose different colors b/c of colorblindness
            this.endNodeDisplay.setRadius(10.0);

            this.canvas.getChildren().add(this.endNodeDisplay);
        }

        final Vertex endVertex = this.graph.getVertex(endComboBox.getValue());
        if(endVertex != null) {
            this.endNodeDisplay.setCenterX(endVertex.getX() / this.zoomLevel);
            this.endNodeDisplay.setCenterY(endVertex.getY() / this.zoomLevel);
        }

    }

    /**
     * This is used to clear the pathfinding drawn path.
     * @author Alex Friedman (ahf)
     */
    private void clearPath()
    {
        canvas.getChildren().removeIf(x -> x instanceof Line);
    }

    /**
     * This is used to re-render the A* path
     * @author Alex Friedman (ahf)
     */
    private boolean updatePath()
    {
        final String currentFloor = "1"; //FIXME: DO BETTER<

        final Color LINE_STROKE_TRANSPARENT = new Color(UIConstants.LINE_COLOR.getRed(), UIConstants.LINE_COLOR.getGreen(), UIConstants.LINE_COLOR.getBlue(), 0.4);

        final Vertex startVertex = this.graph.getVertex(startComboBox.getValue());
        final Vertex endVertex = this.graph.getVertex(endComboBox.getValue());

        if(startVertex != null && endVertex != null && !startVertex.equals(endVertex))
        {
            final Path path = this.graph.getPath(startVertex, endVertex);
            if(path != null)
            {
                final List<Vertex> pathData = path.asList();
                for (int i = 0; i < pathData.size() - 1; i++)
                {
                    final Vertex start = pathData.get(i);
                    final Vertex end = pathData.get(i + 1);

                    final Line line = new Line(start.getX()/zoomLevel, start.getY()/zoomLevel, end.getX()/zoomLevel, end.getY()/zoomLevel);
                    line.setStrokeWidth(UIConstants.LINE_STROKE_WIDTH);

                    final LinearGradient lineGradient = new LinearGradient(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY(), false, CycleMethod.NO_CYCLE,
                            new Stop(0, (start.getFloor().equals(currentFloor) ? Color.ORANGE : LINE_STROKE_TRANSPARENT)),
                            new Stop(1, (end.getFloor().equals(currentFloor) ? Color.ORANGE : LINE_STROKE_TRANSPARENT)));

                    line.setStroke(lineGradient);

                    canvas.getChildren().add(line);
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
     * Used to check if our input is valid to run the pathfinding algorithm or not
     * @author Alex Friedman (ahf)
     */
    private void checkInput() {
        if (startComboBox.getValue() == null ||
                endComboBox.getValue() == null){
            pathfindButton.setDisable(true);
            clearPath();

        }else{
            pathfindButton.setDisable(false);

            clearPath();
            updatePath();
        }
    }
}
