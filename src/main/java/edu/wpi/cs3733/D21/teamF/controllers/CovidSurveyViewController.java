package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.CurrentUser;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.UUID;

public class CovidSurveyViewController extends ServiceRequests implements Initializable {

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
    @FXML private JFXTextField generatedID;
    private ClipboardContent content = new ClipboardContent();


    /**
     * generates a UUID for the survey and displays it.
     */
    public void initialize(URL location, ResourceBundle resources){
        String ticketNumber = UUID.randomUUID().toString();
        generatedID.setText(ticketNumber);
        content.putString(ticketNumber);
    }

    /**
     * creates a service request and puts it in the database, then changes to the submitted view
     * @throws IOException
     */
    @FXML private void handleSubmitPushed() throws IOException, SQLException {
        if(formFilled()) {
            // Create service request, put in database
            String covidInfo = "Temp: " + temperatureField.getText() + "ºF, ";
            if(yes1.isSelected()) covidInfo += "Tested Positive, ";
            if(yes2.isSelected()) covidInfo += "Had Close Contact, ";
            covidInfo += "Symptoms: ";
            if(cough.isSelected()) covidInfo += "Cough, ";
            if(breathing.isSelected()) covidInfo += "Trouble Breathing, ";
            if(fatigue.isSelected()) covidInfo += "Fatigue, ";
            if(aches.isSelected()) covidInfo += "Aches, ";
            if(headache.isSelected()) covidInfo +="Headache, ";
            if(lossOfTaste.isSelected()) covidInfo += "Loss of Tates or Smell, ";
            if(soreThroat.isSelected()) covidInfo += "Sore Throat, ";
            if(congestion.isSelected()) covidInfo += "Congestion, ";
            if(nausea.isSelected()) covidInfo += "Nausea, ";
            if(diarrhea.isSelected()) covidInfo += "Diarrhea, ";
            if(blueSkin.isSelected()) covidInfo += "Blueness or Paleness, ";
            if(pain.isSelected()) covidInfo += "Persistent Pain, ";
            if(confusion.isSelected()) covidInfo += "New Confusion, ";
            if(stayAwake.isSelected()) covidInfo += "Unable to Stay Awake, ";
            if(fever.isSelected()) covidInfo += "fever, ";
            if(covidInfo.endsWith(", ")) covidInfo = covidInfo.substring(0, covidInfo.length() - 2);
            else covidInfo += "None";
            DatabaseAPI.getDatabaseAPI().addServiceReq(generatedID.getText(), "ticket", "", "",covidInfo);
            DatabaseAPI.getDatabaseAPI().addServiceReq(UUID.randomUUID().toString(), "Nurse Appointment", "", "false", generatedID.getText());
            // ServiceEntry ticket = DatabaseAPI.getDatabaseAPI().getServiceEntry(generatedID.getText());

            // Copy UUID to users clipboard TODO add explicit button for this
            Clipboard.getSystemClipboard().setContent(content);


            // Change view to survey submitted page
            FXMLLoader submittedPageLoader = new FXMLLoader();
            submittedPageLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/CovidFormSubmittedView.fxml"));
            Stage submittedStage = new Stage();
            Parent root = submittedPageLoader.load();
            SceneContext.autoTranslate(root);
            CovidFormSubmittedViewController formSubmittedViewController = submittedPageLoader.getController();
            formSubmittedViewController.autoFill(generatedID.getText());
            Scene submitScene = new Scene(root);
            submittedStage.setScene(submitScene);
            submittedStage.setTitle("Submission Complete");
            submittedStage.initModality(Modality.APPLICATION_MODAL);
            submittedStage.showAndWait();

            // Generate a new UUID in the event submit is pressed twice (prevents DB errors)
            generatedID.setText(UUID.randomUUID().toString());
            content.putString(generatedID.getText());


        }
    }
    public boolean formFilled(){
        final String tempStr = temperatureField.getText().trim();
        boolean returnFlag = true;

        //Empty temperature field
        if(tempStr.isEmpty()) {
            setTextErrorStyle(temperatureField);
            returnFlag = false;
        }
        //Verify temperature is a number
        if(!tempStr.matches("\\d*")) {
            setTextErrorStyle(temperatureField);
            returnFlag =  false;
        }
        if(!tempStr.isEmpty()) {
            final int temperature = Integer.parseInt(tempStr);

            if (temperature < 70 || temperature > 115) {
                setTextErrorStyle(temperatureField);
                returnFlag = false;
            }
        }

        //FIXME: ADD USER FEEDBACK

        if(!yes1.isSelected() && !no1.isSelected())
        {
            yes1.setUnSelectedColor(Color.RED);
            no1.setUnSelectedColor(Color.RED);
            returnFlag = false;
        }

        if(!yes2.isSelected() && !no2.isSelected())
        {
            yes2.setUnSelectedColor(Color.RED);
            no2.setUnSelectedColor(Color.RED);
            returnFlag =  false;
        }

        return returnFlag;
    }

    /**
     * groups radio buttons into the two questions so only one is selected at a time for each
     * @author kh
     */

    @FXML
    private void handleRadialButtonPushed(){
        ToggleGroup question1 = new ToggleGroup(); //group for first question
        yes1.setToggleGroup(question1);
        no1.setToggleGroup(question1);

        yes1.setUnSelectedColor(Color.rgb(0x03,0x25,0x6c));
        no1.setUnSelectedColor(Color.rgb(0x03,0x25,0x6c));

        ToggleGroup question2 = new ToggleGroup(); //group for second question
        yes2.setToggleGroup(question2);
        no2.setToggleGroup(question2);

        yes2.setUnSelectedColor(Color.rgb(0x03,0x25,0x6c));
        no2.setUnSelectedColor(Color.rgb(0x03,0x25,0x6c));
    }

    /**
     * allows employees and admins to bypass the survey
     * @throws IOException
     */
    public void handleEmployeeSignIn() throws IOException{
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/EmployeeAdminLogin.fxml");
    }

    public void handleCheckStatus() throws IOException {
        FXMLLoader submittedPageLoader = new FXMLLoader();
        submittedPageLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/CovidFormSubmittedView.fxml"));
        Stage submittedStage = new Stage();
        Parent root = submittedPageLoader.load();
        SceneContext.autoTranslate(root);
        Scene submitScene = new Scene(root);
        submittedStage.setScene(submitScene);
        submittedStage.setTitle("Check Status");
        submittedStage.initModality(Modality.APPLICATION_MODAL);
        submittedStage.showAndWait();
    }

    @FXML
    public void goBack(MouseEvent mouseEvent) throws IOException {
        CurrentUser.getCurrentUser().logout();
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
    }

    @Override
    public void handleHome() throws IOException {
        SceneContext.getSceneContext().loadDefault();
    }
}
