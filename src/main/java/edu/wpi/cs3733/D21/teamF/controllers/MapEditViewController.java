package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.database.EdgeHandler;
import edu.wpi.cs3733.D21.teamF.database.NodeHandler;
import edu.wpi.cs3733.D21.teamF.entities.CurrentUser;
import edu.wpi.cs3733.D21.teamF.entities.EdgeEntry;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.CSVManager;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import edu.wpi.cs3733.D21.teamF.utils.UIConstants;
import edu.wpi.cs3733.uicomponents.MapPanel;
import edu.wpi.cs3733.uicomponents.entities.DrawableCircle;
import edu.wpi.cs3733.uicomponents.entities.DrawableEdge;
import edu.wpi.cs3733.uicomponents.entities.DrawableNode;
import edu.wpi.cs3733.uicomponents.entities.DrawableRectSelection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class MapEditViewController implements IController {

    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton loadButton;
    @FXML
    private JFXButton resetFromDB;
    @FXML
    private JFXButton newButton;
    @FXML
    private JFXButton editButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private JFXButton favoriteButton;
    @FXML
    private JFXComboBox<String> searchComboBox;
    @FXML
    private JFXTextField searchField;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab nodesTab;
    @FXML
    private Tab edgesTab;
    @FXML
    private JFXTreeTableView<NodeEntry> nodeTreeTable;
    @FXML
    private JFXTreeTableView<EdgeEntry> edgeTreeTable;
    @FXML
    private MapPanel mapPanel;
    @FXML
    private JFXToggleButton edgeCreationToggle;

    private boolean clickToMakeEdge;
    private boolean favoriteOnly = false;

    private final ObservableList<EdgeEntry> edgeEntryObservableList = FXCollections.observableArrayList();
    private final ObservableList<NodeEntry> nodeEntryObservableList = FXCollections.observableArrayList();
    private final List<NodeEntry> favoriteList = new ArrayList<>();
    private Circle selectedCircle = null;

    private Line selectedLine = null;
    private Circle firstCircle = null;
    private Circle secondCircle = null;

    List<NodeEntry> nodeList = new ArrayList<>();

    private final DrawableRectSelection rectSelector = new DrawableRectSelection(-1, -1, "N/A");

    //FIXME: DO BETTER!
    private final DrawableCircle alignLeft = new DrawableCircle(-1, -1, "al", "n/a");
    private final DrawableCircle alignHorizontalMiddle = new DrawableCircle(-1, -1, "ahm", "n/a");
    private final DrawableCircle alignRight = new DrawableCircle(-1, -1, "ar", "n/a");
    private final DrawableCircle alignTop = new DrawableCircle(-1, -1, "at", "n/a");
    private final DrawableCircle alignVerticalMiddle = new DrawableCircle(-1, -1, "avm", "n/a");
    private final DrawableCircle alignBottom = new DrawableCircle(-1, -1, "ab", "n/a");

    @FXML
    private void initialize() throws SQLException {
        clickToMakeEdge = false;

        // Node initialization
        List<NodeEntry> data = new ArrayList<>();
        try {
            NodeHandler newNodeHandler = new NodeHandler();
            data = newNodeHandler.genNodeEntryObjects();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        data.stream().sorted(Comparator.comparing(NodeEntry::getNodeID)).collect(Collectors.toList()).forEach(node -> {
            mapPanel.draw(getEditableNode(node));

            nodeEntryObservableList.add(node);
        });

        int colWidth = 150;
        JFXTreeTableColumn<NodeEntry, String> idColumn = new JFXTreeTableColumn<>("Node ID");
        idColumn.setPrefWidth(colWidth);
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().getNodeIDProperty());

        JFXTreeTableColumn<NodeEntry, String> shortColumn = new JFXTreeTableColumn<>("Name");
        shortColumn.setPrefWidth(colWidth);
        shortColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().getShortNameProperty());

        final TreeItem<NodeEntry> root = new RecursiveTreeItem<>(nodeEntryObservableList, RecursiveTreeObject::getChildren);
        nodeTreeTable.setRoot(root);
        nodeTreeTable.setShowRoot(false);
        nodeTreeTable.getColumns().setAll(idColumn, shortColumn);

        final ContextMenu contextMenu = new ContextMenu();

        final MenuItem createNodeMenuItem = new MenuItem("Create Node Here");

        contextMenu.getItems().addAll(createNodeMenuItem);


        mapPanel.getMap().setOnContextMenuRequested(event -> {
            contextMenu.show(mapPanel.getMap(), event.getScreenX(), event.getScreenY());

            createNodeMenuItem.setOnAction((ActionEvent e) -> {
                NodeEntry nodeEntry = new NodeEntry();
                nodeEntry.setXCoordinate("" + (int) (event.getX() * mapPanel.getZoomLevel().get()));
                nodeEntry.setYCoordinate("" + (int) (event.getY() * mapPanel.getZoomLevel().get()));
                nodeEntry.setFloor(mapPanel.getFloor().get());

                try {
                    openEditNodeDialog(nodeEntry);
                    if (!checkNodeEntryNotEmpty(nodeEntry)) return;
                    nodeEntryObservableList.add(nodeEntry); // add the new node to the Observable list (which is linked to table and updates) - KD

                    updateNodeEntry(nodeEntry);
                    handleSearch();
                } catch (IOException | SQLException ioException) {
                    ioException.printStackTrace();
                }


            });
        });

        ObservableList<String> searchables = FXCollections.observableArrayList();
        searchables.add("Node ID");
        searchables.add("Floor");
        searchables.add("Building");
        searchables.add("Node Type");
        searchables.add("Long Name");
        searchables.add("Short Name");
        searchables.add("Edge ID");
        searchables.add("Start Node");
        searchables.add("End Node");
        searchComboBox.setItems(searchables);
        searchComboBox.setValue("Node ID");

        // Edge initialization
        List<EdgeEntry> edgeData = new ArrayList<>();
        try {
            EdgeHandler newEdgeHandler = new EdgeHandler();
            edgeData = newEdgeHandler.genEdgeEntryObjects();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        edgeEntryObservableList.addAll(edgeData.stream().sorted(Comparator.comparing(EdgeEntry::getEdgeID)).collect(Collectors.toList()));


        // Set up cell factory for the edge ID table
        JFXTreeTableColumn<EdgeEntry, String> edgeIDColumn = new JFXTreeTableColumn<>("Edge ID");
        edgeIDColumn.setPrefWidth(300);
        edgeIDColumn.setCellValueFactory(param -> param.getValue().getValue().edgeIDProperty());

        final TreeItem<EdgeEntry> edgeRoot = new RecursiveTreeItem<>(edgeEntryObservableList, RecursiveTreeObject::getChildren);
        edgeTreeTable.getColumns().setAll(edgeIDColumn);
        edgeTreeTable.setRoot(edgeRoot);
        edgeTreeTable.setShowRoot(false);

        // Set up floor comboBox and draw the nodes and edges on current floor
        drawEdgeNodeOnFloor();

        //Bind selector so that we are always on floor
        rectSelector.shouldDisplay().set(false);
        rectSelector.getFloor().bind(mapPanel.getFloor());

        final List<NodeEntry> selectedNodes = new ArrayList<>();



        alignLeft.getFloor().bind(mapPanel.getFloor());
        alignHorizontalMiddle.getFloor().bind(mapPanel.getFloor());
        alignRight.getFloor().bind(mapPanel.getFloor());
        alignTop.getFloor().bind(mapPanel.getFloor());
        alignVerticalMiddle.getFloor().bind(mapPanel.getFloor());
        alignBottom.getFloor().bind(mapPanel.getFloor());

        alignLeft.shouldDisplay().bind(rectSelector.shouldDisplay());
        alignHorizontalMiddle.shouldDisplay().bind(rectSelector.shouldDisplay());
        alignRight.shouldDisplay().bind(rectSelector.shouldDisplay());
        alignTop.shouldDisplay().bind(rectSelector.shouldDisplay());
        alignVerticalMiddle.shouldDisplay().bind(rectSelector.shouldDisplay());
        alignBottom.shouldDisplay().bind(rectSelector.shouldDisplay());

        final int ALIGN_FLOAT_DIST = 20; //FIXME: DO BETTER

        alignLeft.xCoordinateProperty().bind(rectSelector.xProperty());
        alignLeft.yCoordinateProperty().bind(rectSelector.yProperty().subtract(ALIGN_FLOAT_DIST));
        alignLeft.setOnMouseClicked(x -> {
            for(NodeEntry e : selectedNodes)
            {
                if(mapPanel.getNode(e.getNodeID()).shouldDisplay().get()) {
                    final int xCoordinate = (int) (rectSelector.xProperty().get() * mapPanel.getZoomLevel().get());
                    final int yCoordinate = Integer.parseInt(e.getYCoordinate());
                    updateNode(e.getNodeID(), xCoordinate, yCoordinate);
                    //((DrawableNode)mapPanel.getNode(e.getNodeID())).xCoordinateProperty().set(xCoordinate);
                }
            }

        });
        alignLeft.setOnMouseEntered(e -> alignLeft.setFill(Paint.valueOf("#F0C808")));
        colorAlignmentCircles(alignLeft);

        alignHorizontalMiddle.xCoordinateProperty().bind(rectSelector.xProperty().add(rectSelector.widthProperty().divide(2.0)));
        alignHorizontalMiddle.yCoordinateProperty().bind(rectSelector.yProperty().subtract(ALIGN_FLOAT_DIST));
        alignHorizontalMiddle.setOnMouseClicked(x -> {
            for(NodeEntry e : selectedNodes)
            {
                if(mapPanel.getNode(e.getNodeID()).shouldDisplay().get()) {
                    final int xCoordinate = (int) (rectSelector.xProperty().add(rectSelector.widthProperty().divide(2.0)).get() * mapPanel.getZoomLevel().get());
                    final int yCoordinate = Integer.parseInt(e.getYCoordinate());
                    updateNode(e.getNodeID(), xCoordinate, yCoordinate);
                }
                //((DrawableNode)mapPanel.getNode(e.getNodeID())).xCoordinateProperty().set(xCoordinate);
            }
        });
        colorAlignmentCircles(alignHorizontalMiddle);

        alignRight.xCoordinateProperty().bind(rectSelector.xProperty().add(rectSelector.widthProperty()));
        alignRight.yCoordinateProperty().bind(rectSelector.yProperty().subtract(ALIGN_FLOAT_DIST));
        alignRight.setOnMouseClicked(x -> {
            for(NodeEntry e : selectedNodes)
            {
                if(mapPanel.getNode(e.getNodeID()).shouldDisplay().get()){
                final int xCoordinate = (int) (rectSelector.xProperty().add(rectSelector.widthProperty()).get() * mapPanel.getZoomLevel().get());
                final int yCoordinate = Integer.parseInt(e.getYCoordinate());
                updateNode(e.getNodeID(), xCoordinate, yCoordinate);
            }
                //((DrawableNode)mapPanel.getNode(e.getNodeID())).xCoordinateProperty().set(xCoordinate);
            }
        });
        colorAlignmentCircles(alignRight);

        alignTop.xCoordinateProperty().bind(rectSelector.xProperty().add(rectSelector.widthProperty()).add(ALIGN_FLOAT_DIST));
        alignTop.yCoordinateProperty().bind(rectSelector.yProperty());
        alignTop.setOnMouseClicked(x -> {
                    for(NodeEntry e : selectedNodes)
                    {
                        if(mapPanel.getNode(e.getNodeID()).shouldDisplay().get()) {
                            final int xCoordinate = Integer.parseInt(e.getXCoordinate());
                            final int yCoordinate = (int) (rectSelector.yProperty().multiply(mapPanel.getZoomLevel()).get());
                            updateNode(e.getNodeID(), xCoordinate, yCoordinate);
                            //((DrawableNode)mapPanel.getNode(e.getNodeID())).xCoordinateProperty().set(xCoordinate);
                        }
                    }
        });
        colorAlignmentCircles(alignTop);

        alignVerticalMiddle.xCoordinateProperty().bind(rectSelector.xProperty().add(rectSelector.widthProperty()).add(ALIGN_FLOAT_DIST));
        alignVerticalMiddle.yCoordinateProperty().bind(rectSelector.yProperty().add(rectSelector.heightProperty().divide(2.0)));
        alignVerticalMiddle.setOnMouseClicked(x -> {
            for(NodeEntry e : selectedNodes)
            {
                if(mapPanel.getNode(e.getNodeID()).shouldDisplay().get()) {
                    final int xCoordinate = Integer.parseInt(e.getXCoordinate());
                    final int yCoordinate = (int) (rectSelector.yProperty().add(rectSelector.heightProperty().divide(2.0)).multiply(mapPanel.getZoomLevel()).get());
                    updateNode(e.getNodeID(), xCoordinate, yCoordinate);
                    //((DrawableNode)mapPanel.getNode(e.getNodeID())).xCoordinateProperty().set(xCoordinate);
                }
            }
        });
        colorAlignmentCircles(alignVerticalMiddle);

        alignBottom.xCoordinateProperty().bind(rectSelector.xProperty().add(rectSelector.widthProperty()).add(ALIGN_FLOAT_DIST));
        alignBottom.yCoordinateProperty().bind(rectSelector.yProperty().add(rectSelector.heightProperty()));
        alignBottom.setOnMouseClicked(x -> {
            for(NodeEntry e : selectedNodes)
            {
                if(mapPanel.getNode(e.getNodeID()).shouldDisplay().get()) {
                    final int xCoordinate = Integer.parseInt(e.getXCoordinate());
                    final int yCoordinate = (int) (rectSelector.yProperty().add(rectSelector.heightProperty()).multiply(mapPanel.getZoomLevel()).get());
                    updateNode(e.getNodeID(), xCoordinate, yCoordinate);
                    //((DrawableNode)mapPanel.getNode(e.getNodeID())).xCoordinateProperty().set(xCoordinate);
                }
            }
        });
        colorAlignmentCircles(alignBottom);


        //Starts drawing the rectangle
        mapPanel.getMap().setOnDragDetected(x -> {
            rectSelector.x0CoordinateProperty().set((int) (x.getX() * mapPanel.getZoomLevel().get()));
            rectSelector.y0CoordinateProperty().set((int) (x.getY() * mapPanel.getZoomLevel().get()));
            rectSelector.x1CoordinateProperty().set((int) (x.getX() * mapPanel.getZoomLevel().get()));
            rectSelector.y1CoordinateProperty().set((int) (x.getY() * mapPanel.getZoomLevel().get()));

            rectSelector.shouldDisplay().set(true);
        });

        //Allows for drag to select region
        mapPanel.getMap().setOnMouseDragged(x -> {
            rectSelector.x1CoordinateProperty().set((int) (x.getX() * mapPanel.getZoomLevel().get()));
            rectSelector.y1CoordinateProperty().set((int) (x.getY() * mapPanel.getZoomLevel().get()));
        });

        //Clears rect on other click
        mapPanel.getMap().setOnMousePressed(x -> {
            rectSelector.shouldDisplay().set(false);
            selectedNodes.clear();
        });

        mapPanel.getMap().setOnMouseReleased(x -> {
            if(!rectSelector.isVisible())
                return;

            selectedNodes.clear();

            final int X0 = (int) (rectSelector.getX() * mapPanel.getZoomLevel().get());
            final int X1  = (int) ((rectSelector.getX() + rectSelector.getWidth()) * mapPanel.getZoomLevel().get());

            final int Y0 = (int) (rectSelector.getY() * mapPanel.getZoomLevel().get());
            final int Y1  = (int) ((rectSelector.getY() + rectSelector.getHeight()) * mapPanel.getZoomLevel().get());


            //FIXME: DO THIS BETTER
            //Drag done
            for(NodeEntry n : nodeEntryObservableList) {
                final int X = Integer.parseInt(n.getXCoordinate());
                final int Y = Integer.parseInt(n.getYCoordinate());
                if (X0 <= X && X <= X1 && Y0 <= Y && Y <= Y1 && mapPanel.getNode(n.getNodeID()).shouldDisplay().get() && mapPanel.getFloor().get().equals(n.getFloor()))//mapPanel.getFloor().get().equals(n.getFloor()))
                    selectedNodes.add(n);
            }
        });

        /*
          try {
            DatabaseAPI.getDatabaseAPI().editNode(drawableNode.getId(), "" + drawableNode.xCoordinateProperty().get(), "xcoord");
            DatabaseAPI.getDatabaseAPI().editNode(drawableNode.getId(), "" + drawableNode.yCoordinateProperty().get(), "ycoord");

            ///FIXME: BIND PROPERTIES TOGETHER

            for (NodeEntry entry : nodeEntryObservableList) {
                if (entry.getNodeID().equals(drawableNode.getId())) {
                    entry.setXCoordinate("" + drawableNode.xCoordinateProperty().get());
                    entry.setYCoordinate("" + drawableNode.yCoordinateProperty().get());
                    break;
                }
            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }
         */
        rectSelector.setMouseTransparent(true);

        ArrayList<String> favList = DatabaseAPI.getDatabaseAPI().getUserNodes("favorite", CurrentUser.getCurrentUser().getLoggedIn().getUsername());

        for(NodeEntry nodeEntry : nodeEntryObservableList) {
            for(String favID : favList) {
                if(nodeEntry.getNodeID().equals(favID)) favoriteList.add(nodeEntry);
            }
        }

        tabPane.setVisible(false);
        nodeTreeTable.setVisible(false);
        edgeTreeTable.setVisible(false);

    }

    private void colorAlignmentCircles(DrawableCircle drawableCircle) {
        drawableCircle.setOnMouseEntered(e -> drawableCircle.setFill(Color.GOLDENROD.brighter()));
        drawableCircle.setOnMouseExited(e -> drawableCircle.setFill(Color.GRAY.darker()));

    }

    /**
     * Opens an edit dialog based on the tab opened
     * @author KD, LM, KH
     */
    public void handleEdit() throws Exception {

        NodeEntry selectedNode;
        if (nodesTab.isSelected()) {
            if (nodeTreeTable.getSelectionModel().getSelectedIndex() < 0) return;
            try {
                selectedNode = nodeTreeTable.getSelectionModel().getSelectedItem().getValue(); // get item the is selected - KD
            } catch (NullPointerException e) {
                return;
            }

            String targetID = selectedNode.getNodeID();
            DatabaseAPI.getDatabaseAPI().deleteNode(targetID);
            mapPanel.unDraw(targetID);

            String previousID = selectedNode.getNodeID();
            openEditNodeDialog(selectedNode); // allow editing of selection - KD
            updateNodeEntry(selectedNode);
            reassignAssociatedEdges(previousID, selectedNode.getNodeID());

            drawEdgeNodeOnFloor();
        } else if (edgesTab.isSelected()) {
            // Get the current selected edge index
            if (edgeTreeTable.getSelectionModel().getSelectedIndex() < 0) return;
            EdgeEntry selectedEdge;
            try {
                selectedEdge = edgeTreeTable.getSelectionModel().getSelectedItem().getValue();
            } catch (ArrayIndexOutOfBoundsException e) {
                return;
            }
            if (selectedEdge != null) {
                String oldID = selectedEdge.getEdgeID();
                ArrayList<String> newValues = openEditEdgeDialog(selectedEdge);

                if(newValues == null)
                    return;

                DatabaseAPI.getDatabaseAPI().editEdge(oldID, newValues.get(1), "startNode");
                DatabaseAPI.getDatabaseAPI().editEdge(oldID, newValues.get(2), "endNode");
                DatabaseAPI.getDatabaseAPI().editEdge(oldID, newValues.get(0), "edgeId");

                final DrawableEdge drawableEdge = mapPanel.getNode(oldID);
                drawableEdge.setId(newValues.get(0));
                drawableEdge.getFloor().setValue(newValues.get(1));
                drawableEdge.getEndFloor().setValue(newValues.get(2));

                drawEdgeNodeOnFloor();

                // Focus on selected edge both on table and on map
                edgeTreeTable.requestFocus();
                edgeTreeTable.getSelectionModel().clearAndSelect(findEdge(newValues.get(0)));
                edgeTreeTable.scrollTo(findEdge(newValues.get(0)));
                handleSelectEdge();
            }
        }
        handleSearch();
    } //FIXME edited node and node disappeared?

    /**
     * Deletes the selected item based on the tab open
     *
     * @throws SQLException
     * @author KD LM KH ZS
     */
    public void handleDelete() throws SQLException {
        if (nodesTab.isSelected()) {
            int selectedIndex = nodeTreeTable.getSelectionModel().getSelectedIndex(); // get index of table that is selected - KD
            if (selectedIndex < 0) {
                return;
            }
            deleteAssociatedEdges(nodeTreeTable.getTreeItem(selectedIndex).getValue().getNodeID()); // delete all edges connected to the node
            String targetID = nodeTreeTable.getTreeItem(selectedIndex).getValue().getNodeID();
            DatabaseAPI.getDatabaseAPI().deleteNode(targetID);
            nodeEntryObservableList.remove(selectedIndex); // remove said index from table - KD
            selectedCircle = null;
            mapPanel.unDraw(targetID);
        } else if (edgesTab.isSelected()) {
            int index = edgeTreeTable.getSelectionModel().getSelectedIndex();
            EdgeEntry selectedEdge;

            // Check for a valid index (-1 = no selection)
            try {
                // Remove the edge, this will update the TableView automatically
                selectedEdge = edgeTreeTable.getTreeItem(index).getValue();
                DatabaseAPI.getDatabaseAPI().deleteEdge(edgeEntryObservableList.get(index).getEdgeID());
                edgeEntryObservableList.remove(index);
                selectedLine = null;
                mapPanel.unDraw(selectedEdge.getEdgeID());
            } catch (ArrayIndexOutOfBoundsException e) {
                // Create an alert to inform the user there is no edge selected
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(null); // Appears on top of all other windows
                alert.setTitle("No Selection");
                alert.setHeaderText("No Edge Selected");
                alert.setContentText("Please select an edge from the list");
                e.printStackTrace();
                alert.showAndWait();
            }
        }
    }

    /**
     * When new button is clicked, open appropriate dialog
     *
     * @author KD and LM
     */
    public void handleNew() throws IOException, SQLException {
        if (nodesTab.isSelected()) {
            NodeEntry newNode = new NodeEntry(); // create new node - KD
            openEditNodeDialog(newNode); // allow editing of the new node - KD
            if (!checkNodeEntryNotEmpty(newNode)) return;
            nodeEntryObservableList.add(newNode); // add the new node to the Observable list (which is linked to table and updates) - KD

            updateNodeEntry(newNode);
        } else if (edgesTab.isSelected()) {
            EdgeEntry newEdge = new EdgeEntry();
            openEditEdgeDialog(newEdge);
            if (!checkEdgeEntryNotEmpty(newEdge)) {
                return;
            }
            updateEdgeEntry(newEdge);
        }
        handleSearch();
    }

    /**
     * Search filters tree table based on which tab is open
     *
     * @author KD
     */
    public void handleSearch() {
        nodeTreeTable.setPredicate(nodeEntryTreeItem -> {
            if(favoriteOnly) {
                if(!isInFavorites(nodeEntryTreeItem.getValue().getNodeID())) return false;
            }
            if (searchField.getText().length() > 0) {
                switch (searchComboBox.getValue()) {
                    case "Node ID":
                        return nodeEntryTreeItem.getValue().getNodeID().contains(searchField.getText());
                    case "Floor":
                        return nodeEntryTreeItem.getValue().getFloor().equals(searchField.getText());
                    case "Building":
                        return nodeEntryTreeItem.getValue().getBuilding().contains(searchField.getText());
                    case "Node Type":
                        return nodeEntryTreeItem.getValue().getNodeType().contains(searchField.getText());
                    case "Long Name":
                        return nodeEntryTreeItem.getValue().getLongName().contains(searchField.getText());
                    case "Short Name":
                        return nodeEntryTreeItem.getValue().getShortName().contains(searchField.getText());
                    case "Edge ID":
                        for(EdgeEntry edgeEntry : edgeEntryObservableList) {
                            if(edgeEntry.getEdgeID().contains(searchField.getText())) {
                                if(edgeEntry.getEdgeID().contains(nodeEntryTreeItem.getValue().getNodeID())) return true; // here
                            }
                        }
                        return false;
                    case "Start Node":
                        for(EdgeEntry edgeEntry : edgeEntryObservableList) {
                            if(edgeEntry.getStartNode().contains(searchField.getText())) {
                                if(edgeEntry.getStartNode().equals(nodeEntryTreeItem.getValue().getNodeID())) return true;
                            }
                        }
                        return false;
                    case "End Node":
                        for(EdgeEntry edgeEntry : edgeEntryObservableList) {
                            if(edgeEntry.getEndNode().contains(searchField.getText())) {
                                if(edgeEntry.getEndNode().equals(nodeEntryTreeItem.getValue().getNodeID())) return true;
                            }
                        }
                        return false;
                    default:
                        return true;
                }
            }
            return true;
        });
        for (Node node : mapPanel.getCanvas().getChildren()) {
            if (node instanceof DrawableNode) {
                boolean isFavorite = true;
                if(favoriteOnly) isFavorite = isInFavorites(node.getId());
                switch (searchComboBox.getValue()) {
                    case "Node ID":
                        ((DrawableNode) node).setShouldDisplay(isFavorite && node.getId().contains(searchField.getText()));
                        break;
                    case "Floor":
                        ((DrawableNode) node).setShouldDisplay(isFavorite && ((DrawableNode) node).getFloor().get().equals(searchField.getText()));
                        break;
                    case "Building":
                        ((DrawableNode) node).setShouldDisplay(isFavorite && ((DrawableNode) node).getBuilding().contains(searchField.getText()));
                        break;
                    case "Node Type":
                        ((DrawableNode) node).setShouldDisplay(isFavorite && ((DrawableNode) node).getNodeType().contains(searchField.getText()));
                        break;
                    case "Long Name":
                        ((DrawableNode) node).setShouldDisplay(isFavorite && ((DrawableNode) node).getLongName().contains(searchField.getText()));
                        break;
                    case "Short Name":
                        ((DrawableNode) node).setShouldDisplay(isFavorite && ((DrawableNode) node).getShortName().contains(searchField.getText()));
                        break;
                    case "Edge ID":
                        boolean edgeExists = false;
                        for(EdgeEntry edgeEntry : edgeEntryObservableList) {
                            if(edgeEntry.getEdgeID().contains(searchField.getText())) {
                                edgeExists = true;
                                ((DrawableNode) node).setShouldDisplay(isFavorite && edgeEntry.getEdgeID().contains(node.getId()));
                                if(edgeEntry.getEdgeID().contains(node.getId())) break;
                            }
                        } // what about when there are no edges that pass?? - KD
                        if(!edgeExists) ((DrawableNode) node).setShouldDisplay(false);
                        break;
                    case "Start Node":
                        boolean edgeExists2 = false;
                        for(EdgeEntry edgeEntry : edgeEntryObservableList) {
                            if(edgeEntry.getStartNode().contains(searchField.getText())) {
                                edgeExists2 = true;
                                ((DrawableNode) node).setShouldDisplay(isFavorite && edgeEntry.getStartNode().equals(node.getId()));
                                if(edgeEntry.getStartNode().equals(node.getId())) break;
                            }
                        }
                        if(!edgeExists2) ((DrawableNode) node).setShouldDisplay(false);
                        break;
                    case "End Node":
                        boolean edgeExists3 = false;
                        for(EdgeEntry edgeEntry : edgeEntryObservableList) {
                            edgeExists3 = true;
                            if(edgeEntry.getEndNode().contains(searchField.getText())) {
                                ((DrawableNode) node).setShouldDisplay(isFavorite && edgeEntry.getEndNode().equals(node.getId()));
                                if(edgeEntry.getEndNode().equals(node.getId())) break;
                            }
                        }
                        if(!edgeExists3) ((DrawableNode) node).setShouldDisplay(false);
                        break;
                    default:
                        ((DrawableNode) node).setShouldDisplay(true);
                        break;
                }
            }
            if (node instanceof DrawableEdge) {
                boolean isFavorite = true;
                if(favoriteOnly) isFavorite = isInFavorites(((DrawableEdge) node).getStartNode().getNodeID()) || isInFavorites(((DrawableEdge) node).getStartNode().getNodeID());
                switch (searchComboBox.getValue()) {
                    case "Node ID":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && node.getId().contains(searchField.getText()));
                        break;
                    case "Floor":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && ((DrawableEdge) node).getFloor().get().equals(searchField.getText()));
                        break;
                    case "Building":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && ((DrawableEdge) node).getStartNode().getBuilding().contains(searchField.getText()) || ((DrawableEdge) node).getEndNode().getBuilding().contains(searchField.getText()));
                        break;
                    case "Node Type":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && ((DrawableEdge) node).getStartNode().getNodeType().contains(searchField.getText()) || ((DrawableEdge) node).getEndNode().getNodeType().contains(searchField.getText()));
                        break;
                    case "Long Name":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && ((DrawableEdge) node).getStartNode().getLongName().contains(searchField.getText()) || ((DrawableEdge) node).getEndNode().getLongName().contains(searchField.getText()));
                        break;
                    case "Short Name":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && ((DrawableEdge) node).getStartNode().getShortName().contains(searchField.getText()) || ((DrawableEdge) node).getEndNode().getShortName().contains(searchField.getText()));
                        break;
                    case "Edge ID":
                        String edgeID = ((DrawableEdge) node).getStartNode().getNodeID() + "_" + ((DrawableEdge) node).getEndNode().getNodeID();
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && edgeID.contains(searchField.getText()));
                        break;
                    case "Start Node":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && ((DrawableEdge) node).getStartNode().getNodeID().contains(searchField.getText()));
                        break;
                    case "End Node":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && ((DrawableEdge) node).getEndNode().getNodeID().contains(searchField.getText()));
                        break;
                    default:
                        ((DrawableEdge) node).setShouldDisplay(true);
                        break;
                }
            }
        }

        edgeTreeTable.setPredicate(edgeEntryTreeItem -> {
            if(favoriteOnly) {
                if(!(isInFavorites(edgeEntryTreeItem.getValue().getStartNode()) || isInFavorites(edgeEntryTreeItem.getValue().getEndNode()))) return false;
            }
            if (searchField.getText().length() > 0) {
                switch (searchComboBox.getValue()) {
                    case "Edge ID":
                        return edgeEntryTreeItem.getValue().getEdgeID().contains(searchField.getText());
                    case "Start Node":
                        return edgeEntryTreeItem.getValue().getStartNode().contains(searchField.getText());
                    case "End Node":
                        return edgeEntryTreeItem.getValue().getEndNode().contains(searchField.getText());
                    case "Node ID":
                        return edgeEntryTreeItem.getValue().getStartNode().contains(searchField.getText()) || edgeEntryTreeItem.getValue().getEndNode().contains(searchField.getText());
                    case "Floor":
                        for(NodeEntry nodeEntry : nodeEntryObservableList) {
                            if(nodeEntry.getFloor().equals(searchField.getText())) {
                                if(nodeEntry.getNodeID().equals(edgeEntryTreeItem.getValue().getStartNode()) || nodeEntry.getNodeID().equals(edgeEntryTreeItem.getValue().getEndNode()))
                                    return true;
                            }
                        }
                        return false;
                    case "Building":
                        for(NodeEntry nodeEntry : nodeEntryObservableList) {
                            if(nodeEntry.getBuilding().contains(searchField.getText())) {
                                if(nodeEntry.getNodeID().equals(edgeEntryTreeItem.getValue().getStartNode()) || nodeEntry.getNodeID().equals(edgeEntryTreeItem.getValue().getEndNode()))
                                    return true;
                            }
                        }
                        return false;
                    case "Node Type":
                        for(NodeEntry nodeEntry : nodeEntryObservableList) {
                            if(nodeEntry.getNodeType().contains(searchField.getText())) {
                                if(nodeEntry.getNodeID().equals(edgeEntryTreeItem.getValue().getStartNode()) || nodeEntry.getNodeID().equals(edgeEntryTreeItem.getValue().getEndNode()))
                                    return true;
                            }
                        }
                        return false;
                    case "Long Name":
                        for(NodeEntry nodeEntry : nodeEntryObservableList) {
                            if(nodeEntry.getLongName().contains(searchField.getText())) {
                                if(nodeEntry.getNodeID().equals(edgeEntryTreeItem.getValue().getStartNode()) || nodeEntry.getNodeID().equals(edgeEntryTreeItem.getValue().getEndNode()))
                                    return true;
                            }
                        }
                        return false;
                    case "Short Name":
                        for(NodeEntry nodeEntry : nodeEntryObservableList) {
                            if(nodeEntry.getShortName().contains(searchField.getText())) {
                                if(nodeEntry.getNodeID().equals(edgeEntryTreeItem.getValue().getStartNode()) || nodeEntry.getNodeID().equals(edgeEntryTreeItem.getValue().getEndNode()))
                                    return true;
                            }
                        }
                        return false;
                    default:
                        return true;
                }
            }
            return true;
        });
        for (Node node : mapPanel.getCanvas().getChildren()) {
            if (node instanceof DrawableEdge) {
                boolean isFavorite = true;
                if(favoriteOnly) isFavorite = isInFavorites(((DrawableEdge) node).getStartNode().getNodeID()) || isInFavorites(((DrawableEdge) node).getEndNode().getNodeID());
                switch (searchComboBox.getValue()) {
                    case "Node ID":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && (((DrawableEdge) node).getStartNode().getNodeID().contains(searchField.getText()) || ((DrawableEdge) node).getEndNode().getNodeID().contains(searchField.getText())));
                        break;
                    case "Floor":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && (((DrawableEdge) node).getStartNode().getFloor().equals(searchField.getText()) || ((DrawableEdge) node).getEndNode().getFloor().equals(searchField.getText())));
                        break;
                    case "Building":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && (((DrawableEdge) node).getStartNode().getBuilding().contains(searchField.getText()) || ((DrawableEdge) node).getEndNode().getBuilding().contains(searchField.getText())));
                        break;
                    case "Node Type":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && (((DrawableEdge) node).getStartNode().getNodeType().contains(searchField.getText()) || ((DrawableEdge) node).getEndNode().getNodeType().contains(searchField.getText())));
                        break;
                    case "Long Name":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && (((DrawableEdge) node).getStartNode().getLongName().contains(searchField.getText()) || ((DrawableEdge) node).getEndNode().getLongName().contains(searchField.getText())));
                        break;
                    case "Short Name":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && (((DrawableEdge) node).getStartNode().getShortName().contains(searchField.getText()) || ((DrawableEdge) node).getEndNode().getShortName().contains(searchField.getText())));
                        break;
                    case "Edge ID":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && node.getId().contains(searchField.getText()));
                        break;
                    case "Start Node":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && ((DrawableEdge) node).getStartNode().getNodeID().contains(searchField.getText()));
                        break;
                    case "End Node":
                        ((DrawableEdge) node).setShouldDisplay(isFavorite && ((DrawableEdge) node).getEndNode().getNodeID().contains(searchField.getText()));
                        break;
                    default:
                        ((DrawableEdge) node).setShouldDisplay(true);
                        break;
                }
            }
        }

        if (searchField.getText().length() == 0) {
            for (Node node : mapPanel.getCanvas().getChildren()) {
                if (node instanceof DrawableNode && !favoriteOnly) ((DrawableNode) node).setShouldDisplay(true); // added here and below - KD
                if (node instanceof DrawableEdge && !favoriteOnly) ((DrawableEdge) node).setShouldDisplay(true);
            }
        }

    }

    /**
     * Saves to a file based on the tab open
     *
     * @author KD ahf LM
     */
    public void handleSave() {
        if (nodesTab.isSelected()) {
            //FIXME: NULL ERROR CHECK.
            final String fileName = "Untitled";

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save CSV File");
            fileChooser.setInitialFileName(fileName);

            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);

            Stage FileStage = new Stage();
            File file = fileChooser.showSaveDialog(FileStage);

            if (file != null) {
                //FIXME: DO BETTER!!!
                final List<String[]> data = new LinkedList<>();
                Collections.addAll(data, "nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName".split(","));

                data.addAll(nodeEntryObservableList.stream().map(node -> new String[]{
                        node.getNodeID(),
                        node.getXCoordinate(),
                        node.getYCoordinate(),
                        node.getFloor(),
                        node.getBuilding(),
                        node.getNodeType(),
                        node.getLongName(),
                        node.getShortName()
                }).collect(Collectors.toList()));

                try {
                    CSVManager.writeToFile(file, data);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        } else if (edgesTab.isSelected()) {
            final String fileName = "Untitled";

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save CSV File");
            fileChooser.setInitialFileName(fileName);

            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);

            Stage FileStage = new Stage();
            File file = fileChooser.showSaveDialog(FileStage);

            if (file != null) {
                //FIXME: DO BETTER!!!
                final List<String[]> data = new LinkedList<>();
                Collections.addAll(data, "edgeID,startNode,endNode".split(","));

                data.addAll(edgeEntryObservableList.stream().map(edge -> new String[]{
                        edge.getEdgeID(),
                        edge.getStartNode(),
                        edge.getEndNode()
                }).collect(Collectors.toList()));

                try {
                    CSVManager.writeToFile(file, data);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    /**
     * Loads from a file based on the tab open
     *
     * @author KD ahf LM
     */
    public void handleLoad() throws SQLException, IOException {
        if (nodesTab.isSelected()) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("Choose CSV File");
            Stage FileStage = new Stage();
            File file = fileChooser.showOpenDialog(FileStage);
            final String fileName = String.valueOf(file);


            List<String[]> nodeData;

            //FIXME: METHODIZE THISS!!!!
            try {
                nodeData = (fileName == null || fileName.trim().isEmpty()) ? CSVManager.load("MapfAllNodes.csv") : CSVManager.load(new File(fileName));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            if (nodeData.get(0).length != 8) {
                FXMLLoader dialogLoader = new FXMLLoader();
                dialogLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/editorBadCSVView.fxml")); // load in Edit Dialog - KD
                Stage dialogStage = new Stage();
                Parent root = dialogLoader.load();
                dialogStage.initModality(Modality.WINDOW_MODAL); // make window a pop up - KD
                dialogStage.initOwner(newButton.getScene().getWindow());
                dialogStage.setScene(new Scene(root)); // set scene - KD
                dialogStage.showAndWait();
                return; //TODO This is Keith's bad attempt to make sure the user doesn't try to load in an edge CSV
            }

            nodeEntryObservableList.clear();
            mapPanel.clearMap();

            if (nodeData != null) {
                if (!nodeData.isEmpty() && nodeData.get(0).length == 8) {
                    nodeEntryObservableList.addAll(nodeData.stream().map(line -> new NodeEntry(line[0], line[1], line[2], line[3], line[4], line[5], line[6], line[7])).sorted(Comparator.comparing(NodeEntry::getNodeID)).collect(Collectors.toList()));

                    nodeEntryObservableList.forEach(n -> mapPanel.draw(getEditableNode(n)));

                    DatabaseAPI.getDatabaseAPI().dropNodesTable();
                    DatabaseAPI.getDatabaseAPI().createNodesTable();
                    DatabaseAPI.getDatabaseAPI().populateNodes(nodeData); //NOTE: now can specify CSV arguments
                }
            }
            drawEdgeNodeOnFloor(); // TODO this is interesting, if two incompatible CSVs are loaded what will happen?
        } else if (edgesTab.isSelected()) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("Choose CSV File");
            Stage FileStage = new Stage();
            File file = fileChooser.showOpenDialog(FileStage);
            final String fileName = String.valueOf(file);


            List<String[]> edgeData;

            try {
                edgeData = (fileName == null || fileName.trim().isEmpty()) ? CSVManager.load("MapfAllEdges.csv") : CSVManager.load(new File(fileName));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if (edgeData.get(0).length != 3) {
                FXMLLoader dialogLoader = new FXMLLoader();
                dialogLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/editorBadCSVView.fxml")); // load in Edit Dialog - KD
                Stage dialogStage = new Stage();
                Parent root = dialogLoader.load();
                dialogStage.initModality(Modality.WINDOW_MODAL); // make window a pop up - KD
                dialogStage.initOwner(newButton.getScene().getWindow());
                dialogStage.setScene(new Scene(root)); // set scene - KD
                dialogStage.showAndWait();
                return; //TODO This is Keith's bad attempt to make sure the user doesn't try to load in an node CSV
            }

            edgeEntryObservableList.clear();

            if (edgeData != null) {
                if (!edgeData.isEmpty() && edgeData.get(0).length == 3) {
                    edgeEntryObservableList.addAll(edgeData.stream().map(line -> new EdgeEntry(line[0], line[1], line[2])).sorted(Comparator.comparing(EdgeEntry::getEdgeID)).collect(Collectors.toList()));

                    DatabaseAPI.getDatabaseAPI().dropEdgesTable();
                    DatabaseAPI.getDatabaseAPI().createEdgesTable();
                    DatabaseAPI.getDatabaseAPI().populateEdges(edgeData); //NOTE: now can specify CSV arguments
                }
            }
            drawEdgeNodeOnFloor(); // TODO this is interesting, if two incompatible CSVs are loaded what will happen?
        }
    } //FIXME issues with loading CSVs, specifically how they are loaded in.  Eventually check to make sure loading proper CSV, or do two different buttons - KD

    public void handleReset() throws SQLException {
        mapPanel.clearMap();
        nodeEntryObservableList.clear();
        edgeEntryObservableList.clear();
        searchField.setText("");

        List<String[]> nodeData;
        List<String[]> edgeData;

        try {
            nodeData = (CSVManager.load("MapfAllNodes.csv"));
            edgeData = (CSVManager.load("MapfAllEdges.csv"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (nodeData != null && edgeData != null) {
            if (!nodeData.isEmpty() && nodeData.get(0).length == 8 && !edgeData.isEmpty() && edgeData.get(0).length == 3) {
                List<NodeEntry> list = new ArrayList<>();
                for (String[] nodeDatum : nodeData) {
                    NodeEntry nodeEntry = new NodeEntry(nodeDatum[0], nodeDatum[1], nodeDatum[2], nodeDatum[3], nodeDatum[4], nodeDatum[5], nodeDatum[6], nodeDatum[7]);
                    list.add(nodeEntry);
                }
                list.sort(Comparator.comparing(NodeEntry::getNodeID));
                nodeEntryObservableList.addAll(list);
                edgeEntryObservableList.addAll(edgeData.stream().map(line -> new EdgeEntry(line[0], line[1], line[2])).sorted(Comparator.comparing(EdgeEntry::getEdgeID)).collect(Collectors.toList()));

                DatabaseAPI.getDatabaseAPI().dropNodesTable();
                DatabaseAPI.getDatabaseAPI().createNodesTable();
                DatabaseAPI.getDatabaseAPI().populateNodes(nodeData); //NOTE: now can specify CSV arguments

                DatabaseAPI.getDatabaseAPI().dropEdgesTable();
                DatabaseAPI.getDatabaseAPI().createEdgesTable();
                DatabaseAPI.getDatabaseAPI().populateEdges(edgeData); //NOTE: now can specify CSV arguments

                drawEdgeNodeOnFloor(); //FIXME do better with queries
            }
        }

    }

    private DrawableNode getEditableNode(NodeEntry nodeEntry) {
        final DrawableNode drawableNode = nodeEntry.getDrawable();

        Tooltip tt = new JFXTooltip();

        tt.setText(nodeEntry.getShortName() +
                "\nBuilding: " + nodeEntry.getBuilding() +
                "\nNode Type: " + nodeEntry.getNodeType());

        tt.setStyle("-fx-font: normal bold 15 Langdon; "
                + "-fx-background-color: #03256C; "
                + "-fx-text-fill: white;");

        drawableNode.setOnMouseEntered(e -> {
            if (!drawableNode.equals(selectedCircle)) drawableNode.setFill(UIConstants.NODE_COLOR_HIGHLIGHT);
            Tooltip.install(drawableNode, tt);
        });
        drawableNode.setOnMouseExited(e -> {
            if (!drawableNode.equals(selectedCircle)) drawableNode.setFill(UIConstants.NODE_COLOR);
            Tooltip.uninstall(drawableNode, tt);
        });

        final List<DrawableEdge> startEdges = new ArrayList<>();
        final List<DrawableEdge> endEdges = new ArrayList<>();

        if (clickToMakeEdge) {
            drawableNode.setOnMouseClicked(e -> handleCreateEdgeFromNodes(drawableNode));
        }
        else {
            drawableNode.setOnMouseClicked(e -> {
                if(e.isShiftDown())
                    handleCreateEdgeFromNodes(drawableNode);
            });

            drawableNode.setOnMousePressed(e -> handleNodeDragMousePressed(drawableNode, nodeEntry, startEdges, endEdges));

            drawableNode.setOnMouseDragged(e -> {
                handleNodeDragMouseDragged(drawableNode, e, startEdges, endEdges);
            });

            drawableNode.setOnMouseReleased(e -> {
                if(!e.isDragDetect())
                {
                    firstCircle = null;
                    secondCircle = null;
                }
                handleNodeDragMouseReleased(drawableNode);
            });
        }

        return drawableNode;
    }

    /**
     * Used to manage MousePressed when we are dragging.
     */
    private void handleNodeDragMousePressed(DrawableNode drawableNode, NodeEntry nodeEntry, List<DrawableEdge> startEdges, List<DrawableEdge> endEdges)
    {
        if (selectedCircle != null)
            selectedCircle.setFill(UIConstants.NODE_COLOR);
        if (selectedLine != null)
            selectedLine.setStroke(UIConstants.LINE_COLOR);

        selectedCircle = drawableNode;
        drawableNode.setFill(UIConstants.NODE_COLOR_SELECTED);


        tabPane.getSelectionModel().select(nodesTab);
        nodeTreeTable.getSelectionModel().clearAndSelect(findNode(drawableNode.getId()));
        nodeTreeTable.requestFocus();
        nodeTreeTable.scrollTo(findNode(drawableNode.getId()));

        startEdges.clear();
        endEdges.clear();

        for (EdgeEntry edgeEntry : edgeEntryObservableList) {

            if (edgeEntry.getStartNode().equals(nodeEntry.getNodeID())) {
                startEdges.add(mapPanel.getNode(edgeEntry.getEdgeID()));
            }

            if (edgeEntry.getEndNode().equals(nodeEntry.getNodeID())) {
                endEdges.add(mapPanel.getNode(edgeEntry.getEdgeID()));
            }
        }
    }

    private void handleNodeDragMouseDragged(DrawableNode drawableNode, MouseEvent e, List<DrawableEdge> startEdges, List<DrawableEdge> endEdges)
    {
        final int x = (int) (e.getX() * mapPanel.getZoomLevel().get());
        final int y = (int) (e.getY() * mapPanel.getZoomLevel().get());

        drawableNode.xCoordinateProperty().set(x);
        drawableNode.yCoordinateProperty().set(y);

        //FIXME: DO BETTER, DO BINDINGS

        for (DrawableEdge edge : startEdges) {
            edge.getMapStartX().set(x);
            edge.getMapStartY().set(y);
        }

        for (DrawableEdge edge : endEdges) {
            edge.getMapEndX().set(x);
            edge.getMapEndY().set(y);
        }
    }

    private void handleNodeDragMouseReleased(DrawableNode drawableNode)
    {
        try {
            DatabaseAPI.getDatabaseAPI().editNode(drawableNode.getId(), "" + drawableNode.xCoordinateProperty().get(), "xcoord");
            DatabaseAPI.getDatabaseAPI().editNode(drawableNode.getId(), "" + drawableNode.yCoordinateProperty().get(), "ycoord");

            ///FIXME: BIND PROPERTIES TOGETHER

            for (NodeEntry entry : nodeEntryObservableList) {
                if (entry.getNodeID().equals(drawableNode.getId())) {
                    entry.setXCoordinate("" + drawableNode.xCoordinateProperty().get());
                    entry.setYCoordinate("" + drawableNode.yCoordinateProperty().get());
                    break;
                }
            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void updateNode(String id, int xCoordinate, int yCoordinate)
    {
        try {
            DatabaseAPI.getDatabaseAPI().editNode(id, "" + xCoordinate, "xcoord");
            DatabaseAPI.getDatabaseAPI().editNode(id, "" + yCoordinate, "ycoord");

            ///FIXME: BIND PROPERTIES TOGETHER

            for (NodeEntry entry : nodeEntryObservableList) {
                if (entry.getNodeID().equals(id)) {
                    entry.setXCoordinate("" + xCoordinate);
                    entry.setYCoordinate("" + yCoordinate);
                    break;
                }
            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }

        final List<DrawableEdge> startEdges = new ArrayList<>();
        final List<DrawableEdge> endEdges = new ArrayList<>();

        for(Node node : mapPanel.getCanvas().getChildren()) {
            if (node instanceof DrawableNode) {
                if (node.getId().equals(id)) {

                    for (EdgeEntry edgeEntry : edgeEntryObservableList) {
                        if (edgeEntry.getStartNode().equals(id)) {
                            startEdges.add(mapPanel.getNode(edgeEntry.getEdgeID()));
                        }
                        if (edgeEntry.getEndNode().equals(id)) {
                            endEdges.add(mapPanel.getNode(edgeEntry.getEdgeID()));
                        }
                    }

                    ((DrawableNode) node).xCoordinateProperty().set(xCoordinate);
                    ((DrawableNode) node).yCoordinateProperty().set(yCoordinate);

                    //FIXME: DO BETTER, DO BINDINGS

                    for (DrawableEdge edge : startEdges) {
                        edge.getMapStartX().set(xCoordinate);
                        edge.getMapStartY().set(yCoordinate);
                    }

                    for (DrawableEdge edge : endEdges) {
                        edge.getMapEndX().set(xCoordinate);
                        edge.getMapEndY().set(yCoordinate);
                    }

                }

            }
        }
    }

    private void handleCreateEdgeFromNodes(DrawableNode drawableNode)
    {
        if (selectedCircle != null)
            selectedCircle.setFill(UIConstants.NODE_COLOR);
        if (selectedLine != null)
            selectedLine.setStroke(UIConstants.LINE_COLOR);

        selectedCircle = drawableNode;
        drawableNode.setFill(UIConstants.NODE_COLOR_SELECTED);
        if (firstCircle == null) {
            firstCircle = drawableNode;
            tabPane.getSelectionModel().select(nodesTab);
            nodeTreeTable.getSelectionModel().clearAndSelect(findNode(drawableNode.getId()));
            nodeTreeTable.requestFocus();
            nodeTreeTable.scrollTo(findNode(drawableNode.getId()));
        } else {
            secondCircle = drawableNode;
            // Second node selected, create edge
            try {
                tabPane.getSelectionModel().select(edgesTab);
                edgeTreeTable.getSelectionModel().clearAndSelect(findEdge(firstCircle.getId() + "_" + secondCircle.getId()));
                edgeTreeTable.requestFocus();
                edgeTreeTable.scrollTo(findEdge(firstCircle.getId() + "_" + secondCircle.getId()));
                createNewEdgeFromNodes();
                handleSearch();
            } catch (IOException | SQLException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private DrawableEdge getEditableEdge(EdgeEntry edge, NodeEntry startNode, NodeEntry endNode) {
        final DrawableEdge drawableEdge = new DrawableEdge(
                Integer.parseInt(startNode.getXCoordinate()),
                Integer.parseInt(startNode.getYCoordinate()),
                Integer.parseInt(endNode.getXCoordinate()),
                Integer.parseInt(endNode.getYCoordinate()),
                edge.getEdgeID(),
                startNode.getFloor(),
                endNode.getFloor(),
                startNode,
                endNode
        );

        return drawableEdge;
    }

    /**
     * Find the index of a given node with nodeID in nodeList
     *
     * @author ZheCheng
     */
    private int findNode(String nodeID) {

        int index = 0;
        for (TreeItem<NodeEntry> nodeEntryTreeItem : nodeTreeTable.getRoot().getChildren()) {
            if (nodeEntryTreeItem.getValue().getNodeID().equals(nodeID)) {
                return index;
            }
            index++;
        }
        return -1; // FIXME handle this error

        // KD - fixed issue with search filter and node finding.  Now it iterates through the things visible in the tree table, NOT the list since the indices will no longer align.  Same done for edges


    }

    /**
     * Find the index of a given node with nodeID in nodeList
     *
     * @author ZheCheng
     */
    private int findEdge(String nodeID) {
        int index = 0;
        for (TreeItem<EdgeEntry> e : edgeTreeTable.getRoot().getChildren()) {
            if (e.getValue().getEdgeID().equals(nodeID)) {
                break;
            }
            index++;
        }
        return index;
    }

    /**
     * Used to update a node entry in the UI and database //FIXME: Add delete if we already had the node
     *
     * @param nodeEntry
     */
    private void updateNodeEntry(NodeEntry nodeEntry) throws SQLException {

        if (!checkNodeEntryNotEmpty(nodeEntry))
            return;

        String nodeID = nodeEntry.getNodeID();
        int xCoord = Integer.parseInt(nodeEntry.getXCoordinate());
        int yCoord = Integer.parseInt(nodeEntry.getYCoordinate());
        String nodeFloor = nodeEntry.getFloor();
        String nodeBuilding = nodeEntry.getBuilding();
        String nodeType = nodeEntry.getNodeType();
        String longName = nodeEntry.getLongName();
        String shortName = nodeEntry.getShortName();

        DatabaseAPI.getDatabaseAPI().addNode(nodeID, Integer.toString(xCoord), Integer.toString(yCoord), nodeFloor,
                nodeBuilding, nodeType, longName, shortName);

        mapPanel.draw(getEditableNode(nodeEntry));

        nodeTreeTable.requestFocus();
        nodeTreeTable.getSelectionModel().clearAndSelect(findNode(nodeID));
        nodeTreeTable.scrollTo(findNode(nodeID));
        handleSelectNode();
    }

    private void updateEdgeEntry(EdgeEntry edgeEntry) throws SQLException {

        if (edgeEntry.getEdgeID().isEmpty() || edgeEntry.getStartNode().isEmpty() || edgeEntry.getEndNode().isEmpty())
            return; //FIXME: DO BETTER ERROR CHECKING, CHECK THAT WE ARE GETTING INTS

        edgeEntryObservableList.add(edgeEntry); // add the new node to the Observable list (which is linked to table and updates) - KD
        DatabaseAPI.getDatabaseAPI().addEdge(edgeEntry.getEdgeID(), edgeEntry.getStartNode(), edgeEntry.getEndNode());

        drawEdgeNodeOnFloor();
        // Focus on selected edge both on table and on map
        edgeTreeTable.requestFocus();
        edgeTreeTable.getSelectionModel().clearAndSelect(findEdge(edgeEntry.getEdgeID()));
        edgeTreeTable.scrollTo(findEdge(edgeEntry.getEdgeID()));
        handleSelectEdge();
    }

    /**
     * Select node based on selection in Table, focus on the node
     *
     * @author ZheCheng
     */
    public void handleSelectNode() {
        if (nodeTreeTable.getSelectionModel().getSelectedIndex() < 0) {
            // FIXME Error Handling
            return;
        }
        // FIXME: ADD TRY_CATCH
        NodeEntry node = nodeTreeTable.getSelectionModel().getSelectedItem().getValue(); //Changed for handling when the nodeTreeTable is being searched

        if (node == null) {
            //FIXME Null Warning
            return;
        }

        // Check if need to switch map
        if (node.getFloor().equals(mapPanel.getFloor().get())) {
            //mapPanel.drawNodeOnFloor();
        } else {
            //floor = node.getFloor();
            mapPanel.switchMap(node.getFloor());
        }

        // Clear highlight on nodes
        if (selectedCircle != null)
            selectedCircle.setFill(UIConstants.NODE_COLOR);
        // Clear highlight on edges
        if (selectedLine != null)
            selectedLine.setStroke(UIConstants.LINE_COLOR);


        Circle c = (Circle) mapPanel.getCanvas().lookup("#" + node.getNodeID());
        if (c == null) {
            //FIXME Null Warning
            return;
        }
        selectedCircle = c;
        c.setFill(UIConstants.NODE_COLOR_SELECTED);
        mapPanel.centerNode(c);
    }

    /**
     * Used to highlight selected edges
     *
     * @author Alex Friedman & ZheCheng
     */
    public void handleSelectEdge() {
        // Check for a valid index (-1 = no selection)
        if (edgeTreeTable.getSelectionModel().getSelectedIndex() < 0) {
            // FIXME Error Handling
            return;
        }
        // Get selected Edge
        //EdgeEntry edge = edgeEntryObservableList.get(edgeTreeTable.getSelectionModel().getSelectedIndex());
        EdgeEntry edge = edgeTreeTable.getSelectionModel().getSelectedItem().getValue();

        if (edge == null) {
            //FIXME Null Warning
            return;
        }

        // Try to get startNode and endNode from database
        NodeEntry startNode = null;
        NodeEntry endNode = null;
        try {
            startNode = DatabaseAPI.getDatabaseAPI().getNode(edge.getStartNode());
            endNode = DatabaseAPI.getDatabaseAPI().getNode(edge.getEndNode());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // startNode or endNode not stored in database
        if (startNode == null || endNode == null) {
            System.out.println("Edge with no actual Node");
            return;
        }

        // Check if need to switch map
        if (startNode.getFloor().equals(mapPanel.getFloor().get()) || endNode.getFloor().equals(mapPanel.getFloor().get())) {
            //drawEdgeNodeOnFloor();

        } else {
            mapPanel.switchMap(startNode.getFloor());
        }

        // Clear highlight on previously selected line or node
        if (selectedLine != null)
            selectedLine.setStroke(UIConstants.LINE_COLOR);
        if (selectedCircle != null)
            selectedCircle.setFill(UIConstants.NODE_COLOR);

        // Get the line with edgeID
        Line l = (Line) mapPanel.getCanvas().lookup("#" + edge.getEdgeID());
        if (l == null) {
            //FIXME Null Warning
            return;
        }
        selectedLine = l;
        l.setStroke(Color.GREEN);
        mapPanel.centerNode(l);
    }

    /**
     * Helper for adding node that makes sure the node doesn't have empty fields (like when the edit dialog is opened but then closed externally)
     *
     * @param nodeEntry the node entry
     * @return true if the node has no empty fields
     * @author KD
     */
    public boolean checkNodeEntryNotEmpty(NodeEntry nodeEntry) {
        return !nodeEntry.getNodeID().isEmpty() && !nodeEntry.getXCoordinate().isEmpty() && !nodeEntry.getYCoordinate().isEmpty() &&
                !nodeEntry.getFloor().isEmpty() && !nodeEntry.getBuilding().isEmpty() && !nodeEntry.getNodeType().isEmpty() &&
                !nodeEntry.getLongName().isEmpty() && !nodeEntry.getShortName().isEmpty();
    }

    /**
     * Helper for adding node that makes sure the node doesn't have empty fields (like when the edit dialog is opened but then closed externally)
     *
     * @param edgeEntry the node entry
     * @return true if the node has no empty fields
     * @author KD
     */
    public boolean checkEdgeEntryNotEmpty(EdgeEntry edgeEntry) {
        return !edgeEntry.getEdgeID().isEmpty() && !edgeEntry.getStartNode().isEmpty() && !edgeEntry.getEndNode().isEmpty();
    }

    /**
     * Opens the edit dialog to edit a particular node
     *
     * @param editedNode the node being edited
     * @author KD
     */
    private void openEditNodeDialog(NodeEntry editedNode) throws IOException {

        FXMLLoader dialogLoader = new FXMLLoader();
        dialogLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/EditMapNodeDialogView.fxml")); // load in Edit Dialog - KD
        Stage dialogStage = new Stage();
        Parent root = dialogLoader.load();
        EditMapNodeDialogViewController dialogController = dialogLoader.getController(); // get edit dialog's controller - KD

        dialogController.setDialogStage(dialogStage); // set the stage attribute - KD
        dialogController.setTheNode(editedNode); // inject the node attribute so that specific instance is the one edited - KD

        dialogController.setNodeList(nodeEntryObservableList);
        dialogController.setCurrentIDIfEditing(editedNode.getNodeID());
        dialogStage.setTitle("Edit Node");
        dialogStage.initModality(Modality.WINDOW_MODAL); // make window a pop up - KD
        dialogStage.initOwner(newButton.getScene().getWindow());
        dialogStage.setScene(new Scene(root)); // set scene - KD

        dialogStage.showAndWait(); // open pop up - KD
    }

    /**
     * Opens edit dialogue to edit edge
     * Written with code from KD
     *
     * @param editedEdge is the edge being edited
     * @author Karen Hou
     */
    private ArrayList<String> openEditEdgeDialog(EdgeEntry editedEdge) throws IOException {
        FXMLLoader editDialogueLoader = new FXMLLoader();
        editDialogueLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/EditMapEdgeDialogueView.fxml"));
        Stage dialogueStage = new Stage();
        Parent root = editDialogueLoader.load();
        EditMapEdgeDialogueViewController editDialogueController = editDialogueLoader.getController();
        editDialogueController.setDialogueStage(dialogueStage);
        editDialogueController.setEdge(editedEdge);
        editDialogueController.setEdgeList(edgeEntryObservableList);
        editDialogueController.setCurrentIDIfEditing(editedEdge.getEdgeID());
        dialogueStage.setTitle("Edit Edge");
        dialogueStage.initModality(Modality.WINDOW_MODAL);
        dialogueStage.initOwner(newButton.getScene().getWindow());
        dialogueStage.setScene(new Scene(root));
        dialogueStage.showAndWait();

        if(!editDialogueController.okClicked)
            return null;

        ArrayList<String> returnList = new ArrayList<>();
        returnList.add(editedEdge.getEdgeID());
        returnList.add(editedEdge.getStartNode());
        returnList.add(editedEdge.getEndNode());
        return returnList;
    }

    /**
     * Clear the canvas and draw edges and nodes that are on current floor
     *
     * @author ZheCheng
     */
    private void drawEdgeNodeOnFloor() {
        // Clear canvas
        mapPanel.clearMap();
        mapPanel.draw(rectSelector);
        mapPanel.draw(alignLeft);
        mapPanel.draw(alignHorizontalMiddle);
        mapPanel.draw(alignRight);
        mapPanel.draw(alignTop);
        mapPanel.draw(alignVerticalMiddle);
        mapPanel.draw(alignBottom);

        // Reset selections
        selectedLine = null;
        firstCircle = null;
        secondCircle = null;
        nodeList = new ArrayList<>();

        // Draw all edges
        for (EdgeEntry e : edgeEntryObservableList) {
            NodeEntry startNode = null;
            NodeEntry endNode = null;
            try {
                startNode = DatabaseAPI.getDatabaseAPI().getNode(e.getStartNode());
                endNode = DatabaseAPI.getDatabaseAPI().getNode(e.getEndNode());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (startNode == null || endNode == null) {
                System.out.println("Edge with no actual Node");
                //return;
            } else {
                boolean duplicate = false;
                for (NodeEntry n : nodeList) {
                    if (n.getNodeID().equals(startNode.getNodeID())) {
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate) {
                    nodeList.add(startNode);
                }
                duplicate = false;
                for (NodeEntry n : nodeList) {
                    if (n.getNodeID().equals(endNode.getNodeID())) {
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate) {
                    nodeList.add(endNode);
                }


                DrawableEdge edge = getEditableEdge(e, startNode, endNode);
                Line l = mapPanel.draw(edge);

                Tooltip tt = new JFXTooltip();
                tt.textProperty().set("Edge: " + e.getEdgeID());

                tt.setStyle("-fx-font: normal bold 15 Langdon; "
                        + "-fx-background-color: #03256C; "
                        + "-fx-text-fill: white;");

                l.setOnMouseEntered(event -> {
                    if (!l.equals(selectedLine)) l.setStroke(UIConstants.NODE_COLOR_HIGHLIGHT);
                    Tooltip.install(edge, tt);
                });
                l.setOnMouseExited(event -> {
                    if (!l.equals(selectedLine)) l.setStroke(UIConstants.LINE_COLOR);
                    Tooltip.uninstall(edge, tt);
                });
                l.setOnMouseClicked(event -> {
                    if (selectedLine != null)
                        selectedLine.setStroke(UIConstants.LINE_COLOR);
                    if (selectedCircle != null)
                        selectedCircle.setFill(UIConstants.NODE_COLOR);
                    selectedLine = l;
                    // Automagically change to edges tab
                    tabPane.getSelectionModel().select(edgesTab);
                    l.setStroke(Color.GREEN);
                    edgeTreeTable.getSelectionModel().clearAndSelect(findEdge(e.getEdgeID()));
                    edgeTreeTable.requestFocus();
                    edgeTreeTable.scrollTo(findEdge(e.getEdgeID()));
                });
            }
        }

        // Draw all corresponding nodes
        for (NodeEntry n : nodeEntryObservableList) {
            mapPanel.draw(getEditableNode(n));
            // drawCircle(Double.parseDouble(n.getXcoord()) / mapPanel.getZoomLevel(), Double.parseDouble(n.getYcoord()) / mapPanel.getZoomLevel(), n.getNodeID());
        }
        handleSearch();
    }

    /**
     * Open window for user to create new edge with two node selected
     *
     * @throws IOException
     * @throws SQLException
     * @author ZheCheng
     */
    private void createNewEdgeFromNodes() throws IOException, SQLException {
        EdgeEntry newEdge = new EdgeEntry(firstCircle.getId() + "_" + secondCircle.getId(), firstCircle.getId(), secondCircle.getId());
        List<String> values = openEditEdgeDialog(newEdge);

        selectedCircle.setFill(UIConstants.NODE_COLOR);

        firstCircle = null;
        secondCircle = null;

        if (values == null || newEdge.edgeIDProperty().getValue().isEmpty() || newEdge.startNodeProperty().getValue().isEmpty() ||
                newEdge.endNodeProperty().getValue().isEmpty())
            return; //FIXME: DO BETTER ERROR CHECKING
        updateEdgeEntry(newEdge);
    } //FIXME implement if we decide we should, a discussion to be had - KD

    /**
     * Helper for when a node's ID is changed, needs to update all Edge entries that are associated with it in both the observable list and the database
     *
     * @param previousID the previous ID
     * @param newID      the new ID
     * @author KD
     */
    public void reassignAssociatedEdges(String previousID, String newID) throws SQLException {
        for (EdgeEntry edgeEntry : edgeEntryObservableList) {

            if (edgeEntry.getStartNode().equals(previousID)) {
                edgeEntry.setStartNode(newID);
                DatabaseAPI.getDatabaseAPI().deleteEdge(previousID + "_" + edgeEntry.getEndNode());
                DatabaseAPI.getDatabaseAPI().addEdge(edgeEntry.getEdgeID(), edgeEntry.getStartNode(), edgeEntry.getEndNode());
            }

            if (edgeEntry.getEndNode().equals(previousID)) {
                edgeEntry.setEndNode(newID);
                DatabaseAPI.getDatabaseAPI().deleteEdge(edgeEntry.getStartNode() + "_" + previousID);
                DatabaseAPI.getDatabaseAPI().addEdge(edgeEntry.getEdgeID(), edgeEntry.getStartNode(), edgeEntry.getEndNode());
            }
        }
    }

    /**
     * Helper that deletes all edges a node is connected to when a node is deleted
     *
     * @param nodeID
     * @author KD
     */
    public void deleteAssociatedEdges(String nodeID) throws SQLException {

        List<Integer> indicesToRemove = new ArrayList<>();

        for (int index = 0; index < edgeEntryObservableList.size(); index++) {
            if (edgeEntryObservableList.get(index).getStartNode().equals(nodeID) || edgeEntryObservableList.get(index).getEndNode().equals(nodeID)) { // if an edge is connected to said node
                DatabaseAPI.getDatabaseAPI().deleteEdge(edgeEntryObservableList.get(index).getEdgeID());
                mapPanel.unDraw(edgeEntryObservableList.get(index).getEdgeID());
                indicesToRemove.add(index); // This is needed, since removing the elements while going through this for loop can cause the loop to skip entries (as the indices of elements are dynamically updated) - KD
            }
        }
        for (Integer i : indicesToRemove) {
            edgeEntryObservableList.remove(edgeEntryObservableList.get(i));
            for (int index = 0; index < indicesToRemove.size(); index++) {
                indicesToRemove.set(index, indicesToRemove.get(index) - 1); // to accomodate the changing indices of edgeEntryObservableList
            }
        }
    }

    public void handleHome() throws IOException {
        SceneContext.getSceneContext().loadDefault();
    }

    public void handleTabChange(Event event) {
        /*
        Tab theTab = (Tab) event.getSource();
        if (theTab == nodesTab) {
            ObservableList<String> searchables = FXCollections.observableArrayList();
            searchables.add("Node ID");
            searchables.add("Floor");
            searchables.add("Building");
            searchables.add("Node Type");
            searchables.add("Long Name");
            searchables.add("Short Name");
            searchComboBox.setItems(searchables);
            searchComboBox.setValue("Node ID");
        } else if (theTab == edgesTab) {
            ObservableList<String> searchables = FXCollections.observableArrayList();
            searchables.add("Edge ID");
            searchables.add("Start Node");
            searchables.add("End Node");
            searchComboBox.setItems(searchables);
            searchComboBox.setValue("Edge ID");
        }
         */
    }

    /**
     * Handles when the toggle for editor mode is switched between Click and Drag or Edge Creation
     * @author KD
     */
    public void handleToggle() {
        if (edgeCreationToggle.getText().equals("Drag and Drop")) {
            clickToMakeEdge = true;
            edgeCreationToggle.setText("Edge Creation");
        } else {
            clickToMakeEdge = false;
            edgeCreationToggle.setText("Drag and Drop");
        }
        drawEdgeNodeOnFloor();
        handleSearch();
    }

    /**
     * Adds selected node to the favorites list of current user
     * @throws SQLException
     * @author KD
     */
    public void handleFavorite() throws SQLException {
        if(selectedCircle.getFill().equals(Color.GREEN) && mapPanel.getNode(selectedCircle.getId()).shouldDisplay().get()) {
            NodeEntry favNode = nodeTreeTable.getSelectionModel().getSelectedItem().getValue();
            if(favoriteButton.getText().equals("Favorite")) {
                if (isInFavorites(favNode.getNodeID())) return; // dont want duplicate favorite - KD
                DatabaseAPI.getDatabaseAPI().addCollectionEntry(CurrentUser.getCurrentUser().getLoggedIn().getUsername(), favNode.getNodeID(), "favorite");
                favoriteList.add(favNode);
            } else if(favoriteButton.getText().equals("Unfavorite")) {
                DatabaseAPI.getDatabaseAPI().deleteUserNode(favNode.getNodeID(), CurrentUser.getCurrentUser().getLoggedIn().getUsername(), "favorite"); //?
                favoriteList.remove(favNode);
                handleSearch();
            }
        }
    }

    /**
     * Changes favorite only mode
     * @author KD
     */
    public void handleFavoriteToggle() {
        favoriteOnly = !favoriteOnly;
        if(favoriteButton.getText().equals("Favorite")) {
            favoriteButton.setText("Unfavorite");
        } else if(favoriteButton.getText().equals("Unfavorite")) {
            favoriteButton.setText("Favorite");
        }
        handleSearch();
    }

    /**
     * Helper that returns true if the given ID is a favorite
     * @param nodeID
     * @return
     * @author KD
     */
    public boolean isInFavorites(String nodeID) {
        boolean isFavorite = false;
        for(NodeEntry nodeEntry : favoriteList) {
            if(nodeEntry.getNodeID().equals(nodeID)) isFavorite = true;
        }
        return isFavorite;
    }
}

