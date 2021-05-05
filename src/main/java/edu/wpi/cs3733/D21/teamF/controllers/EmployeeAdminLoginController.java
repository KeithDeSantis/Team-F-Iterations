package edu.wpi.cs3733.D21.teamF.controllers;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EmployeeAdminLoginController {
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton signIn;
    @FXML
    private Label errorMessage;
    @FXML
    private JFXButton goBack;


    public void handleButtonPushed(ActionEvent actionEvent) throws IOException, SQLException {
        Button buttonPushed = (JFXButton) actionEvent.getSource();  //Getting current stage
        if (buttonPushed == signIn) {
            if (!DatabaseAPI.getDatabaseAPI().verifyAdminExists()) {
                DatabaseAPI.getDatabaseAPI().addUser("admin", "administrator", "admin", "admin", "true");
                DatabaseAPI.getDatabaseAPI().addUser("staff", "employee", "staff", "staff", "true");
                DatabaseAPI.getDatabaseAPI().addUser("guest", "visitor", "guest", "guest", "true");
            }
            String user = username.getText();
            String pass = password.getText();

            if (CurrentUser.getCurrentUser().login(user, pass) && (CurrentUser.getCurrentUser().getLoggedIn().getUserType() == "employee" || CurrentUser.getCurrentUser().getLoggedIn().getUserType() == "admin")) {
                SceneContext.getSceneContext().loadDefault();
            }
            else {
                errorMessage.setStyle("-fx-text-fill: #c60000FF;");
                password.setText("");
            }
        }
        else if (buttonPushed == goBack) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/CovidSurveyView.fxml");
        }

    }



}
