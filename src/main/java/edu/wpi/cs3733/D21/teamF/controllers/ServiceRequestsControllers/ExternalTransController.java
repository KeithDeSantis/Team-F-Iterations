package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.controllers.ServiceRequests;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.EmailHandler;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


public class ExternalTransController extends ServiceRequests{
    @FXML private JFXTextField patientEmail;
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
    public void handleSubmit(ActionEvent actionEvent) throws IOException, SQLException, MessagingException {
        if(formFilled()) {
            String uuid = UUID.randomUUID().toString();
            String type = "External Transit";
            String assignedPerson = "";
            String additionalInfo = "Location: " + loc.getValue() + "Transit method: " + methodTrans.getText()
                    + "Special info:" + special.getText() + "Email;" + patientEmail.getText();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, assignedPerson, "false", additionalInfo);
            EmailHandler.getEmailHandler().sendEmail(additionalInfo.split(";")[1], "Service Request Confirmation",
                    "Hello,\nThis is a confirmation email for your service request " + type + " it will be completed as soon as possible");
            // Loads form submitted window and passes in current stage to return to request home
            openSuccessWindow();
        }
    }

    @Override
    public boolean formFilled() {
            boolean isFilled = true;
            setNormalStyle(patientEmail, methodTrans, special, loc);

        setNormalStyle(patientEmail, methodTrans, special, loc);
        if(patientEmail.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(patientEmail);
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
        patientEmail.setText("");
        loc.setValue(null);
        methodTrans.setText("");
        special.setText("");
        setNormalStyle(patientEmail, loc, methodTrans, special);
    }

    public void handleHelp(ActionEvent e) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/ExternalTransHelpView.fxml");
    }

    public void goBack(ActionEvent actionEvent)throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/ExternalTrans.fxml");
    }
}
