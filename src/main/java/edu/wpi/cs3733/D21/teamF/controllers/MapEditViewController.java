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

        drawableNode.setOnMouseClicked(e->{
            if(selectedCircle != null)
                selectedCircle.setFill(UIConstants.NODE_COLOR);
            if(selectedLine != null)
                selectedLine.setStroke(UIConstants.LINE_COLOR);

            selectedCircle = drawableNode;
            drawableNode.setFill(UIConstants.NODE_COLOR_SELECTED);
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

    public void handleSearch(KeyEvent keyEvent) {
    }

    public void handleNew(ActionEvent actionEvent) {
    }

    public void handleEdit(ActionEvent actionEvent) {
    }

    public void handleDelete(ActionEvent actionEvent) throws SQLException {
        if(nodesTab.isSelected()) {
            int selectedIndex = nodeTreeTable.getSelectionModel().getSelectedIndex(); // get index of table that is selected - KD
            if (selectedIndex < 0) {
                return;
            }

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
    } //TODO test deleting NODE

    public void handleSave(ActionEvent actionEvent) {
    }

    public void handleLoad(ActionEvent actionEvent) {
    }

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

                DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "L1NODES");
                final String initNodesTable = "CREATE TABLE L1Nodes(NodeID varchar(200), " +
                        "xCoord int, yCoord int, floor varchar(200), building varchar(200), " +
                        "nodeType varchar(200), longName varchar(200), shortName varchar(200), primary key(NodeID))";
                DatabaseAPI.getDatabaseAPI().createTable(ConnectionHandler.getConnection(), initNodesTable);
                DatabaseAPI.getDatabaseAPI().populateNodes(nodeData); //NOTE: now can specify CSV arguments

                DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "L1EDGES");
                final String initEdgesTable = "CREATE TABLE L1Edges(edgeID varchar(200), " +
                        "startNode varchar(200), endNode varchar(200), primary key(edgeID))";
                DatabaseAPI.getDatabaseAPI().createTable(ConnectionHandler.getConnection(), initEdgesTable);
                DatabaseAPI.getDatabaseAPI().populateEdges(edgeData); //NOTE: now can specify CSV arguments

                drawEdgeNodeOnFloor(); //FIXME do better with queries
            }
        }

    }

    public void handleHome(MouseEvent mouseEvent) {
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
    public void handleSelectNode(MouseEvent mouseEvent) {
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
}

