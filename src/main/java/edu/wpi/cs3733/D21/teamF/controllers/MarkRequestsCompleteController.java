package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.database.UserHandler;
import edu.wpi.cs3733.D21.teamF.entities.ServiceEntry;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class MarkRequestsCompleteController implements Initializable {
    @FXML
    private JFXButton close;
    @FXML
    private JFXButton markAsComplete;
    @FXML
    private JFXButton home;
    @FXML private ComboBox<String> employeeDropDown;
    @FXML
    private JFXTreeTableView<ServiceEntry> requestView;
    private ObservableList<ServiceEntry> services = FXCollections.observableArrayList();

    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #F0C808; -fx-text-fill: #000000;");
    }

    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #03256C; -fx-text-fill: #FFFFFF;");
    }
    public void initialize(URL location, ResourceBundle resources) {
        //TreeTable
        List<ServiceEntry> data;
        try {
            data = DatabaseAPI.getDatabaseAPI().genServiceRequestEntries();
            for (ServiceEntry e : data) {
                services.add(e);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        int colWidth = 200;
        JFXTreeTableColumn<ServiceEntry, String> request = new JFXTreeTableColumn<>("Request Type");
        request.setPrefWidth(colWidth);
        request.setCellValueFactory(cellData -> cellData.getValue().getValue().getRequestTypeProperty());

        JFXTreeTableColumn<ServiceEntry, String> assign = new JFXTreeTableColumn<>("Assigned To");
        assign.setPrefWidth(colWidth);
        assign.setCellValueFactory(cellData -> cellData.getValue().getValue().getAssignedToProperty());

        JFXTreeTableColumn<ServiceEntry, String> status = new JFXTreeTableColumn<>("Status");
        status.setPrefWidth(colWidth);
        status.setCellValueFactory(cellData -> cellData.getValue().getValue().getCompleteStatusProperty());

        JFXTreeTableColumn<ServiceEntry, String> additionalInstructions = new JFXTreeTableColumn<>("Additional Details");
        additionalInstructions.setPrefWidth(colWidth);
        additionalInstructions.setCellValueFactory(cellData -> cellData.getValue().getValue().getAdditionalInstructionsProperty());

        /*
        //employeeDropDown
        List<String> employees;
        try {
            UserHandler userHandler = new UserHandler();
            //get all users who are employees
            // employees = userHandler.listAllUsers();
            for (String s : employees)
            {
                employeeDropDown.getItems().add(s);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

         */

        ObservableList<String> testList = FXCollections.observableArrayList();
        testList.add("Keith");
        testList.add("Jay");
        testList.add("Yo");

        assign.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(testList));
        assign.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<ServiceEntry, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<ServiceEntry, String> event) {

            }
        });



        final TreeItem<ServiceEntry> root = new RecursiveTreeItem<ServiceEntry>(services, RecursiveTreeObject::getChildren);
        //JFXTreeTableView<ServiceEntry> requestView = new JFXTreeTableView<ServiceEntry>(root);
        requestView.setEditable(true);
        requestView.setRoot(root);
        requestView.setShowRoot(false);
        requestView.getColumns().setAll(request, assign, status, additionalInstructions);



    }


    public void editingState (MouseEvent mouseEvent){
        Cell editingCell = new Cell();
        editingCell.startEdit();
        editingCell.setText("");
        editingCell.setGraphic(employeeDropDown);
    }
    public void handleClose(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void handleMarkAsComplete(ActionEvent actionEvent) throws Exception {
        ServiceEntry data = requestView.getSelectionModel().getSelectedItem().getValue();
        DatabaseAPI.getDatabaseAPI().editServiceRequest(data.getUuid(),  "true", "completed");
        Stage currentStage = (Stage) markAsComplete.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/MarkRequestsCompleteView.fxml"));
        Scene homeScene = new Scene(root);
        currentStage.setScene(homeScene);
        currentStage.show();
    }

    public void handleHome(ActionEvent actionEvent) throws IOException {
        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage
        Stage stage;
        Parent root;
        stage = (Stage) buttonPushed.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml"));
        stage.getScene().setRoot(root);
        stage.setTitle("Admin Home");
        stage.show();
    }
}
