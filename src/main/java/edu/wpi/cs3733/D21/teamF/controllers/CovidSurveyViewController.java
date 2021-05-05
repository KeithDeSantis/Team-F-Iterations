package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.ServiceEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.UUID;

public class CovidSurveyViewController extends ServiceRequests implements Initializable {

    @FXML private HBox header;
    @FXML private Label title;
    @FXML private Label posTestPrompt;
    @FXML private Label fifteenPrompt;
    @FXML private Label tempPrompt;
    @FXML private Label symptomsPrompt;
    @FXML private JFXRadioButton yes1;
    @FXML private JFXRadioButton yes2;
    @FXML private JFXRadioButton no1;
    @FXML private JFXRadioButton no2;
    @FXML private JFXTextField temperatureField;
    @FXML private JFXCheckBox cough;
    @FXML private JFXCheckBox breathing;
    @FXML private JFXCheckBox fatigue;
    @FXML private JFXCheckBox aches;
    @FXML private JFXCheckBox headache;
    @FXML private JFXCheckBox lossOfTaste;
    @FXML private JFXCheckBox soreThroat;
    @FXML private JFXCheckBox congestion;
    @FXML private JFXCheckBox nausea;
    @FXML private JFXCheckBox diarrhea;
    @FXML private JFXCheckBox blueSkin;
    @FXML private JFXCheckBox pain;
    @FXML private JFXCheckBox confusion;
    @FXML private JFXCheckBox stayAwake;
    @FXML private JFXCheckBox fever;
    @FXML private JFXButton submit;
    @FXML private JFXButton cancel;
    @FXML private JFXButton employeeSignIn;
    @FXML private JFXTextField generatedID;


    /**
     * generates a UUID for the survey and displays it.
     */
    public void initialize(URL location, ResourceBundle resources){
        String ticketNumber = UUID.randomUUID().toString();
        generatedID.setText(ticketNumber);
    }

    /**
     * creates a service request and puts it in the database, then changes to the submitted view
     * @param e
     * @throws IOException
     */
    @FXML private void handleSubmitPushed(ActionEvent e) throws IOException, SQLException {
        if(formFilled()) {
            //create service request, put in database
            DatabaseAPI.getDatabaseAPI().addServiceReq(generatedID.getText(), "ticket", "", "false", "form details etc");
            ServiceEntry ticket = DatabaseAPI.getDatabaseAPI().getServiceEntry(generatedID.getText());
            //change view to survey submitted page
            FXMLLoader submittedPageLoader = new FXMLLoader();
            submittedPageLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/CovidFormSubmittedView.fxml"));
            Stage submittedStage = new Stage();
            Parent root = submittedPageLoader.load();
            CovidFormSubmittedViewController formSubmittedViewController = submittedPageLoader.getController();
            formSubmittedViewController.changeStage((Stage) posTestPrompt.getScene().getWindow());
            Scene submitScene = new Scene(root);
            submittedStage.setScene(submitScene);
            submittedStage.setTitle("Submission Complete");
            submittedStage.initModality(Modality.APPLICATION_MODAL);
            submittedStage.showAndWait();
        }
    }
    public boolean formFilled(){
        boolean complete = true;
        if (Integer.parseInt(temperatureField.getText())<70 || Integer.parseInt(temperatureField.getText())>115 || temperatureField.getText() == ""){
            setTextErrorStyle(temperatureField);
            complete = false;
        }
        return complete;
    }

    /**
     * groups radio buttons into the two questions so only one is selected at a time for each
     * @param e
     * @author kh
     */

    @FXML
    private void handleRadialButtonPushed(ActionEvent e){
        ToggleGroup question1 = new ToggleGroup(); //group for first question
        yes1.setToggleGroup(question1);
        no1.setToggleGroup(question1);

        ToggleGroup question2 = new ToggleGroup(); //group for second question
        yes2.setToggleGroup(question2);
        no2.setToggleGroup(question2);
    }

    /**
     * allows employees and admins to bypass the survey
     * @param actionEvent
     * @throws IOException
     */
    public void handleEmployeeSignIn(ActionEvent actionEvent) throws IOException{
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/EmployeeAdminLogin.fxml");
    }
}
