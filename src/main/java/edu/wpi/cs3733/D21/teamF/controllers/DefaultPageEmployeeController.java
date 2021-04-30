package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class DefaultPageEmployeeController {

    @FXML
    private JFXButton editMap;
    @FXML
    private JFXButton signOut;
    @FXML
    private JFXButton navigation;
    @FXML
    private JFXButton serviceRequest;
    @FXML
    private JFXButton quit;
    @FXML
    private Text title;

    @FXML private JFXButton serviceManager;

    @FXML private void initialize(){
    }

    /**
     * Handles the pushing of a button on the screen
     * @param actionEvent the button's push
     * @throws IOException in case of scene switch, if the next fxml scene file cannot be found
     * @author ZheCheng Song
     */
    @FXML
    private void handleButtonPushed(ActionEvent actionEvent) throws IOException {

        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage
        Stage stage;
        Parent root;

        if (buttonPushed == editMap) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/mapEditView.fxml");
        } else if (buttonPushed == signOut) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
        } else if (buttonPushed == navigation) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml");
        } else if (buttonPushed == serviceRequest) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeView.fxml");
        }
        else if (buttonPushed == serviceManager) {
            // Implement Later
            //TODO Assign to combo box
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestManager.fxml");
        }
        else if (buttonPushed == quit) {
            Platform.exit();
        }
        else if(buttonPushed == signOut){
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
        }
    }
    /** handles highlighting buttons when hovering
     * @author Keith Desantis, insterted by Leo Morris
     * @param mouseEvent The button hovered over
     */
    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #F0C808; -fx-text-fill: #000000;");
    }

    /** handles removing highlight on buttons when not hovering
     * @author Keith Desantis, insterted by Leo Morris
     * @param mouseEvent The button hovered off
     */
    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #03256C; -fx-text-fill: #FFFFFF;");
    }
}
