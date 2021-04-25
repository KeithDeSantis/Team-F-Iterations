package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.ConnectionHandler;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private JFXButton closeLogin;
    @FXML private JFXTextField username;
    @FXML private JFXPasswordField password;
    @FXML private JFXButton signIn;
    @FXML private Text errorMessage;
    @FXML private JFXButton skipSignIn;

    /**
     * closes the login screen and goes back to the default page
     * @param actionEvent
     * @throws IOException
     * @author Jay Yen
     */
    public void handleCloseLogin(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage)closeLogin.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
        Scene homeScene = new Scene(root);
        currentStage.setScene(homeScene);
        currentStage.show();
    }

    /**
     * If the credentials are correct, signs in.  Otherwise, goes to the login screen with the error message
     * @param actionEvent
     * @throws IOException
     * @author Jay Yen
     */
    public void handleSignIn(ActionEvent actionEvent) throws Exception {
        Button buttonPushed = (JFXButton) actionEvent.getSource();  //Getting current stage
        boolean authenticated = false;
        String user = username.getText();
        String pass = password.getText();

        DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "USERS");
        DatabaseAPI.getDatabaseAPI().populateUsers(ConnectionHandler.getConnection());
        authenticated = DatabaseAPI.getDatabaseAPI().authenticate(user, pass);
        if (authenticated){
            errorMessage.setStyle("-fx-text-fill: #e6e6e6;");
            if(/*user.userType == "admin"*/){

                Stage currentStage = (Stage)signIn.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml"));
                Scene homeScene = new Scene(root);
                currentStage.setScene(homeScene);
                currentStage.setTitle("Admin Home");
                currentStage.show();
            // set user privileges to patient, employee or admin
        }
            else if (/*user.userType == "employee"*/){
                Stage currentStage = (Stage)signIn.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageEmployeeView.fxml"));
                Scene homeScene = new Scene(root);
                currentStage.setScene(homeScene);
                currentStage.setTitle("Employee Home");
                currentStage.show();
            }
            else{
                Stage currentStage = (Stage)signIn.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
                Scene homeScene = new Scene(root);
                currentStage.setScene(homeScene);
                currentStage.setTitle("Home");
                currentStage.show();
            }
        }
        else if (buttonPushed == skipSignIn){
            Stage currentStage = (Stage)signIn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
            Scene homeScene = new Scene(root);
            currentStage.setScene(homeScene);
            currentStage.setTitle("Home");
            currentStage.show();
        }
        //displays error message
        else{
            errorMessage.setStyle("-fx-text-fill: #c60000;");
        }
    }
}
