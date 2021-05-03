package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.controllers.ServiceRequests;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class SanitationRequestController extends ServiceRequests {
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

    public boolean formFilled() {
        boolean isFilled = true;

        setNormalStyle(description, clientName, loc);

        if(description.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(description);
        }
        if(loc.getValue() == null){
            isFilled = false;
            setTextErrorStyle(loc);
        }
        if(clientName.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(clientName);
        }
        return isFilled;
    }

    @Override
    public void handleClear(){
        description.setText("");
        clientName.setText("");
        loc.setValue(null);
        setNormalStyle(description, clientName, loc);
    }



}
