package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.Translation.Translator;
import edu.wpi.cs3733.D21.teamF.controllers.ServiceRequests;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.EmailHandler;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ComputerServiceRequestViewController extends ServiceRequests {

    @FXML
    private JFXTextField computerNameText;

    @FXML
    private JFXComboBox<String> computerLocation;

    @FXML
    private JFXTextField requesterEmailText;

    @FXML
    private JFXComboBox<StringProperty> urgencyComboBox;

    @FXML
    private JFXTextArea descriptionText;


    private static final String LOW_URGENCY = "Low";// (fix when possible)";
    private static final String MEDIUM_URGENCY = "Medium";// (fix soon)";
    private static final String HIGH_URGENCY = "High";// (fix ASAP)";

    @FXML
    public void initialize(){

        try {
            urgencyComboBox.setItems(Translator.getTranslator().getTranslationsFor(LOW_URGENCY, MEDIUM_URGENCY, HIGH_URGENCY));
            urgencyComboBox.setConverter(Translator.getTranslator().getTranslationStringConverter());
        } catch(Exception e){}

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
    public void handleSubmit(ActionEvent e) throws IOException, SQLException, MessagingException {
        if(formFilled())
        {
            String uuid = UUID.randomUUID().toString();
            String type = "Computer Service";
            String assignedPerson = "";
            String additionalInfo = "Computer name: " + computerNameText.getText() + "Computer location: " + computerLocation.getValue()
                    + "Urgency: " + urgencyComboBox.getValue() + "Requester Email;" + requesterEmailText.getText();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, assignedPerson, "false", additionalInfo);
            EmailHandler.getEmailHandler().sendEmail(additionalInfo.split(";")[1], "Service Request Confirmation",
                    "Hello,\nThis is a confirmation email for your service request " + type + " it will be completed as soon as possible");

            openSuccessWindow();
        }
    }

    @FXML
    public void handleHelp(ActionEvent e) throws IOException{
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/ComputerServiceHelpView.fxml");
    }

    /**
     * Checks if our form has been filled out correctly and sets the components to use the proper style.
     * @return true if the form has been filled out validly; returns false otherwise.
     */
    public boolean formFilled()
    {
        boolean accept = true;

        //Clear old styles
        setNormalStyle(computerNameText, computerLocation, requesterEmailText, urgencyComboBox,descriptionText);


        if(computerNameText.getText().trim().isEmpty())
        {
            setTextErrorStyle(computerNameText);
            accept = false;
        }

        if(computerLocation.getValue() == null)
        {
            setTextErrorStyle(computerLocation);
            accept = false;
        }

        if(requesterEmailText.getText().trim().isEmpty())
        {
            setTextErrorStyle(requesterEmailText);
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

        setNormalStyle(computerNameText, computerLocation, requesterEmailText, urgencyComboBox,descriptionText);
        computerNameText.setText("");
        computerLocation.setValue(null);
        requesterEmailText.setText("");

        urgencyComboBox.setValue(null);

        descriptionText.setText("");
    }




}
