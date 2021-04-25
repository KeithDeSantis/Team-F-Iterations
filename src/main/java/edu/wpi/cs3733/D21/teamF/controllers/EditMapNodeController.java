package edu.wpi.cs3733.D21.teamF.controllers;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.uicomponents.MapPanel;
import edu.wpi.cs3733.uicomponents.entities.DrawableNode;
import edu.wpi.cs3733.D21.teamF.utils.CSVManager;
import edu.wpi.cs3733.D21.teamF.utils.UIConstants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


import java.sql.*;

/**
 * Controller for the Edit Node Page
 * @author KD
 */
public class EditMapNodeController {

    @FXML private JFXButton goBack;

    @FXML private JFXTreeTableView<NodeEntry> nodeTreeTable;

    @FXML private Label errorMessageLabel;

    @FXML private JFXButton saveToFileButton;
    @FXML private JFXButton loadFromFileButton;

    @FXML private ImageView logoView;

    @FXML private MapPanel mapPanel;

    @FXML private JFXTextField searchField;
    @FXML private JFXComboBox<String> searchComboBox;

    private NodeEntry selectedNode;

    private ObservableList<NodeEntry> nodeList = FXCollections.observableArrayList();

    private Circle selectedCircle = null;


    /**
     * Overriding Initialize for testing and set up
     * @author Keith DeSantis
     */
    @FXML
    private void initialize() {




        final Image logo = new Image(getClass().getResourceAsStream("/imagesAndLogos/BandWLogo.png"));
        logoView.setImage(logo);

        List <NodeEntry> data = new ArrayList<>();
        try {
            data = DatabaseAPI.getDatabaseAPI().genNodeEntries();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        data.stream().sorted(Comparator.comparing(NodeEntry::getNodeID)).collect(Collectors.toList()).forEach(node -> {
            mapPanel.draw(getEditableNode(node));

            nodeList.add(node);
        });
        //mapPanel.drawNodeOnFloor();


        // START OF JFX TREETABLE COLUMN SETUP

        int colWidth = 120;
        JFXTreeTableColumn<NodeEntry, String> idColumn = new JFXTreeTableColumn<>("Node ID");
        idColumn.setPrefWidth(colWidth);
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().getNodeIDProperty());
        JFXTreeTableColumn<NodeEntry, String> shortColumn = new JFXTreeTableColumn<>("Name");
        shortColumn.setPrefWidth(colWidth);
        shortColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().getShortNameProperty());
        final TreeItem<NodeEntry> root = new RecursiveTreeItem<>(nodeList, RecursiveTreeObject::getChildren);
        nodeTreeTable.setRoot(root);
        nodeTreeTable.setShowRoot(false);
        nodeTreeTable.getColumns().setAll(idColumn, shortColumn);
        //nodeTreeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setSelectedNode(newValue.getValue()));
        loadFromFileButton.setDisable(false);


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
                    openEditDialog(nodeEntry);
                    if(!checkNodeEntryNotEmpty(nodeEntry)) return;
                    nodeList.add(nodeEntry); // add the new node to the Observable list (which is linked to table and updates) - KD

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

        //contextMenu.show(map, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
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
     * Handles the pushing of a button on the screen
     * @param actionEvent the button's push
     * @throws IOException in case of scene switch, if the next fxml scene file cannot be found
     * @author ZheCheng Song
     */
    @FXML
    private void handleBackButtonPushed(ActionEvent actionEvent) throws IOException {

        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage
        Stage stage;
        Parent root;

        if (buttonPushed == goBack) {
          //  ConnectionHandler.getConnection().close();
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Default Page");
            stage.show();
        }
    }

    /**
     * Called when delete button is pressed
     * @author KD
     */
    @FXML
    private void handleDeletePushed() throws SQLException {
        int selectedIndex = nodeTreeTable.getSelectionModel().getSelectedIndex(); // get index of table that is selected - KD
        if(selectedIndex<0) { return; }

        String targetID = nodeTreeTable.getTreeItem(selectedIndex).getValue().getNodeID();
        DatabaseAPI.getDatabaseAPI().deleteNode(targetID);
        nodeList.remove(selectedIndex); // remove said index from table - KD

        selectedCircle = null;
        mapPanel.unDraw(targetID);
        //mapPanel.drawNodeOnFloor(); // added to handle deletion without selection - KD
    }

