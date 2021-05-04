package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
<<<<<<< HEAD
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
=======
>>>>>>> 196c5398c9c08344af1e86b7ba9f448e69095966
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ComputerServiceRequestViewController extends ServiceRequests{

    @FXML
    private JFXTextField computerNameText;

    @FXML
    private JFXComboBox<String> computerLocation;

    @FXML
    private JFXTextField requesterTextText;

    @FXML
    private JFXComboBox<String> urgencyComboBox;

    @FXML
    private JFXTextArea descriptionText;


    private static final String LOW_URGENCY = "Low (fix when possible)";
    private static final String MEDIUM_URGENCY = "Medium (fix soon)";
    private static final String HIGH_URGENCY = "High (fix ASAP)";

    @FXML
    public void initialize(){
        // Set up floor comboBox and draw nodes on that floor
        final ObservableList<String> urgencies = FXCollections.observableArrayList();
        urgencies.addAll(LOW_URGENCY, MEDIUM_URGENCY, HIGH_URGENCY);
        urgencyComboBox.setItems(urgencies);

        try{
            List<NodeEntry> nodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries();

            final ObservableList<String> nodeList = FXCollections.observableArrayList();
            for(NodeEntry n: nodeEntries){
                nodeList.add(n.getShortName());
            }
            this.computerLocation.setItems(nodeList);

        } catch(Exception e){

        }
    }

    @FXML
    public void handleSubmit(ActionEvent e) throws IOException, SQLException {
        if(formFilled())
        {
            String uuid = UUID.randomUUID().toString();
            String type = "Computer Service";
            String assignedPerson = "";
            String additionalInfo = "Computer name: " + computerNameText.getText() + "Computer location: " + computerLocation.getValue()
                    + "Urgency: " + urgencyComboBox.getValue() + "Requester: " + requesterTextText.getText();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, assignedPerson, "false", additionalInfo);
            openSuccessWindow();
        }
    }

    /**
     * Checks if our form has been filled out correctly and sets the components to use the proper style.
     * @return true if the form has been filled out validly; returns false otherwise.
     */
    public boolean formFilled()
    {
        boolean accept = true;

        //Clear old styles
        setNormalStyle(computerNameText, computerLocation, requesterTextText, urgencyComboBox,descriptionText);


        if(computerNameText.getText().trim().isEmpty())
        {
            setTextErrorStyle(computerNameText);
            accept = false;
        }

        if(computerLocation.getValue().trim().isEmpty())
        {
            setTextErrorStyle(computerLocation);
            accept = false;
        }

        if(requesterTextText.getText().trim().isEmpty())
        {
            setTextErrorStyle(requesterTextText);
            accept = false;
        }

        if(urgencyComboBox.getValue() == null)
        {
            setTextErrorStyle(urgencyComboBox);
            accept = false;
        }

        if(descriptionText.getText().trim().isEmpty())
        {
            setTextErrorStyle(descriptionText);
            accept = false;
        }

        return accept;
    }


    @FXML
    public void handleClear() {

        setNormalStyle(computerNameText, computerLocation, requesterTextText, urgencyComboBox,descriptionText);
        computerNameText.setText("");
        computerLocation.setValue(null);
        requesterTextText.setText("");

        urgencyComboBox.setValue(null);

        descriptionText.setText("");
    }




}
