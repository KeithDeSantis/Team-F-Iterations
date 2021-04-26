package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.states.DefaultPageAdminState;
import edu.wpi.cs3733.D21.teamF.states.DefaultPageState;
import edu.wpi.cs3733.D21.teamF.states.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private JFXButton closeLogin;
    @FXML private JFXTextField username;
    @FXML private JFXPasswordField password;
    @FXML private JFXButton signIn;

    /**
     * closes the login screen and goes back to the default page
     * @param actionEvent
     * @throws IOException
     * @author Jay Yen
     */
    public void handleCloseLogin(ActionEvent actionEvent) throws IOException {
        DefaultPageState.getDefaultPageState().switchScene(SceneContext.getSceneContext());
    }

    /**
     * If the credentials are correct, signs in.  Otherwise, goes to the login screen with the error message
     * @param actionEvent
     * @throws IOException
     * @author Jay Yen
     */
    public void handleSignIn(ActionEvent actionEvent) throws Exception {
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

        boolean authenticated = false;
        String user = username.getText();
        String pass = password.getText();

        authenticated = DatabaseAPI.getDatabaseAPI().authenticate(user, pass);
        if (authenticated){
            DefaultPageAdminState.getDefaultPageAdminState().switchScene(SceneContext.getSceneContext());
            // set user privileges to patient, employee or admin
        }
        //displays error message
        else{
            Stage currentStage = (Stage)signIn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/LoginFail.fxml"));
            Scene homeScene = new Scene(root);
            currentStage.setScene(homeScene);
            currentStage.show();
        }
    }
}
