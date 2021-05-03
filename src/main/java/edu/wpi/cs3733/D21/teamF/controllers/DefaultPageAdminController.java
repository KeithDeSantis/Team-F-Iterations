package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.entities.CurrentUser;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DefaultPageAdminController {

    @FXML
    private JFXButton editMap;
    @FXML
    private JFXButton manageServices;
    @FXML
    private JFXButton navigation;
    @FXML
    private JFXButton serviceRequest;
    @FXML
    private JFXButton manageAccount;
    @FXML
    private JFXButton quit;
    @FXML
    private JFXButton signOut;
    @FXML
    private JFXButton pathfindingSettingButton;
    @FXML
    private JFXButton favoriteNodes;

    @FXML
    private void initialize(){
        // Set fonts - LM

        // Disable focus on edit button - LM
        editMap.setDisableVisualFocus(true);
    }
    /**
     * Handles the pushing of a button on the screen
     * @param actionEvent the button's push
     * @throws IOException in case of scene switch, if the next fxml scene file cannot be found
     * @author ZheCheng Song
     */
    @FXML
    private void handleButtonPushed(ActionEvent actionEvent) throws IOException {
        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage

        if (buttonPushed == editMap) {
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
        } else if (buttonPushed == favoriteNodes) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/FavoriteNodeSelectionView.fxml");
        }
        else if (buttonPushed == quit) {
            Platform.exit();
        } else if (buttonPushed == signOut){
            CurrentUser.getCurrentUser().logout();
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
        }
    }
}
