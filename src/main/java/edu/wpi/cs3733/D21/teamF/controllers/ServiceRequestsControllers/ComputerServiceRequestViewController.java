package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.controllers.ServiceRequests;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class ComputerServiceRequestViewController extends ServiceRequests {

    @FXML
    private JFXTextField computerNameText;

    @FXML
    private JFXTextField computerLocationText;

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
        try {
            // Set up floor comboBox and draw nodes on that floor
            final ObservableList<String> urgencies = FXCollections.observableArrayList();
            urgencies.addAll(LOW_URGENCY, MEDIUM_URGENCY, HIGH_URGENCY);
            urgencyComboBox.setItems(urgencies);
        } catch(Exception e){}
    }

    @FXML
    public void handleSubmit(ActionEvent e) throws IOException, SQLException {
        if(formFilled())
        {
            String uuid = UUID.randomUUID().toString();
            String type = "Computer Service";
            String assignedPerson = "";
            String additionalInfo = "Computer name: " + computerNameText.getText() + "Computer location: " + computerLocationText.getText()
                    + "Urgency: " + urgencyComboBox.getValue() + "Requester: " + requesterTextText.getText();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, assignedPerson, "false", additionalInfo);
            openSuccessWindow();
        }
    }

    @FXML
    public void handleHelp(ActionEvent e) throws IOException{
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/ComputerServiceHelpView.fxml");
    }

    @FXML
    public void goBack(ActionEvent actionEvent)throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/ComputerServiceRequestView.fxml");
    }

    /**
     * Checks if our form has been filled out correctly and sets the components to use the proper style.
     * @return true if the form has been filled out validly; returns false otherwise.
     */
    public boolean formFilled()
    {
        boolean accept = true;

        //Clear old styles
        setNormalStyle(computerNameText, computerLocationText, requesterTextText, urgencyComboBox,descriptionText);


        if(computerNameText.getText().trim().isEmpty())
        {
            setTextErrorStyle(computerNameText);
            accept = false;
        }

        if(computerLocationText.getText().trim().isEmpty())
        {
            setTextErrorStyle(computerLocationText);
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

        setNormalStyle(computerNameText, computerLocationText, requesterTextText, urgencyComboBox,descriptionText);
        //FIXME: ADD WARNING
        computerNameText.setText("");
        computerLocationText.setText("");
        requesterTextText.setText("");

        urgencyComboBox.setValue(null);


         descriptionText.setText("");
    }




}
