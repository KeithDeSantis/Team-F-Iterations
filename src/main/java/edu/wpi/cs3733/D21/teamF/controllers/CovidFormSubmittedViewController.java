package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.CurrentUser;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


/**
 * Controller for Form Submitted Pop Up
 */
public class CovidFormSubmittedViewController{
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
        if(!completed().isEmpty()){
            isCompleted = true;
            if(isCleared()){
                CurrentUser.getCurrentUser().tempLogin(enterToCheck.getText());
                SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
            }
            CurrentUser.getCurrentUser().tempLogin(enterToCheck.getText());
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml");
            Scene scene = checkButton.getScene();
            ((Stage) scene.getWindow()).close();
        }
        else{
            waitMessage.setVisible(true);
            waitMessage.setStyle("-fx-text-fill: #c60000FF;");
        }
    }

    private String completed() throws SQLException {
        String ticketID = enterToCheck.getText();
        String complete = "";

        if (ticketID.contains("-")) {
            complete = DatabaseAPI.getDatabaseAPI().getServiceEntry(ticketID, "uuid").getCompleteStatus();
            CurrentUser.getCurrentUser().tempLogin(ticketID);
        }
        return complete;
    }

    public void autoFill(String ID){
        enterToCheck.setText(ID);
    }

    public void closePopup() throws IOException {
        Scene scene = checkButton.getScene();
        ((Stage) scene.getWindow()).close();
    }

    private boolean isCleared() throws SQLException{
        String ID = enterToCheck.getText();
        return Boolean.parseBoolean(DatabaseAPI.getDatabaseAPI().getServiceEntry(ID, "additionalInstructions").getCompleteStatus());
    }
}

