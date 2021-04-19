package edu.wpi.fuchsiafalcons.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class DefaultPageAdminController {

    @FXML
    private Button editVertex;
    @FXML
    private Button editEdges;
    @FXML
    private Button AStarDemo;
    @FXML
    private Button serviceRequest;
    @FXML
    private Button service2;
    @FXML
    private Button quit;

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

        if (buttonPushed == editVertex) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/EditMapNodeView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Edit Map Vertex");
            stage.show();
        } else if (buttonPushed == editEdges) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/EditMapEdgesView.fxml"));
            stage.getScene().setRoot(root);  //Changing the stage
            stage.setTitle("Edit Map Edges");
            stage.show();
        } else if (buttonPushed == AStarDemo) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/AStarDemoView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("AStar Pathfinding Demo");
            stage.show();

        } else if (buttonPushed == serviceRequest) {

            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/ServiceRequestHomeView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Language Interpretation Request");
            stage.show();

        } else if (buttonPushed == service2) {
            // Implement Later

            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/FoodDeliveryServiceRequestView.fxml"));
            //Scene scene = new Scene(root);
            //stage.setScene(scene);
            stage.getScene().setRoot(root);
            stage.setTitle("Service Request Two");
            stage.show();

        } else if (buttonPushed == quit) {
            Platform.exit();
        }
    }
}