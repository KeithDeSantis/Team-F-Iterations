package edu.wpi.fuchsiafalcons.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class EditMapMenuController {

    @FXML
    private JFXButton editVertex;
    @FXML
    private JFXButton editEdges;
    @FXML
    private JFXButton home;

    @FXML private ImageView mapEdge;

    @FXML private ImageView mapVertex;

    @FXML private ImageView homeIcon;

    @FXML private void initialize(){
        final Image edge = new Image(getClass().getResourceAsStream("/imagesAndLogos/mapEdge.png"));
        mapEdge.setImage(edge);
        final Image vertex = new Image(getClass().getResourceAsStream("/imagesAndLogos/mapNode.png"));
        mapVertex.setImage(vertex);
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
        } else if (buttonPushed == home) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/DefaultPageAdminView.fxml"));
            stage.getScene().setRoot(root);  //Changing the stage
            stage.setTitle("Admin Home Page");
            stage.show();
        }
    }
}
