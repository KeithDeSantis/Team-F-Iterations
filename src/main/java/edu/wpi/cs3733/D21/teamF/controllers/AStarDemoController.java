package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.CurrentUser;
import edu.wpi.cs3733.D21.teamF.entities.EdgeEntry;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
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
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AStarDemoController extends AbsController implements Initializable {

    @FXML
    private ImageView goBack;

    @FXML
    private MapPanel mapPanel;

    @FXML
    private JFXButton Go;

    @FXML
    private JFXButton Prev;

    @FXML
    private JFXButton Next;

    @FXML
    private JFXButton about;

    @FXML
    private JFXButton clear;

    @FXML
    private Label Instruction;

    @FXML
    private Label ETA;

    @FXML
    private ImageView navIcon;

    @FXML
    public JFXButton viewInstructionsBtn;

    @FXML
    public JFXToggleButton optimize;

    @FXML
    private JFXTreeView<String> treeView;

    //FIXME: DO BETTER
    private Graph graph;

    private final int MAX_RECENTLY_USED = 5;

    private static final double PIXEL_TO_METER_RATIO = 10;

    /**
     * These are done for displaying the start & end nodes. This should be done better (eventually)
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

    // List of intermediate vertices for multi-stop pathfinding - LM
    private final ArrayList<Vertex> vertices = new ArrayList<>();
    private final SimpleStringProperty startNode = new SimpleStringProperty("");
    private final SimpleStringProperty endNode = new SimpleStringProperty("");

    private DrawableNode direction;

    private String currentDirection;

    final ObservableList<String> nodeList = FXCollections.observableArrayList();


    // Create root tree item (will be hidden later)
    TreeItem<String> rootTreeViewItem = new TreeItem<>("shortNames");
    // List of all nodes in each category
    TreeItem<String> conferenceItem = new TreeItem<>("Conference Rooms");
    TreeItem<String> departmentItem = new TreeItem<>("Departments");
    TreeItem<String> entranceItem = new TreeItem<>("Entrances");
    TreeItem<String> infoItem = new TreeItem<>("Information");
    TreeItem<String> labItem = new TreeItem<>("Labs");
    TreeItem<String> parkingItem = new TreeItem<>("Parking");
    TreeItem<String> restroomItem = new TreeItem<>("Restrooms");
    TreeItem<String> retailItem = new TreeItem<>("Retail");
    TreeItem<String> serviceItem = new TreeItem<>("Services");
    TreeItem<String> favoriteItem = new TreeItem<>("Favorites");
    TreeItem<String> recentItem = new TreeItem<>("Recently Used");





    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println(CurrentUser.getCurrentUser().getUuid());

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

        isCurrentlyNavigating.set(false);

        final ContextMenu contextMenu = new ContextMenu();
        final ContextMenu treeViewMenu = new ContextMenu();

        //FIXME: CHANGE TEXT TO BE MORE ACCESSIBLE
        final MenuItem startPathMenu = new MenuItem("Path From Here");
        final MenuItem addStopMenu = new MenuItem("Add Stop Here");
        final MenuItem endPathMenu = new MenuItem("Path End Here");
        final MenuItem whatsHereMenu = new MenuItem("What's Here?");
        final MenuItem addFavoriteMenu = new MenuItem("Add Favorite"); // only added when signed in

        contextMenu.getItems().addAll(startPathMenu,addStopMenu, endPathMenu, new SeparatorMenuItem(), whatsHereMenu);

        // if signed in add favorite option
        if(CurrentUser.getCurrentUser().isAuthenticated()){
            contextMenu.getItems().add(addFavoriteMenu);
        }

        mapPanel.getMap().setOnContextMenuRequested(event -> {
                    if (isCurrentlyNavigating.get()) {
                        return;
                    }

                    final double zoomLevel = mapPanel.getZoomLevel().getValue();
                    final NodeEntry currEntry = getClosest(event.getX() * zoomLevel, event.getY() * zoomLevel);

                    if (currEntry == null)
                        return;

                    // Set whats here menu text back to what it should be on the map
                    whatsHereMenu.setText("What's Here?");

                    // Set add stop text to make sense
                    if (vertices.contains(graph.getVertex(currEntry.getNodeID()))) {
                        addStopMenu.setText("Remove Stop");
                    } else {
                        addStopMenu.setText("Add Stop");
                    }
                    if(CurrentUser.getCurrentUser().getUuid() == null && CurrentUser.getCurrentUser().isAuthenticated()) {
                        try {
                            if (getUserFavorites().contains(currEntry.getNodeID())) {
                                addFavoriteMenu.setText("Remove Favorite");
                            } else {
                                addFavoriteMenu.setText("Add To Favorites");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }


                    contextMenu.show(mapPanel.getMap(), event.getScreenX(), event.getScreenY());

                    // Sets the start node and removes the old start node from the list (re-added in updatePath()) - LM
                    // Combo box updates already call checkInput() so calling it for set start and end nodes here is redundant
                    startPathMenu.setOnAction(e -> {
                        startNode.set(idToShortName(currEntry.getNodeID()));
                        try {
                            handleStartNodeChange();
                        } catch (SQLException sqlException) {
                            sqlException.printStackTrace();
                        }
                    });

                    // When adding a new stop, the vertex is added to the intermediate vertex list and the path is redrawn - LM
                    // No combo box update so we call checkInput()
                    addStopMenu.setOnAction(e -> {
                        if(addStopMenu.getText().equals("Add Stop")) {
                            vertices.add(graph.getVertex(currEntry.getNodeID()));
                            drawStop(currEntry);
                            try {
                                addNodeToRecent(currEntry);
                            } catch (SQLException sqlException) {
                                sqlException.printStackTrace();
                            }
                        } else {
                            vertices.remove(graph.getVertex(currEntry.getNodeID()));
                            mapPanel.unDraw(currEntry.getNodeID());
                            getDrawableNode(currEntry.getNodeID());
                        }
                        checkInput();
                    });

                    // Sets the end node and removed the previous node from the list (re-added in updatePath()) - LM
                    endPathMenu.setOnAction(e -> {
                        endNode.set(idToShortName(currEntry.getNodeID()));
                        try {
                            handleEndNodeChange();
                        } catch (SQLException sqlException) {
                            sqlException.printStackTrace();
                        }
                    });

                    //FIXME: Make these ones require that thing is visible
                    whatsHereMenu.setOnAction(e -> {
                        mapPanel.switchMap(currEntry.getFloor());
                        mapPanel.centerNode(mapPanel.getNode(currEntry.getNodeID())); //FIXME: DO on all?

                        final JFXDialog dialog = new JFXDialog();
                        final JFXDialogLayout layout = new JFXDialogLayout();


                        layout.setHeading(new Text(currEntry.getLongName()));

                        //FIXME: DO BREAKS W/ CSS
                        layout.setBody(new Text(currEntry.getDescription()));

                        final JFXButton closeBtn = new JFXButton("Close");
                        closeBtn.setOnAction(a -> dialog.close());

                        final JFXButton directionsTo = new JFXButton("Direction To");
                        directionsTo.setOnAction(a -> {
                            endNode.set(idToShortName(currEntry.getNodeID()));
                            try {
                                handleEndNodeChange();
                            } catch (SQLException sqlException) {
                                sqlException.printStackTrace();
                            }
                            dialog.close();
                        });

                        final JFXButton directionsFrom = new JFXButton("Directions From");
                        directionsFrom.setOnAction(a -> {
                            startNode.set(idToShortName(currEntry.getNodeID()));
                            try {
                                handleStartNodeChange();
                            } catch (SQLException sqlException) {
                                sqlException.printStackTrace();
                            }
                            dialog.close();
                        });

                        if(CurrentUser.getCurrentUser().isAuthenticated()) {
                            final JFXButton toggleFavorite = new JFXButton("Add To Favorites");
                            try{
                                if(getUserFavorites().contains(currEntry.getNodeID())){
                                    toggleFavorite.setText("Remove Favorite");
                                }
                            } catch (SQLException sqlException){
                                sqlException.printStackTrace();
                            }
                            toggleFavorite.setOnAction(a -> {
                                if (toggleFavorite.getText().equals("Add To Favorites")) {
                                    try {
                                        addNodeToFavorites(currEntry);
                                    } catch (SQLException sqlException) {
                                        sqlException.printStackTrace();
                                    }
                                } else {
                                    try {
                                        removeNodeFromFavorites(currEntry);
                                    } catch (SQLException sqlException) {
                                        sqlException.printStackTrace();
                                    }
                                }
                                dialog.close();
                            });
                            layout.setActions(toggleFavorite, directionsTo, directionsFrom, closeBtn);
                        } else {
                            layout.setActions(directionsTo, directionsFrom, closeBtn);
                        }

                        dialog.setContent(layout);
                        mapPanel.showDialog(dialog);
                    });

                    addFavoriteMenu.setOnAction(e -> {
                        if (addFavoriteMenu.getText().equals("Add To Favorites")) {
                            try {
                                addNodeToFavorites(currEntry);
                            } catch (SQLException sqlException) {
                                sqlException.printStackTrace();
                            }
                        } else {
                            try {
                                removeNodeFromFavorites(currEntry);
                            } catch (SQLException sqlException) {
                                sqlException.printStackTrace();
                            }
                        }
                    });
                });

        Go.setDisable(true);
        Prev.setVisible(false);
        Next.setVisible(false);
        pathVertex.clear();
        Instruction.setVisible(false);
        navIcon.setVisible(false);
        ETA.setVisible(false);

        viewInstructionsBtn.visibleProperty().bind(ETA.visibleProperty());

        direction = null;

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

        //~~~~~~~~~ Tree View Setup ~~~~~~~~

        // categorize node short names and add them to appropriate tree view (root items declared before initialize)
        for (NodeEntry node: allNodeEntries) {
            switch (node.getNodeType()){
                case "CONF":
                    conferenceItem.getChildren().add(new TreeItem<>(node.getShortName()));
                    break;
                case "DEPT":
                    departmentItem.getChildren().add(new TreeItem<>(node.getShortName()));
                    break;
                case "EXIT":
                    entranceItem.getChildren().add(new TreeItem<>(node.getShortName()));
                    break;
                case "INFO":
                    infoItem.getChildren().add(new TreeItem<>(node.getShortName()));
                    break;
                case "LABS":
                    labItem.getChildren().add(new TreeItem<>(node.getShortName()));
                    break;
                case "PARK":
                    parkingItem.getChildren().add(new TreeItem<>(node.getShortName()));
                    break;
                case "RETL":
                    retailItem.getChildren().add(new TreeItem<>(node.getShortName()));
                    break;
                case "SERV":
                    serviceItem.getChildren().add(new TreeItem<>(node.getShortName()));
                    break;
                case "REST":
                    restroomItem.getChildren().add(new TreeItem<>(node.getShortName()));
                    break;
            }
        }


        // add tree items to root item (shown in order of addition)
        rootTreeViewItem.getChildren().addAll(conferenceItem, departmentItem, entranceItem, infoItem,
                labItem, parkingItem, retailItem, serviceItem, restroomItem);

        // Check if a user is signed in a create the Tree Items for favorite and recent if needed
        if(CurrentUser.getCurrentUser().getUuid() == null && CurrentUser.getCurrentUser().isAuthenticated()) {

            try {
                for (String ID : getUserFavorites()) {
                    favoriteItem.getChildren().add(new TreeItem<>(idToShortName(ID)));
                }
                for (String ID : getUserRecent()){
                    recentItem.getChildren().add(0, new TreeItem<>(idToShortName(ID)));
                }

                // Add tree items at start of the list of items (Top of the page)
                rootTreeViewItem.getChildren().add(0, recentItem);
                rootTreeViewItem.getChildren().add(0, favoriteItem);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        // Set the root item
        treeView.setRoot(rootTreeViewItem);

        // Hide root item (we don't need it visible, we always want to list to be there
        this.treeView.setShowRoot(false);

        // Add a context menu to the tree view for when an item is selected
        treeView.setOnContextMenuRequested(event -> {
            // If navigating: do nothing
            if(isCurrentlyNavigating.get()){ return; }

            final NodeEntry currEntry = findNodeEntry(shortNameToID(treeView.getSelectionModel().getSelectedItem().getValue()));
            if(currEntry == null){return;}

            // Replace text on the whats here menu to make a little more sense
            whatsHereMenu.setText("What's This?");

            // Swap text on the add stop item based on if selected node in in the list
            if(vertices.contains(graph.getVertex(currEntry.getNodeID()))){
                addStopMenu.setText("Remove Stop");
            } else {
                addStopMenu.setText("Add Stop");
            }

            if(CurrentUser.getCurrentUser().isAuthenticated()) {
                try {
                    if (getUserFavorites().contains(currEntry.getNodeID())) {
                        addFavoriteMenu.setText("Remove Favorite");
                    } else {
                        addFavoriteMenu.setText("Add To Favorites");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // Show context menu
            contextMenu.show(treeView, event.getScreenX(), event.getScreenY());

            // Sets the start node and removes the old start node from the list (re-added in updatePath()) - LM
            // Combo box updates already call checkInput() so calling it for set start and end nodes here is redundant
            startPathMenu.setOnAction(e -> {
                startNode.set(idToShortName(currEntry.getNodeID()));
                try {
                    handleStartNodeChange();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            });

            // When adding a new stop, the vertex is added to the intermediate vertex list and the path is redrawn - LM
            // No combo box update so we call checkInput()
            addStopMenu.setOnAction(e -> {
                if(addStopMenu.getText().equals("Add Stop")) {
                    vertices.add(graph.getVertex(currEntry.getNodeID()));
                    drawStop(currEntry);
                    try {
                        addNodeToRecent(currEntry);
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                } else {
                    vertices.remove(graph.getVertex(currEntry.getNodeID()));
                    mapPanel.unDraw(currEntry.getNodeID());
                    getDrawableNode(currEntry.getNodeID());
                }
                checkInput();
            });

            // Sets the end node and removed the previous node from the list (re-added in updatePath()) - LM
            endPathMenu.setOnAction(e -> {
                endNode.set(idToShortName(currEntry.getNodeID()));
                try {
                    handleEndNodeChange();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            });

            //FIXME: Make these ones require that thing is visible
            whatsHereMenu.setOnAction(e -> {
                mapPanel.switchMap(currEntry.getFloor());
                mapPanel.centerNode(mapPanel.getNode(currEntry.getNodeID())); //FIXME: DO on all?

                final JFXDialog dialog = new JFXDialog();
                final JFXDialogLayout layout = new JFXDialogLayout();


                layout.setHeading(new Text(currEntry.getLongName()));

                //FIXME: DO BREAKS W/ CSS
                layout.setBody(new Text(currEntry.getDescription()));

                final JFXButton closeBtn = new JFXButton("Close");
                closeBtn.setOnAction(a -> dialog.close());

                final JFXButton directionsTo = new JFXButton("Direction To");
                directionsTo.setOnAction(a -> {
                    endNode.set(idToShortName(currEntry.getNodeID()));
                    try {
                        handleEndNodeChange();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                    dialog.close();
                });

                final JFXButton directionsFrom = new JFXButton("Directions From");
                directionsFrom.setOnAction(a -> {
                    startNode.set(idToShortName(currEntry.getNodeID()));
                    try {
                        handleStartNodeChange();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                    dialog.close();
                });

                if(CurrentUser.getCurrentUser().isAuthenticated()) {
                    final JFXButton toggleFavorite = new JFXButton("Add To Favorites");
                    try{
                        if(getUserFavorites().contains(currEntry.getNodeID())){
                            toggleFavorite.setText("Remove Favorite");
                        }
                    } catch (SQLException sqlException){
                        sqlException.printStackTrace();
                    }
                    toggleFavorite.setOnAction(a -> {
                        if (toggleFavorite.getText().equals("Add To Favorites")) {
                            try {
                                addNodeToFavorites(currEntry);
                            } catch (SQLException sqlException) {
                                sqlException.printStackTrace();
                            }
                        } else {
                            try {
                                removeNodeFromFavorites(currEntry);
                            } catch (SQLException sqlException) {
                                sqlException.printStackTrace();
                            }
                        }
                        dialog.close();
                    });
                    layout.setActions(toggleFavorite, directionsTo, directionsFrom, closeBtn);
                } else {
                    layout.setActions(directionsTo, directionsFrom, closeBtn);
                }

                dialog.setContent(layout);
                mapPanel.showDialog(dialog);
            });

            addFavoriteMenu.setOnAction(e -> {
                if (addFavoriteMenu.getText().equals("Add To Favorites")) {
                    try {
                        addNodeToFavorites(currEntry);
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                } else {
                    try {
                        removeNodeFromFavorites(currEntry);
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
            });
        });

        if(CurrentUser.getCurrentUser().getUuid() != null)
        {
            try {
                if(DatabaseAPI.getDatabaseAPI().getServiceEntry(CurrentUser.getCurrentUser().getUuid(), "uuid").getCompleteStatus().equals("false"))
                    endNode.set(idToShortName("FEXIT00301"));
                else
                    endNode.set(idToShortName("FEXIT00201"));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Draws an intermediate stop on the map
     * @param nodeEntry The NodeEntry of the stop being drawn
     * @author Leo Morris
     */
    private void drawStop(NodeEntry nodeEntry) {
        DrawableNode stop = new DrawableNode(Integer.parseInt(nodeEntry.getXCoordinate()),
                Integer.parseInt(nodeEntry.getYCoordinate()),
                nodeEntry.getNodeID(), nodeEntry.getFloor(), nodeEntry.getBuilding(), nodeEntry.getNodeType(),
                nodeEntry.getLongName(), nodeEntry.getShortName());

        stop.setStrokeWidth(2.0);
        stop.setFill(new Color(0.75,0,0,1));
        stop.setStroke(new Color(1,0,0,1));
        stop.setScaleX(1.5);
        stop.setScaleY(1.5);
        stop.setMouseTransparent(true);
        mapPanel.draw(stop);
        mapPanel.switchMap(nodeEntry.getFloor());
        mapPanel.centerNode(stop);
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
    public void handleStartNodeChange() throws SQLException {
        checkInput();
       // if(this.startNodeDisplay != null)
        //    mapPanel.unDraw(this.startNodeDisplay.getId());
        //FIXME: USE BINDINGS
        if(!(startNode.getValue().isEmpty())) {
            this.startNodeDisplay = mapPanel.getNode(shortNameToID(startNode.get()));

            mapPanel.switchMap(findNodeEntry(startNodeDisplay.getId()).getFloor());
            mapPanel.centerNode(startNodeDisplay);
            addNodeToRecent(DatabaseAPI.getDatabaseAPI().getNode(shortNameToID(startNode.getValue())));
        }
    }
    /**
     * Helper function used to draw the startNode with given ID, snatched from handleStartNodeChange()
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

            final BooleanBinding isStartNode = Bindings.equal(startNode, idToShortName(drawableNode.getId()));
            final BooleanBinding isEndNode = Bindings.equal(endNode, idToShortName(drawableNode.getId()));
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
     *
     * @author Alex Friedman (ahf)
     */
    @FXML
    public void handleEndNodeChange() throws SQLException {
        checkInput();
//        if(this.endNodeDisplay != null)
//            mapPanel.unDraw(this.endNodeDisplay.getId());
        //FIXME: USE BINDINGS?
        if(!(endNode.getValue().isEmpty())) {
            this.endNodeDisplay = mapPanel.getNode(shortNameToID(endNode.getValue()));//getDrawableNode(endComboBox.getValue(), Color.GREEN, 10);
            mapPanel.switchMap(findNodeEntry(endNodeDisplay.getId()).getFloor());
            mapPanel.centerNode(endNodeDisplay);
            addNodeToRecent(DatabaseAPI.getDatabaseAPI().getNode(shortNameToID(endNode.getValue())));
        }
    }


    /**
     * This is used to re-render the A* path
     *
     * @author Alex Friedman (ahf)
     */
    private boolean updatePath()
    {
        final Vertex startVertex = this.graph.getVertex(shortNameToID(startNode.getValue()));
        final Vertex endVertex = this.graph.getVertex(shortNameToID(endNode.getValue()));


        List<Vertex> pathVertices = new ArrayList<>();
        pathVertices.clear();
        pathVertices.addAll(vertices);

        final Path path;

        if(startVertex != null && endVertex != null && !startVertex.equals(endVertex))
        {
            if(!pathVertices.isEmpty()) {
                if (pathVertices.get(0) != startVertex) {
                    pathVertices.add(0, startVertex);
                }
                if (pathVertices.get(pathVertices.size() - 1) != endVertex) {
                    pathVertices.add(endVertex);
                }
            } else {
                pathVertices.add(0, startVertex);
                pathVertices.add(endVertex);
            }



            if(optimize.isSelected()) {
                path = this.graph.getUnorderedPath(pathVertices);
            } else {
                path = this.graph.getPath(pathVertices);
            }


            pathVertex.clear();
            if(path != null)
            {
                pathVertex.addAll(path.asList());

                parseRoute();

                final Color LINE_STROKE_TRANSPARENT = new Color(Color.DARKGRAY.getRed(), Color.DARKGRAY.getGreen(), Color.DARKGRAY.getBlue(), 0.75);

                for (int i = 0; i < pathVertex.size() - 1; i++)
                {
                    final Vertex start = pathVertex.get(i);
                    final Vertex end = pathVertex.get(i + 1);

                    //int startX, int startY, int endX, int endY, String ID, String startFloor, String endFloor
                    //FIXME: DO BETTER ID WHEN WE HAVE MULTIPLE PATH DIRECTIONS!!!
                    final DrawableEdge edge = new DrawableEdge((int)start.getX(), (int)start.getY(), (int)end.getX(), (int)end.getY(), start.getID() + "_" + end.getID(), start.getFloor(), end.getFloor(), new NodeEntry(), new NodeEntry());
                    edge.setStrokeWidth(UIConstants.LINE_STROKE_WIDTH);

                    edge.getStrokeDashArray().setAll(20d, 20d, 20d, 20d);
                    edge.setStrokeLineJoin(StrokeLineJoin.ROUND);
                    edge.setStrokeLineCap(StrokeLineCap.ROUND);

                    edge.strokeProperty().bind(
                            Bindings.when(Bindings.isEmpty(stopsList)).then(Color.RED).otherwise(
                                    Bindings.when(Bindings.integerValueAt(stopsList, currentStep).greaterThan(i)).then(LINE_STROKE_TRANSPARENT).otherwise(Color.rgb(0x03, 0x25, 0x6c))
                            )
                    );

                    final double maxOffset = edge.getStrokeDashArray().stream().reduce(0d, Double::sum);

                    Timeline pathTimeline = new Timeline(
                            new KeyFrame(
                                    Duration.ZERO,
                                    new KeyValue(
                                            edge.strokeDashOffsetProperty(),0, Interpolator.LINEAR
                                    )
                            ),
                            new KeyFrame(
                                    Duration.seconds(2),
                                    new KeyValue(
                                            edge.strokeDashOffsetProperty(),
                                            maxOffset,
                                            Interpolator.LINEAR
                                    )
                            )
                    );
                    pathTimeline.setRate(-1);
                    pathTimeline.setCycleCount(Timeline.INDEFINITE);
                    pathTimeline.play();



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

        return false; //We had an error
    }

    /**
     * Used to check if our input is valid to run the pathfinding algorithm or not
     *
     * @author Alex Friedman (ahf)
     */
    @FXML private void checkInput() {
        if (startNode.getValue().isEmpty() || endNode.getValue().isEmpty()){
          mapPanel.getCanvas().getChildren().removeIf(x -> x instanceof DrawableEdge);
        }else{
            mapPanel.getCanvas().getChildren().removeIf(x -> x instanceof DrawableEdge);
            if(updatePath()) {
                ETA.textProperty().unbind();
                ETA.setText("ETA"); //FIXME: DO BETTER EVENTUALLY
                Go.setDisable(false);
            }
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
        instructionsList.add("Arrived at destination!");

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
        NodeEntry preN;
        NodeEntry curN;
        Vertex preV = pathVertex.get(startIndex);
        Vertex curV;
        for(int i = startIndex + 1; i < pathVertex.size(); i++){
            curV = pathVertex.get(i);
            if(!curV.getID().substring(1, 5).equals(preV.getID().substring(1, 5)) || i == pathVertex.size() - 1){
                preN = findNodeEntry(preV.getID()); // get previous node to check stair/elevator type
                curN = findNodeEntry(curV.getID());


                if (curN == null) return -1;
                String type = preN.getNodeType();
                if (type.equals("STAI")) {type = "stairs";}
                else {type = "elevator";}
                instructionsList.add("Take the " + type + " to floor " + preV.getFloor());
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
        Next.setVisible(true);
        Next.setDisable(false);
        Prev.setVisible(true);
        Prev.setDisable(true);
        optimize.setDisable(true);
        Instruction.setVisible(true);
        navIcon.setVisible(true);
        ETA.setVisible(true);
        treeView.setManaged(false);
        treeView.setVisible(false);
        treeView.setDisable(true);
        about.setDisable(true);
        clear.setDisable(true);
        mapPanel.disableInteract();

        currentStep.set(0);

        isCurrentlyNavigating.set(true);

        parseRoute();

        // Add additional instruction to notice arrive of a stop
        List<Vertex> allStops = new ArrayList<>();
        allStops.add(pathVertex.get(0));
        allStops.addAll(vertices);
        allStops.add(pathVertex.get(pathVertex.size()-1));
        if(allStops.size() > 2){
            if(optimize.isSelected())
                allStops = graph.getEfficientOrder(allStops.toArray(new Vertex[0]));
            for(int i = 1; i < allStops.size() - 1; i++){
                for(int j = 0; j < stopsList.size(); j++){
                    if(allStops.get(i).getID().equals(pathVertex.get(stopsList.get(j)).getID())){
                        stopsList.add(j, stopsList.get(j));
                        instructionsList.add(j, "Reached " + idToShortName(allStops.get(i).getID()));
                        etaList.add(j, etaList.get(j));
                        break;
                    }
                }
            }
        }

        mapPanel.switchMap(pathVertex.get(0).getFloor());

        if(userNodeDisplay != null)
            mapPanel.unDraw(userNodeDisplay.getId());
        mapPanel.draw(this.userNodeDisplay);

        this.startNodeDisplay = mapPanel.getNode(pathVertex.get(0).getID());
        this.endNodeDisplay = mapPanel.getNode(pathVertex.get(pathVertex.size()-1).getID());
        mapPanel.centerNode(userNodeDisplay);

        startNodeDisplay.toFront();
        endNodeDisplay.toFront();
        userNodeDisplay.toFront();

        drawSEIcons();

        Instruction.textProperty().bind(Bindings.when(Bindings.isEmpty(instructionsList)).then("").otherwise(Bindings.stringValueAt(instructionsList, currentStep)));
        ETA.textProperty().bind(Bindings.stringValueAt(etaList, currentStep));

        currentDirection = "UP";
        drawDirection();
        setNavIcon();
        Go.setText("End Navigation");
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
        setNavIcon();
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
        setNavIcon();

        if(direction != null && currentStep.get() == Math.min(stopsList.size() - 1, instructionsList.size() - 1))
                mapPanel.unDraw(this.direction.getId());

    }

    /**
     * Function to react to 'End Navigation' button being pressed and stop the stepper
     * @author ZheCheng Song
     */
    public void endNavigation() {
        Prev.setVisible(false);
        Next.setVisible(false);
        Instruction.setVisible(false);
        ETA.setVisible(false);
        navIcon.setVisible(false);
        currentStep.set(0);
        isCurrentlyNavigating.set(false);
        optimize.setDisable(false);
        treeView.setManaged(true);
        treeView.setVisible(true);
        treeView.setDisable(false);
        about.setDisable(false);
        clear.setDisable(false);
        mapPanel.enableInteract();

        unDrawSEIcons();

        if(direction != null)
            mapPanel.unDraw(this.direction.getId());

        //mapPanel.switchMap(pathVertex.get(0).getFloor());
        //mapPanel.centerNode(startNodeDisplay);
        Go.setText("Start Navigation");
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
     * @throws IOException If loading the scene fails
     * @author Leo Morris
     */
    public void handleGoBack() throws IOException {
        SceneContext.getSceneContext().loadDefault();
    }


    /**
     * Checks the current instruction and applies the corresponding icon to the navigation bar
     * @author Leo Morris
     */
    public void setNavIcon() {
        String curInstruction = instructionsList.get(currentStep.get()).toLowerCase();
        Image image = null;
        if (curInstruction.contains("elevator")) {
            image = new Image(getClass().getResourceAsStream("/imagesAndLogos/navIcons/takeElevatorYellow.png"));
        }
        else if (curInstruction.contains("right")) {
            image = new Image(getClass().getResourceAsStream("/imagesAndLogos/navIcons/turnRightYellow.png"));
        }
        else if (curInstruction.contains("left")) {
            image = new Image(getClass().getResourceAsStream("/imagesAndLogos/navIcons/turnLeftYellow.png"));
        }
        else if (curInstruction.contains("forward")) {
            image = new Image(getClass().getResourceAsStream("/imagesAndLogos/navIcons/goForwardYellow.png"));
        }
        else if (curInstruction.contains("around")) {
            image = new Image(getClass().getResourceAsStream("/imagesAndLogos/navIcons/uTurnYellow.png"));
        }
        else if(curInstruction.contains("stair")){
            final int nextFloor = mapPanel.getDoubleStringConverter().fromString(curInstruction.substring(curInstruction.length()-1)).intValue();//Integer.parseInt(curInstruction.substring(curInstruction.length()-1));
            final int currentFloor = mapPanel.getDoubleStringConverter().fromString(pathVertex.get(currentStep.get()-1).getFloor()).intValue();
            if(nextFloor > currentFloor){
                image = new Image(getClass().getResourceAsStream("/imagesAndLogos/navIcons/goUpStairsYellow.png"));
            } else {
                image = new Image(getClass().getResourceAsStream("/imagesAndLogos/navIcons/goDownStairsYellow.png"));
            }
        }
        else if (curInstruction.contains("arrived")){
            image = new Image(getClass().getResourceAsStream("/imagesAndLogos/navIcons/stopYellow.png"));
        }
        navIcon.setImage(image);
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
        layout.setHeading(new Text("Directions from: " + startNode.getValue() + " to " +  endNode.getValue()));

        StringBuilder directions = new StringBuilder();
        for(int i = 0; i < stopsList.size(); i++)
        {
            final String instruction = instructionsList.get(i);
            final String eta = etaList.get(i);

            if(i < stopsList.size() - 1)
                directions.append(instruction).append("\t\t(").append(eta).append(")\n");
            else
                directions.append(instruction);
        }

        //FIXME: DO BREAKS W/ CSS
        layout.setBody(new Text(directions.toString()));

        final JFXButton closeBtn = new JFXButton("Close");
        closeBtn.setOnAction(a -> dialog.close());

        final JFXButton printBtn = new JFXButton("Print");
        printBtn.setOnAction(a -> {
            try {
                printInstructions();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dialog.close();});


        layout.setActions(printBtn, closeBtn);

        dialog.setContent(layout);
        mapPanel.showDialog(dialog);
    }


    /**
     * Prints the current instructions as a PDF file.
     * @author Alex Friedman (ahf)
     * @since 05-02-2021 - branch: ahf-fullDirections
     */
    private void printInstructions() throws IOException {

        final int INSTRUCTIONS_PER_PAGE = 5;


        final String initialFloor = mapPanel.getFloor().get();
        final int initialCurrStep = currentStep.get();
        final double initZoomLevel = mapPanel.getZoomLevel().get();

        final File file = new File(System.currentTimeMillis() + ".pdf");

        //Create the document
        final PDDocument pdfDocument = new PDDocument();


        mapPanel.getZoomLevel().set(2);

        final int numPages = (int) Math.ceil((double) stopsList.size()/INSTRUCTIONS_PER_PAGE);

        for(int p = 0; p < numPages; p++) {
            //Create the first page of the document.
            final PDPage page = new PDPage();
            pdfDocument.addPage(page);

            //Create the ContentStream so that we can add data to the document
            final PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);

            //Begin the text

            /*
             * Page title
             */
            contentStream.beginText();
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25 + 64, 740);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 36);

            contentStream.showText("Brigham and Women's Hospital");
            contentStream.newLine();
            contentStream.endText();

            final BufferedImage logoImage = ImageIO.read(getClass().getResourceAsStream("/imagesAndLogos/BandWLogo.png"));
            PDImageXObject pdfLogo = LosslessFactory.createFromImage(pdfDocument,logoImage);
            contentStream.drawImage(pdfLogo, 25, 720, 55, 64);

            /*
             * Instruction Information
             */
            contentStream.beginText();
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 690);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);

            contentStream.showText("Directions from: " + startNode.getValue());
            contentStream.newLine();
            contentStream.endText();



            contentStream.beginText();
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 670);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);

            contentStream.showText("To: " + endNode.getValue());
            contentStream.newLine();
            contentStream.endText();

            /*
             * Page numbers
             */
            contentStream.beginText();
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(480, 30);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);

            contentStream.showText("Page " + (p + 1) + " of " + numPages);
            contentStream.newLine();
            contentStream.endText();

            //Display instructions

            for (int i = p * INSTRUCTIONS_PER_PAGE; i < Math.min(stopsList.size(), (p + 1) * INSTRUCTIONS_PER_PAGE); i++) {
                contentStream.beginText();
                contentStream.setLeading(14.5f);
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);
                contentStream.newLineAtOffset(25, 630 - ((110 * (i % INSTRUCTIONS_PER_PAGE))));
                //  contentStream.newLine();
                final String instruction = instructionsList.get(i);
                final String eta = etaList.get(i);

                if (i < stopsList.size() - 1)
                    contentStream.showText(instruction + "     (" + eta + ")");
                else
                    contentStream.showText(instruction);
                contentStream.endText();


                final Vertex currVertex = pathVertex.get(stopsList.get(i));
                mapPanel.switchMap(currVertex.getFloor());
                mapPanel.centerNode(mapPanel.getNode(currVertex.getID()));
                currentStep.set(i);

                final SnapshotParameters params = new SnapshotParameters();
                final int cX = (int) currVertex.getX();
                final int cY = (int) currVertex.getY();

                final int captureDimensions = 200;

                final int minX = (int) ((cX / mapPanel.getZoomLevel().get()) - captureDimensions / 2);
                final int minY = (int) ((cY / mapPanel.getZoomLevel().get()) - captureDimensions / 2);

                params.setViewport(new Rectangle2D(minX, minY, captureDimensions, captureDimensions));
                // System.out.println(params.getViewport());

                final WritableImage image = mapPanel.getCanvas().snapshot(params, null);
                final BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

                //  ImageIO.write(bufferedImage, "png", new File(System.currentTimeMillis() + ".png"));

                final double aspect = (double) bufferedImage.getWidth() / bufferedImage.getHeight();

                final java.awt.Image scaledImage = bufferedImage.getScaledInstance(100, (int) (100 * aspect), java.awt.Image.SCALE_SMOOTH);
                final BufferedImage scaledBuffered = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                scaledBuffered.getGraphics().drawImage(scaledImage, 0, 0, null);

                PDImageXObject pdfImage = LosslessFactory.createFromImage(pdfDocument, scaledBuffered);

                contentStream.drawImage(pdfImage, 320, 555 - (i % INSTRUCTIONS_PER_PAGE) * 110);


                // ImageIO.write(bufferedImage, "png", new File(System.currentTimeMillis() + ".png"));
            }
            contentStream.close();
        }
        currentStep.set(initialCurrStep);
        mapPanel.switchMap(initialFloor);
        mapPanel.getZoomLevel().set(initZoomLevel);





        pdfDocument.save(file);
        pdfDocument.close();

        final Desktop desktop = Desktop.getDesktop();
        desktop.open(file);
    }

    /**
     * Clears all node selections and exits pathfinding mode
     * @author Leo Morris
     */
    public void clearList() {

        vertices.clear();
        startNode.set("");
        endNode.set("");
        mapPanel.clearMap();
        Go.setDisable(true);

        for(NodeEntry e : allNodeEntries)
            getDrawableNode(e.getNodeID());

        if(pathVertex.size() != 0) {
            endNavigation();
        }
    }

    /**
     * Calls the appropriate method to change the state of navigation.
     * Allows the start/end buttons to be one button
     * @author Leo Morris
     */
    public void toggleNavigation() {
        if(isCurrentlyNavigating.get()){
            endNavigation();
        } else {
            startNavigation();
        }
    }

    public void gotoAboutPage() throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AboutPageView.fxml");
    }

    public List<String> getUserFavorites() throws SQLException {
        if(!CurrentUser.getCurrentUser().isAuthenticated()) return null;
        return DatabaseAPI.getDatabaseAPI().getUserNodes("favorite", CurrentUser.getCurrentUser().getLoggedIn().getUsername());
    }

    public List<String> getUserRecent() throws SQLException{
        if(!CurrentUser.getCurrentUser().isAuthenticated()) return null;
        return DatabaseAPI.getDatabaseAPI().getUserNodes("recent", CurrentUser.getCurrentUser().getLoggedIn().getUsername());
    }

    public void addNodeToFavorites(NodeEntry node) throws SQLException{
        if(CurrentUser.getCurrentUser().isAuthenticated()) {
            if(getUserFavorites().contains(node.getNodeID())){return;}
            DatabaseAPI.getDatabaseAPI().addCollectionEntry(CurrentUser.getCurrentUser().getLoggedIn().getUsername(), node.getNodeID(), "favorite");
            favoriteItem.getChildren().add(0, new TreeItem<>(node.getShortName()));
        }
    }

    public void removeNodeFromFavorites(NodeEntry node) throws SQLException{
        if(CurrentUser.getCurrentUser().isAuthenticated()){
            if(!getUserFavorites().contains(node.getNodeID())){return;}
            DatabaseAPI.getDatabaseAPI().deleteUserNode(node.getNodeID(), CurrentUser.getCurrentUser().getLoggedIn().getUsername(), "favorite");
            favoriteItem.getChildren().removeIf(t -> t.getValue().equals(node.getShortName())); // Remove any tree item with a matching short name from the list
        }
    }

    public void addNodeToRecent(NodeEntry node) throws SQLException{
        if(CurrentUser.getCurrentUser().isAuthenticated()) {
            if(!favoriteItem.getChildren().removeIf(t -> t.getValue().equals(node.getShortName()))) // Remove any tree item with a matching short name (prevents duplicates)
                DatabaseAPI.getDatabaseAPI().addCollectionEntry(CurrentUser.getCurrentUser().getLoggedIn().getUsername(), node.getNodeID(), "recent"); // if it wasn't removed, add it to the db list
            recentItem.getChildren().add(0, new TreeItem<>(node.getShortName()));
            while(getUserRecent().size() > MAX_RECENTLY_USED){ // Should only run once, just covers a previously missed deletion
                String IDtoRemove = shortNameToID(recentItem.getChildren().get(MAX_RECENTLY_USED).getValue());
                DatabaseAPI.getDatabaseAPI().deleteUserNode(IDtoRemove, CurrentUser.getCurrentUser().getLoggedIn().getUsername(), "recent");
                recentItem.getChildren().remove(MAX_RECENTLY_USED);
            }
        }
    }


    /**
     * Centers map on a node when selected from the tree view without opening the "What's this?" menu
     * @author Leo Morris
     */
    public void handleListSelection() {
        if(treeView.getSelectionModel().getSelectedItem()!=null &&
                !treeView.getSelectionModel().getSelectedItem().getParent().equals(rootTreeViewItem) &&
                !treeView.getSelectionModel().getSelectedItem().equals(rootTreeViewItem)) { // Do not center on drop downs, root item or null items, only actual nodes
            mapPanel.switchMap(findNodeEntry(shortNameToID(treeView.getSelectionModel().getSelectedItem().getValue())).getFloor());
            mapPanel.centerNode(mapPanel.getNode(shortNameToID(treeView.getSelectionModel().getSelectedItem().getValue())));
        }
    }



    private void drawSEIcons(){
        for(int i = 0; i < stopsList.size() - 1; i++){
            if(instructionsList.get(i).contains("Take")){
                Vertex curV = pathVertex.get(stopsList.get(i));
                Vertex nexV = pathVertex.get(stopsList.get(i + 1));

                String Ins = instructionsList.get(i);

                ImageView imageViewOne = new ImageView();
                ImageView imageViewTwo = new ImageView();
                Image firstImage = null;
                Image secondImage = null;
                if (Ins.contains("elevator")) {
                    firstImage = new Image(getClass().getResourceAsStream("/imagesAndLogos/navIcons/takeElevatorYellow.png"));
                    secondImage = new Image(getClass().getResourceAsStream("/imagesAndLogos/navIcons/takeElevatorYellow.png"));
                }
                else if(Ins.contains("stair")){
                    double currentFloor =  mapPanel.getDoubleStringConverter().fromString(curV.getFloor());
                    double nextFloor =  mapPanel.getDoubleStringConverter().fromString(nexV.getFloor());
                    if(nextFloor > currentFloor){
                        firstImage = new Image(getClass().getResourceAsStream("/imagesAndLogos/navIcons/goUpStairsYellow.png"));
                        secondImage = new Image(getClass().getResourceAsStream("/imagesAndLogos/navIcons/goDownStairsYellow.png"));
                    } else {
                        firstImage = new Image(getClass().getResourceAsStream("/imagesAndLogos/navIcons/goDownStairsYellow.png"));
                        secondImage = new Image(getClass().getResourceAsStream("/imagesAndLogos/navIcons/goUpStairsYellow.png"));
                    }
                }
                final int curStep = i;

                imageViewOne.setImage(firstImage);
                final SimpleIntegerProperty imageOneX = new SimpleIntegerProperty((int) Math.floor(curV.getX()));
                final SimpleIntegerProperty imageOneY = new SimpleIntegerProperty((int) Math.floor(curV.getY()));
                imageViewOne.xProperty().bind(imageOneX.divide(mapPanel.getZoomLevel()).subtract(imageViewOne.fitWidthProperty().divide(2)));
                imageViewOne.yProperty().bind(imageOneY.divide(mapPanel.getZoomLevel()).subtract(imageViewOne.fitHeightProperty().divide(2)));
                imageViewOne.setFitWidth(20);
                imageViewOne.setFitHeight(20);
                imageViewOne.setId(curV.getFloor());
                imageViewOne.setOnMouseClicked(event -> goToStep(curStep + 1));
                imageViewOne.setOnMouseEntered(event -> {
                    imageViewOne.setFitWidth(30);
                    imageViewOne.setFitHeight(30);
                });
                imageViewOne.setOnMouseExited(event -> {
                    imageViewOne.setFitWidth(20);
                    imageViewOne.setFitHeight(20);
                });

                imageViewTwo.setImage(secondImage);
                final SimpleIntegerProperty imageTwoX = new SimpleIntegerProperty((int) Math.floor(nexV.getX()));
                final SimpleIntegerProperty imageTwoY = new SimpleIntegerProperty((int) Math.floor(nexV.getY()));
                imageViewTwo.xProperty().bind(imageTwoX.divide(mapPanel.getZoomLevel()).subtract(imageViewTwo.fitWidthProperty().divide(2)));
                imageViewTwo.yProperty().bind(imageTwoY.divide(mapPanel.getZoomLevel()).subtract(imageViewTwo.fitHeightProperty().divide(2)));
                imageViewTwo.setFitWidth(20);
                imageViewTwo.setFitHeight(20);
                imageViewTwo.setId(nexV.getFloor());
                imageViewTwo.setOnMouseClicked(event -> goToStep(curStep));
                imageViewTwo.setOnMouseEntered(event -> {
                    imageViewTwo.setFitWidth(30);
                    imageViewTwo.setFitHeight(30);
                });
                imageViewTwo.setOnMouseExited(event -> {
                    imageViewTwo.setFitWidth(20);
                    imageViewTwo.setFitHeight(20);
                });

                final BooleanBinding firstImageOnFloor = Bindings.equal(imageViewOne.getId(), mapPanel.getFloor());
                final BooleanBinding secondImageOnFloor = Bindings.equal(imageViewTwo.getId(), mapPanel.getFloor());

                imageViewOne.toFront();
                imageViewTwo.toFront();

                imageViewOne.visibleProperty().bind(Bindings.when(firstImageOnFloor).then(true).otherwise(false));
                imageViewTwo.visibleProperty().bind(Bindings.when(secondImageOnFloor).then(true).otherwise(false));

                mapPanel.getCanvas().getChildren().add(imageViewOne);
                mapPanel.getCanvas().getChildren().add(imageViewTwo);
            }
        }
    }

    private void unDrawSEIcons(){
        mapPanel.getCanvas().getChildren().removeIf(x -> x instanceof ImageView && !x.equals(mapPanel.getMap()));
    }

    // Will come in handy when implementing Treeview for instructions
    private void goToStep(int step){
        if(step < 0 || step > stopsList.size() - 1 || step == currentStep.get()) return;
        if(step > currentStep.get()){
            while(step != currentStep.get()){
                goToNextNode();
            }
        }else{
            while(step != currentStep.get()){
                goToPrevNode();
            }
        }
    }
}