    /**
     * Call when the new node button is pressed to open edit dialog and add node
     * @throws IOException
     */
    @FXML
    private void handleNewNode() throws IOException, SQLException {
        NodeEntry newNode = new NodeEntry(); // create new node - KD
        openEditDialog(newNode); // allow editing of the new node - KD
        if(!checkNodeEntryNotEmpty(newNode)) return;
        nodeList.add(newNode); // add the new node to the Observable list (which is linked to table and updates) - KD

        mapPanel.draw(getEditableNode(newNode));

        updateNodeEntry(newNode);
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
     * Used to update a node entry in the UI and database //FIXME: Add delete if we already had the node
     * @param nodeEntry
     */
    private void updateNodeEntry(NodeEntry nodeEntry) throws SQLException {

        if(!checkNodeEntryNotEmpty(nodeEntry))
            return;

        String nodeID = nodeEntry.getNodeID();
        int xCoordinate = Integer.parseInt(nodeEntry.getXcoord());
        int yCoordinate = Integer.parseInt(nodeEntry.getYcoord());
        String nodeFloor = nodeEntry.getFloor();
        String nodeBuilding = nodeEntry.getBuilding();
        String nodeType = nodeEntry.getNodeType();
        String longName = nodeEntry.getLongName();
        String shortName = nodeEntry.getShortName();


        DatabaseAPI.getDatabaseAPI().addNode(nodeID, ""+ xCoordinate, ""+ yCoordinate, nodeFloor, nodeBuilding, nodeType, longName, shortName);

        mapPanel.draw(getEditableNode(nodeEntry));

        nodeTreeTable.requestFocus();
        nodeTreeTable.getSelectionModel().clearAndSelect(findNode(nodeID));
        nodeTreeTable.scrollTo(findNode(nodeID));
        handleSelectNode();
    }


    /**
     * Called when a node is to be edited, pens the dialog editing page with the chosen node
     * @author KD
     */
    @FXML
    private void handleEditNode() throws IOException, SQLException {
        if(nodeTreeTable.getSelectionModel().getSelectedItem() == null) { return; }
        NodeEntry selectedNode = nodeTreeTable.getSelectionModel().getSelectedItem().getValue(); // get item the is selected - KD
        if(selectedNode == null) { return; } // ensure there is a selection - KD

        String targetID = selectedNode.getNodeID();
        DatabaseAPI.getDatabaseAPI().deleteNode(targetID);
        mapPanel.unDraw(targetID);

        openEditDialog(selectedNode); // allow editing of selection - KD

        updateNodeEntry(selectedNode);
    }

    /**
     * Opens the edit dialog to edit a particular node
     * @param editedNode the node being edited
     * @author KD
     */
    private void openEditDialog(NodeEntry editedNode) throws IOException {

        FXMLLoader dialogLoader = new FXMLLoader();
        dialogLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/EditMapNodeDialogView.fxml")); // load in Edit Dialog - KD
        Stage dialogStage = new Stage();
        Parent root = dialogLoader.load();
        EditMapNodeDialogViewController dialogController = dialogLoader.getController(); // get edit dialog's controller - KD

        dialogController.setDialogStage(dialogStage); // set the stage attribute - KD
        dialogController.setTheNode(editedNode); // inject the node attribute so that specific instance is the one edited - KD

        dialogController.setNodeList(nodeList);
        dialogController.setCurrentIDIfEditing(editedNode.getNodeID());
        dialogStage.setTitle("Edit Node");
        dialogStage.initModality(Modality.WINDOW_MODAL); // make window a pop up - KD
        dialogStage.initOwner((Stage) goBack.getScene().getWindow());
        dialogStage.setScene(new Scene(root)); // set scene - KD

        dialogStage.showAndWait(); // open pop up - KD
    }

    @FXML
    /**
     * Used to load files
     * @author KD, ahf
     */
    public void handleLoadButtonClicked(ActionEvent actionEvent) throws Exception {
        //FIXME: NULL ERROR CHECK.
        //Maybe this should be methodized out of the controller? - ahf (yes I know I wrote this, I was being lazy)

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
            errorMessageLabel.setText(e.getMessage());
            errorMessageLabel.setStyle("-fx-text-fill: red");
            e.printStackTrace();
            return;
        }
        nodeList.clear();
        mapPanel.clearMap();

        if(nodeData != null )
        {
            if(!nodeData.isEmpty() && nodeData.get(0).length == 8 )
            {
                nodeList.addAll(nodeData.stream().map(line -> {
                    return new NodeEntry(line[0], line[1], line[2], line[3], line[4], line[5], line[6], line[7]);
                }).sorted(Comparator.comparing(NodeEntry::getNodeID)).collect(Collectors.toList()));

                nodeList.forEach(n -> mapPanel.draw(getEditableNode(n)));

                DatabaseAPI.getDatabaseAPI().dropNodesTable();
                DatabaseAPI.getDatabaseAPI().createNodesTable();
                DatabaseAPI.getDatabaseAPI().populateNodes(nodeData); //NOTE: now can specify CSV arguments
            }
        }

        errorMessageLabel.setText("File successfully read.");
        errorMessageLabel.setStyle("-fx-text-fill: black");

       // mapPanel.drawNodeOnFloor();
    }


