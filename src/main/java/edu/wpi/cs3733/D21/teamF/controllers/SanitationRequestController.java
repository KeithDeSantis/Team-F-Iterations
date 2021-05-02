package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SanitationRequestController extends ServiceRequests {
    @FXML private JFXButton cancel;
    @FXML private JFXTextField description;
    @FXML private JFXComboBox<String> loc;
    @FXML private JFXTextField clientName;

    @FXML
    private void initialize(){
        try {
            List<NodeEntry> nodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries();
            final ObservableList<String> nodeList = FXCollections.observableArrayList();
            for (NodeEntry e : nodeEntries){
                nodeList.add(e.getShortName());
            }
            this.loc.setItems(nodeList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        if(formFilled()) {
            String uuid = UUID.randomUUID().toString();
            String type = "Sanitation Services";
            String person = "";
            String completed = "false";
            String additionalInfo = "Delivery Location: " + loc.getValue() + "Job Description: " + description.getText();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, person, completed, additionalInfo);

            // Loads form submitted window and passes in current stage to return to request home
            openSuccessWindow();
        }
    }
    public void handleClear(ActionEvent actionEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/SanitationRequest.fxml");

    }
    public void handleCancel(ActionEvent actionEvent) throws IOException{
        if(/*user is admin*/) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml");
        }
        else if (/*user is employee*/){
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageEmployeeView.fxml");
        }
        else{
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
        }
    }
    public boolean formFilled() {
        return description.getText().length()>0 && loc.getValue().length()>0 && clientName.getText().length()>0;
    }



}
