package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.AccountEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.xml.soap.Text;
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
    private Text errorMessage;
    @FXML
    private JFXButton skipSignIn;

    /**
     * closes the login screen and goes back to the default page
     *
     * @param actionEvent
     * @throws IOException
     * @author Jay Yen
     */
    public void handleCloseLogin(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage) closeLogin.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
        Scene homeScene = new Scene(root);
        currentStage.setScene(homeScene);
        currentStage.show();
    }

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
                //errorMessage.setStyle("-fx-text-fill: #e6e6e6;");
                if (userInfo.getUserType().equals("administrator")) {
                    Stage currentStage = (Stage) signIn.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml"));
                    Scene homeScene = new Scene(root);
                    currentStage.setScene(homeScene);
                    currentStage.setTitle("Admin Home");
                    currentStage.show();
                    // set user privileges to patient, employee or admin
                } else if (userInfo.getUserType().equals("employee")) {
                    Stage currentStage = (Stage) signIn.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageEmployeeView.fxml"));
                    Scene homeScene = new Scene(root);
                    currentStage.setScene(homeScene);
                    currentStage.setTitle("Employee Home");
                    currentStage.show();
                } else {
                    Stage currentStage = (Stage) signIn.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
                    Scene homeScene = new Scene(root);
                    currentStage.setScene(homeScene);
                    currentStage.setTitle("Home");
                    currentStage.show();
                }
            }
        } else if (buttonPushed == skipSignIn) {
            Stage currentStage = (Stage) signIn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
            Scene homeScene = new Scene(root);
            currentStage.setScene(homeScene);
            currentStage.setTitle("Home");
            currentStage.show();
        }
        //displays error message
        else {
            //errorMessage.setStyle("-fx-text-fill: #c60000;");
        }
    }

    /**
     * If the credentials are correct, signs in.  Otherwise, goes to the login screen with the error message
     *
     * @param actionEvent
     * @throws IOException
     * @author Jay Yen
     */
    public void handleSignIn(ActionEvent actionEvent) throws Exception {
        if (!DatabaseAPI.getDatabaseAPI().verifyAdminExists()) {
            DatabaseAPI.getDatabaseAPI().addUser("admin", "administrator", "admin", "admin");
        }

        boolean authenticated = false;
        String user = username.getText();
        String pass = password.getText();

        authenticated = DatabaseAPI.getDatabaseAPI().authenticate(user, pass);
        if (authenticated) {

            Stage currentStage = (Stage) signIn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml"));
            Scene homeScene = new Scene(root);
            currentStage.setScene(homeScene);
            currentStage.show();
            // set user privileges to patient, employee or admin
        }
        //displays error message
        else {
            Stage currentStage = (Stage) signIn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/LoginFail.fxml"));
            Scene homeScene = new Scene(root);
            currentStage.setScene(homeScene);
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