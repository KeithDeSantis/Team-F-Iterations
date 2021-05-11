package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.Translation.Translator;
import edu.wpi.cs3733.D21.teamF.controllers.components.NavigationListCell;
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
import edu.wpi.cs3733.uicomponents.entities.DrawableFloorInstruction;
import edu.wpi.cs3733.uicomponents.entities.DrawableNode;
import edu.wpi.cs3733.uicomponents.entities.DrawableUser;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    public JFXButton endNavBtn;

    @FXML
    public MapPanel mapPanel;

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
    public JFXToggleButton disableStair;

    @FXML
    private JFXTreeView<String> treeView;

    @FXML
    private JFXTreeView<String> instructionTreeView;
    
    @FXML
    private JFXListView<String> stopList;

    //FIXME: DO BETTER
    private Graph graph;
    private static final int MAX_RECENTLY_USED = 5;
    private static final double PIXEL_TO_METER_RATIO = 10;

    /**
     * These are done for displaying the start & end nodes. This should be done better (eventually)
     * @author Alex Friedman (ahf)
     */
    private DrawableNode startNodeDisplay;
    private DrawableNode endNodeDisplay;
    private DrawableUser userNodeDisplay;

    @FXML
    private HBox topHBox;

    @FXML
    private HBox lowerHBox;

    // Global variables for the stepper
    private final ObservableList<Vertex> pathVertex = FXCollections.observableArrayList();

    private List<NodeEntry> allNodeEntries = new ArrayList<>();

    private final BooleanProperty isCurrentlyNavigating = new SimpleBooleanProperty(false);
    private final ObservableList<Integer> stopsList = FXCollections.observableArrayList();
    private final ObservableList<String> instructionsList = FXCollections.observableArrayList();
    private final ObservableList<String> etaList = FXCollections.observableArrayList();
    private final ObservableList<String> directionsList = FXCollections.observableArrayList();
    private final IntegerProperty currentStep = new SimpleIntegerProperty(0);

    // List of intermediate vertices for multi-stop pathfinding - LM
    private final ObservableList<Vertex> vertices = FXCollections.observableArrayList();
    private final StringProperty startNode = new SimpleStringProperty("");
    private final StringProperty endNode = new SimpleStringProperty("");

    private final StringProperty currentDirection = new SimpleStringProperty("UP");
    private final StringProperty nextDirection = new SimpleStringProperty("UP");

    final ObservableList<String> nodeList = FXCollections.observableArrayList();


    // Create root tree item (will be hidden later)
    private final TreeItem<String> rootTreeViewItem = new TreeItem<>("shortNames");
    // List of all nodes in each category
    private final TreeItem<String> conferenceItem = new TreeItem<>("Conference Rooms");
    private final TreeItem<String> departmentItem = new TreeItem<>("Departments");
    private final TreeItem<String> entranceItem = new TreeItem<>("Entrances");
    private final TreeItem<String> infoItem = new TreeItem<>("Information");
    private final TreeItem<String> labItem = new TreeItem<>("Labs");
    private final TreeItem<String> parkingItem = new TreeItem<>("Parking");
    private final TreeItem<String> restroomItem = new TreeItem<>("Restrooms");
    private final TreeItem<String> retailItem = new TreeItem<>("Retail");
    private final TreeItem<String> serviceItem = new TreeItem<>("Services");
    private final TreeItem<String> favoriteItem = new TreeItem<>("Favorites");
    private final TreeItem<String> recentItem = new TreeItem<>("Recently Used");

    private final TreeItem<String> instructionTreeViewItem = new TreeItem<>("Instructions");

    private boolean filterNodes = false; // Boolean for filtering user selections to only outdoor nodes

    private String direct = "UP";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        SceneContext.bindTreeItem(rootTreeViewItem, false);
        SceneContext.bindTreeItem(instructionTreeViewItem, true);

        final Rectangle confGraphic = new Rectangle(16, 16);
        confGraphic.setFill(getNodeTypeColor("CONF"));
        conferenceItem.setGraphic(confGraphic);

        final Rectangle deptGraphic = new Rectangle(16, 16);
        deptGraphic.setFill(getNodeTypeColor("DEPT"));
        departmentItem.setGraphic(deptGraphic);

        final Rectangle exitGraphic = new Rectangle(16, 16);
        exitGraphic.setFill(getNodeTypeColor("EXIT"));
        entranceItem.setGraphic(exitGraphic);

        final Rectangle infoGraphic = new Rectangle(16, 16);
        infoGraphic.setFill(getNodeTypeColor("INFO"));
        infoItem.setGraphic(infoGraphic);

        final Rectangle labsGraphic = new Rectangle(16, 16);
        labsGraphic.setFill(getNodeTypeColor("LABS"));
        labItem.setGraphic(labsGraphic);

        final Rectangle parkingGraphic = new Rectangle(16, 16);
        parkingGraphic.setFill(getNodeTypeColor("PARK"));
        parkingItem.setGraphic(parkingGraphic);

        final Rectangle restroomGraphic = new Rectangle(16, 16);
        restroomGraphic.setFill(getNodeTypeColor("REST"));
        restroomItem.setGraphic(restroomGraphic);

        final Rectangle retailGraphic = new Rectangle(16, 16);
        retailGraphic.setFill(getNodeTypeColor("RETL"));
        retailItem.setGraphic(retailGraphic);

        final Rectangle serviceGraphic = new Rectangle(16, 16);
        serviceGraphic.setFill(getNodeTypeColor("SERV"));
        serviceItem.setGraphic(serviceGraphic);

        //ahf - yes this should be done better. At some point.
        try {
            allNodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries();
            List<EdgeEntry> allEdgeEntries = DatabaseAPI.getDatabaseAPI().genEdgeEntries();
            this.graph = GraphLoader.load(allNodeEntries, allEdgeEntries);
        } catch (Exception e) {
            this.graph = new Graph();
            e.printStackTrace();
        }

        String algorithmFromAPI;
        try {
            algorithmFromAPI = DatabaseAPI.getDatabaseAPI().getCurrentAlgorithm();

            if(algorithmFromAPI == null) {
                DatabaseAPI.getDatabaseAPI().addSystemPreferences("MASTER", "A Star"); //We default to A* if noting explicitly set
                algorithmFromAPI = "A Star";
            }
            else
                graph.setPathfindingAlgorithm(algorithmFromAPI);

        } catch (SQLException exception) {
            algorithmFromAPI = "A Star"; //Default to A*
            exception.printStackTrace();
        }



        // Saving Usable nodes' shortName to List
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

        //FIXME: CHANGE TEXT TO BE MORE ACCESSIBLE
        final MenuItem startPathMenu = new MenuItem("Path From Here");
        startPathMenu.textProperty().bind(Translator.getTranslator().getTranslationBinding(startPathMenu.getText()));

        final StringProperty addStopMenuText = new SimpleStringProperty("Add Stop Here");
        final MenuItem addStopMenu = new MenuItem();
        addStopMenu.textProperty().bind(Translator.getTranslator().getTranslationBinding(addStopMenuText));

        final MenuItem endPathMenu = new MenuItem("Path End Here");
        endPathMenu.textProperty().bind(Translator.getTranslator().getTranslationBinding(endPathMenu.getText()));

        final StringProperty whatsHereMenuText = new SimpleStringProperty("What's Here?");
        final MenuItem whatsHereMenu = new MenuItem();
        whatsHereMenu.textProperty().bind(Translator.getTranslator().getTranslationBinding(whatsHereMenuText));

        final StringProperty addFavoriteMenuText = new SimpleStringProperty("Add Favorite");
        final MenuItem addFavoriteMenu = new MenuItem(); // only added when signed in
        addFavoriteMenu.textProperty().bind(Translator.getTranslator().getTranslationBinding(addFavoriteMenuText));

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
            if(filterNodes && !(currEntry.getNodeType().equals("PARK") || currEntry.getNodeType().equals("WALK"))){return;}

            // Set whats here menu text back to what it should be on the map
            whatsHereMenuText.set("What's Here?");

            // Set add stop text to make sense
            if (vertices.contains(graph.getVertex(currEntry.getNodeID()))) {
                addStopMenuText.set("Remove Stop");
            } else {
                addStopMenuText.set("Add Stop");
            }
            if(CurrentUser.getCurrentUser().getUuid() == null && CurrentUser.getCurrentUser().isAuthenticated()) {
                try {
                    if (getUserFavorites().contains(currEntry.getNodeID())) {
                        addFavoriteMenuText.set("Remove Favorite");
                    } else {
                        addFavoriteMenuText.set("Add To Favorites");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


            contextMenu.show(mapPanel.getMap(), event.getScreenX(), event.getScreenY());

            // Sets the start node and removes the old start node from the list (re-added in updatePath()) - LM
            // Combo box updates already call checkInput() so calling it for set start and end nodes here is redundant
            startPathMenu.setOnAction(handleStartPathMenu(currEntry));

            // When adding a new stop, the vertex is added to the intermediate vertex list and the path is redrawn - LM
            // No combo box update so we call checkInput()
            addStopMenu.setOnAction(handleAddStopMenu(addStopMenuText, currEntry));

            // Sets the end node and removed the previous node from the list (re-added in updatePath()) - LM
            endPathMenu.setOnAction(handleEndPathMenu(currEntry));

            //FIXME: Make these ones require that thing is visible
            whatsHereMenu.setOnAction(handleWhatsHereMenu(currEntry, 2));

            addFavoriteFromMenu(addFavoriteMenuText, addFavoriteMenu, currEntry);
        });

        Go.setDisable(true);
        pathVertex.clear();

        viewInstructionsBtn.visibleProperty().bind(ETA.visibleProperty());

        topHBox.visibleProperty().bind(isCurrentlyNavigating.not());
        topHBox.disableProperty().bind(isCurrentlyNavigating);
        topHBox.managedProperty().bind(isCurrentlyNavigating.not());
        lowerHBox.visibleProperty().bind(isCurrentlyNavigating);
        lowerHBox.disableProperty().bind(isCurrentlyNavigating.not());
        lowerHBox.managedProperty().bind(isCurrentlyNavigating);

        // initializes navigation related bindings
        Next.disableProperty().bind(Bindings.when(
                isCurrentlyNavigating.and(currentStep.isNotEqualTo(Bindings.createIntegerBinding(() -> stopsList.size() - 1 , stopsList)))).then(false).otherwise(true));
        Prev.disableProperty().bind(Bindings.when(
                isCurrentlyNavigating.and(currentStep.isNotEqualTo(0))).then(false).otherwise(true));
        Next.visibleProperty().bind(isCurrentlyNavigating);
        Prev.visibleProperty().bind(isCurrentlyNavigating);
        Instruction.visibleProperty().bind(isCurrentlyNavigating);
        ETA.visibleProperty().bind(isCurrentlyNavigating);
        navIcon.visibleProperty().bind(isCurrentlyNavigating);
        instructionTreeView.managedProperty().bind(isCurrentlyNavigating);
        instructionTreeView.visibleProperty().bind(isCurrentlyNavigating);
        instructionTreeView.disableProperty().bind(isCurrentlyNavigating.not());
        optimize.disableProperty().bind(isCurrentlyNavigating);
        disableStair.disableProperty().bind(isCurrentlyNavigating);
        if(algorithmFromAPI.equalsIgnoreCase("depth-first-search") || algorithmFromAPI.equalsIgnoreCase("breadth-first-search"))
        {
            disableStair.visibleProperty().set(false);
            disableStair.setManaged(false);
        }

        about.disableProperty().bind(isCurrentlyNavigating);
        clear.disableProperty().bind(isCurrentlyNavigating);
        treeView.managedProperty().bind(isCurrentlyNavigating.not());
        treeView.visibleProperty().bind(isCurrentlyNavigating.not());
        treeView.disableProperty().bind(isCurrentlyNavigating);

        /*
         * initializes user node
         */

        final DrawableUser drawableUser = new DrawableUser(0, 0, "userNode", "");

        final ObjectBinding<Vertex> vertexProperty = Bindings.when(Bindings.isEmpty(stopsList).or(Bindings.isEmpty(pathVertex)))
                .then(new Vertex("N/A", -1, -1, "N/A"))
                .otherwise(Bindings.valueAt(pathVertex, Bindings.integerValueAt(stopsList, currentStep)));

        drawableUser.shouldDisplay().bind(isCurrentlyNavigating);

        drawableUser.getFloor().bind(Bindings.createStringBinding(() -> vertexProperty.get().getFloor(), vertexProperty));

        drawableUser.xCoordinateProperty().bind(Bindings.createDoubleBinding(() -> vertexProperty.get().getX(), vertexProperty));

        drawableUser.yCoordinateProperty().bind(Bindings.createDoubleBinding(() -> vertexProperty.get().getY(), vertexProperty));
        this.userNodeDisplay = drawableUser;


        final DrawableUser ghost = new DrawableUser(0, 0, "userGhost", "");
        ghost.opacityProperty().set(0.5);
        ghost.shouldDisplay().bind(isCurrentlyNavigating);
        ghost.getFloor().bind(Bindings.createStringBinding(() -> vertexProperty.get().getFloor(), vertexProperty));

        final IntegerBinding nextStepProp = Bindings.createIntegerBinding(() -> Math.min(currentStep.get() + 1, stopsList.size() - 1), currentStep, stopsList);

        final ObjectBinding<Vertex> nextVertexProperty = Bindings.when(Bindings.isEmpty(stopsList).or(Bindings.isEmpty(pathVertex)))
                .then(new Vertex("N/A", -1, -1, "N/A"))
                .otherwise(Bindings.valueAt(pathVertex, Bindings.integerValueAt(stopsList, nextStepProp)));

        final IntegerProperty x0 = new SimpleIntegerProperty();
        final IntegerProperty y0 = new SimpleIntegerProperty();
        x0.bind(Bindings.createDoubleBinding(() -> vertexProperty.get().getX(), vertexProperty));
        y0.bind(Bindings.createDoubleBinding(() -> vertexProperty.get().getY(), vertexProperty));

        final IntegerProperty x1 = new SimpleIntegerProperty();
        final IntegerProperty y1 = new SimpleIntegerProperty();
        x1.bind(Bindings.createDoubleBinding(() -> nextVertexProperty.get().getX(), nextVertexProperty));
        y1.bind(Bindings.createDoubleBinding(() -> nextVertexProperty.get().getY(), nextVertexProperty));

        final Animation animation = new Transition() {

            {
               setCycleDuration(Duration.millis(2000));
            }
            protected void interpolate(double fraction) {
                ghost.xCoordinateProperty().set((int) (fraction * x1.get() + (1 - fraction) * x0.get()));
                ghost.yCoordinateProperty().set((int) (fraction * y1.get() + (1 - fraction) * y0.get()));
                ghost.directionAngleProperty().bind(Bindings.createDoubleBinding(() -> getAngleFor(nextDirection.get()), nextDirection));
            }

        };
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();

        animation.rateProperty().bind(Bindings.createDoubleBinding(() -> {
          //  animation.stop(); // stop animation so it can restart from beginning
            double ans = Math.sqrt(Math.pow(x1.get() - x0.get(), 2) + Math.pow(y1.get() - y0.get(), 2));

            //NaN is possible somehow?. Don't remove it!
            if(Double.isNaN(ans) || ans == 0 || ans == Double.NEGATIVE_INFINITY || ans == Double.POSITIVE_INFINITY)
                ans = 10;

            return (2000/(ans * 10));
        }, x0, x1, y0, y1));

        animation.rateProperty().addListener((observable, oldValue, newValue) -> {
            animation.stop();
            animation.play();
        });


        mapPanel.draw(ghost);

        userNodeDisplay.directionAngleProperty().bind(Bindings.createDoubleBinding(() -> getAngleFor(currentDirection.get()), currentDirection));

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



        // Setting up instruction tree view
        instructionTreeView.setRoot(instructionTreeViewItem);
        this.instructionTreeView.setShowRoot(false);

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
            if(filterNodes && !(currEntry.getNodeType().equals("PARK") || currEntry.getNodeType().equals("WALK"))){return;}

            // Replace text on the whats here menu to make a little more sense
            whatsHereMenuText.set("What's This?");

            // Swap text on the add stop item based on if selected node in in the list
            if(vertices.contains(graph.getVertex(currEntry.getNodeID()))){
                addStopMenuText.set("Remove Stop");
            } else {
                addStopMenuText.set("Add Stop");
            }

            if(CurrentUser.getCurrentUser().isAuthenticated()) {
                try {
                    if (getUserFavorites().contains(currEntry.getNodeID())) {
                        addFavoriteMenuText.set("Remove Favorite");
                    } else {
                        addFavoriteMenuText.set("Add To Favorites");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // Show context menu
            contextMenu.show(treeView, event.getScreenX(), event.getScreenY());

            // Sets the start node and removes the old start node from the list (re-added in updatePath()) - LM
            // Combo box updates already call checkInput() so calling it for set start and end nodes here is redundant
            startPathMenu.setOnAction(handleStartPathMenu(currEntry));

            // When adding a new stop, the vertex is added to the intermediate vertex list and the path is redrawn - LM
            // No combo box update so we call checkInput()
            addStopMenu.setOnAction(handleAddStopMenu(addStopMenuText, currEntry));

            // Sets the end node and removed the previous node from the list (re-added in updatePath()) - LM
            endPathMenu.setOnAction(handleEndPathMenu(currEntry));

            //FIXME: Make these ones require that thing is visible
            whatsHereMenu.setOnAction(handleWhatsHereMenu(currEntry, 3));

            addFavoriteFromMenu(addFavoriteMenuText, addFavoriteMenu, currEntry);
        });
        try{
            if(CurrentUser.getCurrentUser().getUuid() != null && DatabaseAPI.getDatabaseAPI().getServiceEntry(CurrentUser.getCurrentUser().getUuid(), "additionalInstructions").getCompleteStatus().equals("false")) {
                if (DatabaseAPI.getDatabaseAPI().getServiceEntry(CurrentUser.getCurrentUser().getUuid(), "uuid").getCompleteStatus().equals("false")) {
                    endNode.set(idToShortName("FEXIT00301"));
                } else {
                    endNode.set(idToShortName("FEXIT00201"));
                }
                contextMenu.getItems().remove(endPathMenu);
                contextMenu.getItems().remove(addStopMenu);
                filterNodes = true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        
        /*
         * Initialize the stop list
         */

        stopList.setCellFactory(x -> {
            final NavigationListCell cell = new NavigationListCell();
            cell.getCloseBtn().visibleProperty().bind(isCurrentlyNavigating.not());
            cell.getCloseBtn().setStyle("-fx-background-color: #E8321E; -fx-text-fill: #FFFFFF;");
            cell.getCloseBtn().setOnMouseClicked(e -> {
                final String shortName = cell.getLabel().getText();
                final String ID = shortNameToID(shortName);
                vertices.removeIf(vertex -> vertex.getID().equals(ID));

                if(startNode.get().equals(shortName)) {
                    startNode.set("");
                    checkInput();
                    Go.setDisable(true);
                }
                if(endNode.get().equals(shortName)) {
                    endNode.set("");
                    checkInput();
                    Go.setDisable(true);
                }
            });

            cell.setOnMouseClicked(e -> {
                if(isCurrentlyNavigating.get())
                    return;
                mapPanel.centerNode(mapPanel.getNode(shortNameToID(cell.getLabel().getText())));
                mapPanel.switchMap(mapPanel.getNode(shortNameToID(cell.getLabel().getText())).getFloor().get());
            });

            return cell;
        });


        startNode.addListener((observable, oldValue, newValue) -> updateDestinationList());
        endNode.addListener((observable, oldValue, newValue) -> updateDestinationList());

        vertices.addListener((ListChangeListener<Vertex>) c -> {
            while(c.next()){} //Needed to get all changes
            updateDestinationList();
        });

    }

    private void updateDestinationList() {
        stopList.getItems().clear();
        List<String> destinationLists = new ArrayList<>();
        if(!startNode.get().isEmpty())
            destinationLists.add(startNode.get());

        destinationLists.addAll(vertices.stream().map(v -> idToShortName(v.getID())).collect(Collectors.toList()));
        if(!endNode.get().isEmpty())
            destinationLists.add(endNode.get());

        for(String stop : destinationLists)//for (Vertex stop : vertices)
        {
            stopList.getItems().add(stop);//stop.getID()));
        }
    }

    private EventHandler<ActionEvent> handleWhatsHereMenu(NodeEntry currEntry, int i) {
        return e -> {
            mapPanel.switchMap(currEntry.getFloor());
            mapPanel.centerNode(mapPanel.getNode(currEntry.getNodeID())); //FIXME: DO on all?

            final JFXDialog dialog = new JFXDialog();
            final JFXDialogLayout layout = new JFXDialogLayout();

            layout.setHeading(new Text(currEntry.getLongName()));

            ScrollPane scrollPane = new ScrollPane();
            Label instructionsLabel = new Label();
            scrollPane.setPrefWidth(500);
            scrollPane.setPrefHeight(220);
            instructionsLabel.setText(currEntry.getDescription());
            layout.setBody(scrollPane);
            scrollPane.setContent(instructionsLabel);

            //FIXME: DO BREAKS W/ CSS
            //layout.setBody(new Text(currEntry.getDescription()));

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

            if (CurrentUser.getCurrentUser().isAuthenticated()) {
                final JFXButton toggleFavorite = new JFXButton("Add To Favorites");
                boolean addFavorite = true;
                try {
                    if (getUserFavorites().contains(currEntry.getNodeID())) {
                        toggleFavorite.setText("Remove Favorite");
                        addFavorite = false;
                    }
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
                boolean finalAddFavorite = addFavorite;
                toggleFavorite.setOnAction(a -> {
                    if (finalAddFavorite) {
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

                layout.setActions(toggleFavorite, directionsFrom, closeBtn);
            } else {
                layout.setActions(directionsFrom, closeBtn);
            }
            if (!filterNodes) {
                layout.getActions().add(layout.getActions().size() - i, directionsTo);
            }

            dialog.setContent(layout);
            SceneContext.autoTranslate(dialog);
            mapPanel.showDialog(dialog);
        };
    }

    private EventHandler<ActionEvent> handleEndPathMenu(NodeEntry currEntry) {
        return e -> {
            endNode.set(idToShortName(currEntry.getNodeID()));
            try {
                handleEndNodeChange();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        };
    }

    private EventHandler<ActionEvent> handleAddStopMenu(StringProperty stopMenuText, NodeEntry currEntry) {
        return e -> {
            if (stopMenuText.get().equals("Add Stop")) {
                vertices.add(graph.getVertex(currEntry.getNodeID()));
                mapPanel.switchMap(currEntry.getFloor());
                mapPanel.centerNode(mapPanel.getNode(currEntry.getNodeID()));
                //drawStop(currEntry);
                try {
                    addNodeToRecent(currEntry);
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            } else {
                vertices.remove(graph.getVertex(currEntry.getNodeID()));
            }
            checkInput();
        };
    }

    private EventHandler<ActionEvent> handleStartPathMenu(NodeEntry currEntry) {
        return e -> {
            if (filterNodes && !(currEntry.getNodeType().equals("PARK") || currEntry.getNodeType().equals("WALK")))
                return;
            startNode.set(idToShortName(currEntry.getNodeID()));
            try {
                handleStartNodeChange();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        };
    }

    private void addFavoriteFromMenu(StringProperty favoritesText, MenuItem addFavoriteMenu, NodeEntry currEntry) {
        addFavoriteMenu.setOnAction(e -> {
            if (favoritesText.get().equals("Add To Favorites")) {
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

    private double getAngleFor(String dir)
    {
        if(dir == null) return 0;
        switch (dir) {
            case "UP":
                return (Math.toRadians(90));
            case "LEFT":
                return (0);
            case "RIGHT":
                return (Math.toRadians(180));
            case "DOWN":
               return (Math.toRadians(270));
        }
        return 0;
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

            mapPanel.switchMap(this.startNodeDisplay.getFloor().get());//findNodeEntry(startNodeDisplay.getId()).getFloor());
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

            final BooleanBinding isStop = Bindings.createBooleanBinding(() -> vertices.contains(graph.getVertex(nodeID)), vertices);



            drawableNode.scaleXProperty().bind(Bindings.when(isStartOrEndNode.or(isStop)).then(1).otherwise(0.8));
            drawableNode.scaleYProperty().bind(drawableNode.scaleXProperty());

            drawableNode.setStrokeWidth(2.0);

            drawableNode.fillProperty().bind(Bindings.when(isStartOrEndNode).then(getNodeTypeColor(drawableNode.getNodeType())).otherwise(
                    Bindings.when(isStop).then(new Color(.75, 0, 0, 1)).otherwise(new Color(0, 0, 0, 0))

                    ));

            drawableNode.strokeProperty().bind(
                    Bindings.when(isStartNode).then(Color.ORANGE).otherwise(
                            Bindings.when(isEndNode).then(Color.GREEN).otherwise(
                                    Bindings.when(isStop).then(new Color(1, 0, 0, 1)).otherwise(
                                        getNodeTypeColor(drawableNode.getNodeType())))
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
                    + "-fx-background-color: #03256C; "
                    + "-fx-text-fill: white;");
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
            mapPanel.switchMap(this.endNodeDisplay.getFloor().get());////findNodeEntry(endNodeDisplay.getId()).getFloor());
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


        List<Vertex> pathVertices = new ArrayList<>(vertices);

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
                if(disableStair.isSelected())
                    path = this.graph.getUnorderedPathNoStair(pathVertices);
                else
                    path = this.graph.getUnorderedPath(pathVertices);
            } else {
                if(disableStair.isSelected())
                    path = this.graph.getPathNoStair(pathVertices);
                else
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
                    tt.textProperty().set(instructionsList.get(localStop) + "\n" + etaList.get(localStop));

                    tt.setStyle("-fx-font: normal bold 15 Langdon; "
                            + "-fx-background-color: #03256C; "
                            + "-fx-text-fill: white;");
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
        directionsList.clear();
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

        // Calculate Directions
        direct = "UP";
        for (String inst : instructionsList) {
            directionsList.add(direct);
            changeDirection(inst);
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
        switch (direct) {
            case "UP":
                direct = "DOWN";
                break;
            case "LEFT":
                direct = "RIGHT";
                break;
            case "RIGHT":
                direct = "LEFT";
                break;
            case "DOWN":
                direct = "UP";
                break;
        }
    }

    private void switchDirectionRight() {
        switch (direct) {
            case "UP":
                direct = "RIGHT";
                break;
            case "LEFT":
                direct = "UP";
                break;
            case "RIGHT":
                direct = "DOWN";
                break;
            case "DOWN":
                direct = "LEFT";
                break;
        }
    }

    private void switchDirectionLeft() {
        switch (direct) {
            case "UP":
                direct = "LEFT";
                break;
            case "LEFT":
                direct = "DOWN";
                break;
            case "RIGHT":
                direct = "UP";
                break;
            case "DOWN":
                direct = "RIGHT";
                break;
        }
    }

    /**
     * Function to react to 'Start Navigation' button being pressed and start the route stepper
     * @author ZheCheng Song
     */
    public void startNavigation() {
        currentStep.set(0);
        isCurrentlyNavigating.set(true);

        parseRoute();

        mapPanel.switchMap(pathVertex.get(0).getFloor());


        //if(userNodeDisplay != null) //This can't be null anymore
        mapPanel.unDraw(userNodeDisplay.getId());
        mapPanel.draw(this.userNodeDisplay);

        this.startNodeDisplay = mapPanel.getNode(pathVertex.get(0).getID());
        this.endNodeDisplay = mapPanel.getNode(pathVertex.get(pathVertex.size()-1).getID());
        mapPanel.centerNode(userNodeDisplay);

        startNodeDisplay.toFront();
        endNodeDisplay.toFront();
        userNodeDisplay.toFront();

        drawSEIcons();

        addInstructionsToTreeView();

        final StringProperty currentInstructionText = new SimpleStringProperty("");
        currentInstructionText.bind(Bindings.when(Bindings.isEmpty(instructionsList)).then("").otherwise(Bindings.stringValueAt(instructionsList, currentStep)));


        Instruction.textProperty().bind(Translator.getTranslator().getTranslationBinding(currentInstructionText));//Bindings.when(Bindings.isEmpty(instructionsList)).then("").otherwise(Bindings.createStringBinding(() -> Translator.getTranslator().translate(instructionsList.get(currentStep.get())), currentStep)));///Bindings.stringValueAt(instructionsList, currentStep)));
        ETA.textProperty().bind(Bindings.stringValueAt(etaList, currentStep));
        currentDirection.bind(Bindings.stringValueAt(directionsList, currentStep));
        nextDirection.bind(Bindings.stringValueAt(directionsList,
                Bindings.createIntegerBinding(() -> Math.min(currentStep.get() + 1, stopsList.size() - 1), currentStep, stopsList)));

        setNavIcon();
    }

    /**
     * Function to react to 'Prev' button being pressed and go to the previous point with stepper
     * @author ZheCheng Song
     */
    public void goToPrevNode() {
        currentStep.set(currentStep.get() - 1);

        if(!pathVertex.get(stopsList.get(currentStep.get())).getFloor().equals(mapPanel.getFloor().getValue())){
            mapPanel.switchMap(pathVertex.get(stopsList.get(currentStep.get())).getFloor());
        }

        mapPanel.centerNode(userNodeDisplay);

        setNavIcon();
    }

    /**
     * Function to react to 'Next' button being pressed and go to the next point with stepper
     * @author ZheCheng Song
     */
    public void goToNextNode() {
        currentStep.set(currentStep.get() + 1);

        if(!pathVertex.get(stopsList.get(currentStep.get())).getFloor().equals(mapPanel.getFloor().getValue())){
            mapPanel.switchMap(pathVertex.get(stopsList.get(currentStep.get())).getFloor());
        }

        mapPanel.centerNode(userNodeDisplay);

        setNavIcon();
    }

    /**
     * Function to react to 'End Navigation' button being pressed and stop the stepper
     * @author ZheCheng Song
     */
    public void endNavigation() {
        currentStep.set(0);
        isCurrentlyNavigating.set(false);
        unDrawSEIcons();
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
            final int nextFloor = mapPanel.getDoubleStringConverter().fromString(curInstruction.substring(curInstruction.length()-2)).intValue();//Integer.parseInt(curInstruction.substring(curInstruction.length()-1));
            final int currentFloor = mapPanel.getDoubleStringConverter().fromString(pathVertex.get(currentStep.get()).getFloor()).intValue();
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
    public void handleViewInstructions() throws IOException {
        final JFXDialog dialog = new JFXDialog();
        final JFXDialogLayout layout = new JFXDialogLayout();

        //TODO: Italics for previously finished instructions?
        //TODO: better align ETA text.
        layout.setHeading(new Text("Directions from: " + startNode.getValue() + " to " +  endNode.getValue()));

        StringBuilder directions = new StringBuilder();
        for(int i = 0; i < stopsList.size(); i++)
        {
            final String instruction = Translator.getTranslator().translate(instructionsList.get(i));
            final String eta = etaList.get(i);

            if(i < stopsList.size() - 1)
                directions.append(instruction).append("\t\t(").append(eta).append(")\n");
            else
                directions.append(instruction);
        }
        //FIXME: DO BREAKS W/ CSS
        ScrollPane scrollPane = new ScrollPane();
        Label directionsLabel = new Label();
        scrollPane.setPrefWidth(400);
        scrollPane.setPrefHeight(220);
        directionsLabel.setText(directions.toString());
        layout.setBody(scrollPane);
        scrollPane.setContent(directionsLabel);
        //directionsLabel.textProperty().bind(Translator.getTranslator().getTranslationBinding(directionsLabel.getText()));


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
        SceneContext.autoTranslate(dialog);
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
        final double initZoomLevel = mapPanel.getInternalZoomLevel().get();

        final File file = new File(System.currentTimeMillis() + ".pdf");

        //Create the document
        final PDDocument pdfDocument = new PDDocument();


        mapPanel.getInternalZoomLevel().set(2);

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

            contentStream.showText(Translator.getTranslator().translate("Directions from: ") + startNode.getValue());
            contentStream.newLine();
            contentStream.endText();



            contentStream.beginText();
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 670);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);

            contentStream.showText(Translator.getTranslator().translate("To: ") + endNode.getValue());
            contentStream.newLine();
            contentStream.endText();

            /*
             * Page numbers
             */
            contentStream.beginText();
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(480, 30);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);

            contentStream.showText(Translator.getTranslator().translate("Page " + (p + 1) + " of " + numPages));
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
                    contentStream.showText(Translator.getTranslator().translate(instruction) + "     (" + eta + ")");
                else
                    contentStream.showText(Translator.getTranslator().translate(instruction));
                contentStream.endText();


                final Vertex currVertex = pathVertex.get(stopsList.get(i));
                mapPanel.switchMap(currVertex.getFloor());
                mapPanel.centerNode(mapPanel.getNode(currVertex.getID()));
                currentStep.set(i);
                changeDirection(instructionsList.get(currentStep.get()));

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

                contentStream.drawImage(pdfImage, 350, 555 - (i % INSTRUCTIONS_PER_PAGE) * 110);


                // ImageIO.write(bufferedImage, "png", new File(System.currentTimeMillis() + ".png"));
            }
            contentStream.close();
        }
        currentStep.set(initialCurrStep);
        mapPanel.switchMap(initialFloor);
        mapPanel.getInternalZoomLevel().set(initZoomLevel);





        pdfDocument.save(file);
        pdfDocument.close();

        final Desktop desktop = Desktop.getDesktop();
        desktop.open(file);
    }

    /**
     * Clears all node selections and exits pathfinding mode
     * @author Leo Morris
     */
    public void clearList() throws SQLException {

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

        if(filterNodes && CurrentUser.getCurrentUser().getUuid() != null){
            if(DatabaseAPI.getDatabaseAPI().getServiceEntry(CurrentUser.getCurrentUser().getUuid(), "uuid").getCompleteStatus().equals("false")){
                endNode.set(idToShortName("FEXIT00301"));
            } else {
                endNode.set(idToShortName("FEXIT00201"));
            }
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
            if(!recentItem.getChildren().removeIf(t -> t.getValue().equals(node.getShortName()))) // Remove any tree item with a matching short name (prevents duplicates)
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

                double currentFloor =  mapPanel.getDoubleStringConverter().fromString(curV.getFloor());
                double nextFloor =  mapPanel.getDoubleStringConverter().fromString(nexV.getFloor());
                boolean isUp = nextFloor > currentFloor;

                //String Ins, boolean isUp, int xCoordinate, int yCoordinate, String ID, String floor
                final DrawableFloorInstruction imageOne = new DrawableFloorInstruction(Ins, isUp, (int) Math.floor(curV.getX()), (int) Math.floor(curV.getY()), curV.getFloor(), curV.getFloor());
                final DrawableFloorInstruction imageTwo = new DrawableFloorInstruction(Ins, !isUp, (int) Math.floor(nexV.getX()), (int) Math.floor(nexV.getY()), nexV.getFloor(), nexV.getFloor());
                mapPanel.draw(imageOne);
                mapPanel.draw(imageTwo);

                final int curStep = i;

                imageOne.setOnMouseClicked(event -> goToStep(curStep + 1));

                imageTwo.setOnMouseClicked(event -> goToStep(curStep));
            }
        }
    }

    private void unDrawSEIcons(){
        mapPanel.getCanvas().getChildren().removeIf(x -> x instanceof DrawableFloorInstruction);
    }

    // Will come in handy when implementing TreeView for instructions
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

    public void handleInstructionListSelection() {
        if(instructionTreeView.getSelectionModel().getSelectedItem()!=null &&
                !instructionTreeView.getSelectionModel().getSelectedItem().getParent().equals(instructionTreeViewItem) &&
                !instructionTreeView.getSelectionModel().getSelectedItem().equals(instructionTreeViewItem)) { // Do not center on drop downs, root item or null items, only actual nodes
            // Fill in
            int index = getInstructionIndex(instructionTreeView.getSelectionModel().getSelectedItem().getValue());
            goToStep(index);
        }
    }

    private int getInstructionIndex (String ins) {
        for(int i = 0; i < instructionsList.size(); i++){
            if (ins.contains(instructionsList.get(i)))
                return i;
        }
        return -1;
    }

    private void addInstructionsToTreeView(){
        instructionTreeViewItem.getChildren().clear();

        TreeItem<String> floorOneInstruction = new TreeItem<>("Floor One Instructions");
        TreeItem<String> floorTwoInstruction = new TreeItem<>("Floor Two Instructions");
        TreeItem<String> floorThreeInstruction = new TreeItem<>("Floor Three Instructions");
        TreeItem<String> floorGroundInstruction = new TreeItem<>("Ground Floor Instructions");
        TreeItem<String> floorLowerOneInstruction = new TreeItem<>("Floor L1 Instructions");
        TreeItem<String> floorLowerTwoInstruction = new TreeItem<>("Floor L2 Instructions");


        String floor = pathVertex.get(0).getFloor();
        int index = 0;
        for(String ins : instructionsList){
            index++;
            switch (floor) {
                case "L2":
                    floorLowerTwoInstruction.getChildren().add(new TreeItem<>(index + ". " +ins));
                    break;
                case "L1":
                    floorLowerOneInstruction.getChildren().add(new TreeItem<>(index + ". " +ins));
                    break;
                case "G":
                case " G":
                    floorGroundInstruction.getChildren().add(new TreeItem<>(index + ". " +ins));
                    break;
                case "1":
                case " 1":
                    floorOneInstruction.getChildren().add(new TreeItem<>(index + ". " +ins));
                    break;
                case "2":
                case " 2":
                    floorTwoInstruction.getChildren().add(new TreeItem<>(index + ". " +ins));
                    break;
                case "3":
                case " 3":
                    floorThreeInstruction.getChildren().add(new TreeItem<>(index + ". " +ins));
                    break;
                default:
                    break;
            }
            if(ins.contains("Take")){
                floor = ins.substring(ins.length()-2);
            }
        }

        instructionTreeViewItem.getChildren().addAll(floorLowerTwoInstruction, floorLowerOneInstruction, floorGroundInstruction
                , floorOneInstruction, floorTwoInstruction, floorThreeInstruction);
        instructionTreeViewItem.getChildren().removeIf(x -> x.getChildren().isEmpty());
    }
}
