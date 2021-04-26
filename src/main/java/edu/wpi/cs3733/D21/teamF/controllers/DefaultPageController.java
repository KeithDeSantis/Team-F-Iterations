package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.states.PathfindingState;
import edu.wpi.cs3733.D21.teamF.states.SceneContext;
import edu.wpi.cs3733.D21.teamF.states.ServiceRequestHomeState;
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

public class DefaultPageController {
    @FXML
    private Button AStarDemo;
    @FXML
    private Button serviceRequest;
    @FXML
    private Button quit;
    @FXML
    private Button loginButton;

    @FXML private ImageView directionsImage;

    @FXML private ImageView serviceButton;

    @FXML private ImageView login;

    @FXML private void initialize(){
        final Image directions = new Image(getClass().getResourceAsStream("/imagesAndLogos/directionsArrow.png"));
        directionsImage.setImage(directions);
        final Image serviceRequest = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButton.png"));
        serviceButton.setImage(serviceRequest);
        final Image lockImage = new Image(getClass().getResourceAsStream("/imagesAndLogos/login.png"));
        login.setImage(lockImage);
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

        if (buttonPushed == loginButton) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/Login.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Log in");
            stage.show();
        } else if (buttonPushed == AStarDemo) {
            PathfindingState.getPathfindingState().switchScene(SceneContext.getSceneContext());
        } else if (buttonPushed == serviceRequest) {
            ServiceRequestHomeState.getServiceRequestHomeState().switchScene(SceneContext.getSceneContext());
        } else if (buttonPushed == quit) {
            Platform.exit();
        }
    }
}
