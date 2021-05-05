package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.CurrentUser;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DefaultPageController implements Initializable {
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
    JFXTextField verifyAgain;
    @FXML private VBox buttons;
    @FXML private VBox covidBox;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Apply fonts to title and buttons

        // CLear visual focus for login button (unknown why it defaults to false) - LM
        loginButton.setDisableVisualFocus(true);
        buttons.setStyle("visibility: hidden");
        covidBox.setStyle("visibility: visible");
    }

    private void openSurvey() throws IOException {
        FXMLLoader surveyLoader = new FXMLLoader();
        surveyLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/CovidSurveyView.fxml"));
        Stage dialogStage = new Stage();
        Parent root = surveyLoader.load();
        CovidSurveyViewController dialogController = surveyLoader.getController();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(navigation.getScene().getWindow());
        dialogStage.setScene(new Scene(root));

        dialogStage.showAndWait();
    }

    private void changeButtons() throws SQLException {
        String ticketID = verifyAgain.getText();
        if (CurrentUser.getCurrentUser().getLoggedIn().getUsername() == verifyAgain.getText() ||
        DatabaseAPI.getDatabaseAPI().getServiceEntry(ticketID).getCompleteStatus().equals("true")){
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
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/Login.fxml");
        } else if (buttonPushed == navigation) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml");
        } else if (buttonPushed == serviceRequest) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml");
        } else if (buttonPushed == quit) {
            Platform.exit();
        } else if (buttonPushed == covidSurvey) {
            openSurvey();
        }
    }


}
