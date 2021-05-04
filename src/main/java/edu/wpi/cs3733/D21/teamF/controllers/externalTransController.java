package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class externalTransController extends ServiceRequests{
    @FXML private JFXTextField patientName;
    @FXML private JFXComboBox<String> loc;
    @FXML private JFXTextField methodTrans;
    @FXML private JFXTextField special;


    @FXML
    public void initialize(){
        try{
            List<NodeEntry> nodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries();

            final ObservableList<String> nodeList = FXCollections.observableArrayList();
            for(NodeEntry n: nodeEntries){
                nodeList.add(n.getShortName());
            }
            this.loc.setItems(nodeList);

        } catch(Exception e){

        }
    }

    @FXML
    public void handleSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        if(formFilled()) {
            String uuid = UUID.randomUUID().toString();
            String type = "External Transit";
            String assignedPerson = "";
            String additionalInfo = "Location: " + loc.getValue() + "Transit method: " + methodTrans.getText()
                    + "Special info:" + special.getText();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, assignedPerson, "false", additionalInfo);
            // Loads form submitted window and passes in current stage to return to request home
            openSuccessWindow();
        }
    }

    @Override
    public boolean formFilled() {
        boolean isFilled = true;

        setNormalStyle(patientName, methodTrans, special, loc);
        if(patientName.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(patientName);
        }
        if(methodTrans.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(methodTrans);
        }
        if(loc.getValue() == null){
            isFilled = false;
            setTextErrorStyle(loc);
        }
        return isFilled;
    }

    @Override
    public void handleClear() {
        patientName.setText("");
        loc.setValue(null);
        methodTrans.setText("");
        special.setText("");
        setNormalStyle(patientName, loc, methodTrans, special);
    }
}
