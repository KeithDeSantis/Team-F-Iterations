package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;

public class PreferredPathfindingAlgoController implements IController {

    @FXML JFXComboBox<String> algorithmComboBox;
    @FXML Label title;
    @FXML JFXButton okButton;

    @FXML
    public void initialize() throws SQLException {
        ObservableList<String> algoList = FXCollections.observableArrayList();
        algoList.add("A Star");
        algoList.add("Breadth-First-Search");
        algoList.add("Depth-First-Search");
        algoList.add("Best-First-Search");
        algoList.add("Dijkstra");

        algorithmComboBox.setItems(algoList);
        algorithmComboBox.setValue(DatabaseAPI.getDatabaseAPI().getCurrentAlgorithm());
    }

    public void handleOkClicked() throws SQLException {
        Stage stage = (Stage) algorithmComboBox.getScene().getWindow();
        DatabaseAPI.getDatabaseAPI().deleteSystemPreference("MASTER");
        DatabaseAPI.getDatabaseAPI().addSystemPreferences("MASTER", algorithmComboBox.getValue());
        stage.close();
    }
}
