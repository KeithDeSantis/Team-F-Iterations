package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.D21.teamF.database.ConnectionHandler;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.EdgeEntry;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.CSVManager;
import edu.wpi.cs3733.D21.teamF.utils.UIConstants;
import edu.wpi.cs3733.uicomponents.MapPanel;
import edu.wpi.cs3733.uicomponents.entities.DrawableEdge;
import edu.wpi.cs3733.uicomponents.entities.DrawableNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MapEditViewController {

    @FXML private JFXButton saveButton;
    @FXML private JFXButton loadButton;
    @FXML private JFXButton resetFromDB;
    @FXML private JFXButton newButton;
    @FXML private JFXButton editButton;
    @FXML private JFXButton deleteButton;
    @FXML private JFXComboBox<String> searchComboBox = new JFXComboBox<String>();
    @FXML private JFXTextField searchField;
    @FXML private TabPane tabPane;
    @FXML private Tab nodesTab;
    @FXML private Tab edgesTab;
    @FXML private JFXTreeTableView<NodeEntry> nodeTreeTable;
    @FXML private JFXTreeTableView<EdgeEntry> edgeTreeTable;
    @FXML private MapPanel mapPanel;
    @FXML private Text title;

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

        Font titleFont = Font.loadFont("file:src/main/resources/fonts/Volkhov-Regular.ttf", 60);
        title.setFont(titleFont);

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
                nodeEntry.setXcoord("" + (int)(event.getX() * mapPanel.getZoomLevel().get()));
                nodeEntry.setYcoord("" + (int)(event.getY() * mapPanel.getZoomLevel().get()));
                nodeEntry.setFloor(mapPanel.getFloor().get());

                try {
                    openEditNodeDialog(nodeEntry);
                    if(!checkNodeEntryNotEmpty(nodeEntry)) return;
                    nodeEntryObservableList.add(nodeEntry); // add the new node to the Observable list (which is linked to table and updates) - KD

                    mapPanel.draw(getEditableNode(nodeEntry));

                    updateNodeEntry(nodeEntry);
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
        edgeIDColumn.setPrefWidth(300);
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

        drawableNode.setOnMouseClicked(e-> {
            if (selectedCircle != null)
                selectedCircle.setFill(UIConstants.NODE_COLOR);
            if (selectedLine != null)
                selectedLine.setStroke(UIConstants.LINE_COLOR);

            selectedCircle = drawableNode;
            drawableNode.setFill(UIConstants.NODE_COLOR_SELECTED);

            /* TODO this section allows for "clicking any two nodes and prompting a new edge"  Not sure if we want it implemented since its kind of an inconvenience when
            // TODO clicking around nodes, if we do we'll have to work out some issue with it - KD
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
                    createNewEdgeFromNodes();
                    tabPane.getSelectionModel().select(edgesTab);
                    edgeTreeTable.getSelectionModel().clearAndSelect(findEdge(firstCircle.getId()+"_"+secondCircle.getId()));
                    edgeTreeTable.requestFocus();
                    edgeTreeTable.scrollTo(findEdge(firstCircle.getId()+"_"+secondCircle.getId()));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
             */
            // Automagically change to nodes tab
            tabPane.getSelectionModel().select(nodesTab);
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
            if(n.getNodeID().equals(nodeID)){
                return index;
            }
            index++;
        }
        return -1; // FIXME handle this error
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
        nodeList = new ArrayList<NodeEntry>();

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
                boolean duplicate = false;
                for (NodeEntry n: nodeList) {
                    if(n.getNodeID().equals(startNode.getNodeID())){
                        duplicate = true;
                        break;
                    }
                }
                if(!duplicate){
                    nodeList.add(startNode);
                }
                duplicate = false;
                for (NodeEntry n: nodeList) {
                    if(n.getNodeID().equals(endNode.getNodeID())){
                        duplicate = true;
                        break;
                    }
                }
                if(!duplicate){
                    nodeList.add(endNode);
                }


                Line l = mapPanel.draw(getEditableEdge(e, startNode, endNode));

                l.setOnMouseEntered(event->{if(!l.equals(selectedLine))l.setStroke(UIConstants.NODE_COLOR_HIGHLIGHT);});
                l.setOnMouseExited(event->{if(!l.equals(selectedLine))l.setStroke(UIConstants.LINE_COLOR);});
                l.setOnMouseClicked(event->{
                    if(selectedLine != null)
                        selectedLine.setStroke(UIConstants.LINE_COLOR);
                    if(selectedCircle != null)
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
        for(NodeEntry n : nodeEntryObservableList){
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
        openEditEdgeDialog(newEdge);
        if (newEdge.edgeIDProperty().getValue().isEmpty() || newEdge.startNodeProperty().getValue().isEmpty() ||
                newEdge.endNodeProperty().getValue().isEmpty())
            return; //FIXME: DO BETTER ERROR CHECKING
        updateEdgeEntry(newEdge);
    } //FIXME implement if we decide we should, a discussion to be had - KD

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
        dialogueStage.initOwner((Stage) newButton.getScene().getWindow());
        dialogueStage.setScene(new Scene(root));
        dialogueStage.showAndWait();
        ArrayList<String> returnList = new ArrayList<>();
        returnList.add(editedEdge.getEdgeID());
        returnList.add(editedEdge.getStartNode());
        returnList.add(editedEdge.getEndNode());
        return returnList;
    }

    /**
     * Opens the edit dialog to edit a particular node
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
        dialogStage.initOwner((Stage) newButton.getScene().getWindow());
        dialogStage.setScene(new Scene(root)); // set scene - KD

        dialogStage.showAndWait(); // open pop up - KD
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
     * Used to update a node entry in the UI and database //FIXME: Add delete if we already had the node
     * @param nodeEntry
     */
    private void updateNodeEntry(NodeEntry nodeEntry) throws SQLException {

        if(!checkNodeEntryNotEmpty(nodeEntry))
            return;

        String nodeID = nodeEntry.getNodeID();
        int xCoord = Integer.parseInt(nodeEntry.getXcoord());
        int yCoord = Integer.parseInt(nodeEntry.getYcoord());
        String nodeFloor = nodeEntry.getFloor();
        String nodeBuilding = nodeEntry.getBuilding();
        String nodeType = nodeEntry.getNodeType();
        String longName = nodeEntry.getLongName();
        String shortName = nodeEntry.getShortName();


        DatabaseAPI.getDatabaseAPI().addNode(nodeID, xCoord, yCoord, nodeFloor, nodeBuilding, nodeType, longName, shortName);

        mapPanel.draw(getEditableNode(nodeEntry));

        nodeTreeTable.requestFocus();
        nodeTreeTable.getSelectionModel().clearAndSelect(findNode(nodeID));
        nodeTreeTable.scrollTo(findNode(nodeID));
        handleSelectNode();
    }

    /**
     * Used to highlight selected edges
     * @author Alex Friedman & ZheCheng
     */
    public void handleSelectEdge() {
        // Check for a valid index (-1 = no selection)
        if(edgeTreeTable.getSelectionModel().getSelectedIndex() < 0){
            // FIXME Error Handling
            return;
        }
        // Get selected Edge
        EdgeEntry edge = edgeEntryObservableList.get(edgeTreeTable.getSelectionModel().getSelectedIndex());

        if(edge == null){
            //FIXME Null Warning
            return;
        }

        // Try to get startNode and endNode from database
        NodeEntry startNode = null;
        NodeEntry endNode = null;
        try {
            startNode = DatabaseAPI.getDatabaseAPI().getNode(ConnectionHandler.getConnection(), edge.getStartNode());
            endNode = DatabaseAPI.getDatabaseAPI().getNode(ConnectionHandler.getConnection(), edge.getEndNode());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // startNode or endNode not stored in database
        if(startNode == null || endNode == null) {
            System.out.println("Edge with no actual Node");
            return;
        }

        // Check if need to switch map
        if(startNode.getFloor().equals(mapPanel.getFloor().get()) || endNode.getFloor().equals(mapPanel.getFloor().get())) {
            //drawEdgeNodeOnFloor();

        }else{
            mapPanel.switchMap(startNode.getFloor());
        }

        // Clear highlight on previously selected line or node
        if(selectedLine != null)
            selectedLine.setStroke(UIConstants.LINE_COLOR);
        if(selectedCircle != null)
            selectedCircle.setFill(UIConstants.NODE_COLOR);

        // Get the line with edgeID
        Line l = (Line) mapPanel.getCanvas().lookup("#"+edge.getEdgeID());
        if(l == null){
            //FIXME Null Warning
            return;
        }
        selectedLine = l;
        l.setStroke(Color.GREEN);
        mapPanel.centerNode(l);
    }

    /**
     * Search filters tree table based on which tab is open
     * @param keyEvent
     * @author KD
     */
    public void handleSearch(KeyEvent keyEvent) {
        if(nodesTab.isSelected()) {
            nodeTreeTable.setPredicate(new Predicate<TreeItem<NodeEntry>>() {
                @Override
                public boolean test(TreeItem<NodeEntry> nodeEntryTreeItem) {
                    if(searchField.getText().length()>0) {
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
                            default:
                                return true;
                        }
                    }
                    return true;
                }
            });
        }
        else if(edgesTab.isSelected()) {
            edgeTreeTable.setPredicate(new Predicate<TreeItem<EdgeEntry>>() {
                @Override
                public boolean test(TreeItem<EdgeEntry> edgeEntryTreeItem) {
                    if(searchField.getText().length()>0) {
                        switch (searchComboBox.getValue()) {
                            case "Edge ID":
                                return edgeEntryTreeItem.getValue().getEdgeID().contains(searchField.getText());
                            case "Start Node":
                                return edgeEntryTreeItem.getValue().getStartNode().contains(searchField.getText());
                            case "End Node":
                                return edgeEntryTreeItem.getValue().getEndNode().contains(searchField.getText());
                            default:
                                return true;
                        }
                    }
                    return true;
                }
            });
        }
    }

    /**
     * When new button is clicked, open appropriate dialog
     * @param actionEvent
     * @author KD and LM
     */
    public void handleNew(ActionEvent actionEvent) throws IOException, SQLException {
        if(nodesTab.isSelected()) {
            NodeEntry newNode = new NodeEntry(); // create new node - KD
            openEditNodeDialog(newNode); // allow editing of the new node - KD
            if(!checkNodeEntryNotEmpty(newNode)) return;
            nodeEntryObservableList.add(newNode); // add the new node to the Observable list (which is linked to table and updates) - KD

            updateNodeEntry(newNode);
        }
        else if(edgesTab.isSelected()) {
            EdgeEntry newEdge = new EdgeEntry();
            openEditEdgeDialog(newEdge);
            if(!checkEdgeEntryNotEmpty(newEdge)){return;}
            updateEdgeEntry(newEdge);
        }
    }

    /**
     * Helper for adding node that makes sure the node doesn't have empty fields (like when the edit dialog is opened but then closed externally)
     * @param edgeEntry the node entry
     * @return true if the node has no empty fields
     * @author KD
     */
    public boolean checkEdgeEntryNotEmpty(EdgeEntry edgeEntry) {
        return!edgeEntry.getEdgeID().isEmpty() && !edgeEntry.getStartNode().isEmpty() && !edgeEntry.getEndNode().isEmpty();
    }

    /**
     * Helper for adding node that makes sure the node doesn't have empty fields (like when the edit dialog is opened but then closed externally)
     * @param nodeEntry the node entry
     * @return true if the node has no empty fields
     * @author KD
     */
    public boolean checkNodeEntryNotEmpty(NodeEntry nodeEntry) {
        return!nodeEntry.getNodeID().isEmpty() && !nodeEntry.getXcoord().isEmpty() && !nodeEntry.getYcoord().isEmpty() &&
                !nodeEntry.getFloor().isEmpty() && !nodeEntry.getBuilding().isEmpty() && !nodeEntry.getNodeType().isEmpty() &&
                !nodeEntry.getLongName().isEmpty() && !nodeEntry.getShortName().isEmpty();
    }

    /**
     * Opens an edit dialog based on the tab opened
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     * @author KD, LM, KH
     */
    public void handleEdit(ActionEvent actionEvent) throws SQLException, IOException {
        NodeEntry selectedNode;
        if(nodesTab.isSelected()) {
            try{
                selectedNode = nodeTreeTable.getSelectionModel().getSelectedItem().getValue(); // get item the is selected - KD
            } catch (NullPointerException e){
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
        }
        else if(edgesTab.isSelected()) {
            // Get the current selected edge index
            int index = edgeTreeTable.getSelectionModel().getSelectedIndex();
            EdgeEntry selectedEdge;
            try {
                selectedEdge = edgeEntryObservableList.get(index);
            } catch (ArrayIndexOutOfBoundsException e){
                return;
            }
            if (selectedEdge != null) {
                String oldID = selectedEdge.getEdgeID();
                ArrayList<String> newValues = openEditEdgeDialog(selectedEdge);

                DatabaseAPI.getDatabaseAPI().updateEntry("L1Edges", "edge", oldID, "startNode", newValues.get(1));
                DatabaseAPI.getDatabaseAPI().updateEntry("L1Edges", "edge", oldID, "endNode", newValues.get(2));
                DatabaseAPI.getDatabaseAPI().updateEntry("L1Edges", "edge", oldID, "id", newValues.get(0));

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
    } //FIXME edited node and node disappeared?

    /**
     * Deletes the selected item based on the tab open
     * @param actionEvent
     * @throws SQLException
     * @author KD LM KH ZS
     */
    public void handleDelete(ActionEvent actionEvent) throws SQLException {
        if(nodesTab.isSelected()) {
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
        }

        else if (edgesTab.isSelected()) {
            int index = edgeTreeTable.getSelectionModel().getSelectedIndex();
            EdgeEntry selectedEdge = null;

            // Check for a valid index (-1 = no selection)
            try{
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
     * Helper for when a node's ID is changed, needs to update all Edge entries that are associated with it in both the observable list and the database
     * @param previousID the previous ID
     * @param newID the new ID
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
     * @param nodeID
     * @author KD
     */
    public void deleteAssociatedEdges(String nodeID) throws SQLException {

        List<Integer> indicesToRemove = new ArrayList<Integer>();

        for (int index = 0; index < edgeEntryObservableList.size(); index++) {
            if (edgeEntryObservableList.get(index).getStartNode().equals(nodeID) || edgeEntryObservableList.get(index).getEndNode().equals(nodeID)) { // if an edge is connected to said node
                DatabaseAPI.getDatabaseAPI().deleteEdge(edgeEntryObservableList.get(index).getEdgeID());
                mapPanel.unDraw(edgeEntryObservableList.get(index).getEdgeID());
                indicesToRemove.add(index); // This is needed, since removing the elements while going through this for loop can cause the loop to skip entries (as the indices of elements are dynamically updated) - KD
            }
        }
        for(Integer i : indicesToRemove) {
            edgeEntryObservableList.remove(edgeEntryObservableList.get(i));
            for(int index = 0; index<indicesToRemove.size(); index++) {
                indicesToRemove.set(index, indicesToRemove.get(index) - 1); // to accomodate the changing indices of edgeEntryObservableList
            }
        }
    }

    /**
     * Saves to a file based on the tab open
     * @param actionEvent
     * @author KD ahf LM
     */
    public void handleSave(ActionEvent actionEvent) {
        if(nodesTab.isSelected()) {
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
                final List<String[]> data = new LinkedList<String[]>();
                Collections.addAll(data, "nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName".split(","));

                data.addAll(nodeEntryObservableList.stream().map(node -> {
                    return new String[] {
                            node.getNodeID(),
                            node.getXcoord(),
                            node.getYcoord(),
                            node.getFloor(),
                            node.getBuilding(),
                            node.getNodeType(),
                            node.getLongName(),
                            node.getShortName()
                    };
                }).collect(Collectors.toList()));

                try {
                    CSVManager.writeToFile(file, data);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
        else if(edgesTab.isSelected()) {
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
                final List<String[]> data = new LinkedList<String[]>();
                Collections.addAll(data, "edgeID,startNode,endNode".split(","));

                data.addAll(edgeEntryObservableList.stream().map(edge -> {
                    return new String[]{
                            edge.getEdgeID(),
                            edge.getStartNode(),
                            edge.getEndNode()
                    };
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
     * @param actionEvent
     * @author KD ahf LM
     */
    public void handleLoad(ActionEvent actionEvent) throws SQLException, IOException {
        if(nodesTab.isSelected()) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("Choose CSV File");
            Stage FileStage = new Stage();
            File file = fileChooser.showOpenDialog(FileStage);
            final String fileName = String.valueOf(file);


            List<String[]> nodeData = null;

            //FIXME: METHODIZE THISS!!!!
            try {
                nodeData = (fileName == null || fileName.trim().isEmpty()) ? CSVManager.load("MapfAllNodes.csv") : CSVManager.load(new File(fileName));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            if(nodeData.get(0).length != 8) {
                FXMLLoader dialogLoader = new FXMLLoader();
                dialogLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/editorBadCSVView.fxml")); // load in Edit Dialog - KD
                Stage dialogStage = new Stage();
                Parent root = dialogLoader.load();
                dialogStage.initModality(Modality.WINDOW_MODAL); // make window a pop up - KD
                dialogStage.initOwner((Stage) newButton.getScene().getWindow());
                dialogStage.setScene(new Scene(root)); // set scene - KD
                dialogStage.showAndWait();
                return; //TODO This is Keith's bad attempt to make sure the user doesn't try to load in an edge CSV
            }

            nodeEntryObservableList.clear();
            mapPanel.clearMap();

            if(nodeData != null )
            {
                if(!nodeData.isEmpty() && nodeData.get(0).length == 8 )
                {
                    nodeEntryObservableList.addAll(nodeData.stream().map(line -> {
                        return new NodeEntry(line[0], line[1], line[2], line[3], line[4], line[5], line[6], line[7]);
                    }).sorted(Comparator.comparing(NodeEntry::getNodeID)).collect(Collectors.toList()));

                    nodeEntryObservableList.forEach(n -> mapPanel.draw(getEditableNode(n)));

                    DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "L1NODES");
                    final String initNodesTable = "CREATE TABLE L1Nodes(NodeID varchar(200), " +
                            "xCoord int, yCoord int, floor varchar(200), building varchar(200), " +
                            "nodeType varchar(200), longName varchar(200), shortName varchar(200), primary key(NodeID))";
                    DatabaseAPI.getDatabaseAPI().createTable(ConnectionHandler.getConnection(), initNodesTable);
                    DatabaseAPI.getDatabaseAPI().populateNodes(nodeData); //NOTE: now can specify CSV arguments
                }
            }
            drawEdgeNodeOnFloor(); // TODO this is interesting, if two incompatible CSVs are loaded what will happen?
        }
        else if(edgesTab.isSelected()) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("Choose CSV File");
            Stage FileStage = new Stage();
            File file = fileChooser.showOpenDialog(FileStage);
            final String fileName = String.valueOf(file);


            List<String[]> edgeData = null;

            try {
                edgeData = (fileName == null || fileName.trim().isEmpty()) ? CSVManager.load("MapfAllEdges.csv") : CSVManager.load(new File(fileName));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if(edgeData.get(0).length != 3) {
                FXMLLoader dialogLoader = new FXMLLoader();
                dialogLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/editorBadCSVView.fxml")); // load in Edit Dialog - KD
                Stage dialogStage = new Stage();
                Parent root = dialogLoader.load();
                dialogStage.initModality(Modality.WINDOW_MODAL); // make window a pop up - KD
                dialogStage.initOwner((Stage) newButton.getScene().getWindow());
                dialogStage.setScene(new Scene(root)); // set scene - KD
                dialogStage.showAndWait();
                return; //TODO This is Keith's bad attempt to make sure the user doesn't try to load in an node CSV
            }

            edgeEntryObservableList.clear();

            if(edgeData != null )
            {
                if(!edgeData.isEmpty() && edgeData.get(0).length == 3 )
                {
                    edgeEntryObservableList.addAll(edgeData.stream().map(line -> {
                        return new EdgeEntry(line[0], line[1], line[2]);
                    }).sorted(Comparator.comparing(EdgeEntry::getEdgeID)).collect(Collectors.toList()));

                    DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "L1EDGES");
                    final String initEdgesTable = "CREATE TABLE L1Edges(edgeID varchar(200), " +
                            "startNode varchar(200), endNode varchar(200), primary key(edgeID))";
                    DatabaseAPI.getDatabaseAPI().createTable(ConnectionHandler.getConnection(), initEdgesTable);
                    DatabaseAPI.getDatabaseAPI().populateEdges(edgeData); //NOTE: now can specify CSV arguments
                }
            }
            drawEdgeNodeOnFloor(); // TODO this is interesting, if two incompatible CSVs are loaded what will happen?
        }
    } //FIXME issues with loading CSVs, specifically how they are loaded in.  Eventually check to make sure loading proper CSV, or do two different buttons - KD

    public void handleReset(ActionEvent actionEvent) throws SQLException {
        mapPanel.clearMap();
        nodeEntryObservableList.clear();
        edgeEntryObservableList.clear();

        List<String[]> nodeData = null;
        List<String[]> edgeData = null;

        try {
            nodeData = (CSVManager.load("MapfAllNodes.csv"));
            edgeData = (CSVManager.load("MapfAllEdges.csv"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if(nodeData != null && edgeData != null)
        {
            if(!nodeData.isEmpty() && nodeData.get(0).length == 8 && !edgeData.isEmpty() && edgeData.get(0).length == 3 )
            {
                nodeEntryObservableList.addAll(nodeData.stream().map(line -> {
                    return new NodeEntry(line[0], line[1], line[2], line[3], line[4], line[5], line[6], line[7]);
                }).sorted(Comparator.comparing(NodeEntry::getNodeID)).collect(Collectors.toList()));
                edgeEntryObservableList.addAll(edgeData.stream().map(line -> {
                    return new EdgeEntry(line[0], line[1], line[2]);
                }).sorted(Comparator.comparing(EdgeEntry::getEdgeID)).collect(Collectors.toList()));

                DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "L1NODES"); //FIXME Sometimes when resetting there is an error on line 853 since it says 'L1NODES' already exists in schema.  Makes me think sometimes this line isn't working?? - KD
                final String initNodesTable = "CREATE TABLE L1Nodes(NodeID varchar(200), " +
                        "xCoord int, yCoord int, floor varchar(200), building varchar(200), " +
                        "nodeType varchar(200), longName varchar(200), shortName varchar(200), primary key(NodeID))";
                DatabaseAPI.getDatabaseAPI().createTable(ConnectionHandler.getConnection(), initNodesTable); //FIXME there error is thrown here for reference - KD
                DatabaseAPI.getDatabaseAPI().populateNodes(nodeData); //NOTE: now can specify CSV arguments

                DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "L1EDGES");
                final String initEdgesTable = "CREATE TABLE L1Edges(edgeID varchar(200), " +
                        "startNode varchar(200), endNode varchar(200), primary key(edgeID))";
                DatabaseAPI.getDatabaseAPI().createTable(ConnectionHandler.getConnection(), initEdgesTable);
                DatabaseAPI.getDatabaseAPI().populateEdges(edgeData); //NOTE: now can specify CSV arguments

                drawEdgeNodeOnFloor(); //FIXME do better with queries
            }
        }

    } //FIXME sometimes error 'L1NODES' already exists in Schema 'APP' happens and the map isnt drawn

    public void handleHome(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml"));
        stage.getScene().setRoot(root);
        stage.setTitle("Admin Home");
        stage.show();
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
     * Select node based on selection in Table, focus on the node
     * @author ZheCheng
     */
    public void handleSelectNode() {
        if(nodeTreeTable.getSelectionModel().getSelectedIndex() < 0){
            // FIXME Error Handling
            return;
        }
        // FIXME: ADD TRY_CATCH
        NodeEntry node = nodeTreeTable.getSelectionModel().getSelectedItem().getValue(); //Changed for handling when the nodeTreeTable is being searched

        if(node == null){
            //FIXME Null Warning
            return;
        }

        // Check if need to switch map
        if(node.getFloor().equals(mapPanel.getFloor().get())){
            //mapPanel.drawNodeOnFloor();
            if(selectedCircle != null)
                selectedCircle.setFill(UIConstants.NODE_COLOR);
        }else{
            //floor = node.getFloor();
            mapPanel.switchMap(node.getFloor());
        }

        // Clear highlight on edges
        if(selectedLine != null)
            selectedLine.setStroke(UIConstants.LINE_COLOR);



        Circle c = (Circle) mapPanel.getCanvas().lookup("#"+node.getNodeID());
        if(c == null){
            //FIXME Null Warning
            return;
        }
        selectedCircle = c;
        c.setFill(UIConstants.NODE_COLOR_SELECTED);
        mapPanel.centerNode(c);
    }

    public void handleTabChange(Event event) {
        Tab theTab = (Tab) event.getSource();
        if(theTab == nodesTab) {
            ObservableList<String> searchables = FXCollections.observableArrayList();
            searchables.add("Node ID");
            searchables.add("Floor");
            searchables.add("Building");
            searchables.add("Node Type");
            searchables.add("Long Name");
            searchables.add("Short Name");
            searchComboBox.setItems(searchables);
            searchComboBox.setValue("Node ID");
        } else if(theTab == edgesTab) {
            ObservableList<String> searchables = FXCollections.observableArrayList();
            searchables.add("Edge ID");
            searchables.add("Start Node");
            searchables.add("End Node");
            searchComboBox.setItems(searchables);
            searchComboBox.setValue("Edge ID");
        }
    }
}

