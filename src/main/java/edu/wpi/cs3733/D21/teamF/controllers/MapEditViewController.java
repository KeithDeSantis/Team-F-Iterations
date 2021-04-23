package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.D21.teamF.database.ConnectionHandler;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.EdgeEntry;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.UIConstants;
import edu.wpi.cs3733.uicomponents.MapPanel;
import edu.wpi.cs3733.uicomponents.entities.DrawableEdge;
import edu.wpi.cs3733.uicomponents.entities.DrawableNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MapEditViewController {

    @FXML private JFXButton saveButton;
    @FXML private JFXButton loadButton;
    @FXML private JFXButton resetFromDB;
    @FXML private JFXButton newButton;
    @FXML private JFXButton editButton;
    @FXML private JFXButton deleteButton;
    @FXML private JFXComboBox<String> searchComboBox;
    @FXML private JFXTextField searchBar;
    @FXML private TabPane tabPane;
    @FXML private Tab nodesTab;
    @FXML private Tab edgesTab;
    @FXML private JFXTreeTableView nodeTreeTable;
    @FXML private JFXTreeTableView edgeTreeTable;
    @FXML private MapPanel mapPanel;

    private final ObservableList<EdgeEntry> edgeEntryObservableList = FXCollections.observableArrayList();
    private ObservableList<NodeEntry> nodeEntryObservableList = FXCollections.observableArrayList();
    private Circle selectedCircle = null;

    private Line selectedLine = null;
    private Circle firstCircle = null;
    private Circle secondCircle = null;

    List<NodeEntry> nodeList = new ArrayList<>();

    @FXML
    private void initialize(){
        // Set fonts for buttons
        Font buttonDefault = Font.loadFont("file:src/main/resources/fonts/Montserrat-SemiBold.ttf", 20);
        saveButton.setFont(buttonDefault);
        loadButton.setFont(buttonDefault);
        resetFromDB.setFont(buttonDefault);
        newButton.setFont(buttonDefault);
        editButton.setFont(buttonDefault);
        deleteButton.setFont(buttonDefault);

        // Node initialization
        List<NodeEntry> data = new ArrayList<>();
        try {
            data = DatabaseAPI.getDatabaseAPI().genNodeEntries(ConnectionHandler.getConnection());
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        data.stream().sorted(Comparator.comparing(NodeEntry::getNodeID)).collect(Collectors.toList()).forEach(node -> {
            mapPanel.draw(getEditableNode(node));

            nodeEntryObservableList.add(node);
        });

        int colWidth = 120;
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
                nodeEntry.setXcoord("" + (int)(event.getX() * mapPanel.getZoomLevel().get()));
                nodeEntry.setYcoord("" + (int)(event.getY() * mapPanel.getZoomLevel().get()));
                nodeEntry.setFloor(mapPanel.getFloor().get());
/*
                try {
                    openEditDialog(nodeEntry);
                    if(!checkNodeEntryNotEmpty(nodeEntry)) return;
                    nodeList.add(nodeEntry); // add the new node to the Observable list (which is linked to table and updates) - KD

                    mapPanel.draw(getEditableNode(nodeEntry));

                    updateNodeEntry(nodeEntry);
                } catch (IOException | SQLException ioException) {
                    ioException.printStackTrace();
                }
                */

            });
        });

        ObservableList<String> searchables = FXCollections.observableArrayList();
        searchables.add("Node ID");
        searchables.add("Floor");
        searchables.add("Building");
        searchables.add("Node Type");
        searchables.add("Long Name");
        searchables.add("Short Name");
        searchComboBox.setItems(searchables);
        searchComboBox.setValue("Node ID");

        // Edge initialization
        List <EdgeEntry> edgeData = new ArrayList<>();
        try {
            edgeData = DatabaseAPI.getDatabaseAPI().genEdgeEntries(ConnectionHandler.getConnection());
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        edgeData.stream().sorted(Comparator.comparing(EdgeEntry::getEdgeID)).collect(Collectors.toList()).forEach(e -> edgeEntryObservableList.add(e));


        // Set up cell factory for the edge ID table
        JFXTreeTableColumn<EdgeEntry, String> edgeIDColumn = new JFXTreeTableColumn<>("Edge ID");
        edgeIDColumn.setPrefWidth(250);
        edgeIDColumn.setCellValueFactory(param -> param.getValue().getValue().edgeIDProperty());

        final TreeItem<EdgeEntry> edgeRoot = new RecursiveTreeItem<>(edgeEntryObservableList, RecursiveTreeObject::getChildren);
        edgeTreeTable.getColumns().setAll(edgeIDColumn);
        edgeTreeTable.setRoot(edgeRoot);
        edgeTreeTable.setShowRoot(false);

        // Set up floor comboBox and draw the nodes and edges on current floor
        drawEdgeNodeOnFloor();

    }

    private DrawableNode getEditableNode(NodeEntry nodeEntry)
    {
        final DrawableNode drawableNode = nodeEntry.getDrawable();

        drawableNode.setOnMouseEntered(e->{if(!drawableNode.equals(selectedCircle)) drawableNode.setFill(UIConstants.NODE_COLOR_HIGHLIGHT);});
        drawableNode.setOnMouseExited(e->{if(!drawableNode.equals(selectedCircle)) drawableNode.setFill(UIConstants.NODE_COLOR);});

        drawableNode.setOnMouseClicked(e->{
            if(selectedCircle != null)
                selectedCircle.setFill(UIConstants.NODE_COLOR);
            selectedCircle = drawableNode;
            drawableNode.setFill(UIConstants.NODE_COLOR_SELECTED);
            nodeTreeTable.getSelectionModel().clearAndSelect(findNode(drawableNode.getId()));
            nodeTreeTable.requestFocus();
            nodeTreeTable.scrollTo(findNode(drawableNode.getId()));});

        return drawableNode;
    }

    /**
     * Find the index of a given node with nodeID in nodeList
     * @author ZheCheng
     */
    private int findNode(String nodeID){
        int index = 0;
        for(NodeEntry n: nodeEntryObservableList){
            if(n.getNodeID() == nodeID){
                break;
            }
            index++;
        }
        return index;
    }

    /**
     * Clear the canvas and draw edges and nodes that are on current floor
     * @author ZheCheng
     */
    private void drawEdgeNodeOnFloor() {
        // Clear canvas
        mapPanel.clearMap();

        // Reset selections
        selectedLine = null;
        firstCircle = null;
        secondCircle = null;
        nodeList = new ArrayList<>();

        // Draw all edges
        for(EdgeEntry e : edgeEntryObservableList){
            NodeEntry startNode = null;
            NodeEntry endNode = null;
            try {
                startNode = DatabaseAPI.getDatabaseAPI().getNode(ConnectionHandler.getConnection(), e.getStartNode());
                endNode = DatabaseAPI.getDatabaseAPI().getNode(ConnectionHandler.getConnection(), e.getEndNode());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if(startNode == null || endNode == null) {
                System.out.println("Edge with no actual Node");
                //return;
            }
            else
            {
                if(!nodeList.contains(startNode))
                    nodeList.add(startNode);
                if(!nodeList.contains(endNode))
                    nodeList.add(endNode);


                Line l = mapPanel.draw(getEditableEdge(e, startNode, endNode));

                l.setOnMouseEntered(event->{if(!l.equals(selectedLine))l.setStroke(UIConstants.NODE_COLOR_HIGHLIGHT);});
                l.setOnMouseExited(event->{if(!l.equals(selectedLine))l.setStroke(UIConstants.LINE_COLOR);});
                l.setOnMouseClicked(event->{
                    if(selectedLine != null)
                        selectedLine.setStroke(UIConstants.LINE_COLOR);
                    selectedLine = l;
                    l.setStroke(Color.GREEN);
                    edgeTreeTable.getSelectionModel().clearAndSelect(findEdge(e.getEdgeID()));
                    edgeTreeTable.requestFocus();
                    edgeTreeTable.scrollTo(findEdge(e.getEdgeID()));
                });
            }
        }

        // Draw all corresponding nodes
        for(NodeEntry n : nodeList){
            mapPanel.draw(getEditableNode(n));
            // drawCircle(Double.parseDouble(n.getXcoord()) / mapPanel.getZoomLevel(), Double.parseDouble(n.getYcoord()) / mapPanel.getZoomLevel(), n.getNodeID());
        }
    }

    /**
     * Find the index of a given node with nodeID in nodeList
     * @author ZheCheng
     */
    private int findEdge(String nodeID){
        int index = 0;
        for(EdgeEntry e: edgeEntryObservableList){
            if(e.getEdgeID() == nodeID){
                break;
            }
            index++;
        }
        return index;
    }

    private DrawableEdge getEditableEdge(EdgeEntry edge, NodeEntry startNode, NodeEntry endNode)
    {
        final DrawableEdge drawableEdge = new DrawableEdge(
                Integer.parseInt(startNode.getXcoord()),
                Integer.parseInt(startNode.getYcoord()),
                Integer.parseInt(endNode.getXcoord()),
                Integer.parseInt(endNode.getYcoord()),
                edge.getEdgeID(),
                startNode.getFloor(),
                endNode.getFloor()
        );

        return drawableEdge;
    }

    /**
     * Open window for user to create new edge with two node selected
     *
     * @throws IOException
     * @throws SQLException
     * @author ZheCheng
     */
    private void createNewEdgeFromNodes() throws IOException, SQLException{
        EdgeEntry newEdge = new EdgeEntry(firstCircle.getId()+"_"+secondCircle.getId(),firstCircle.getId(),secondCircle.getId());
        openEditDialogue(newEdge);
        if (newEdge.edgeIDProperty().getValue().isEmpty() || newEdge.startNodeProperty().getValue().isEmpty() ||
                newEdge.endNodeProperty().getValue().isEmpty())
            return; //FIXME: DO BETTER ERROR CHECKING
        updateEdgeEntry(newEdge);
    }

    /**
     * Opens edit dialogue to edit edge
     * Written with code from KD
     *
     * @param editedEdge is the edge being edited
     * @author Karen Hou
     */
    private ArrayList<String> openEditDialogue(EdgeEntry editedEdge) throws IOException {
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
        dialogueStage.initOwner((Stage) newButton.getScene().getWindow());
        dialogueStage.setScene(new Scene(root));

        dialogueStage.showAndWait();

        ArrayList<String> returnList = new ArrayList<>();

        returnList.add(editedEdge.getEdgeID());
        returnList.add(editedEdge.getStartNode());
        returnList.add(editedEdge.getEndNode());

        return returnList;
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
     * Used to highlight selected edges
     * @author Alex Friedman & ZheCheng
     */
    public void handleSelectEdge() {
        // Check for a valid index (-1 = no selection)
        if (edgeTreeTable.getSelectionModel().getSelectedIndex() < 0) {
            // FIXME Error Handling
            return;
        }
        // Get selected Edge
        EdgeEntry node = edgeEntryObservableList.get(edgeTreeTable.getSelectionModel().getSelectedIndex());

        if (node == null) {
            //FIXME Null Warning
            return;
        }
    }

    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #F0C808; -fx-text-fill: #000000;");
    }

    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #03256C; -fx-text-fill: #FFFFFF;");
    }

    public void handleSearch(KeyEvent keyEvent) {
    }

    public void handleNew(ActionEvent actionEvent) {
    }

    public void handleEdit(ActionEvent actionEvent) {
    }

    public void handleDelete(ActionEvent actionEvent) {
    }

    public void handleSave(ActionEvent actionEvent) {
    }

    public void handleLoad(ActionEvent actionEvent) {
    }

    public void handleReset(ActionEvent actionEvent) {
    }

    public void handleHome(MouseEvent mouseEvent) {
    }
}
