package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.AccountEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private JFXButton closeLogin;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton signIn;
    @FXML
    private Label errorMessage;
    @FXML
    private JFXButton skipSignIn;

    public void handleButtonPushed(ActionEvent actionEvent) throws IOException, SQLException {
        Button buttonPushed = (JFXButton) actionEvent.getSource();  //Getting current stage
        if (buttonPushed == closeLogin) {
            Stage currentStage = (Stage) closeLogin.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
            Scene homeScene = new Scene(root);
            currentStage.setScene(homeScene);
            currentStage.show();
        } else if (buttonPushed == signIn) {
            if (!DatabaseAPI.getDatabaseAPI().verifyAdminExists()) {
                DatabaseAPI.getDatabaseAPI().addUser("admin", "administrator", "admin", "admin");
            }
            boolean authenticated = false;
            String user = username.getText();
            String pass = password.getText();

            authenticated = DatabaseAPI.getDatabaseAPI().authenticate(user, pass);
            if (authenticated) {
                AccountEntry userInfo = DatabaseAPI.getDatabaseAPI().getUser(user);
                errorMessage.setStyle("-fx-text-fill: #c6000000;");
                if (userInfo.getUserType().equals("administrator")) {
                    Stage currentStage = (Stage) signIn.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml"));
                    Scene homeScene = new Scene(root);
                    currentStage.setScene(homeScene);
                    currentStage.setTitle("Admin Home");
                    currentStage.show();
                    // set user privileges to employee
                } else if (userInfo.getUserType().equals("employee")) {
                    Stage currentStage = (Stage) signIn.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageEmployeeView.fxml"));
                    Scene homeScene = new Scene(root);
                    currentStage.setScene(homeScene);
                    currentStage.setTitle("Employee Home");
                    currentStage.show();
                } else{
                    Stage currentStage = (Stage) signIn.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
                    Scene homeScene = new Scene(root);
                    currentStage.setScene(homeScene);
                    currentStage.setTitle("Home");
                    currentStage.show();
                }
            }
            else {
                errorMessage.setStyle("-fx-text-fill: #c60000FF;");
                password.setText("");
            }
        }
            else if (buttonPushed == skipSignIn) {
                Stage currentStage = (Stage) signIn.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
                Scene homeScene = new Scene(root);
                currentStage.setScene(homeScene);
                currentStage.setTitle("Home");
                currentStage.show();
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