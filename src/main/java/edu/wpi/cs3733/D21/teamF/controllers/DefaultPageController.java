package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.Translation.Translator;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.AccountEntry;
import edu.wpi.cs3733.D21.teamF.entities.CurrentUser;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
    private JFXButton serviceRequest;
    @FXML
    private JFXButton quit;
    @FXML
    private JFXButton loginButton;
    @FXML
    private JFXButton surveyButton;
    @FXML
    private JFXButton covidInfo;
    @FXML
    private JFXButton surveyButton2;
    @FXML
    private JFXButton manageServices;
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
        loginButton.textProperty().bind(Bindings.when(CurrentUser.getCurrentUser().authenticatedProperty()).then("Sign Out").otherwise("Login"));


        loginButton.textProperty().bind(Translator.getTranslator().getTranslationBinding(loginButton.getText()));

        // loginLabel.textProperty().bind(Translator.getTranslator().getTranslationBinding(loginLabel.getText()));

        navigation.textProperty().bind(Translator.getTranslator().getTranslationBinding(navigation.getText()));

        editMap.textProperty().bind(Translator.getTranslator().getTranslationBinding(editMap.getText()));

        serviceRequest.textProperty().bind(Translator.getTranslator().getTranslationBinding(serviceRequest.getText()));

        manageServices.textProperty().bind(Translator.getTranslator().getTranslationBinding(manageServices.getText()));

        manageAccount.textProperty().bind(Translator.getTranslator().getTranslationBinding(manageAccount.getText()));

        pathfindingSettingButton.textProperty().bind(Translator.getTranslator().getTranslationBinding(pathfindingSettingButton.getText()));


        covidInfo.textProperty().bind(Translator.getTranslator().getTranslationBinding(covidInfo.getText()));

        quit.textProperty().bind(Translator.getTranslator().getTranslationBinding(quit.getText()));

        credits.textProperty().bind(Translator.getTranslator().getTranslationBinding(credits.getText()));


        try {
            resetButtons();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // used to see how to toggle visibility
//    @FXML
//    private void handleTestVisibility(ActionEvent actionEvent){
//        buttons.setVisible(true);
//        covidBox.setVisible(false);
//    }
    private void changeButtons() {
//        String ticketID = verifyAgain.getText();
//        if (CurrentUser.getCurrentUser().getLoggedIn().getUsername().equals(verifyAgain.getText()) ||
//                DatabaseAPI.getDatabaseAPI().getServiceEntry(ticketID).getCompleteStatus().equals("true")) {
        buttons.setVisible(true);
        covidBox.setVisible(false);
        credits.setVisible(true);
        covidBox.setManaged(false);
        // }
    }

    private void resetButtons() throws SQLException {
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
                    pathfindingSettingButton.setManaged(false);
                    pathfindingSettingButton.setVisible(false);
                    manageAccount.setManaged(false);
                    manageAccount.setVisible(false);
                    surveyButton.setManaged(false);
                    surveyButton.setVisible(false);
                    surveyButton2.setManaged(false);
                    surveyButton2.setVisible(false);
            }
            loginLabel.setText("Hello, " + user.getUsername() + "!");

        } else if (CurrentUser.getCurrentUser().getUuid() != null &&
                isCleared(CurrentUser.getCurrentUser().getUuid())) {
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

            loginLabel.setText("Please Log in.");
        } else {
            buttons.setVisible(false);
            covidBox.setVisible(true);
            covidBox.setManaged(true);
            surveyButton.setVisible(true);
            surveyButton.setManaged(true);
            loginLabel.setText("Please Log in.");
        }
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
        } else if (buttonPushed == editMap) {
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
            Stage dialogStage = new Stage();
            Parent root2 = dialogLoader.load();
            SceneContext.autoTranslate(root2);
            dialogStage.initModality(Modality.WINDOW_MODAL); // make window a pop up - KD
            dialogStage.initOwner(pathfindingSettingButton.getScene().getWindow());
            dialogStage.setScene(new Scene(root2)); // set scene - KD
            dialogStage.showAndWait();
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
                    } else {
                        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml");
                    }
                }
                else{
                    fillOutTheSurvey.setStyle("-fx-text-fill: #c60000FF;");
                }
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




}
