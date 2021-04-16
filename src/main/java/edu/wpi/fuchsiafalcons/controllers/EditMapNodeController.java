package edu.wpi.fuchsiafalcons.controllers;
import edu.wpi.fuchsiafalcons.entities.NodeEntry;
import edu.wpi.fuchsiafalcons.utils.*;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import edu.wpi.fuchsiafalcons.database.*;
import org.w3c.dom.NodeList;

import java.sql.*;

/**
 * Controller for the Edit Node Page
 * @author KD
 */
public class EditMapNodeController {

    @FXML private Button goBack;
    @FXML private TableView<NodeEntry> nodeTable;
    @FXML private TableColumn<NodeEntry, String> NodeIDColumn;
    @FXML private TableColumn<NodeEntry, String> ShortNameColumn;

    @FXML private TextField filenameField;
    @FXML private Label errorMessageLabel;

    @FXML private Button saveToFileButton;
    @FXML private Button loadFromFileButton;

    @FXML private ScrollPane scroll;
    @FXML private ImageView map;
    @FXML private Pane canvas;
    @FXML private ImageView logoView;

    @FXML private ComboBox floorComboBox;

    private ObservableList<NodeEntry> nodeList = FXCollections.observableArrayList();
    private NodeEntry selectedNode;

    final double zoomLevel = 5.0;
    private String floor = "1";
    /**
     * Overriding Initialize for testing and set up
     * @author Keith DeSantis
     */
    @FXML
    private void initialize() {

        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        map.setPreserveRatio(true);
        final Image image = new Image(getClass().getResourceAsStream("/maps/01_thefirstfloor.png"));
        final double width = image.getWidth()/zoomLevel;
        final double height = image.getHeight()/zoomLevel;
        canvas.setPrefSize(width,height);
        map.setFitWidth(width);
        map.setFitHeight(height);
        map.setImage(image); // Copied from A* Vis - KD

        final Image logo = new Image(getClass().getResourceAsStream("/imagesAndLogos/BandWLogo.png"));
        logoView.setImage(logo);

        PopulateDB populateDB = new PopulateDB();
        List <NodeEntry> data = new ArrayList<>();
        try {
            data = populateDB.genNodeEntries(ConnectionHandler.getConnection());
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        for (NodeEntry e : data)
        {
            nodeList.add(e);
        }

        nodeTable.setItems(nodeList); // Put Observable list into TableView so that we can watch for changes in values - KD
        NodeIDColumn.setCellValueFactory(cellData -> cellData.getValue().getNodeIDProperty()); // Set first column to display first name property - KD
        ShortNameColumn.setCellValueFactory(cellData -> cellData.getValue().getShortNameProperty()); // Set second column to display second name property - KD
        nodeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setSelectedNode(newValue)); // Listener to watch for changes - KD
        loadFromFileButton.setDisable(false);

        final ObservableList<String> floorName = FXCollections.observableArrayList();
        floorName.addAll("1","2","3","L1","L2","G");
        floorComboBox.setItems(floorName);
        floorComboBox.setValue(floor);
        drawNodeOnFloor();
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
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/DefaultPageView.fxml"));
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
        int selectedIndex = nodeTable.getSelectionModel().getSelectedIndex(); // get index of table that is selected - KD
        if(selectedIndex<0) { return; }
        String targetID = nodeTable.getItems().get(selectedIndex).getNodeID();
        DatabaseAPI.getDatabaseAPI().deleteNode(targetID);
        nodeTable.getItems().remove(selectedIndex); // remove said index from table - KD
    }

    /**
     * Call when the new node button is pressed to open edit dialog and add node
     * @throws IOException
     */
    @FXML
    private void handleNewNode() throws IOException, SQLException {
        NodeEntry newNode = new NodeEntry(); // create new node - KD
        openEditDialog(newNode); // allow editing of the new node - KD

        if(newNode.getNodeID().isEmpty() || newNode.getXcoord().isEmpty() || newNode.getYcoord().isEmpty() ||
        newNode.getFloor().isEmpty() || newNode.getBuilding().isEmpty() || newNode.getNodeType().isEmpty() ||
        newNode.getLongName().isEmpty() || newNode.getShortName().isEmpty())
            return; //FIXME: DO BETTER ERROR CHECKING, CHECK THAT WE ARE GETTING INTS


        String nodeID = newNode.getNodeID();
        int xCoord = Integer.parseInt(newNode.getXcoord());
        int yCoord = Integer.parseInt(newNode.getYcoord());
        String nodeFloor = newNode.getFloor();
        String nodeBuilding = newNode.getBuilding();
        String nodeType = newNode.getNodeType();
        String longName = newNode.getLongName();
        String shortName = newNode.getShortName();

        nodeList.add(newNode); // add the new node to the Observable list (which is linked to table and updates) - KD
        DatabaseAPI.getDatabaseAPI().addNode(nodeID, xCoord, yCoord, nodeFloor, nodeBuilding, nodeType, longName, shortName);
    }


