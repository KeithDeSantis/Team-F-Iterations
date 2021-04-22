package edu.wpi.fuchsiafalcons.controllers;

import edu.wpi.fuchsiafalcons.pathfinding.Graph;
import edu.wpi.fuchsiafalcons.pathfinding.GraphLoader;
import edu.wpi.fuchsiafalcons.pathfinding.Path;
import edu.wpi.fuchsiafalcons.pathfinding.Vertex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
    public Label titleLabel;
    @FXML
    private Button goBack;
    @FXML
    private ImageView map;

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

    private double height, width;

    /**
     * These are done for displaying the start & end nodes. This should be done better (eventually)
     * @author Alex Friedman (ahf)
     */
    private Circle startNodeDisplay;
    private Circle endNodeDisplay;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        map.toBack();
        map.setPreserveRatio(true);

        final Image image = new Image(getClass().getResourceAsStream("/maps/01_thefirstfloor.png"));

        width = image.getWidth()/zoomLevel;
        height = image.getHeight()/zoomLevel;

        canvas.setPrefSize(width,height);
        map.setFitWidth(width);
        map.setFitHeight(height);


        map.setImage(image);
        //ahf - yes this should be done better. At some point.

        try {
            this.graph = GraphLoader.load("nodes_1.csv", "edges.csv");
        } catch (Exception e) {
            this.graph = new Graph();
            e.printStackTrace();
            //return;
        }

        final ObservableList<String> nodeList = FXCollections.observableArrayList();
        nodeList.addAll(this.graph.getVertices().stream().map(vertex -> vertex.getID()).collect(Collectors.toList()));

        startComboBox.setItems(nodeList);
        endComboBox.setItems(nodeList);
        pathfindButton.setDisable(true);
        initializeDrag();
    }

    private double OFFSET_X = 0, OFFSET_Y = 0;

    /**
     * Creates all EventHandlers for the drag zoom functionality
     */
    public void initializeDrag() {
        final double MIN_X = -20, MAX_X = 920, MIN_Y = 10, MAX_Y = 485;
        EventHandler<MouseEvent> pressHandler = pressEvent -> {
            EventHandler<MouseEvent> dragHandler = dragEvent -> {
                map.setTranslateX(median(
                        dragEvent.getSceneX() - pressEvent.getSceneX(),
                         MIN_X - map.getLayoutX() - OFFSET_X,
                        -map.getLayoutX() + MAX_X - width - OFFSET_X
                ));

                map.setTranslateY(median(
                        dragEvent.getSceneY() - pressEvent.getSceneY(),
                        MIN_Y - map.getLayoutY() - OFFSET_Y,
                        -map.getLayoutY() + MAX_Y - height - OFFSET_Y
                ));
            };
            map.addEventFilter(MouseEvent.MOUSE_DRAGGED, dragHandler);
            EventHandler<MouseEvent> releaseHandler = releaseEvent -> {
                map.setLayoutX(map.getLayoutX() + map.getTranslateX());
                map.setLayoutY(map.getLayoutY() + map.getTranslateY());
                map.setTranslateX(0);
                map.setTranslateY(0);
            };
            map.addEventFilter(MouseEvent.MOUSE_RELEASED, releaseHandler);
        };
        map.addEventFilter(MouseEvent.MOUSE_PRESSED, pressHandler);

        final double SENSITIVITY = 0.001;
        EventHandler<ScrollEvent> scrollHandler = scrollEvent -> {
            final double ZOOM_FACTOR = median(0.4, 2,map.getScaleX() - scrollEvent.getDeltaY() * SENSITIVITY);
            final double RATIO = ZOOM_FACTOR / map.getScaleX();
            map.setScaleX(ZOOM_FACTOR);
            map.setScaleY(ZOOM_FACTOR);
            height *= RATIO;
            width *= RATIO;
            OFFSET_X -= (width - width / RATIO) / 2;
            OFFSET_Y -= (height - height / RATIO) / 2;

            final double RADIAL_DISTANCE_X = (OFFSET_X + map.getLayoutX() + width/2 - scrollEvent.getSceneX());
            final double RADIAL_DISTANCE_Y = (OFFSET_Y + map.getLayoutY() + height/2 - scrollEvent.getSceneY());
            map.setLayoutX(map.getLayoutX() + (RADIAL_DISTANCE_X - RADIAL_DISTANCE_X / RATIO));
            map.setLayoutY(map.getLayoutY() + (RADIAL_DISTANCE_Y - RADIAL_DISTANCE_Y / RATIO));
//            if(RATIO >= 1) {
//            } else {
//                map.setLayoutX(median(map.getLayoutX() + (RADIAL_DISTANCE_X - RADIAL_DISTANCE_X / RATIO),
//                        MIN_X - OFFSET_X, MAX_X - width - OFFSET_X));
//                map.setLayoutY(median(map.getLayoutY() + (RADIAL_DISTANCE_Y - RADIAL_DISTANCE_Y / RATIO),
//                        MIN_Y - OFFSET_Y, MAX_Y - height - OFFSET_Y));
//            }

            System.out.println(
                    RADIAL_DISTANCE_X
                    + " " +
                    RADIAL_DISTANCE_Y
            );
        };
        map.addEventHandler(ScrollEvent.SCROLL, scrollHandler);
    }

    /**
     * Finds the median of three values
     * @param a the first value
     * @param b the second value
     * @param c the third value
     * @return the middle value
     */
    public double median(double a, double b, double c) {
        return a + b + c - Math.max(a, Math.max(b, c)) - Math.min(a, Math.min(b, c));
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
    public void handlePathfindButtonClicked(ActionEvent actionEvent) {

        final Vertex startVertex = this.graph.getVertex(startComboBox.getValue());
        final Vertex endVertex = this.graph.getVertex(endComboBox.getValue());

       //canvas.getChildren().clear();

        canvas.getChildren().removeIf(x -> {
            return x instanceof Line;
        });

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
                    line.setStrokeWidth(2);
                    line.setStroke(Color.ORANGE);

                    canvas.getChildren().add(line);
                }
            }
        }
        else
        {
            //FIXME: INFORM USER OF ERROR
        }

    }

    /**
     *
     * @param actionEvent
     * @author Alex Friedman (ahf)
     */
    @FXML
    public void handleStartBoxAction(ActionEvent actionEvent) {
        checkInput();
        if(this.startNodeDisplay == null)
        {
            this.startNodeDisplay = new Circle();
            this.startNodeDisplay.setFill(Color.BLUE);
            this.startNodeDisplay.setRadius(7.0);

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
     * @param actionEvent
     * @author Alex Friedman (ahf)
     */
    @FXML
    public void handleEndBoxAction(ActionEvent actionEvent) {
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

    public void checkInput() {
        if (startComboBox.getValue() == null ||
                endComboBox.getValue() == null){
            pathfindButton.setDisable(true);
        }else{
            pathfindButton.setDisable(false);
        }
    }
}
