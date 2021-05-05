package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.CurrentUser;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


/**
 * Controller for Form Submitted Pop Up
 */
public class CovidFormSubmittedViewController {
    @FXML private JFXTextField username;
    @FXML private JFXTextField password;
    @FXML private JFXTextField displayUuid;
    @FXML private JFXTextField enterToCheck;
    @FXML private JFXButton generateUuid;
    @FXML private JFXButton checkButton;
    Stage previousStage = new Stage();
    @FXML private JFXButton Button;
    @FXML private Text errorMessage;
    @FXML private Text waitMessage;
    @FXML private Text loginMessage;

    boolean isCompleted;

    /**
     * Handles check button by checking to see if the the user is clear to enter, automatically redirects to navigation page.
     * @author keithdesantis
     */
    @FXML
    private void handleCheck() throws IOException, SQLException {
        //do we need to check to see if the input is a uuid or username?

        System.out.println(completed());
        if(completed().equals("true")){
            isCompleted = true;
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml");
            //set destination to 75 Lobby entrance
        }
        else if(completed().equals("false")){
            isCompleted = true;
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml");
            //set destination to emergency entrance
        }
        else{
            waitMessage.setVisible(true);
            waitMessage.setStyle("-fx-text-fill: #c60000FF;");
        }
    }

    private String completed() throws SQLException {
        String ticketID = enterToCheck.getText();
        String complete = "";

        if(ticketID.contains("-")){
            complete = DatabaseAPI.getDatabaseAPI().getServiceEntry(ticketID).getCompleteStatus();
        }
        else if(CurrentUser.getCurrentUser().getLoggedIn().getUsername().equals(enterToCheck.getText())){
            complete = DatabaseAPI.getDatabaseAPI().getUser(ticketID).getCovidStatus();
        }
        else{
            loginMessage.setStyle("-fx-text-fill: #c60000FF;");
            enterToCheck.setText("");
        }
        return complete;
    }



    public void changeStage(Stage previousStage){
        this.previousStage = previousStage;
    }


    public void handleLogin() throws SQLException {
        if (!DatabaseAPI.getDatabaseAPI().verifyAdminExists()) {
            DatabaseAPI.getDatabaseAPI().addUser("admin", "administrator", "admin", "admin", "true");
            DatabaseAPI.getDatabaseAPI().addUser("staff", "employee", "staff", "staff", "true");
            DatabaseAPI.getDatabaseAPI().addUser("guest", "visitor", "guest", "guest", "true");
        }
        String user = username.getText();
        String pass = password.getText();

        if (CurrentUser.getCurrentUser().login(user, pass)) {
            //SceneContext.getSceneContext().loadDefault();
        }
        else {
            errorMessage.setStyle("-fx-text-fill: #c60000FF;");
            password.setText("");
        }
    }
}

