package edu.wpi.fuchsiafalcons.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.fuchsiafalcons.database.ConnectionHandler;
import edu.wpi.fuchsiafalcons.database.DatabaseAPI;
import edu.wpi.fuchsiafalcons.entities.EdgeEntry;
import edu.wpi.fuchsiafalcons.entities.NodeEntry;
import edu.wpi.fuchsiafalcons.utils.CSVManager;
import edu.wpi.fuchsiafalcons.utils.UIConstants;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.util.Callback;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


public class EditMapEdgesController {

    @FXML
    private Button goBack;

    @FXML
    private JFXButton newEdge;

    @FXML
    private JFXButton editEdge;

    @FXML
    private JFXButton deleteEdge;

    @FXML
    private JFXButton loadCSV;

    @FXML
    private JFXButton saveCSV;

    @FXML
    private JFXTextField CSVFile;

    @FXML
    private Label CSVErrorLabel;

    @FXML
    private JFXTreeTableView<EdgeEntry> edgeTable;

    @FXML
    private Pane canvas;

    // Create an observable list of edges
    private final ObservableList<EdgeEntry> edgeEntryObservableList = FXCollections.observableArrayList();

    @FXML
    private ScrollPane scroll;
    @FXML
    private ImageView map;

    @FXML private ComboBox<String> floorComboBox;

    private double zoomLevel = 3.0;
    private String floor = "1";
    private Line selectedLine = null;

    private Circle firstCircle = null;
    private Circle secondCircle = null;

    private Image F1Image,F2Image,F3Image,L1Image,L2Image,GImage = null;

