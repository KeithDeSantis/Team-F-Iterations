package edu.wpi.fuchsiafalcons.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.fuchsiafalcons.database.DatabaseAPI;
import edu.wpi.fuchsiafalcons.entities.NodeEntry;
import edu.wpi.fuchsiafalcons.entities.ServiceEntry;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MarkRequestsCompleteController implements Initializable {
    @FXML
    private JFXButton close;
    @FXML
    private JFXButton markAsComplete;
    @FXML
    private JFXButton home;
    @FXML
    private JFXTreeTableView<ServiceEntry> requestView;
    private ObservableList<ServiceEntry> services = FXCollections.observableArrayList();

    public void initialize(URL location, ResourceBundle resources) {
        int colWidth = 300;
        JFXTreeTableColumn<ServiceEntry, String> request = new JFXTreeTableColumn<>("Request Type");
        request.setPrefWidth(colWidth);
        request.setCellValueFactory(cellData -> cellData.getValue().getValue().getRequestTypeProperty());

        JFXTreeTableColumn<ServiceEntry, String> assign = new JFXTreeTableColumn<>("Assigned To");
        assign.setPrefWidth(colWidth);
        assign.setCellValueFactory(cellData -> cellData.getValue().getValue().getAssignedToProperty());

        JFXTreeTableColumn<ServiceEntry, String> status = new JFXTreeTableColumn<>("Status");
        status.setPrefWidth(colWidth);
        status.setCellValueFactory(cellData -> cellData.getValue().getValue().getCompleteStatusProperty());

        final TreeItem<ServiceEntry> root = new RecursiveTreeItem<ServiceEntry>(services, RecursiveTreeObject::getChildren);
        //JFXTreeTableView<ServiceEntry> requestView = new JFXTreeTableView<ServiceEntry>(root);
        requestView.setRoot(root);
        requestView.setShowRoot(false);
        requestView.getColumns().setAll(request, assign, status);

        ArrayList<ServiceEntry> data;
        try {
            data = DatabaseAPI.getDatabaseAPI().genServiceEntries();
            for (ServiceEntry e : data) {
                services.add(e);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        //add table entries like in account manager
        //syntax of adding item: services.add(new ServiceEntry("Request Type", "Assigned To", "Status));

    }

    public void handleClose(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void handleMarkAsComplete(ActionEvent actionEvent) throws SQLException, IOException {
        ServiceEntry data = requestView.getSelectionModel().getSelectedItem().getValue();
        DatabaseAPI.getDatabaseAPI().editServiceReq(data.getUuid(), "completed", "true");
        Stage currentStage = (Stage) markAsComplete.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/MarkRequestsCompleteView.fxml"));
        Scene homeScene = new Scene(root);
        currentStage.setScene(homeScene);
        currentStage.show();
    }

    public void handleHome(ActionEvent actionEvent) throws IOException {
        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage
        Stage stage;
        Parent root;
        stage = (Stage) buttonPushed.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/DefaultPageAdminView.fxml"));
        stage.getScene().setRoot(root);
        stage.setTitle("Admin Home");
        stage.show();
    }
}