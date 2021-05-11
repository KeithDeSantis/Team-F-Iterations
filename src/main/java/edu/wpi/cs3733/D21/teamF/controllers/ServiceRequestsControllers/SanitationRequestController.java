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

public class SanitationRequestController extends ServiceRequests {
    @FXML private JFXTextField description;
    @FXML private JFXComboBox<String> loc;
    @FXML private JFXTextField clientEmail;

    @FXML
    private void initialize(){
        try {
            List<NodeEntry> nodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries();
            final ObservableList<String> nodeList = FXCollections.observableArrayList();
            for (NodeEntry e : nodeEntries){
                nodeList.add(e.getShortName());
            }
            this.loc.setItems(nodeList);

        } catch (Exception e) { }

    }

    public void handleSubmit(ActionEvent actionEvent) throws IOException, SQLException, MessagingException {
        if(formFilled()) {
            String uuid = UUID.randomUUID().toString();
            String type = "Sanitation Services";
            String person = "";
            String completed = "false";
            String additionalInfo = "Delivery Location: " + loc.getValue() + "Job Description: " + description.getText()
                    + "Email;" + clientEmail.getText();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, person, completed, additionalInfo);
            EmailHandler.getEmailHandler().sendEmail(additionalInfo.split(";")[1], "Service Request Confirmation",
                    "Hello,\nThis is a confirmation email for your service request " + type + " it will be completed as soon as possible");


            // Loads form submitted window and passes in current stage to return to request home
            openSuccessWindow();
        }
    }

    public boolean formFilled() {
        boolean isFilled = true;

        setNormalStyle(description, clientEmail, loc);

        if(description.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(description);
        }
        if(loc.getValue() == null){
            isFilled = false;
            setTextErrorStyle(loc);
        }
        if(clientEmail.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(clientEmail);
        }
        return isFilled;
    }

    @Override
    public void handleClear(){
        description.setText("");
        clientEmail.setText("");
        loc.setValue(null);
        setNormalStyle(description, clientEmail, loc);
    }

    public void handleHelp(ActionEvent e) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/SanitationHelpView.fxml");
    }

    public void goBack(ActionEvent actionEvent)throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/SanitationRequest.fxml");
    }

}
