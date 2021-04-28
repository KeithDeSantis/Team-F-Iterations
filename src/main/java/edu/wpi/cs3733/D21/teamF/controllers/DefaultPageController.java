package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.sun.javafx.tk.FontLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class DefaultPageController {
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
    private Text title;


    @FXML private void initialize(){
        // Apply fonts to title and buttons

        // CLear visual focus for login button (unknown why it defaults to false) - LM
        loginButton.setDisableVisualFocus(true);
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
        } else if (buttonPushed == navigation) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("AStar Pathfinding Demo");
            stage.show();

        } else if (buttonPushed == serviceRequest) {

            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Service Request Home");
            stage.show();

        } else if (buttonPushed == quit) {
            Platform.exit();
        }

        else if (buttonPushed == covidSurvey){
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/CovidSurveyView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Covid Survey");
            stage.show();
        }
    }

    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #F0C808; -fx-text-fill: #000000;");
    }

    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #03256C; -fx-text-fill: #FFFFFF;");
    }
}