    List<NodeEntry> nodeList = new ArrayList<>();
    /**
     * Initializes controller, called when EditMapEdges.fxml is loaded.
     *
     * @author Leo Morris
     */
    @FXML
    private void initialize() {
        List<EdgeEntry> edgeList = new ArrayList<>();
        // Clear the file error label
        CSVErrorLabel.setStyle("-fx-text-fill: black");
        CSVErrorLabel.setText("");

        // setup the map view
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        map.setPreserveRatio(true);
        final Image image = new Image(getClass().getResourceAsStream("/maps/01_thefirstfloor.png"));

        final double width = image.getWidth() / zoomLevel;
        final double height = image.getHeight() / zoomLevel;

        canvas.setPrefSize(width,height);

        map.setFitWidth(width);
        map.setFitHeight(height);
        map.setImage(image); // Copied from EditMapEdges - LM

        // Set the save button to disabled by default (enabled by a valid file name being entered)
        saveCSV.setDisable(true);

        /*
        //FIXME: do better, hook into db

        final String sql = "CREATE TABLE L1Edges(EdgeID varchar(200), " +
                "startNode varchar(200), endNode varchar(200), " +
                "primary key(edgeID))";

        try {
            DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "L1Edges");
            DatabaseAPI.getDatabaseAPI().createTable(ConnectionHandler.getConnection(), sql);

            List<String[]> edgeData = CSVManager.load("MapfAllEdges.csv");
            edgeList = DatabaseAPI.getDatabaseAPI().genEdgeEntries(ConnectionHandler.getConnection());
            DatabaseAPI.getDatabaseAPI().populateEdges(edgeData);
            edgeEntryObservableList.addAll(edgeData.stream().map(line-> new EdgeEntry(line[0], line[1], line[2])).collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }
         */

        List <EdgeEntry> data = new ArrayList<>();
        try {
            data = DatabaseAPI.getDatabaseAPI().genEdgeEntries(ConnectionHandler.getConnection());
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        data.stream().sorted(Comparator.comparing(EdgeEntry::getEdgeID)).collect(Collectors.toList()).forEach(e -> edgeEntryObservableList.add(e));

        // Set up cell factory for the edge ID table
        JFXTreeTableColumn<EdgeEntry, String> edgeIDColumn = new JFXTreeTableColumn<>("Edge ID");
        edgeIDColumn.setPrefWidth(250);
        edgeIDColumn.setCellValueFactory(param -> param.getValue().getValue().edgeIDProperty());

        final TreeItem<EdgeEntry> root = new RecursiveTreeItem<>(edgeEntryObservableList, RecursiveTreeObject::getChildren);
        edgeTable.getColumns().setAll(edgeIDColumn);
        edgeTable.setRoot(root);
        edgeTable.setShowRoot(false);

        // Set up floor comboBox and draw the nodes and edges on current floor
        final ObservableList<String> floorName = FXCollections.observableArrayList();
        floorName.addAll("1","2","3","L1","L2","G");
        floorComboBox.setItems(floorName);
        floorComboBox.setValue(floor);
        drawEdgeNodeOnFloor();
        // Listen for selection changes on the table view
        //edgeTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> showSelectedEdge(newVwalue)));
    }

    /**
     * Deletes the selected node from the table. Called when the delete button is pressed
     * Creates an alert pop-up if no edge is selected
     *
     * @author Leo Morris
     */
    @FXML
    private void handleDelete() throws SQLException {
        // Get the current selected edge
        int index = edgeTable.getSelectionModel().getSelectedIndex();
        EdgeEntry selectedEdge = null;

        // Check for a valid index (-1 = no selection)
        try{
            // Remove the edge, this will update the TableView automatically
            DatabaseAPI.getDatabaseAPI().deleteEdge(edgeEntryObservableList.get(index).getEdgeID());
            edgeEntryObservableList.remove(index);
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

        if(selectedEdge!=null){
            // Remove the edge, this will update the TableView automatically
            DatabaseAPI.getDatabaseAPI().deleteEdge(selectedEdge.getEdgeID());
            edgeEntryObservableList.remove(index);

            // Delete Selected edge and corresponding node on map
            selectedLine = null;
            drawEdgeNodeOnFloor();
        }
    }

    /**
     * Called when an edge is selected to be edited
     *
     * @author Karen Hou
     */
    @FXML
    private void handleEditEdge() throws IOException, SQLException {
        // Get the current selected edge index
        int index = edgeTable.getSelectionModel().getSelectedIndex();
        EdgeEntry selectedEdge;
        try {
            selectedEdge = edgeEntryObservableList.get(index);
        } catch (ArrayIndexOutOfBoundsException e){
            // Create an alert to inform the user there is no edge selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null); // Appears on top of all other windows
            alert.setTitle("No Selection");
            alert.setHeaderText("No Edge Selected");
            alert.setContentText("Please select an edge from the list");
            e.printStackTrace();
            alert.showAndWait();
            return;
        }
        if (selectedEdge != null) {
            String oldID = selectedEdge.getEdgeID();
            ArrayList<String> newValues = openEditDialogue(selectedEdge);

            DatabaseAPI.getDatabaseAPI().updateEntry("L1Edges", "edge", oldID, "startNode", newValues.get(1));
            DatabaseAPI.getDatabaseAPI().updateEntry("L1Edges", "edge", oldID, "endNode", newValues.get(2));
            DatabaseAPI.getDatabaseAPI().updateEntry("L1Edges", "edge", oldID, "id", newValues.get(0));

            // Focus on selected edge both on table and on map
            edgeTable.requestFocus();
            edgeTable.getSelectionModel().clearAndSelect(findEdge(newValues.get(0)));
            edgeTable.scrollTo(findEdge(newValues.get(0)));
            handleSelectEdge();
        }
    }

    /**
     * Called when an edge is to be created
     *
     * @author Karen Hou
     */
    @FXML
    private void handleNewEdge() throws IOException, SQLException {
        EdgeEntry newEdge = new EdgeEntry();
        openEditDialogue(newEdge);
        if(!checkEdgeEntryNotEmpty(newEdge)){return;}
        updateEdgeEntry(newEdge);
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

    private void updateEdgeEntry(EdgeEntry edgeEntry) throws SQLException {

        if (edgeEntry.getEdgeID().isEmpty() || edgeEntry.getStartNode().isEmpty() || edgeEntry.getEndNode().isEmpty())
            return; //FIXME: DO BETTER ERROR CHECKING, CHECK THAT WE ARE GETTING INTS

        edgeEntryObservableList.add(edgeEntry); // add the new node to the Observable list (which is linked to table and updates) - KD
        DatabaseAPI.getDatabaseAPI().addEdge(edgeEntry.getEdgeID(), edgeEntry.getStartNode(), edgeEntry.getEndNode());

        // Focus on selected edge both on table and on map
        edgeTable.requestFocus();
        edgeTable.getSelectionModel().clearAndSelect(findEdge(edgeEntry.getEdgeID()));
        edgeTable.scrollTo(findEdge(edgeEntry.getEdgeID()));
        handleSelectEdge();
    }

    /**
     * Handles pressing of "HOME" button, replaces handleButtonPushed to remove some excess lines
     *
     * @throws IOException
     * @author Leo Morris
     */
    @FXML
    private void handleHomeButton() throws IOException {
        Stage stage;
        Parent root;
        stage = (Stage) goBack.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/DefaultPageAdminView.fxml"));
        stage.getScene().setRoot(root);
        stage.setTitle("Default Page");
        stage.show();
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
        editDialogueLoader.setLocation(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/EditMapEdgeDialogueView.fxml"));
        Stage dialogueStage = new Stage();
        Parent root = editDialogueLoader.load();
        EditMapEdgeDialogueViewController editDialogueController = editDialogueLoader.getController();

        editDialogueController.setDialogueStage(dialogueStage);
        editDialogueController.setEdge(editedEdge);

        editDialogueController.setEdgeList(edgeEntryObservableList);
        editDialogueController.setCurrentIDIfEditing(editedEdge.getEdgeID());
        dialogueStage.setTitle("Edit Edge");
        dialogueStage.initModality(Modality.WINDOW_MODAL);
        dialogueStage.initOwner((Stage) goBack.getScene().getWindow());
        dialogueStage.setScene(new Scene(root));

        dialogueStage.showAndWait();

        ArrayList<String> returnList = new ArrayList<>();

        returnList.add(editedEdge.getEdgeID());
        returnList.add(editedEdge.getStartNode());
        returnList.add(editedEdge.getEndNode());

        return returnList;
    }

    /**
     * Saves the current list of edges to a CSV file at the location specified in the TextField by the user
     * Written with code from KD and AHF
     *
     * @param actionEvent Save button action
     * @author Leo Morris
     */
    public void handleSaveButton(ActionEvent actionEvent) {
        //FIXME: NULL ERROR CHECK.
        final String fileName = CSVFile.getText();

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
                return new String[] {
                        edge.getEdgeID(),
                        edge.getStartNode(),
                        edge.getEndNode()
                };
            }).collect(Collectors.toList()));

            try {
                CSVManager.writeToFile(file, data);
            } catch (Exception e) {
                CSVErrorLabel.setStyle("-fx-text-fill: red");
                CSVErrorLabel.setText(e.getMessage());
                e.printStackTrace();
                return;
            }
        }

        CSVErrorLabel.setText("File successfully exported.");
        CSVErrorLabel.setStyle("-fx-text-fill: black");

        CSVFile.setText("");
        saveCSV.setDisable(true);
        //loadFromFileButton.setDisable(true); //FIXME: ENABLE WHEN WE ADD A WAY TO LOAD CSV FROM IN JAR
    }

