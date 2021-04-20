package edu.wpi.fuchsiafalcons.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class MarkRequestsCompleteController implements Initializable {
    @FXML
    private JFXButton close;
    @FXML
    private JFXButton markAsComplete;
    @FXML
    private JFXButton home;
    @FXML
    private JFXTreeTableView<Service> requestView;

    public void initialize(URL location, ResourceBundle resources) {
        //creating table columns
        JFXTreeTableView<service> treeTableView = new JFXTreeTableView<service>();

        JFXTreeTableColumn<service, String> type = new JFXTreeTableColumn<>("Request Type");
        type.setPrefWidth(300);
        type.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<service, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<service, String> param) {
                return param.getValue().getValue().requestType;
            }
        });
        JFXTreeTableColumn<service, String> assign = new JFXTreeTableColumn<>("Assigned To");
        assign.setPrefWidth(300);
        type.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<service, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<service, String> param) {
                return param.getValue().getValue().assignedTo;
            }
        });
        JFXTreeTableColumn<service, String> status = new JFXTreeTableColumn<>("Status");
        status.setPrefWidth(300);
        type.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<service, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<service, String> param) {
                return param.getValue().getValue().completeStatus;
            }
        });



        //assign.setCellValueFactory(new TreeItemPropertyValueFactory<>("Assigned To"));
        //status.setCellValueFactory(new TreeItemPropertyValueFactory<>("Status"));

        JFXTreeTableView.getColumns().add(type);
        JFXTreeTableView.getColumns().add(assign);
        JFXTreeTableView.getColumns().add(status);
        //add table entries like in account manager
        //syntax of adding item: TreeItem nameOfRequest = new TreeItem(new service("Request Type", "Assigned To", "Status));
    }

    public void handleClose(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void handleMarkAsComplete(ActionEvent actionEvent) {
        //change status to complete
        //isSelected(int rowIndex) is true if the row is selected

    }

    public void handleHome(ActionEvent actionEvent) {
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
