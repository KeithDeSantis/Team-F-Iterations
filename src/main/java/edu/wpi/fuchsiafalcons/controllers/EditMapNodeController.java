package edu.wpi.fuchsiafalcons.controllers;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.fuchsiafalcons.entities.NodeEntry;
import edu.wpi.fuchsiafalcons.utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import edu.wpi.fuchsiafalcons.database.*;

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

    @FXML private ScrollPane scroll;
    @FXML private ImageView map;
    @FXML private Pane canvas;
    @FXML private ImageView logoView;

    @FXML private ComboBox<String> floorComboBox;

    @FXML private JFXButton zoomInButton;
    @FXML private JFXButton zoomOutButton;

    @FXML private JFXTextField searchField;
    @FXML private JFXComboBox<String> searchComboBox;

    private ObservableList<NodeEntry> nodeList = FXCollections.observableArrayList();
    private NodeEntry selectedNode;

    double zoomLevel = 4.0;
    private String floor = "1";
    private Circle selectedCircle = null;

    private Image F1Image,F2Image,F3Image,L1Image,L2Image,GImage = null;

    /**
     * Overriding Initialize for testing and set up
     * @author Keith DeSantis
     */
    @FXML
    private void initialize() {

        //Setting up dragging and dropping nodes - KD
        map.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (event.getGestureSource() != map && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        map.setOnDragDropped((DragEvent event) -> {
            try {
                dropCircle(event, (Circle) event.getGestureSource());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });


        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        map.setPreserveRatio(true);
        F1Image = new Image(getClass().getResourceAsStream("/maps/01_thefirstfloor.png"));

        final double width = F1Image.getWidth()/zoomLevel;
        final double height = F1Image.getHeight()/zoomLevel;

        canvas.setPrefSize(width,height);

        map.setFitWidth(width);
        map.setFitHeight(height);
        map.setImage(F1Image); // Copied from A* Vis - KD

        final Image logo = new Image(getClass().getResourceAsStream("/imagesAndLogos/BandWLogo.png"));
        logoView.setImage(logo);

        List <NodeEntry> data = new ArrayList<>();
        try {
            data = DatabaseAPI.getDatabaseAPI().genNodeEntries(ConnectionHandler.getConnection());
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        data.stream().sorted(Comparator.comparing(NodeEntry::getNodeID)).collect(Collectors.toList()).forEach(e -> nodeList.add(e));

        // START OF JFX TREETABLE COLUMN SETUP

        int colWidth = 120;
        JFXTreeTableColumn<NodeEntry, String> idColumn = new JFXTreeTableColumn<>("Node ID");
        idColumn.setPrefWidth(colWidth);
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().getNodeIDProperty());
        JFXTreeTableColumn<NodeEntry, String> shortColumn = new JFXTreeTableColumn<>("Name");
        shortColumn.setPrefWidth(colWidth);
        shortColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().getShortNameProperty());


        final TreeItem<NodeEntry> root = new RecursiveTreeItem<NodeEntry>(nodeList, RecursiveTreeObject::getChildren);
        nodeTreeTable.setRoot(root);
        nodeTreeTable.setShowRoot(false);
        nodeTreeTable.getColumns().setAll(idColumn, shortColumn);
        //nodeTreeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setSelectedNode(newValue.getValue()));
        loadFromFileButton.setDisable(false);

        // Set up floor comboBox and draw nodes on that floor
        final ObservableList<String> floorName = FXCollections.observableArrayList();
        floorName.addAll("1","2","3","L1","L2","G");
        floorComboBox.setItems(floorName);
        floorComboBox.setValue(floor);
        drawNodeOnFloor();

        final ObservableList<String> searchName = FXCollections.observableArrayList();
        searchName.addAll("Node ID","Floor","Building","Node Type","Long Name", "Short Name");
        searchComboBox.setItems(searchName);
        searchComboBox.setValue("Node ID");

        final ContextMenu contextMenu = new ContextMenu();

        final MenuItem createNodeMenuItem = new MenuItem("Create Node Here");

        contextMenu.getItems().addAll(createNodeMenuItem);


        map.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(map, event.getScreenX(), event.getScreenY());

                createNodeMenuItem.setOnAction((ActionEvent e) -> {
                    NodeEntry nodeEntry = new NodeEntry();
                    nodeEntry.setXcoord("" + (event.getX() * zoomLevel));
                    nodeEntry.setYcoord("" + (event.getY() * zoomLevel));
                    nodeEntry.setFloor(floorComboBox.valueProperty().get());

                    try {
                        openEditDialog(nodeEntry);
                        if(!checkNodeEntryNotEmpty(nodeEntry)) return;
                        nodeList.add(nodeEntry); // add the new node to the Observable list (which is linked to table and updates) - KD
                        updateNodeEntry(nodeEntry);
                    } catch (IOException | SQLException ioException) {
                        ioException.printStackTrace();
                    }
                });
            }
        });

        //contextMenu.show(map, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
    }

    /**
     * Handles the pushing of a button on the screen
     * @param actionEvent the button's push
     * @throws IOException in case of scene switch, if the next fxml scene file cannot be found
     * @author ZheCheng Song
     */
    @FXML
    private void handleBackButtonPushed(ActionEvent actionEvent) throws IOException, SQLException {

        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage
        Stage stage;
        Parent root;

        if (buttonPushed == goBack) {
          //  ConnectionHandler.getConnection().close();
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/DefaultPageAdminView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Default Page");
            stage.show();
        }
    }

    /**
     * Called when delete button is pressed
     * @param actionEvent
     * @author KD
     */
    @FXML
    private void handleDeletePushed(ActionEvent actionEvent) throws SQLException {
        int selectedIndex = nodeTreeTable.getSelectionModel().getSelectedIndex(); // get index of table that is selected - KD
        if(selectedIndex<0) { return; }

        String targetID = nodeTreeTable.getTreeItem(selectedIndex).getValue().getNodeID();
        DatabaseAPI.getDatabaseAPI().deleteNode(targetID);
        nodeList.remove(selectedIndex); // remove said index from table - KD

        selectedCircle = null;
        drawNodeOnFloor(); // added to handle deletion without selection - KD
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
        int xCoord = Integer.parseInt(nodeEntry.getXcoord());
        int yCoord = Integer.parseInt(nodeEntry.getYcoord());
        String nodeFloor = nodeEntry.getFloor();
        String nodeBuilding = nodeEntry.getBuilding();
        String nodeType = nodeEntry.getNodeType();
        String longName = nodeEntry.getLongName();
        String shortName = nodeEntry.getShortName();


        DatabaseAPI.getDatabaseAPI().addNode(nodeID, xCoord, yCoord, nodeFloor, nodeBuilding, nodeType, longName, shortName);

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
        dialogLoader.setLocation(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/EditMapNodeDialogView.fxml")); // load in Edit Dialog - KD
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

        if(nodeData != null )
        {
            if(!nodeData.isEmpty() && nodeData.get(0).length == 8 )
            {
                nodeList.addAll(nodeData.stream().map(line -> {
                    return new NodeEntry(line[0], line[1], line[2], line[3], line[4], line[5], line[6], line[7]);
                }).sorted(Comparator.comparing(NodeEntry::getNodeID)).collect(Collectors.toList()));

                DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "L1NODES");
                final String initNodesTable = "CREATE TABLE L1Nodes(NodeID varchar(200), " +
                        "xCoord int, yCoord int, floor varchar(200), building varchar(200), " +
                        "nodeType varchar(200), longName varchar(200), shortName varchar(200), primary key(NodeID))";
                DatabaseAPI.getDatabaseAPI().createTable(ConnectionHandler.getConnection(), initNodesTable);
                DatabaseAPI.getDatabaseAPI().populateNodes(nodeData); //NOTE: now can specify CSV arguments
            }
        }

        errorMessageLabel.setText("File successfully read.");
        errorMessageLabel.setStyle("-fx-text-fill: black");


        drawNodeOnFloor();
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
     * Handle switching floor using combobox
     * @param actionEvent
     * @author ZheCheng
     */
    @FXML
    public void handleFloorBoxAction(ActionEvent actionEvent) {
        floor = floorComboBox.getValue().toString();
        switchMap();
    }

    /**
     * Handle switching floor map and redraw the nodes in new floor
     * @author ZheCheng
     */
    private void switchMap(){
        switch(floor){
            case "1": if (F1Image == null)F1Image = new Image("/maps/01_thefirstfloor.png");
            map.setImage(F1Image); break;
            case "2": if (F2Image == null)F2Image = new Image("/maps/02_thesecondfloor.png");
            map.setImage(F2Image); break;
            case "3": if (F3Image == null)F3Image = new Image("/maps/03_thethirdfloor.png");
            map.setImage(F3Image); break;
            case "L1": if (L1Image == null)L1Image = new Image("/maps/00_thelowerlevel1.png");
            map.setImage(L1Image); break;
            case "L2": if (L2Image == null)L2Image = new Image("/maps/00_thelowerlevel2.png");
            map.setImage(L2Image); break;
            case "G": if (GImage == null)GImage = new Image("/maps/00_thegroundfloor.png");
            map.setImage(GImage); break;
            default: if (F1Image == null)F1Image = new Image("/maps/01_thefirstfloor.png");
                map.setImage(F1Image); System.out.println("No Such Floor!"); break; //FIXME : Error Handling
        }
        floorComboBox.setValue(floor);
        drawNodeOnFloor();
    }

    /**
     * Clear the canvas and draw nodes that are on current floor
     * @author ZheCheng
     */
    private void drawNodeOnFloor(){
        canvas.getChildren().removeIf(x -> {
            return x instanceof Circle;
        });
        selectedCircle = null;
        for(NodeEntry n : nodeList){
            if(n.getFloor().equals(floor)) {
                drawCircle(Double.parseDouble(n.getXcoord())/zoomLevel, Double.parseDouble(n.getYcoord())/zoomLevel, n.getNodeID());
            }
        }
    }

    /**
     * Draw a single circle to represent the node
     * @author ZheCheng
     */
    private void drawCircle(double x, double y, String nodeID){
        Circle c = new Circle(x, y, UIConstants.NODE_RADIUS);
        c.setFill(UIConstants.NODE_COLOR);
        c.setId(nodeID);
        c.setOnMouseEntered(e->{if(!c.equals(selectedCircle))c.setFill(UIConstants.NODE_COLOR_HIGHLIGHT);});
        c.setOnMouseExited(e->{if(!c.equals(selectedCircle))c.setFill(UIConstants.NODE_COLOR);});
        c.setOnMouseClicked(e->{
            if(selectedCircle != null)
                selectedCircle.setFill(UIConstants.NODE_COLOR);
            selectedCircle = c;
            c.setFill(UIConstants.NODE_COLOR_SELECTED);
            nodeTreeTable.getSelectionModel().clearAndSelect(findNode(nodeID));
            nodeTreeTable.requestFocus();nodeTreeTable.scrollTo(findNode(nodeID));});
        c.setOnDragDetected((MouseEvent event) -> {
            Dragboard db = c.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("Circle source text");
            db.setContent(content);
        });
        c.setOnMouseDragged((MouseEvent event) -> {
            event.setDragDetect(true);
        });


        this.canvas.getChildren().add(c);
    }

    /**
     * Handles the dropping of a dragged node so its position is updated
     * @param event The dropping of the node
     * @param c The circle dropped
     * @throws SQLException
     * @author KD
     */
    public void dropCircle(DragEvent event, Circle c) throws SQLException {
        Dragboard db = event.getDragboard();
        if (db.hasString()) {
            canvas.getChildren().remove(c);
            NodeEntry movedNode = nodeList.get(findNode(c.getId()));
            movedNode.setXcoord(String.valueOf((int) (event.getX()*zoomLevel)));
            movedNode.setYcoord(String.valueOf((int) (event.getY()*zoomLevel)));
            drawCircle(event.getX(),event.getY(),c.getId());


            String nodeID = movedNode.getNodeID();
            int xCoord = Integer.parseInt(movedNode.getXcoord());
            int yCoord = Integer.parseInt(movedNode.getYcoord());
            String nodeFloor = movedNode.getFloor();
            String nodeBuilding = movedNode.getBuilding();
            String nodeType = movedNode.getNodeType();
            String longName = movedNode.getLongName();
            String shortName = movedNode.getShortName();
            DatabaseAPI.getDatabaseAPI().deleteNode(nodeID);
            DatabaseAPI.getDatabaseAPI().addNode(nodeID, xCoord, yCoord, nodeFloor, nodeBuilding, nodeType, longName, shortName);
            event.setDropCompleted(true);
        } else {
            event.setDropCompleted(false);
        }
        event.consume();
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
        if(node.getFloor().equals(floor)){
            drawNodeOnFloor();
            if(selectedCircle != null)
                selectedCircle.setFill(UIConstants.NODE_COLOR);
        }else{
            floor = node.getFloor();
            switchMap();
        }
        Circle c = (Circle) canvas.lookup("#"+node.getNodeID());
        if(c == null){
            //FIXME Null Warning
            return;
        }
        selectedCircle = c;
        c.setFill(UIConstants.NODE_COLOR_SELECTED);
        centerNode(c);
    }

    /**
     * Center the given node in scrollpane
     * @param c The node to be centered
     * @author ZheCheng
     */
    public void centerNode(Circle c){

        double h = scroll.getContent().getBoundsInLocal().getHeight();
        double y = (c.getBoundsInParent().getMaxY() +
                c.getBoundsInParent().getMinY()) / 2.0;
        double v = scroll.getViewportBounds().getHeight();
        scroll.setVvalue(scroll.getVmax() * ((y - 0.5 * v) / (h - v)));

        double w = scroll.getContent().getBoundsInLocal().getWidth();
        double x = (c.getBoundsInParent().getMaxX() +
                c.getBoundsInParent().getMinX()) / 2.0;
        double hw = scroll.getViewportBounds().getWidth();
        scroll.setHvalue(scroll.getHmax() * -((x - 0.5 * hw) / (hw - w)));

    }

 /**
     * Resets the database
     * @author KD and ahf
     */
    public void handleResetDatabase() throws Exception {

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

                DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "L1NODES");
                final String initNodesTable = "CREATE TABLE L1Nodes(NodeID varchar(200), " +
                        "xCoord int, yCoord int, floor varchar(200), building varchar(200), " +
                        "nodeType varchar(200), longName varchar(200), shortName varchar(200), primary key(NodeID))";
                DatabaseAPI.getDatabaseAPI().createTable(ConnectionHandler.getConnection(), initNodesTable);
                DatabaseAPI.getDatabaseAPI().populateNodes(nodeData); //NOTE: now can specify CSV arguments
            }
        }
        errorMessageLabel.setText("");
        errorMessageLabel.setStyle("-fx-text-fill: black");

        drawNodeOnFloor();
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

    /**
     * Basic implementation of Zooming the map by changing the zoom level and reloading
     * @param actionEvent the press of zoom in or zoom out
     * @author KD
     */
    public void handleZoom(ActionEvent actionEvent) { //TODO Fix Centering so centering node works when zoom level is changed
        JFXButton btn = (JFXButton) actionEvent.getSource();
        if(btn == zoomInButton) {
            if(zoomLevel > 1) {
                zoomLevel--;
                zoomOutButton.setDisable(false);
                if(zoomLevel==1) zoomInButton.setDisable(true);
            }

        } else if (btn == zoomOutButton) {
            if(zoomLevel < 8) {
                zoomLevel++;
                zoomInButton.setDisable(false);
                if(zoomLevel==8) zoomOutButton.setDisable(true);
            }
        }
        drawNodeOnFloor();
        Image image = map.getImage();
        double width = image.getWidth()/zoomLevel;
        double height = image.getHeight()/zoomLevel;
        canvas.setPrefSize(width,height);
        map.setFitWidth(width);
        map.setFitHeight(height);
        map.setImage(image);
    }

    /**
     * Filters the node list based on what the user is searching
     * @author KD
     */
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
