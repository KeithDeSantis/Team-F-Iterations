package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.AccountEntry;
import edu.wpi.cs3733.D21.teamF.entities.ServiceEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
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
    private JFXButton assignButton;
    @FXML
    private JFXButton cancel;
     @FXML
    private JFXButton home;
    @FXML private ComboBox<String> employeeDropDown;
    @FXML
    private JFXTreeTableView<ServiceEntry> requestView;
    private ObservableList<ServiceEntry> services = FXCollections.observableArrayList();
    private String selectedPerson;

    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #F0C808; -fx-text-fill: #000000;");
    }

    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #03256C; -fx-text-fill: #FFFFFF;");
    }

    public void handleHoverOnCancel(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #d30000;");
    }

    public void handleHoverOffCancel(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #d30000; -fx-text-fill: #FFFFFF;");
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

        ObservableList<String> employees = FXCollections.observableArrayList();
        List<AccountEntry> userData = null;
        try {
            userData = DatabaseAPI.getDatabaseAPI().genAccountEntries();
        } catch (SQLException e) {

        }
        if (userData != null){
            for (AccountEntry a : userData){
                if (a.getUserType().equals("employee")){
                    employees.add(a.getUsername());
                }
            }
        }

        assign.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(employees));
        assign.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<ServiceEntry, String>>() {
            @Override
                public void handle(TreeTableColumn.CellEditEvent<ServiceEntry, String> t) {
                     t.getRowValue().getValue().setAssignedTo(t.getNewValue());
                }
        });

        final TreeItem<ServiceEntry> root = new RecursiveTreeItem<ServiceEntry>(services, RecursiveTreeObject::getChildren);
        //JFXTreeTableView<ServiceEntry> requestView = new JFXTreeTableView<ServiceEntry>(root);
        requestView.setEditable(true);
        requestView.setRoot(root);
        requestView.setShowRoot(false);
        requestView.getColumns().setAll(request, assign, status, additionalInstructions);
    }


    public void handleClose(ActionEvent actionEvent) throws IOException{
        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage
        Stage stage;
        Parent root;
        stage = (Stage) buttonPushed.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml"));
        stage.getScene().setRoot(root);
        stage.setTitle("Admin Home");
        stage.show();
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

    public void handleAssign(ActionEvent actionEvent) throws Exception {
        //ServiceEntry data = requestView.getSelectionModel().getSelectedItem().getValue();
        for (ServiceEntry s:services){
            DatabaseAPI.getDatabaseAPI().editServiceRequest(s.getUuid(), s.getAssignedTo(), "assignedperson");
        }
                Stage currentStage = (Stage) markAsComplete.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/MarkRequestsCompleteView.fxml"));
        Scene homeScene = new Scene(root);
        currentStage.setScene(homeScene);
        currentStage.show();
    }

    public void handleCancel(ActionEvent actionEvent) throws IOException{
        Stage currentStage = (Stage) markAsComplete.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/MarkRequestsCompleteView.fxml"));
        Scene homeScene = new Scene(root);
        currentStage.setScene(homeScene);
        currentStage.show();
    }
}