    /**
     * Called when a node is to be edited, pens the dialog editing page with the chosen node
     * @author KD
     */
    @FXML
    private void handleEditNode() throws IOException, SQLException {
        NodeEntry selectedNode = nodeTable.getSelectionModel().getSelectedItem(); // get item the is selected - KD
        if(selectedNode == null) { return; } // ensure there is a selection - KD

        String targetID = selectedNode.getNodeID();
        DatabaseAPI.getDatabaseAPI().deleteNode(targetID);

        openEditDialog(selectedNode); // allow editing of selection - KD

        String nodeID = selectedNode.getNodeID();
        int xCoord = Integer.parseInt(selectedNode.getXcoord());
        int yCoord = Integer.parseInt(selectedNode.getYcoord());
        String nodeFloor = selectedNode.getFloor();
        String nodeBuilding = selectedNode.getBuilding();
        String nodeType = selectedNode.getNodeType();
        String longName = selectedNode.getLongName();
        String shortName = selectedNode.getShortName();

        DatabaseAPI.getDatabaseAPI().addNode(nodeID, xCoord, yCoord, nodeFloor, nodeBuilding, nodeType, longName, shortName);
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
        dialogStage.setTitle("Edit Node");
        dialogStage.initModality(Modality.WINDOW_MODAL); // make window a pop up - KD
        dialogStage.initOwner((Stage) goBack.getScene().getWindow());
        dialogStage.setScene(new Scene(root)); // set scene - KD

        dialogStage.showAndWait(); // open pop up - KD
    }

    /**
     * Setter for selected node
     * @param node
     * @author KD
     */
    public void setSelectedNode(NodeEntry node) { selectedNode = node; }

    @FXML
    /**
     * Used to load files
     * @author KD, ahf
     */
    public void handleLoadButtonClicked(ActionEvent actionEvent) throws Exception {
        //FIXME: NULL ERROR CHECK.
        //Maybe this should be methodized out of the controller? - ahf (yes I know I wrote this, I was being lazy)
        final String fileName = filenameField.getText();

        nodeList.clear();

        List<String[]> nodeData = null;
        List<String[]> edgeData = null;

        try {
            nodeData = (fileName == null || fileName.trim().isEmpty()) ? CSVManager.load("L1Nodes.csv") : CSVManager.load(new File(fileName));
            edgeData = CSVManager.load("L1Edges.csv");
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
                }).collect(Collectors.toList()));
                PopulateDB p = new PopulateDB();
                p.main(ConnectionHandler.getConnection(), nodeData, edgeData); //NOTE: now can specify CSV arguments
            }
        }

        errorMessageLabel.setText("File successfully read.");
        errorMessageLabel.setStyle("-fx-text-fill: black");
        filenameField.setText("");
        saveToFileButton.setDisable(true);
       // loadFromFileButton.setDisable(true); //FIXME: ENABLE WHEN WE ADD A WAY TO LOAD CSV FROM IN JAR

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
        final String fileName = filenameField.getText();

        //Maybe this should be methodized out of the controller? - ahf (yes I know I wrote this, I was being lazy)

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
            CSVManager.writeFile(fileName, data);
        } catch (Exception e) {
            errorMessageLabel.setStyle("-fx-text-fill: red");
            errorMessageLabel.setText(e.getMessage());
            e.printStackTrace();
            return;
        }

        errorMessageLabel.setText("File successfully exported.");
        errorMessageLabel.setStyle("-fx-text-fill: black");

        filenameField.setText("");
        saveToFileButton.setDisable(true);
        //loadFromFileButton.setDisable(true); //FIXME: ENABLE WHEN WE ADD A WAY TO LOAD CSV FROM IN JAR
    }

    /**
     * Used to make sure load and save buttons are only enabled when they should be
     * @param keyEvent
     * @author ahf, KD
     */
    @FXML
    public void handleFileNameType(KeyEvent keyEvent) {
        errorMessageLabel.setText("");
        errorMessageLabel.setStyle("-fx-text-fill: black");

        //TODO: better checking
        final boolean disableBtns = !filenameField.getText().endsWith(".csv");

        saveToFileButton.setDisable(disableBtns);
       // loadFromFileButton.setDisable(disableBtns); //FIXME: ENABLE WHEN WE ADD A WAY TO LOAD CSV FROM IN JAR
        loadFromFileButton.setDisable(false); //FIXME: REM WHEN ABOVE CONDITION IS MET.

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
        String source = "";
        switch(floor){
            case "1": source = "/maps/01_thefirstfloor.png"; break;
            case "2": source = "/maps/02_thesecondfloor.png"; break;
            case "3": source = "/maps/03_thethirdfloor.png"; break;
            case "L1": source = "/maps/00_thelowerlevel1.png"; break;
            case "L2": source = "/maps/00_thelowerlevel2.png"; break;
            case "G": source = "/maps/00_thegroundfloor.png"; break;
        }
        Image image = new Image(getClass().getResourceAsStream(source));
        map.setImage(image);
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
        Circle c = new Circle(x, y, 7.0);
        c.setFill(Color.BLUE);
        c.setOnMouseEntered(e->{c.setFill(Color.RED);});
        c.setOnMouseExited(e->{c.setFill(Color.BLUE);});
        c.setOnMouseClicked(e->{nodeTable.getSelectionModel().clearAndSelect(findNode(nodeID));nodeTable.requestFocus();});

        this.canvas.getChildren().add(c);
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
}
