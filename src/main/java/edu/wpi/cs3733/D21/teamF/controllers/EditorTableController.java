package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXTreeTableView;
import edu.wpi.cs3733.D21.teamF.entities.EdgeEntry;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class EditorTableController extends AbsController {

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab nodesTab;
    @FXML
    private Tab edgesTab;
    @FXML
    private JFXTreeTableView<NodeEntry> nodeTreeTable;
    @FXML
    private JFXTreeTableView<EdgeEntry> edgeTreeTable;

    private MapEditViewController realController;


    public void initialize() {
    }

    public void handleSelectNode() {
        realController.handleSelectNode();
    }

    public void setRealController(MapEditViewController cont) {
        this.realController = cont;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public Tab getNodesTab() {
        return nodesTab;
    }

    public Tab getEdgesTab() {
        return edgesTab;
    }

    public JFXTreeTableView<NodeEntry> getNodeTreeTable() {
        return nodeTreeTable;
    }

    public JFXTreeTableView<EdgeEntry> getEdgeTreeTable() {
        return edgeTreeTable;
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public void setNodesTab(Tab nodesTab) {
        this.nodesTab = nodesTab;
    }

    public void setEdgesTab(Tab edgesTab) {
        this.edgesTab = edgesTab;
    }

    public void setNodeTreeTable(JFXTreeTableView<NodeEntry> nodeTreeTable) {
        this.nodeTreeTable = nodeTreeTable;
    }

    public void setEdgeTreeTable(JFXTreeTableView<EdgeEntry> edgeTreeTable) {
        this.edgeTreeTable = edgeTreeTable;
    }

    public void handleSelectEdge() {
        realController.handleSelectEdge();
    }
}
