package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class EditMapMenuController {

    @FXML
    private JFXButton mapVertex;
    @FXML
    private JFXButton editEdges;
    @FXML
    private JFXButton home;

    @FXML private ImageView mapEdge;

    @FXML private ImageView mapNode;

    @FXML private ImageView homeIcon;

    @FXML private void initialize(){
        final Image edge = new Image(getClass().getResourceAsStream("/imagesAndLogos/mapEdge.png"));
        mapEdge.setImage(edge);
        final Image vertex = new Image(getClass().getResourceAsStream("/imagesAndLogos/mapNode.png"));
        mapNode.setImage(vertex);
        final Image homeButton = new Image(getClass().getResourceAsStream("/imagesAndLogos/home.png"));
        homeIcon.setImage(homeButton);
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

        if (buttonPushed == mapVertex) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/EditMapNodeView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Edit Map Vertex");
            stage.show();
        } else if (buttonPushed == editEdges) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/uicomponents/MapPanel.fxml"));//"/edu/wpi/cs3733/D21/teamF/fxml/EditMapEdgesView.fxml"));
            stage.getScene().setRoot(root);  //Changing the stage
            stage.setTitle("Edit Map Edges");
            stage.show();
        } else if (buttonPushed == home) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml"));
            stage.getScene().setRoot(root);  //Changing the stage
            stage.setTitle("Admin Home Page");
            stage.show();
        }
    }
}
