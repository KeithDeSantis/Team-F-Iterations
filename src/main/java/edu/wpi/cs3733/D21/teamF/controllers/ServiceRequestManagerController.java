package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.AccountEntry;
import edu.wpi.cs3733.D21.teamF.entities.ServiceEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ServiceRequestManagerController implements Initializable {
    @FXML private JFXButton markAsComplete;
    @FXML private JFXButton saveChanges;
    @FXML private JFXButton removeAssignment;
    @FXML private ImageView goBack;
    @FXML private JFXTreeTableView<ServiceEntry> requestView;

    private ObservableList<ServiceEntry> services = FXCollections.observableArrayList();
    private ServiceEntry selectedEntry;
    private int index;





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

        JFXTreeTableColumn<ServiceEntry, String> status = new JFXTreeTableColumn<>("Completed");
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
                     saveChanges.setDisable(false);
                     removeAssignment.setDisable(false);
                }
        });

        final TreeItem<ServiceEntry> root = new RecursiveTreeItem<ServiceEntry>(services, RecursiveTreeObject::getChildren);
        //JFXTreeTableView<ServiceEntry> requestView = new JFXTreeTableView<ServiceEntry>(root);
        requestView.setEditable(true);
        requestView.setRoot(root);
        requestView.setShowRoot(false);
        requestView.getColumns().setAll(request, assign, status, additionalInstructions);

        // Disable buttons by default
        markAsComplete.setDisable(true);
        saveChanges.setDisable(true);
        removeAssignment.setDisable(true);
    }


    public void handleHome(MouseEvent mouseEvent) throws IOException{
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml");
    }

    /**
     * Marks assignments as complete and updates database
     * @throws Exception
     * @author Leo Morris
     */
    public void handleMarkAsComplete() throws Exception {
        // Updates data base with the new completed status (toggles from current)
        DatabaseAPI.getDatabaseAPI().editServiceRequest(selectedEntry.getUuid(),
                String.valueOf(!Boolean.parseBoolean(selectedEntry.getCompleteStatus())), "completed");
        refreshTable();
    }

    /**
     * Refreshes requestTable by pulling from the database and reloading the list of requests.
     * After refresh the users selection is restored
     * @author Leo Morris
     */
    private void refreshTable() {
        // Updates table from newly modified DB
        services.clear();
        try {
            List<ServiceEntry> data = DatabaseAPI.getDatabaseAPI().genServiceRequestEntries();
            for (ServiceEntry e : data) {
                services.add(e);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Refreshes table and restores selection
        requestView.refresh();
        requestView.requestFocus();
        requestView.getSelectionModel().select(index);
        getSelection();
    }

    public void saveAssignments() throws Exception {
        //ServiceEntry data = requestView.getSelectionModel().getSelectedItem().getValue();
        for (ServiceEntry s:services){
            DatabaseAPI.getDatabaseAPI().editServiceRequest(s.getUuid(), s.getAssignedTo(), "assignedperson");
        }
        saveChanges.setDisable(true);
        refreshTable();
    }

    /**
     * Removes the assignment of a service request by setting the assigned person value to an empty string
     * @author Leo Morris
     */
    public void removeAssignment() throws Exception {
        DatabaseAPI.getDatabaseAPI().editServiceRequest(selectedEntry.getUuid(), "", "assignedperson");
        refreshTable();
    }

    /**
     * Handles when a selection is made on the tree table view
     * @param mouseEvent The event that triggers the method
     * @author Leo Morris
     */
    public void handleSelection(MouseEvent mouseEvent){
        getSelection();
    }

    /**
     * Called when tree table item is selected, enables relevant buttons in button bar based on data
     * @author Leo Morris
     */
    public void getSelection() {
        try {
            selectedEntry = requestView.getSelectionModel().getSelectedItem().getValue();
            index = requestView.getSelectionModel().getSelectedIndex();
        } catch (ArrayIndexOutOfBoundsException e){
            // If no selection, reset text and set relevant buttons to disabled
            markAsComplete.setText("Mark Complete");
            markAsComplete.setDisable(true);
            removeAssignment.setDisable(true);
            index = -1;
        }

        // Enable mark complete button and set appropriate text
        if(selectedEntry.getCompleteStatus().equals("false")){
            markAsComplete.setText("Mark Complete");
            markAsComplete.setDisable(false);
        } else if(selectedEntry.getCompleteStatus().equals("true")){
            markAsComplete.setText("Mark Incomplete");
            markAsComplete.setDisable(false);
        }

        // Enable remove assignment button if a person is assigned
        if(!selectedEntry.getAssignedTo().isEmpty()){
            removeAssignment.setDisable(false);
        } else{
            removeAssignment.setDisable(true);
        }

    }

}