    /**
     * Saves the current list of nodes to a CSV
     * @param actionEvent
     * @author ahf, KD
     */
    public void handleSaveButtonClicked(ActionEvent actionEvent)
    {
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

            data.addAll(nodeList.stream().map(node -> {
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
                errorMessageLabel.setStyle("-fx-text-fill: red");
                errorMessageLabel.setText(e.getMessage());
                e.printStackTrace();
                return;
            }
        }

        errorMessageLabel.setText("File successfully exported.");
        errorMessageLabel.setStyle("-fx-text-fill: black");

    }



    /**
     * Find the index of a given node with nodeID in nodeList
     * @author ZheCheng
     */
    private int findNode(String nodeID){
        int index = 0;
        for(NodeEntry n: nodeList){
            if(n.getNodeID() == nodeID){
                break;
            }
            index++;
        }
        return index;
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
        Circle c = (Circle) mapPanel.getCanvas().lookup("#"+node.getNodeID());
        if(c == null){
            //FIXME Null Warning
            return;
        }
        selectedCircle = c;
        c.setFill(UIConstants.NODE_COLOR_SELECTED);
        mapPanel.centerNode(c);
    }



 /**
     * Resets the database
     * @author KD and ahf
     */
    public void handleResetDatabase() throws Exception {

        mapPanel.clearMap();
        nodeList.clear();

        List<String[]> nodeData = null;

        try {
            nodeData = (CSVManager.load("MapfAllNodes.csv"));
        } catch (Exception e) {
            errorMessageLabel.setText(e.getMessage());
            errorMessageLabel.setStyle("-fx-text-fill: red");
            e.printStackTrace();
            return;
        }

        if(nodeData != null )
        {
            if(!nodeData.isEmpty() && nodeData.get(0).length == 8 )
            {
                nodeList.addAll(nodeData.stream().map(line -> {
                    return new NodeEntry(line[0], line[1], line[2], line[3], line[4], line[5], line[6], line[7]);
                }).sorted(Comparator.comparing(NodeEntry::getNodeID)).collect(Collectors.toList()));

                nodeList.forEach(x -> mapPanel.draw(getEditableNode(x)));

                DatabaseAPI.getDatabaseAPI().dropNodesTable();
                DatabaseAPI.getDatabaseAPI().createNodesTable();
                DatabaseAPI.getDatabaseAPI().populateNodes(nodeData); //NOTE: now can specify CSV arguments
            }
        }
        errorMessageLabel.setText("");
        errorMessageLabel.setStyle("-fx-text-fill: black");

    }

    /**
     * Used to make it so that we can right-click to create nodes.
     * @param contextMenuEvent
     * @author Alex Friedman (ahf), KD
     */
    public void handleMapContextMenu(ContextMenuEvent contextMenuEvent) {
        //FIXME: do better, at the same time, we can't actually do this in FXML


        nodeTreeTable.setStyle("-fx-tree-table-color: #006DA3;");

    }

    public void handleSearchNode() {
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
}
