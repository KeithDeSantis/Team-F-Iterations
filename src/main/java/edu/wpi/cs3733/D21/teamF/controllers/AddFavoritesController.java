package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddFavoritesController {
    @FXML
    private Label editFavoritesList;
    @FXML
    private JFXListView<NodeEntry> favoritesTreeList;
    @FXML
    private JFXButton exit;
    @FXML
    private JFXButton add;
    @FXML
    private JFXButton remove;
    @FXML
    private JFXComboBox<String> newFavoriteComboBox;

    private final ObservableList<NodeEntry> nodeList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        List<NodeEntry> data = new ArrayList<>();
        try {
            data = DatabaseAPI.getDatabaseAPI().genNodeEntries(); //change later
        } catch (SQLException e){
            e.printStackTrace();
        }
        nodeList.addAll(data);
        updateList();
    }

    @FXML
    private void favoriteClicked() {
        if(favoritesTreeList.getSelectionModel().getSelectedIndex() < 0) {
            return;
        }
        NodeEntry node = favoritesTreeList.getSelectionModel().getSelectedItem();
        if(node == null){
            return;
        }
    }

    /**
     * Closes this window
     */
    @FXML
    private void exit() {
        ((Stage) exit.getScene().getWindow()).close();
    }

    /**
     * Adds a NodeEntry to the list
     */
    @FXML
    private void add() {
        NodeEntry nodeEntry = new NodeEntry();
        nodeEntry.setNodeID(newFavoriteComboBox.getSelectionModel().getSelectedItem());
        nodeList.add(nodeEntry);
        updateList();
    }

    /**
     * Removes an item from the tree list
     * @author Tony Vuolo (bdane)
     */
    @FXML
    private void remove() {
        int index = favoritesTreeList.getSelectionModel().getSelectedIndex();
        if(index >= 0) {
            nodeList.remove(index);
        }
        updateList();
    }

    /**
     * Updates the tree list
     * @author Tony Vuolo (bdane)
     */
    private void updateList() {
        favoritesTreeList = new JFXListView<>();
        for(NodeEntry nodeEntry : nodeList) {
            favoritesTreeList.getItems().add(nodeEntry);
        }
    }

    /**
     * Resets selection in combo box
     * @author Tony Vuolo (bdane)
     */
    @FXML
    public void newFavoriteComboBox() {
        for(NodeEntry nodeEntry : favoritesTreeList.getItems()) {
            if(nodeEntry.getNodeID().equals(newFavoriteComboBox.getSelectionModel().getSelectedItem())) {

                return;
            }
        }
    }
}
