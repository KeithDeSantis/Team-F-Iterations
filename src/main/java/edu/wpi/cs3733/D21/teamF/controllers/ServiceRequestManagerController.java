package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.AccountEntry;
import edu.wpi.cs3733.D21.teamF.entities.ServiceEntry;
import edu.wpi.cs3733.D21.teamF.utils.EmailHandler;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.ComboBoxTreeTableCell;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ServiceRequestManagerController extends AbsController implements Initializable {
    @FXML private JFXButton markAsComplete;
    @FXML private JFXButton saveChanges;
    @FXML private JFXButton removeAssignment;
    @FXML private JFXTreeTableView<ServiceEntry> requestView;
    @FXML private JFXButton delete;

    private final ObservableList<ServiceEntry> services = FXCollections.observableArrayList();
    private ServiceEntry selectedEntry;
    private int index;

    public void initialize(URL location, ResourceBundle resources) {
        //TreeTable
        List<ServiceEntry> data;
        try {
            data = DatabaseAPI.getDatabaseAPI().genServiceRequestEntries();
            services.addAll(data);
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

        JFXTreeTableColumn<ServiceEntry, String> status = new JFXTreeTableColumn<>("Completed/Cleared");
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
        assign.setOnEditCommit(t -> {
                 t.getRowValue().getValue().setAssignedTo(t.getNewValue());
                 saveChanges.setDisable(false);
                 removeAssignment.setDisable(false);
            });

        final TreeItem<ServiceEntry> root = new RecursiveTreeItem<>(services, RecursiveTreeObject::getChildren);
        //JFXTreeTableView<ServiceEntry> requestView = new JFXTreeTableView<ServiceEntry>(root);
        requestView.setEditable(true);
        requestView.setRoot(root);
        requestView.setShowRoot(false);
        requestView.getColumns().setAll(request, assign, status, additionalInstructions);

        // Disable buttons by default
        markAsComplete.setDisable(true);
        saveChanges.setDisable(true);
        removeAssignment.setDisable(true);
        delete.setDisable(true);
    }


    public void handleHome() throws IOException{
        SceneContext.getSceneContext().loadDefault();
    }

    /**
     * Marks assignments as complete and updates database
     * @throws Exception
     * @author Leo Morris
     */
    public void handleMarkAsComplete() throws Exception {
        // Updates data base with the new completed status (toggles from current)
//        DatabaseAPI.getDatabaseAPI().editServiceRequest(selectedEntry.getUuid(),
//                String.valueOf(!Boolean.parseBoolean(selectedEntry.getCompleteStatus())), "completed");
        String newBoolean;
        if(selectedEntry.getCompleteStatus().equals("true")){
            newBoolean = "false";
        }
        else if (selectedEntry.getCompleteStatus().equals("false")){
            newBoolean = "true";
        }
        else{
            newBoolean = "true";
        }
        DatabaseAPI.getDatabaseAPI().editServiceRequest(selectedEntry.getUuid(), newBoolean, "completed");
        if (newBoolean.equals("true")){
            EmailHandler.getEmailHandler().sendEmail(selectedEntry.getAdditionalInstructions().split(":")[1],
                    "Your Covid-19 clearance level has been updated!", "Please check your status with the application" +
                    " your ticket number is: " + selectedEntry.getAdditionalInstructions().split(":")[0]);
        }
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
            services.addAll(data);
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
     * Delete a service request from the DB
     * @throws SQLException
     * @author KD
     */
    public void handleDelete() throws SQLException {
        DatabaseAPI.getDatabaseAPI().deleteServiceRequest(selectedEntry.getUuid());
        services.remove(selectedEntry);
        delete.setDisable(true);
    }

    /**
     * Handles when a selection is made on the tree table view
     * @author Leo Morris
     */
    public void handleSelection(){
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
            delete.setDisable(true);
            index = -1;
        }

        delete.setDisable(false);

        // Enable mark complete button and set appropriate text
        switch (selectedEntry.getCompleteStatus()) {
            case "false":
            case "true":
            case "":
                markAsComplete.setText("Toggle Status");
                markAsComplete.setDisable(false);
                break;
        }

        // Enable remove assignment button if a person is assigned
        removeAssignment.setDisable(selectedEntry.getAssignedTo().isEmpty());
    }

}
