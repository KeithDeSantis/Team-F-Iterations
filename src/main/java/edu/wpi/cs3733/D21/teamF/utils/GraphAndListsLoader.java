package edu.wpi.cs3733.D21.teamF.utils;

import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.database.EdgeHandler;
import edu.wpi.cs3733.D21.teamF.database.NodeHandler;
import edu.wpi.cs3733.D21.teamF.entities.EdgeEntry;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamF.pathfinding.GraphLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GraphAndListsLoader {

    private Graph graph;

    private ObservableList<EdgeEntry> edgeEntryObservableList = FXCollections.observableArrayList();
    private ObservableList<NodeEntry> nodeEntryObservableList = FXCollections.observableArrayList();

    private GraphAndListsLoader() {
        // Node initialization
        List<NodeEntry> data = new ArrayList<>();
        try {
            NodeHandler newNodeHandler = new NodeHandler();
            data = newNodeHandler.genNodeEntryObjects();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        data.stream().sorted(Comparator.comparing(NodeEntry::getNodeID)).collect(Collectors.toList()).forEach(node -> {
            nodeEntryObservableList.add(node);
        });

        // Edge initialization
        List<EdgeEntry> edgeData = new ArrayList<>();
        try {
            EdgeHandler newEdgeHandler = new EdgeHandler();
            edgeData = newEdgeHandler.genEdgeEntryObjects();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        edgeEntryObservableList.addAll(edgeData.stream().sorted(Comparator.comparing(EdgeEntry::getEdgeID)).collect(Collectors.toList()));

        // Graph initialization
        try {
            this.graph = GraphLoader.load(DatabaseAPI.getDatabaseAPI().genNodeEntries(), DatabaseAPI.getDatabaseAPI().genEdgeEntries());
        } catch (Exception e) {
            this.graph = new Graph();
            e.printStackTrace();
        }
    }

    private static class GraphAndListLoaderSingletonHelper {
        private static final GraphAndListsLoader graphAndListsLoader = new GraphAndListsLoader();
    }

    public static GraphAndListsLoader getGraphAndListsLoader() {
        return GraphAndListLoaderSingletonHelper.graphAndListsLoader;
    }

    public void reloadGraph() {
        try {
            this.graph = GraphLoader.load(DatabaseAPI.getDatabaseAPI().genNodeEntries(), DatabaseAPI.getDatabaseAPI().genEdgeEntries());
        } catch (Exception e) {
            this.graph = new Graph();
            e.printStackTrace();
        }
    }

    /*
    public void loadFromDB() {
        // Node initialization
        List<NodeEntry> data = new ArrayList<>();
        try {
            NodeHandler newNodeHandler = new NodeHandler();
            data = newNodeHandler.genNodeEntryObjects();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        data.stream().sorted(Comparator.comparing(NodeEntry::getNodeID)).collect(Collectors.toList()).forEach(node -> {
            nodeEntryObservableList.add(node);
        });

        // Edge initialization
        List<EdgeEntry> edgeData = new ArrayList<>();
        try {
            EdgeHandler newEdgeHandler = new EdgeHandler();
            edgeData = newEdgeHandler.genEdgeEntryObjects();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        edgeEntryObservableList.addAll(edgeData.stream().sorted(Comparator.comparing(EdgeEntry::getEdgeID)).collect(Collectors.toList()));

        // Graph initialization
        try {
            this.graph = GraphLoader.load(DatabaseAPI.getDatabaseAPI().genNodeEntries(), DatabaseAPI.getDatabaseAPI().genEdgeEntries());
        } catch (Exception e) {
            this.graph = new Graph();
            e.printStackTrace();
        }
    }
     */

    public Graph getGraph() {
        return graph;
    }

    public ObservableList<EdgeEntry> getEdgeEntryObservableList() {
        return edgeEntryObservableList;
    }

    public ObservableList<NodeEntry> getNodeEntryObservableList() {
        return nodeEntryObservableList;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setEdgeEntryObservableList(ObservableList<EdgeEntry> edgeEntryObservableList) {
        this.edgeEntryObservableList = edgeEntryObservableList;
    }

    public void setNodeEntryObservableList(ObservableList<NodeEntry> nodeEntryObservableList) {
        this.nodeEntryObservableList = nodeEntryObservableList;
    }
}
