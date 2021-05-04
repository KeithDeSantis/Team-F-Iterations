package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CovidSurveyViewController {

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


    @FXML
    private void initialize(){
    }

    /**
     * handles the back button (image icon) being pushed
     * @throws IOException
     * @author kh
     */
    public void handleHome() throws IOException{
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
    }

    /**
     * handles the submit button being pushed
     * @throws IOException
     */
    @FXML private void handleSubmitPushed() throws IOException{
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


    /**
     * handles cancel button being pushed and returns to home page
     * @param e
     * @throws IOException
     * @author kh
     */
    @FXML
    private void handleCancelPushed(ActionEvent e) throws IOException {
        Button buttonPushed = (Button) e.getSource();

        if (buttonPushed == cancel) { // is cancel button
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
        }
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

        ToggleGroup question2 = new ToggleGroup(); //group for second question
        yes2.setToggleGroup(question2);
        no2.setToggleGroup(question2);
    }
}
