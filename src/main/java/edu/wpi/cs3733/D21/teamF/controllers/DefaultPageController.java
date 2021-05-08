package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
    private JFXButton navigation;
    @FXML
    private JFXButton serviceRequest;
    @FXML
    private JFXButton quit;
    @FXML
    private JFXButton loginButton;
    @FXML
    private JFXButton covidSurvey;
    @FXML
    private JFXButton manageServices;
    @FXML
    private JFXButton pathfindingSettingButton;
    @FXML
    private JFXButton manageAccount;
    @FXML
    private JFXButton editMap;
    @FXML
    private Label loginLabel;
    @FXML
    JFXTextField verifyAgain;
    @FXML private VBox buttons;
    @FXML private VBox covidBox;

    @FXML private void initialize(){
        // Apply fonts to title and buttons

        // CLear visual focus for login button (unknown why it defaults to false) - LM
        loginButton.setDisableVisualFocus(true);
	//Bind login/logout
        loginButton.textProperty().bind(Bindings.when(CurrentUser.getCurrentUser().authenticatedProperty()).then("Sign Out").otherwise("Login"));

        resetButtons();
    }

    private void resetButtons(){
        AccountEntry user = CurrentUser.getCurrentUser().getLoggedIn();
        if(user != null) {
            switch (user.getUserType()){
                case "administrator":
                    manageServices.setManaged(true);
                    manageServices.setVisible(true);
                    editMap.setManaged(true);
                    editMap.setVisible(true);
                    pathfindingSettingButton.setManaged(true);
                    pathfindingSettingButton.setVisible(true);
                    manageAccount.setManaged(true);
                    manageAccount.setVisible(true);
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
            }
            loginLabel.setText("Hello, " + user.getUsername() + "!");
        } else {
            manageServices.setManaged(false);
            manageServices.setVisible(false);
            editMap.setManaged(false);
            editMap.setVisible(false);
            pathfindingSettingButton.setManaged(false);
            pathfindingSettingButton.setVisible(false);
            manageAccount.setManaged(false);
            manageAccount.setVisible(false);
            loginLabel.setText("Please Log In");
        }
    }

    @FXML
    private void changeButtons() throws SQLException {
        String ticketID = verifyAgain.getText();
        if (CurrentUser.getCurrentUser().getLoggedIn().getUsername().equals(verifyAgain.getText()) ||
        DatabaseAPI.getDatabaseAPI().getServiceEntry(ticketID, "uuid").getCompleteStatus().equals("true")){
            buttons.setStyle("visibility: visible");
            covidBox.setStyle("visibility: hidden");
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
    private void handleButtonPushed(ActionEvent actionEvent) throws IOException {

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
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml");
        } else if (buttonPushed == serviceRequest) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml");
        } else if (buttonPushed == manageAccount) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AccountManagerView.fxml");
        } else if(buttonPushed == pathfindingSettingButton) {
            FXMLLoader dialogLoader = new FXMLLoader();
            dialogLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/PreferredPathfindingAlgoView.fxml"));
            Stage dialogStage = new Stage();
            Parent root2 = dialogLoader.load();
            dialogStage.initModality(Modality.WINDOW_MODAL); // make window a pop up - KD
            dialogStage.initOwner(pathfindingSettingButton.getScene().getWindow());
            dialogStage.setScene(new Scene(root2)); // set scene - KD
            dialogStage.showAndWait();
        }
        else if (buttonPushed == quit) {
            Platform.exit();
        }
        else if (buttonPushed == covidSurvey){
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/CovidSurveyView.fxml");
        }
    }
    public void handleCovidVaccine() throws IOException {
        final FXMLLoader dialogLoader = new FXMLLoader();
        dialogLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/CovidVaccineDialog.fxml"));
        final Stage dialogStage = new Stage();
        final Parent root = dialogLoader.load();

        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(covidSurvey.getScene().getWindow());
        dialogStage.setScene(new Scene(root));

        dialogStage.showAndWait();
    }




}
