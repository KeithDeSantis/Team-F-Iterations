package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;


import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.controllers.ServiceRequests;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class InternalTransportationController extends ServiceRequests {

    @FXML private JFXComboBox<String> deliverLocation;

    @FXML private JFXDatePicker movingDate;

    @FXML private JFXTextField email;

    @FXML private JFXComboBox<String> patientRoom;

    @FXML private JFXCheckBox relativesCheckBox;

    @FXML private JFXCheckBox doctorCheckBox;

    @FXML
    public void initialize(){
        //cancel.setDisableVisualFocus(true); // Clears visual focus from cancel button, cause unknown - LM

        try{
            List<NodeEntry> nodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries();

            final ObservableList<String> nodeList = FXCollections.observableArrayList();
            for(NodeEntry n: nodeEntries){
                nodeList.add(n.getShortName());
            }
            this.patientRoom.setItems(nodeList);
            this.deliverLocation.setItems(nodeList);

        } catch(Exception e){ }
    }

    public boolean formFilled() {

        boolean isFilled = true;
        setNormalStyle(deliverLocation, movingDate, email, patientRoom);

        if(deliverLocation.getValue() == null) {
            isFilled = false;
            setTextErrorStyle(deliverLocation);
        }
        if(movingDate.getValue() == null) {
            isFilled = false;
            setTextErrorStyle(movingDate);
        }
//        if(movingTime.getValue() == null) {
//            isFilled = false;
//            setTextErrorStyle(movingTime);
//        }
        if(email.getText().length() <= 0) {
            isFilled = false;
            setTextErrorStyle(email);
        }
        if(patientRoom.getValue() == null) {
            isFilled = false;
            setTextErrorStyle(patientRoom);
        }

        return isFilled;
    }

    public void handleSubmit(ActionEvent e) throws IOException, SQLException {
        if(formFilled()) // form is complete
        {
            String uuid = UUID.randomUUID().toString();
            String type = "Internal Transport";
            String person = "";
            String completed = "false";
            String additionalInfo = "Delivery Location: " + deliverLocation.getValue() + "Delivery Date: " + movingDate.getValue()
                    + "Patient Room: " + patientRoom.getValue() + " Email;" + email.getText();

            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, person, completed, additionalInfo);

            // Loads form submitted window and passes in current stage to return to request home
            openSuccessWindow();
        }
    }


    public void handleClear() {
        deliverLocation.setValue(null);
        movingDate.setValue(null);
        email.setText("");
        patientRoom.setValue(null);
        relativesCheckBox.setSelected(false);
        doctorCheckBox.setSelected(false);
        setNormalStyle(deliverLocation, movingDate, email, patientRoom, relativesCheckBox, doctorCheckBox);
    }

    public void handleHelp(ActionEvent actionEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/InternalTransportationHelpView.fxml");
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/InternalTransportationView.fxml");
    }

}
