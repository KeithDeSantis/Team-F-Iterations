package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.D21.teamF.database.NodeHandler;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FavoriteNodeSelectionController {

    @FXML private JFXButton addButton;
    @FXML private JFXButton removeButton;
    @FXML private JFXButton clearButton;
    @FXML private JFXTreeTableView<NodeEntry> allNodeView;
    @FXML private JFXTreeTableView<NodeEntry> favoriteNodeView;
    private ObservableList<NodeEntry> allNodesList = FXCollections.observableArrayList();
    private ObservableList<NodeEntry> favoriteNodesList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        List<NodeEntry> data = new ArrayList<>();
        try {
            NodeHandler newNodeHandler = new NodeHandler();
            data = newNodeHandler.genNodeEntryObjects();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        data.stream().sorted(Comparator.comparing(NodeEntry::getNodeID)).collect(Collectors.toList()).forEach(node -> {
            allNodesList.add(node);
        });

        //TODO load in favorites

        int colWidth = 199;
        JFXTreeTableColumn<NodeEntry, String> idColumn = new JFXTreeTableColumn<>("Node ID");
        idColumn.setPrefWidth(colWidth);
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().getNodeIDProperty());
        JFXTreeTableColumn<NodeEntry, String> shortColumn = new JFXTreeTableColumn<>("Name");
        shortColumn.setPrefWidth(colWidth);
        shortColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().getShortNameProperty());
        final TreeItem<NodeEntry> root = new RecursiveTreeItem<>(allNodesList, RecursiveTreeObject::getChildren);
        allNodeView.setRoot(root);
        allNodeView.setShowRoot(false);
        allNodeView.getColumns().setAll(idColumn, shortColumn);

        JFXTreeTableColumn<NodeEntry, String> favIdColumn = new JFXTreeTableColumn<>("Favorite Node ID");
        favIdColumn.setPrefWidth(colWidth);
        favIdColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().getNodeIDProperty());
        JFXTreeTableColumn<NodeEntry, String> favShortColumn = new JFXTreeTableColumn<>("Name");
        favShortColumn.setPrefWidth(colWidth);
        favShortColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().getShortNameProperty());
        final TreeItem<NodeEntry> favRoot = new RecursiveTreeItem<>(favoriteNodesList, RecursiveTreeObject::getChildren);
        favoriteNodeView.setRoot(favRoot);
        favoriteNodeView.setShowRoot(false);
        favoriteNodeView.getColumns().setAll(favIdColumn, favShortColumn);

    }

    public void handleAdd() {
        if(allNodeView.getSelectionModel().getSelectedIndex() < 0) return;
        if(!favoriteNodesList.contains(allNodeView.getSelectionModel().getSelectedItem().getValue()))
            favoriteNodesList.add(allNodeView.getSelectionModel().getSelectedItem().getValue());
        //TODO add to favorites in db
    }

    public void handleRemove() {
        if(favoriteNodeView.getSelectionModel().getSelectedIndex() < 0) return;
        favoriteNodesList.remove(favoriteNodeView.getSelectionModel().getSelectedItem().getValue());
        //TODO remove from favorites in db
    }

    public void handleClear() {
        favoriteNodesList.clear();
        //TODO clear favorites in db
    }

    public void handleHome() throws IOException {
        SceneContext.getSceneContext().loadDefault();
    }
}