    /**
     * Disables the save and load buttons if the file name is invalid
     *
     * @param keyEvent Calls on key release in the CSVFile TextField
     */
    @FXML
    public void handleFileNameType(KeyEvent keyEvent) {
        CSVErrorLabel.setText("");
        CSVErrorLabel.setStyle("-fx-text-fill: black");

        final boolean disableBtns = !CSVFile.getText().endsWith(".csv");

        saveCSV.setDisable(disableBtns);
        loadCSV.setDisable(false); //FIXME: CHANGE CONDITION ONCE LOADING FROM JAR IS IMPLEMENTED
    }

    /**
     * Loads in the CSV file specified in the CSVFile TextField by the user
     * Written with code from KD and AHF
     *
     * @param actionEvent Pressing the Load CSV Button
     * @author Leo Morris
     */
    public void handleLoadButtonClicked(ActionEvent actionEvent) throws SQLException { //FIXME: NULL ERROR CHECK.
        //Maybe this should be methodized out of the controller? - ahf (yes I know I wrote this, I was being lazy)

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Choose CSV File");
        Stage FileStage = new Stage();
        File file = fileChooser.showOpenDialog(FileStage);
        final String fileName = String.valueOf(file);


        List<String[]> edgeData = null;

        //FIXME: METHODIZE THISS!!!!
        try {
            edgeData = (fileName == null || fileName.trim().isEmpty()) ? CSVManager.load("MapfAllEdges.csv") : CSVManager.load(new File(fileName));
        } catch (Exception e) {
            CSVErrorLabel.setText(e.getMessage());
            CSVErrorLabel.setStyle("-fx-text-fill: red");
            e.printStackTrace();
            return;
        }
        edgeEntryObservableList.clear();

        if(edgeData != null )
        {
            if(!edgeData.isEmpty() && edgeData.get(0).length == 8 )
            {
                edgeEntryObservableList.addAll(edgeData.stream().map(line -> {
                    return new EdgeEntry(line[0], line[1], line[2]);
                }).sorted(Comparator.comparing(EdgeEntry::getEdgeID)).collect(Collectors.toList()));

                DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "L1EDGES");
                final String initEdgesTable = "CREATE TABLE L1Edges(edgeID varchar(200), " +
                    "startNode varchar(200), endNode varchar(200), primary key(edgeID))";
                DatabaseAPI.getDatabaseAPI().createTable(ConnectionHandler.getConnection(), initEdgesTable);
                DatabaseAPI.getDatabaseAPI().populateNodes(edgeData); //NOTE: now can specify CSV arguments
            }
        }

