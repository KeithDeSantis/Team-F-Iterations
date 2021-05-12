package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.Translation.Translator;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.AccountEntry;
import edu.wpi.cs3733.D21.teamF.entities.CurrentUser;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class DefaultPageController extends AbsController {
    @FXML
    private JFXButton credits;
    @FXML
    private JFXButton navigation;
    @FXML
    private JFXButton googleMapsPage;
    @FXML
    private JFXButton serviceRequest;
    @FXML
    private JFXButton quit;
    @FXML
    private JFXButton loginButton;
    @FXML
    private JFXButton about;
    @FXML
    private JFXButton surveyButton;
    @FXML
    private JFXButton surveyButton2;
    @FXML
    private JFXButton manageServices;
    @FXML
    private JFXButton quitEntry;
    @FXML
    JFXTextField verifyAgain;
    @FXML
    private VBox buttons;
    @FXML
    private VBox covidBox;
    @FXML
    private JFXButton pathfindingSettingButton;
    @FXML
    private JFXButton manageAccount;
    @FXML
    private JFXButton editMap;
    @FXML
    private Label loginLabel;
    @FXML
    private JFXButton enterApp;
    @FXML
    private Label fillOutTheSurvey;
    @FXML
    private JFXButton employeeAdminSignIn;
    boolean isCompleted;

    @FXML
    private void initialize() {
        // CLear visual focus for login button (unknown why it defaults to false) - LM
        loginButton.setDisableVisualFocus(true);
        //Bind login/logout
        //loginButton.textProperty().bind(Bindings.when(CurrentUser.getCurrentUser().authenticatedProperty()).then("Sign Out").otherwise("Login"));



        try {
            resetButtons();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private void changeButtons() {
        buttons.setVisible(true);
        covidBox.setVisible(false);
        credits.setVisible(true);
        covidBox.setManaged(false);
    }

    private void resetButtons() throws SQLException {

        loginButton.textProperty().unbind();
        if(CurrentUser.getCurrentUser().authenticatedProperty().get())
            loginButton.textProperty().bind(Translator.getTranslator().getTranslationBinding("Sign Out"));
        else
            loginButton.textProperty().bind(Translator.getTranslator().getTranslationBinding("Login"));
        //loginButton.textProperty().bind(Bindings.when(CurrentUser.getCurrentUser().authenticatedProperty()).then(Translator.getTranslator().getTranslationBinding("Sign Out")).otherwise(Translator.getTranslator().getTranslationBinding("Login")));
        AccountEntry user = CurrentUser.getCurrentUser().getLoggedIn();
        if (user != null && CurrentUser.getCurrentUser().isAuthenticated()) {
            changeButtons();
            switch (user.getUserType()) {
                case "administrator":
                    manageServices.setManaged(true);
                    manageServices.setVisible(true);
                    editMap.setManaged(true);
                    editMap.setVisible(true);
                    pathfindingSettingButton.setManaged(true);
                    pathfindingSettingButton.setVisible(true);
                    manageAccount.setManaged(true);
                    manageAccount.setVisible(true);
                    googleMapsPage.setVisible(true);
                    googleMapsPage.setManaged(true);
                    surveyButton.setManaged(false);
                    surveyButton.setVisible(false);
                    surveyButton2.setManaged(false);
                    surveyButton2.setVisible(false);
                    break;

                case "employee":
                    manageServices.setManaged(true);
                    manageServices.setVisible(true);
                    editMap.setManaged(true);
                    editMap.setVisible(true);
                    googleMapsPage.setVisible(true);
                    googleMapsPage.setManaged(true);
                    pathfindingSettingButton.setManaged(false);
                    pathfindingSettingButton.setVisible(false);
                    manageAccount.setManaged(false);
                    manageAccount.setVisible(false);
                    surveyButton.setManaged(false);
                    surveyButton.setVisible(false);
                    surveyButton2.setManaged(false);
                    surveyButton2.setVisible(false);
                    break;

                default:
                    manageServices.setManaged(false);
                    manageServices.setVisible(false);
                    editMap.setManaged(false);
                    editMap.setVisible(false);
                    googleMapsPage.setVisible(true);
                    googleMapsPage.setManaged(true);
                    pathfindingSettingButton.setManaged(false);
                    pathfindingSettingButton.setVisible(false);
                    manageAccount.setManaged(false);
                    manageAccount.setVisible(false);
                    surveyButton.setManaged(false);
                    surveyButton.setVisible(false);
                    surveyButton2.setManaged(false);
                    surveyButton2.setVisible(false);
            }
            loginLabel.textProperty().unbind();
            loginLabel.textProperty().bind(Translator.getTranslator().getTranslationBinding("Hello, " + user.getUsername() + "!"));

        } else if (CurrentUser.getCurrentUser().getUuid() != null && isCleared(CurrentUser.getCurrentUser().getUuid())) {
            displayDefaultUser();

            loginLabel.textProperty().unbind();
            loginLabel.textProperty().bind(Translator.getTranslator().getTranslationBinding("PLease Log in."));
        } else {
            buttons.setVisible(false);
            covidBox.setVisible(true);
            covidBox.setManaged(true);
            surveyButton.setVisible(true);
            surveyButton.setManaged(true);
            loginLabel.textProperty().unbind();
            loginLabel.textProperty().bind(Translator.getTranslator().getTranslationBinding("PLease Log in."));
        }
    }

    private void displayDefaultUser() {
        covidBox.setVisible(false);
        buttons.setVisible(true);
        manageServices.setManaged(false);
        manageServices.setVisible(false);
        editMap.setManaged(false);
        editMap.setVisible(false);
        pathfindingSettingButton.setManaged(false);
        pathfindingSettingButton.setVisible(false);
        manageAccount.setManaged(false);
        manageAccount.setVisible(false);
        surveyButton.setManaged(false);
        surveyButton.setVisible(false);
        surveyButton2.setManaged(true);
        surveyButton2.setVisible(true);
    }

    /**
     * Handles the pushing of a button on the screen
     *
     * @param actionEvent the button's push
     * @throws IOException in case of scene switch, if the next fxml scene file cannot be found
     * @author ZheCheng Song
    */
    @FXML
    private void handleButtonPushed(ActionEvent actionEvent) throws IOException, SQLException {

        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage

        if (buttonPushed == loginButton) {
            if(CurrentUser.getCurrentUser().isAuthenticated()) {
                CurrentUser.getCurrentUser().logout();
                resetButtons();
            }else
                SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/Login.fxml");
        }else if (buttonPushed == googleMapsPage){
            System.out.println("here");
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/GoogleMapsView.fxml");
            System.out.println("here1");
        }
        else if (buttonPushed == editMap) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/mapEditView.fxml");
        } else if (buttonPushed == manageServices) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestManagerView.fxml");
        } else if (buttonPushed == navigation) {
            if(CurrentUser.getCurrentUser().isAuthenticated() || isCleared(CurrentUser.getCurrentUser().getUuid())) {
                SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml");
            } else {
                FXMLLoader submittedPageLoader = new FXMLLoader();
                submittedPageLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/CovidFormSubmittedView.fxml"));
                Stage submittedStage = new Stage();
                Parent root = submittedPageLoader.load();
                SceneContext.autoTranslate(root);
                Scene submitScene = new Scene(root);
                submittedStage.setScene(submitScene);
                submittedStage.setTitle("Check COVID Status");
                submittedStage.initModality(Modality.APPLICATION_MODAL);
                submittedStage.showAndWait();
            }
        } else if (buttonPushed == serviceRequest) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml");
        } else if (buttonPushed == manageAccount) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AccountManagerView.fxml");
        } else if(buttonPushed == pathfindingSettingButton) {
            FXMLLoader dialogLoader = new FXMLLoader();
            dialogLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/PreferredPathfindingAlgoView.fxml"));
            AccountManagerController.loadPopup(dialogLoader, pathfindingSettingButton, false);
        }
        else if (buttonPushed == quit) {
            Platform.exit();
        }
        else if (buttonPushed == surveyButton || buttonPushed == surveyButton2){
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/CovidSurveyView.fxml");
        }
        else if (buttonPushed == employeeAdminSignIn){
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/EmployeeAdminLogin.fxml");
        }
        else if (buttonPushed == enterApp){
                //do we need to check to see if the input is a uuid or username?
                if(!completed(verifyAgain.getText()).isEmpty()) {
                    isCompleted = true;
                    if (isCleared(verifyAgain.getText())) {
                        displayDefaultUser();
                    } else {
                        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml");
                    }
                }
                else{
                    fillOutTheSurvey.setStyle("-fx-text-fill: #c60000FF;");
                }
            }
        else if (buttonPushed == quitEntry) {
            Platform.exit();
        }
        }
    private String completed(String ticketID) throws SQLException {
        String complete = "";

        if (ticketID.contains("-")) {
            complete = DatabaseAPI.getDatabaseAPI().getServiceEntry(ticketID, "uuid").getCompleteStatus();
            CurrentUser.getCurrentUser().tempLogin(ticketID);
        }
        return complete;
    }

    private boolean isCleared(String ID) throws SQLException{
        String value = DatabaseAPI.getDatabaseAPI().getServiceEntry(ID, "additionalInstructions").getCompleteStatus();
        if(value == null) //FIXME: DO BETTER!
            return false;
        return Boolean.parseBoolean(DatabaseAPI.getDatabaseAPI().getServiceEntry(ID, "additionalInstructions").getCompleteStatus());
    }

    public void handleCovidVaccine() throws IOException {
        final FXMLLoader dialogLoader = new FXMLLoader();
        dialogLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/CovidVaccineDialog.fxml"));
        final Stage dialogStage = new Stage();
        final Parent root = dialogLoader.load();
        SceneContext.autoTranslate(root);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(surveyButton.getScene().getWindow());
        dialogStage.setScene(new Scene(root));

        dialogStage.showAndWait();
    }

    public void handleCredits() throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/CreditsView.fxml");
    }

    public void handleAbout() throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AboutPageView.fxml");
    }
}
