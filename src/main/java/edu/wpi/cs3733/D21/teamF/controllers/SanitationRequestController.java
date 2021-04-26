package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.database.ConnectionHandler;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;

import edu.wpi.cs3733.D21.teamF.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamF.pathfinding.GraphLoader;
import edu.wpi.cs3733.D21.teamF.pathfinding.Vertex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.List;
import java.util.stream.Collectors;

public class SanitationRequestController {

    @FXML private JFXButton submit;
    @FXML private JFXButton cancel;
    @FXML private JFXTextArea description;
    @FXML private JFXComboBox<String> loc;
    @FXML private JFXComboBox<String> employeeAssigned;


    private void initialize(){
        Graph graph = new Graph();
        try {
            List<NodeEntry> nodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries(ConnectionHandler.getConnection());
        //    List<UserEntry> UserEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries(ConnectionHandler.getConnection());



            final ObservableList<String> nodeList = FXCollections.observableArrayList();
            nodeList.addAll(nodeEntries.stream().map(NodeEntry::getShortName)
                    .sorted().collect(Collectors.toList()));
            this.loc.setItems(nodeList);

        } catch (Exception e) {
            e.printStackTrace();
            //return;
        }

    }

    public void handleSubmit(ActionEvent actionEvent){



    }

}