        CSVErrorLabel.setText("File successfully read.");
        CSVErrorLabel.setStyle("-fx-text-fill: black");
        CSVFile.setText("");
        saveCSV.setDisable(true);
        // loadFromFileButton.setDisable(true); //FIXME: ENABLE WHEN WE ADD A WAY TO LOAD CSV FROM IN JAR

        drawEdgeNodeOnFloor();
    }

    /**
     * Used to highlight selected edges
     * @author Alex Friedman & ZheCheng
     */
    public void handleSelectEdge() {
        // Check for a valid index (-1 = no selection)
        if(edgeTable.getSelectionModel().getSelectedIndex() < 0){
            // FIXME Error Handling
            return;
        }
        // Get selected Edge
        EdgeEntry node = edgeEntryObservableList.get(edgeTable.getSelectionModel().getSelectedIndex());

        if(node == null){
            //FIXME Null Warning
            return;
        }

        // Try to get startNode and endNode from database
        NodeEntry startNode = null;
        NodeEntry endNode = null;
        try {
            startNode = DatabaseAPI.getDatabaseAPI().getNode(ConnectionHandler.getConnection(), node.getStartNode());
            endNode = DatabaseAPI.getDatabaseAPI().getNode(ConnectionHandler.getConnection(), node.getEndNode());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // startNode or endNode not stored in database
        if(startNode == null || endNode == null) {
            System.out.println("Edge with no actual Node");
            return;
        }

        // Check if need to switch map
        if(startNode.getFloor().equals(floor) || endNode.getFloor().equals(floor)) {
            drawEdgeNodeOnFloor();
            if(selectedLine != null)
                selectedLine.setStroke(UIConstants.LINE_COLOR);
        }else{
            floor = startNode.getFloor();
            switchMap();
        }

        // Get the line with edgeID
        Line l = (Line) canvas.lookup("#"+node.getEdgeID());
        if(l == null){
            //FIXME Null Warning
            return;
        }
        selectedLine = l;
        l.setStroke(Color.GREEN);
        centerNode(l);
    }

    /**
     * Handle switching floor using combobox
     * @param actionEvent
     * @author ZheCheng
     */
    @FXML
    public void handleFloorBoxAction(ActionEvent actionEvent) {
        floor = floorComboBox.getValue();
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
        drawEdgeNodeOnFloor();
    }

    /**
     * Clear the canvas and draw edges and nodes that are on current floor
     * @author ZheCheng
     */
    private void drawEdgeNodeOnFloor() {
        // Clear canvas
        canvas.getChildren().removeIf(x -> {
            return x instanceof Circle;
        });
        canvas.getChildren().removeIf(x -> {
            return x instanceof Line;
        });

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
            }else if(startNode.getFloor().equals(floor) || endNode.getFloor().equals(floor)) {
                if(!nodeList.contains(startNode))
                    nodeList.add(startNode);
                if(!nodeList.contains(endNode))
                    nodeList.add(endNode);
                drawLine(Double.parseDouble(startNode.getXcoord())/zoomLevel,
                        Double.parseDouble(startNode.getYcoord())/zoomLevel,
                        Double.parseDouble(endNode.getXcoord())/zoomLevel,
                        Double.parseDouble(endNode.getYcoord())/zoomLevel,
                        e.getEdgeID());
            }
        }

        // Draw all corresponding nodes
        for(NodeEntry n : nodeList){
            drawCircle(Double.parseDouble(n.getXcoord()) / zoomLevel, Double.parseDouble(n.getYcoord()) / zoomLevel, n.getNodeID());
        }
    }

    /**
     * Draw a single circle to represent the node
     * @author ZheCheng
     */
    private void drawCircle(double x, double y, String nodeID){
        Circle c = new Circle(x, y, 4.0);
        c.setFill(UIConstants.NODE_COLOR);
        c.setId(nodeID);
        c.setOnMouseEntered(e->{if(!c.equals(firstCircle)&&!c.equals(secondCircle))c.setFill(UIConstants.NODE_COLOR_HIGHLIGHT);});
        c.setOnMouseExited(e->{if(!c.equals(firstCircle)&&!c.equals(secondCircle))c.setFill(UIConstants.NODE_COLOR);});
        c.setOnMouseClicked(e->{
            if(c.equals(firstCircle)) {
                firstCircle = null;
                return;
            }
            c.setFill(UIConstants.NODE_COLOR_SELECTED);
            if(firstCircle == null)
                firstCircle = c;
            else {
                secondCircle = c;
                // Second node selected, create edge
                try {
                    createNewEdge();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        this.canvas.getChildren().add(c);
    }

    /**
     * Open window for user to create new edge with two node selected
     *
     * @throws IOException
     * @throws SQLException
     * @author ZheCheng
     */
    private void createNewEdge() throws IOException, SQLException{
        EdgeEntry newEdge = new EdgeEntry(firstCircle.getId()+"_"+secondCircle.getId(),firstCircle.getId(),secondCircle.getId());
        openEditDialogue(newEdge);
        if (newEdge.edgeIDProperty().getValue().isEmpty() || newEdge.startNodeProperty().getValue().isEmpty() ||
                newEdge.endNodeProperty().getValue().isEmpty())
            return; //FIXME: DO BETTER ERROR CHECKING
        updateEdgeEntry(newEdge);
    }


    /**
     * Draw a single line to represent the edge
     * @author ZheCheng
     */
    private void drawLine(double startX, double startY, double endX, double endY, String edgeID){
        Line l = new Line(startX, startY, endX, endY);
        l.setStrokeWidth(UIConstants.LINE_STROKE_WIDTH);
        l.setStroke(UIConstants.LINE_COLOR);
        l.setId(edgeID);
        l.setOnMouseEntered(e->{if(!l.equals(selectedLine))l.setStroke(UIConstants.NODE_COLOR_HIGHLIGHT);});
        l.setOnMouseExited(e->{if(!l.equals(selectedLine))l.setStroke(UIConstants.LINE_COLOR);});
        l.setOnMouseClicked(e->{
            if(selectedLine != null)
                selectedLine.setStroke(UIConstants.LINE_COLOR);
            selectedLine = l;
            l.setStroke(Color.GREEN);
            edgeTable.getSelectionModel().clearAndSelect(findEdge(edgeID));
            edgeTable.requestFocus();
            edgeTable.scrollTo(findEdge(edgeID));
        });

        this.canvas.getChildren().add(l);
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

    /**
     * Center the given line in scrollpane
     * @param l The line to be centered
     * @author ZheCheng
     */
    public void centerNode(Line l){

        double h = scroll.getContent().getBoundsInLocal().getHeight();
        double y = (l.getBoundsInParent().getMaxY() +
                l.getBoundsInParent().getMinY()) / 2.0;
        double v = scroll.getViewportBounds().getHeight();
        scroll.setVvalue(scroll.getVmax() * ((y - 0.5 * v) / (h - v)));

        double w = scroll.getContent().getBoundsInLocal().getWidth();
        double x = (l.getBoundsInParent().getMaxX() +
                l.getBoundsInParent().getMinX()) / 2.0;
        double hw = scroll.getViewportBounds().getWidth();
        scroll.setHvalue(scroll.getHmax() * -((x - 0.5 * hw) / (hw - w)));
    }

    public void handleResetDB(ActionEvent actionEvent) throws SQLException {

        edgeEntryObservableList.clear();

        List<String[]> edgeData = null;

        try {
            edgeData = (CSVManager.load("MapfAllEdges.csv"));
        } catch (Exception e) {
            CSVErrorLabel.setText(e.getMessage());
            CSVErrorLabel.setStyle("-fx-text-fill: red");
            e.printStackTrace();
            return;
        }

        if(edgeData != null )
        {
            if(!edgeData.isEmpty() && edgeData.get(0).length == 8 )
            {
                nodeList.addAll(edgeData.stream().map(line -> {
                    return new NodeEntry(line[0], line[1], line[2], line[3], line[4], line[5], line[6], line[7]);
                }).sorted(Comparator.comparing(NodeEntry::getNodeID)).collect(Collectors.toList()));

                DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "L1EDGES");
                final String initEdgesTable = "CREATE TABLE L1Edges(edgeID varchar(200), " +
                        "startNode varchar(200), endNode varchar(200), primary key(edgeID))";
                DatabaseAPI.getDatabaseAPI().createTable(ConnectionHandler.getConnection(), initEdgesTable);
                DatabaseAPI.getDatabaseAPI().populateEdges(edgeData); //NOTE: now can specify CSV arguments
            }
        }
        CSVErrorLabel.setText("");
        CSVErrorLabel.setStyle("-fx-text-fill: black");

        CSVFile.setText("");
        saveCSV.setDisable(true);
        drawEdgeNodeOnFloor();
    }
}
