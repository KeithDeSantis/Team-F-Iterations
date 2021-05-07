package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.CurrentUser;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


/**
 * Controller for Form Submitted Pop Up
 */
public class CovidFormSubmittedViewController implements IController {
    @FXML private JFXTextField enterToCheck;
    @FXML private JFXButton checkButton;
    @FXML private Text waitMessage;

    boolean isCompleted;

    /**
     * Handles check button by checking to see if the the user is clear to enter, automatically redirects to navigation page.
     * @author keithdesantis
     */
    @FXML
    private void handleCheck() throws IOException, SQLException {
        //do we need to check to see if the input is a uuid or username?
        if(completed().equals("true")){
            isCompleted = true;
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml");
            Scene scene = checkButton.getScene();
            ((Stage) scene.getWindow()).close();
            //set destination to 75 Lobby entrance
        }
        else if(completed().equals("false")){
            isCompleted = true;
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml");
            Scene scene = checkButton.getScene();
            ((Stage) scene.getWindow()).close();
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
            CurrentUser.getCurrentUser().tempLogin(ticketID);
        }
        else if(CurrentUser.getCurrentUser().getLoggedIn().getUsername().equals(enterToCheck.getText())){
          //  complete = DatabaseAPI.getDatabaseAPI().getUser(ticketID).getCovidStatus();
        }
        else{
            //loginMessage.setStyle("-fx-text-fill: #c60000FF;");
            enterToCheck.setText("");
        }
        return complete;
    }

    public void autoFill(String ID){
        enterToCheck.setText(ID);
    }

    public void closePopup() {
        Scene scene = checkButton.getScene();
        ((Stage) scene.getWindow()).close();
    }
}

