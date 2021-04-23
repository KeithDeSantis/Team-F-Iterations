package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
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

public class DefaultPageEmployeeController {

    @FXML
    private JFXButton editVertex;
    @FXML
    private JFXButton editEdges;
    @FXML
    private JFXButton AStarDemo;
    @FXML
    private JFXButton serviceRequest;
    @FXML
    private JFXButton quit;

    @FXML private JFXButton serviceManager;

    @FXML private ImageView directionsImage;

    @FXML private ImageView serviceButton;

    @FXML private ImageView mapEdge;

    @FXML private ImageView mapNode;

    @FXML private ImageView manageServices;

    @FXML private void initialize(){
        final Image directions = new Image(getClass().getResourceAsStream("/imagesAndLogos/directionsArrow.png"));
        directionsImage.setImage(directions);
        final Image serviceRequest = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButton.png"));
        serviceButton.setImage(serviceRequest);
        final Image mapLine = new Image(getClass().getResourceAsStream("/imagesAndLogos/mapEdge.png"));
        mapEdge.setImage(mapLine);
        final Image mapDot = new Image(getClass().getResourceAsStream("/imagesAndLogos/mapNode.png"));
        mapNode.setImage(mapDot);
        final Image serviceManage = new Image(getClass().getResourceAsStream("/imagesAndLogos/manageServices.png"));
        manageServices.setImage(serviceManage);
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

        if (buttonPushed == editVertex) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/EditMapNodeView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Edit Map Vertex");
            stage.show();
        } else if (buttonPushed == editEdges) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/EditMapEdgesView.fxml"));
            stage.getScene().setRoot(root);  //Changing the stage
            stage.setTitle("Edit Map Edges");
            stage.show();
        } else if (buttonPushed == AStarDemo) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("AStar Pathfinding Demo");
            stage.show();

        } else if (buttonPushed == serviceRequest) {

            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Service Request Home");
            stage.show();
        }

        else if (buttonPushed == serviceManager) {
            // Implement Later

            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestManageView.fxml"));
            //Scene scene = new Scene(root);
            //stage.setScene(scene);
            stage.getScene().setRoot(root);
            stage.setTitle("Service Request Manager");
            stage.show();

        }
        else if (buttonPushed == quit) {
            Platform.exit();
        }
    }
}
