package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.states.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class DefaultPageAdminController {

    @FXML
    private JFXButton editMap;
    @FXML
    private JFXButton manageServices;
    @FXML
    private JFXButton AStarDemo;
    @FXML
    private JFXButton serviceRequest;
    @FXML
    private JFXButton manageAccount;
    @FXML
    private JFXButton quit;
    @FXML
    private ImageView directionsImage;
    @FXML
    private ImageView serviceButton;
    @FXML
    private ImageView manageAccounts;
    @FXML
    private ImageView mapIcon;
    @FXML
    private ImageView serviceManageIcon;
    @FXML
    private void initialize(){
        final Image directions = new Image(getClass().getResourceAsStream("/imagesAndLogos/directionsArrow.png"));
        directionsImage.setImage(directions);
        final Image serviceRequest = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButton.png"));
        serviceButton.setImage(serviceRequest);
        final Image account = new Image(getClass().getResourceAsStream("/imagesAndLogos/manageAccounts.png"));
        manageAccounts.setImage(account);
        final Image map = new Image(getClass().getResourceAsStream("/imagesAndLogos/map.png"));
        mapIcon.setImage(map);
        final Image serviceManage = new Image(getClass().getResourceAsStream("/imagesAndLogos/manageServices.png"));
        serviceManageIcon.setImage(serviceManage);
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
        Stage stage;
        Parent root;

        if (buttonPushed == editMap) {
            EditMapState.getEditMapState().switchScene(SceneContext.getSceneContext());
        } else if (buttonPushed == manageServices) {
            MarkRequestsCompleteState.getMarkRequestsCompleteState().switchScene(SceneContext.getSceneContext());
        } else if (buttonPushed == AStarDemo) {
            PathfindingState.getPathfindingState().switchScene(SceneContext.getSceneContext());
        } else if (buttonPushed == serviceRequest) {
            ServiceRequestHomeState.getServiceRequestHomeState().switchScene(SceneContext.getSceneContext());
        } else if (buttonPushed == manageAccount) {
            AccountManagerState.getAccountManagerState().switchScene(SceneContext.getSceneContext());
        } else if (buttonPushed == quit) {
            Platform.exit();
        }
    }
}
