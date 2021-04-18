package edu.wpi.fuchsiafalcons.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.fuchsiafalcons.database.ConnectionHandler;
import edu.wpi.fuchsiafalcons.database.DatabaseAPI;
import edu.wpi.fuchsiafalcons.entities.EdgeEntry;
import edu.wpi.fuchsiafalcons.entities.NodeEntry;
import edu.wpi.fuchsiafalcons.utils.CSVManager;
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
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
    private JFXTreeTableView edgeTable;

    // Create an observable list of edges
    private ObservableList<EdgeEntry> edgeEntryObservableList = FXCollections.observableArrayList();

    @FXML
    private ScrollPane scroll;
    @FXML
    private ImageView map;

    /**
     * Initializes controller, called when EditMapEdges.fxml is loaded.
     *
     * @author Leo Morris
     */
    @FXML
    private void initialize() throws SQLException {
        // Clear the file error label
        CSVErrorLabel.setStyle("-fx-text-fill: black");
        CSVErrorLabel.setText("");

        // setup the map view
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        map.setPreserveRatio(true);
        final Image image = new Image(getClass().getResourceAsStream("/maps/01_thefirstfloor.png"));
        final double zoomLevel = 2.0;
        final double width = image.getWidth()/zoomLevel;
        final double height = image.getHeight()/zoomLevel;
        map.setFitWidth(width);
        map.setFitHeight(height);
        map.setImage(image); // Copied from EditMapEdges - LM

        // Set the save button to disabled by default (enabled by a valid file name being entered)
        saveCSV.setDisable(true);

        //FIXME: do better, hook into db

        final String sql = "CREATE TABLE L1Edges(EdgeID varchar(200), " +
                "startNode varchar(200), endNode varchar(200), " +
                "primary key(edgeID))";
        List<EdgeEntry> edgeList = new ArrayList<>();
        try {
            DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "L1Edges");
            DatabaseAPI.getDatabaseAPI().createTable(ConnectionHandler.getConnection(), sql);
            edgeList = DatabaseAPI.getDatabaseAPI().genEdgeEntries(ConnectionHandler.getConnection());
            DatabaseAPI.getDatabaseAPI().populateEdges(CSVManager.load("L1Edges.csv"));
//            edgeEntryObservableList.addAll( CSVManager.load("L1Edges.csv").stream().map(line-> {
//                return new EdgeEntry(line[0], line[1], line[2]);
//            }).collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set up cell factory for the edge ID table
        JFXTreeTableColumn<EdgeEntry, String> edgeIDColumn = new JFXTreeTableColumn<>("Edge ID");
        edgeIDColumn.setPrefWidth(250);
        edgeIDColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<EdgeEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<EdgeEntry, String> param) {
                return param.getValue().getValue().edgeIDProperty();
            }
        });
        for(EdgeEntry e: edgeList){
            edgeEntryObservableList.add(e);
        }

        final TreeItem<EdgeEntry> root = new RecursiveTreeItem<EdgeEntry>(edgeEntryObservableList, RecursiveTreeObject::getChildren);
        edgeTable.getColumns().setAll(edgeIDColumn);
        edgeTable.setRoot(root);
        edgeTable.setShowRoot(false);

        // Set initial selected edge to null
        showSelectedEdge(null);

        // Listen for selection changes on the table view
        //edgeTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> showSelectedEdge(newVwalue)));
    }

    /**
     * Called from listener on the table view.
     * Shows the selected edge on the map view.
     * @param edge the selected edge
     * @author Leo Morris
     */
    public void showSelectedEdge(EdgeEntry edge){
        //TODO show selected edge on Map
    }

    /**
     * Deletes the selected node from the table. Called when the delete button is pressed
     * Creates an alert pop-up if no edge is selected
     * @author Leo Morris
     */
    @FXML
    private void handleDelete() throws SQLException{
        // Get the current selected edge
        int index = edgeTable.getSelectionModel().getSelectedIndex();

        // Check for a valid index (-1 = no selection)
        if(index >= 0 && index <= edgeEntryObservableList.size() - 1){
            // Remove the edge, this will update the TableView automatically
            DatabaseAPI.getDatabaseAPI().deleteEdge(edgeEntryObservableList.get(index).getEdgeID());
            edgeEntryObservableList.remove(index);
        } else {
            // Create an alert to inform the user there is no edge selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null); // Appears on top of all other windows
            alert.setTitle("No Selection");
            alert.setHeaderText("No Edge Selected");
            alert.setContentText("Please select an edge from the list");

            alert.showAndWait();
        }
    }

    /**
     * Called when an edge is selected to be edited
     * @author Karen Hou
     */
    @FXML
    private void handleEditEdge() throws IOException, SQLException {
        int index = edgeTable.getSelectionModel().getSelectedIndex();
        EdgeEntry selectedEdge = edgeEntryObservableList.get(index);
        if(selectedEdge != null){
            String oldID = selectedEdge.getEdgeID();
            String oldStartNode = selectedEdge.getStartNode();
            String oldEndNode = selectedEdge.getEndNode();
            ArrayList<String> newValues = openEditDialogue(selectedEdge);
            while(!oldID.equals(newValues.get(0)) || !oldStartNode.equals(newValues.get(1)) || !oldEndNode.equals(newValues.get(2))) {
                DatabaseAPI.getDatabaseAPI().updateEntry("L1Edges", "edge", oldID, "Startnode", newValues.get(1));
                DatabaseAPI.getDatabaseAPI().updateEntry("L1Edges", "edge", oldID, "endnode", newValues.get(2));
                DatabaseAPI.getDatabaseAPI().updateEntry("L1Edges", "edge", oldID, "ID", newValues.get(0));
            }
            //DatabaseAPI.getDatabaseAPI().deleteEdge(selectedEdge.getEdgeID());
            //updateEdgeEntry(selectedEdge);
        }
    }

    /**
     * Called when an edge is to be created
     * @author Karen Hou
     */
    @FXML
    private void handleNewEdge() throws IOException, SQLException {
        EdgeEntry newEdge = new EdgeEntry();
        openEditDialogue(newEdge);
        if(newEdge.edgeIDProperty().getValue().isEmpty() || newEdge.startNodeProperty().getValue().isEmpty() ||
            newEdge.endNodeProperty().getValue().isEmpty())
            return; //FIXME: DO BETTER ERROR CHECKING
        updateEdgeEntry(newEdge);
    }

    private void updateEdgeEntry(EdgeEntry edgeEntry) throws SQLException {

        if(edgeEntry.getEdgeID().isEmpty() || edgeEntry.getStartNode().isEmpty() || edgeEntry.getEndNode().isEmpty())
            return; //FIXME: DO BETTER ERROR CHECKING, CHECK THAT WE ARE GETTING INTS

        String edgeID = edgeEntry.getEdgeID();
        String startNode = edgeEntry.getStartNode();
        String endNode = edgeEntry.getEndNode();

        edgeEntryObservableList.add(edgeEntry); // add the new node to the Observable list (which is linked to table and updates) - KD
        DatabaseAPI.getDatabaseAPI().addEdge(edgeID, startNode, endNode);
/*
        nodeTreeTable.requestFocus();
        nodeTreeTable.getSelectionModel().clearAndSelect(findNode(nodeID));
        nodeTreeTable.scrollTo(findNode(nodeID));
        selectNode();
        */
    }

    /**
     * Handles the pushing of a button on the screen
     * @param actionEvent the button's push
     * @throws IOException in case of scene switch, if the next fxml scene file cannot be found
     * @author ZheCheng Song
     */
    /*
    @FXML
    private void handleButtonPushed(ActionEvent actionEvent) throws IOException {

        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage
        Stage stage;
        Parent root;

        if (buttonPushed == goBack) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/DefaultPageView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Default Page");
            stage.show();
        }
    }
*/

    /**
     * Handles pressing of "HOME" button, replaces handleButtonPushed to remove some excess lines
     * @throws IOException
     * @author Leo Morris
     */
    @FXML
    private void handleHomeButton() throws IOException{
        Stage stage;
        Parent root;
        stage = (Stage) goBack.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/DefaultPageView.fxml"));
        stage.getScene().setRoot(root);
        stage.setTitle("Default Page");
        stage.show();
    }
    /**
     * Opens edit dialogue to edit edge
     * Written with code from KD
     * @param editedEdge is the edge being edited
     * @author Karen Hou
     */
    private ArrayList<String> openEditDialogue(EdgeEntry editedEdge) throws IOException{
        FXMLLoader editDialogueLoader = new FXMLLoader();
        editDialogueLoader.setLocation(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/EditMapEdgeDialogueView.fxml"));
        Stage dialogueStage = new Stage();
        Parent root = editDialogueLoader.load();
        EditMapEdgeDialogueViewController editDialogueController = editDialogueLoader.getController();

        editDialogueController.setDialogueStage(dialogueStage);
        editDialogueController.setEdge(editedEdge);

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
     * @param actionEvent Save button action
     * @author Leo Morris
     */
    public void handleSaveButton(ActionEvent actionEvent){
        final String fileName = CSVFile.getText();

        final List<String[]> data = new LinkedList<String[]>();
        Collections.addAll(data, "edgeID, startingNode, endingNode".split(","));

        data.addAll(edgeEntryObservableList.stream().map(edge->{
            return new String[] {
                    edge.edgeIDProperty().getValue(),
                    edge.startNodeProperty().getValue(),
                    edge.endNodeProperty().getValue()
            };
        }).collect(Collectors.toList()));
        try{
            CSVManager.writeFile(fileName, data);
        } catch (Exception e){
            CSVErrorLabel.setStyle("-fx-text-fill: red");
            CSVErrorLabel.setText(e.getMessage());
            e.printStackTrace();
            return;
        }

        CSVErrorLabel.setStyle("-fx-text-fill: black");
        CSVErrorLabel.setText("File successfully exported");

        CSVFile.setText("");
        saveCSV.setDisable(true);
    }

    /**
     * Disables the save and load buttons if the file name is invalid
     * @param keyEvent Calls on key release in the CSVFile TextField
     */
    @FXML
    public void handleFileNameType(KeyEvent keyEvent){
        CSVErrorLabel.setText("");
        CSVErrorLabel.setStyle("-fx-text-fill: black");

        final boolean disableBtns = !CSVFile.getText().endsWith(".csv");

        saveCSV.setDisable(disableBtns);
        loadCSV.setDisable(false); //FIXME: CHANGE CONDITION ONCE LOADING FROM JAR IS IMPLEMENTED
    }

    /**
     * Loads in the CSV file specified in the CSVFile TextField by the user
     * Written with code from KD and AHF
     * @param actionEvent Pressing the LOad CSV Button
     * @author Leo Morris
     */
    public void handleLoadButtonClicked(ActionEvent actionEvent){
        final String fileName = CSVFile.getText();

        edgeEntryObservableList.clear();

        List<String[]> edgeData = null;

        try {
            edgeData = (fileName == null || fileName.trim().isEmpty()) ? CSVManager.load("L1Edges.csv") : CSVManager.load(new File(fileName));
        } catch (Exception e){
            CSVErrorLabel.setStyle("-fx-text-fill: red");
            CSVErrorLabel.setText(e.getMessage());
            e.printStackTrace();
            return;
        }

        if(edgeData != null){
            if(!edgeData.isEmpty() && edgeData.get(0).length == 3) {
                edgeEntryObservableList.addAll(edgeData.stream().map(line-> {
                    return new EdgeEntry(line[0], line[1], line[2]);
                }).collect(Collectors.toList()));
            }
        }
    }

}
