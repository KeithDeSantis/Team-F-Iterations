package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.SQLException;

public class PreferedPathfindingAlgoController {

    @FXML JFXComboBox<String> algorithmComboBox;
    @FXML Label title;
    @FXML JFXButton okButton;

    @FXML
    public void initialize() throws SQLException {
        ObservableList<String> algoList = FXCollections.observableArrayList();
        algoList.add("A Star");
        algoList.add("Breadth-First-Search");
        algoList.add("Depth-First-Search");
        algoList.add("Best-first-search");
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

    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #F0C808; -fx-text-fill: #000000");
    }

    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #03256C; -fx-text-fill: #FFFFFF");
    }
}
