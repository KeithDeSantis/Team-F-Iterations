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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.activation.ActivationID;

public class LoginController {

    @FXML private JFXButton closeLogin;
    @FXML private JFXTextField username;
    @FXML private JFXPasswordField password;
    @FXML private JFXButton signIn;
    @FXML private Text errorMessage;
    @FXML private JFXButton skipSignIn;

    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #F0C808; -fx-text-fill: #000000;");
    }

    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #03256C; -fx-text-fill: #FFFFFF;");
    }
    public void handleButtonPushed(ActionEvent actionEvent)throws IOException {
        Button buttonPushed = (JFXButton) actionEvent.getSource();  //Getting current stage
        if (buttonPushed == closeLogin) {
            Stage currentStage = (Stage) closeLogin.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
            Scene homeScene = new Scene(root);
            currentStage.setScene(homeScene);
            currentStage.show();
        } else if (buttonPushed == signIn) {
            DatabaseAPI.getDatabaseAPI().addUser("admin", "administrator", "admin", "admin");
        /*
        //TODO: test and see if this verifies DB existence
        ResultSet rset = ConnectionHandler.getConnection().getMetaData().getCatalogs();
        String databaseName = "";
        while(rset.next()){
            databaseName = rset.getString(1);
        }

        System.out.println(databaseName);

        if (databaseName.equals("projectC1")){
            System.out.println("here2");
            DatabaseAPI.getDatabaseAPI().addUser("admin", "administrator", "admin", "admin");
        }
        else{
            System.out.println("here1");
            ConnectionHandler.getConnection();
            DatabaseAPI.getDatabaseAPI().createNodesTable();
            DatabaseAPI.getDatabaseAPI().createEdgesTable();
            DatabaseAPI.getDatabaseAPI().createUserTable();
            DatabaseAPI.getDatabaseAPI().createServiceRequestTable(); //TODO: make this better
            DatabaseAPI.getDatabaseAPI().addUser("admin", "administrator", "admin", "admin");
        }
        rset.close();
         */

            Button buttonPushed = (JFXButton) actionEvent.getSource();  //Getting current stage
            boolean authenticated = false;
            String user = username.getText();
            String pass = password.getText();
//FIXME is this the old code? How fix?
//        DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "USERS");
//        DatabaseAPI.getDatabaseAPI().populateUsers(ConnectionHandler.getConnection());
            authenticated = DatabaseAPI.getDatabaseAPI().authenticate(user, pass);
            if (authenticated) {
                errorMessage.setStyle("-fx-text-fill: #e6e6e6;");
                if (/*user.userType == "admin"*/) {

                    Stage currentStage = (Stage) signIn.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml"));
                    Scene homeScene = new Scene(root);
                    currentStage.setScene(homeScene);
                    currentStage.setTitle("Admin Home");
                    currentStage.show();
                    // set user privileges to patient, employee or admin
                } else if (/*user.userType == "employee"*/) {
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
                errorMessage.setStyle("-fx-text-fill: #c60000;");
            }
        }
    }
}
